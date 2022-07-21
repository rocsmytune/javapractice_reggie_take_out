package com.project.reggie.controller;

import com.project.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/***
 * file upload and download
 */

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    /***
     * File upload
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());

        String originalName = file.getOriginalFilename();
        String suffix =originalName.substring(originalName.lastIndexOf("."));
        //avoid overlap filename
        String randomName = UUID.randomUUID().toString() + suffix;

        //create file directory
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + randomName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(randomName);
    }
}
