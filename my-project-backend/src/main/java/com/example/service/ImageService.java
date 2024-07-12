package com.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

public interface ImageService {
    String uploadAvatar(MultipartFile file, int id) throws IOException;
    void fetchImageFromMinio(OutputStream steam, String image) throws Exception;
}
