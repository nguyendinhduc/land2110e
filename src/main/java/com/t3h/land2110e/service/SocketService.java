package com.t3h.land2110e.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3h.land2110e.entity.MessageEntity;
import com.t3h.land2110e.entity.UserProfileEntity;
import com.t3h.land2110e.model.response.OnlineResponse;
import com.t3h.land2110e.repository.FriendResponseRepository;
import com.t3h.land2110e.repository.MessageEntityRepository;
import com.t3h.land2110e.repository.UserProfileRepository;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class SocketService {
    private static final Logger LOG = LoggerFactory.getLogger(SocketService.class);

    private SocketIOServer socketIOServer;
    private Map<Integer, SocketIOClient> mapConnections;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private FriendResponseRepository friendResponseRepository;
    @Autowired
    private MessageEntityRepository messageEntityRepository;
    private Executor executor;

    @PostConstruct
    public void inits() {
        mapConnections = new HashMap<>();
        executor = Executors.newFixedThreadPool(5);
        Configuration config = new Configuration();
//        config.setHostname("0.0.0.0");
        config.setPort(1901);
        socketIOServer = new SocketIOServer(config);
        socketIOServer.addConnectListener(socketIOClient -> {
            LOG.info("onConnect " + socketIOClient.getRemoteAddress().toString());
        });
//        socketIOServer.addDisconnectListener(this::dissconnect);
        socketIOServer.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                dissconnect(socketIOClient);
            }
        });
        socketIOServer.addEventListener("connected", String.class, this::startConnect);
        socketIOServer.addEventListener("events", String.class, this::startConnect);
        socketIOServer.addEventListener("message", String.class, this::sendMessage);
        socketIOServer.start();
    }


    private void startConnect(SocketIOClient socketIOClient, String userId, AckRequest ackRequest) {
        try {
            mapConnections.put(Integer.parseInt(userId), socketIOClient);
            //tính sau
            saveConnect(Integer.parseInt(userId), true);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void saveConnect(int userId, boolean isConnect) {
        Observable.create((ObservableOnSubscribe<Boolean>) t -> {
                    UserProfileEntity userProfile = userProfileRepository.findById(userId);
                    if (userProfile != null) {
                        userProfileRepository.save(userProfile);
                        List<Integer> friends = friendResponseRepository.getFriendIds(userId, "");

                        OnlineResponse online = new OnlineResponse();
                        online.setId(userId);
                        online.setOnline(isConnect);
                        String strOnline = objectMapper.writeValueAsString(online);
                        for (Integer friend : friends) {
                            for (Integer id : mapConnections.keySet()) {
                                if (friend.equals(id)) {
                                    mapConnections.get(id).sendEvent("status", strOnline);
                                }
                            }
                        }
                    }
                }).subscribeOn(Schedulers.from(executor))
                .subscribe();
    }

    private void dissconnect(SocketIOClient socketIOClien) {
        for (Integer id : mapConnections.keySet()) {
            if (mapConnections.get(id) == socketIOClien) {
                mapConnections.remove(id);
                saveConnect(id, false);
            }
        }

    }

    public SocketIOServer getSocketIOServer() {
        return socketIOServer;
    }

    private void sendMessage(SocketIOClient socketIOClient, String messageJson, AckRequest ackRequest) {
        try {
            MessageEntity message = objectMapper.readValue(messageJson, MessageEntity.class);
            message.setId(UUID.randomUUID());
            message.setUpdatedAt(new Date());
            message.setCreatedAt(message.getCreatedAt());
            String contentJson = objectMapper.writeValueAsString(message);
            socketIOClient.sendEvent("sent", contentJson);
            SocketIOClient client = mapConnections.get(message.getReceiverId());
            if (client != null) {
                client.sendEvent("message", contentJson);
            }
            saveMessage( message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void saveMessage(MessageEntity message) {
        Observable.create((ObservableOnSubscribe<Boolean>) t -> {
                     messageEntityRepository.save(message);
                }).subscribeOn(Schedulers.from(executor))
                .subscribe();
    }

}
