package com.hotel.booking.controller;

import com.hotel.booking.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String imageUrl = cloudinaryService.uploadImage(file);

        return ResponseEntity.ok(
                Map.of(
                        "imageURL", imageUrl
                )
        );
    }

    @GetMapping("/delete-image")
    public ResponseEntity<?> deleteImage(@RequestParam String imageURL)throws IOException{
        boolean response = cloudinaryService.deleteImage(imageURL);

        return ResponseEntity.ok(
                Map.of(
                        "DeleteStatus", response
                )
        );
    }
}