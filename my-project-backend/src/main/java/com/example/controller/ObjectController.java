package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class ObjectController {

    @Resource
    ImageService service;
    @GetMapping("/images/**")
    public void imageFetch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.fetchImage(request, response);
    }

    private void fetchImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = request.getServletPath().substring(7);
        ServletOutputStream stream = response.getOutputStream();
        if(imagePath.length() <= 13){
            response.setStatus(404);
            stream.println(RestBean.failure(404,"Not found Image").toString());
        }else{
            try {
                service.fetchImageFromMinio(stream, imagePath);
                response.setHeader("Cache-Control","max-age=2592000");
                response.setHeader("Content-Type","image/jpg");
            } catch (ErrorResponseException e) {
                if(e.response().code() == 404){
                    response.setStatus(404);
                    stream.println(RestBean.failure(404,"Not found Image").toString());
                }else {
                    log.error("从Minio获取图片出现异常：" + e.getMessage(), e);
                }
            }
        }
    }
}
