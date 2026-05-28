package com.hotel.booking.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        return uploadResult.get("secure_url").toString();
    }

    public boolean deleteImage(String imageUrl) throws IOException {

        if (imageUrl == null || imageUrl.isBlank()) {
            return false;
        }

        String publicId = extractPublicId(imageUrl);

        Map result = cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.emptyMap()
        );

        return "ok".equals(result.get("result"));
    }

    private String extractPublicId(String imageUrl) {

        /*
          Example URL:

          https://res.cloudinary.com/demo/image/upload/v174842/image123.jpg

          public_id:
          image123
        */

        String[] parts = imageUrl.split("/");

        String fileName = parts[parts.length - 1];

        // remove extension
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}