package com.t3h.land2110e.controller;

import com.t3h.land2110e.model.Pair;
import com.t3h.land2110e.security.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
public class ImageController {
    @Autowired
    private FirebaseService firebaseService;
    @PostMapping("/image")
    public String postImage(
            @RequestParam("imageFile") List<MultipartFile> imageFile
            ) throws IOException {
        return firebaseService.uploadFile(imageFile.get(0));
    }

    @GetMapping(value = "/image/{imageName}",  produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(
            @PathVariable String imageName
    ){

        Pair<byte[], String> content =  firebaseService.getImage(imageName);
        return content.getT1();

    }
}
