package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @program: mmall
 * @description: 文件service接口
 * @author: Ypwang1024
 * @create: 2018-05-16 22:58
 **/
public interface IFileService {
    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    String upload(MultipartFile file, String path);
}
