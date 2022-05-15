package com.t3h.land2110e.controller;

import com.t3h.land2110e.security.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ImageController {
    @Autowired
    private FirebaseService firebaseService;
    @PostMapping("/image")
    public String postImage(
            @RequestParam MultipartFile imageFile
            ) throws IOException {
        return firebaseService.uploadFile(imageFile);
    }

    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(
            @PathVariable String imageName
    ){
//        res.getWriter().write();
        return firebaseService.getImage(imageName);
    }
}
