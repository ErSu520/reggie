package com.flash.reggie.controller;


import com.flash.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Filter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());

        val originalFilename = file.getOriginalFilename();
        val newFilename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + newFilename));
        }catch (IOException e){
            e.printStackTrace();
        }
        return R.success(newFilename);
    }

    @GetMapping("/download")
    public void download(HttpServletRequest request, String name, HttpServletResponse response){
        try {
            File file = new File(basePath + name);
            FileInputStream inputStream = new FileInputStream(file);
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int byteCount = -1;
            byte[] bytes = new byte[1024];
            while((byteCount = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0 , byteCount);
                outputStream.flush();
            }
        }catch (IOException e){
            if(e.getMessage().contains("系统找不到指定的文件")){
                log.error("系统找不到指定的文件");
            }
        }
    }

}
