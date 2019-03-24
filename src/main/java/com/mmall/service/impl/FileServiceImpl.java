package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @program: mmall
 * @description: 文件service接口 实现类
 * @author: ypwang
 * @create: 2018-05-16 22:59
 **/
@Service("fileServiceImpl")
@Slf4j
public class FileServiceImpl implements IFileService {

    // private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtendName = fileName.substring(fileName.lastIndexOf('.') + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtendName;
        // 打印日志
        // 注意这里{}占位符只是logger的写法
        log.info("开始上传文件，上传文件的文件名：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadFileName);

        // 判断文件夹是否存在
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            log.info("上传文件到{}成功", path);

            // 将targetFile上传到我们的ftp服务器上;
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 上传完之后，删除upload下面对应的文件
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常", e);
            e.printStackTrace();
        }

        return targetFile.getName();
    }
}
