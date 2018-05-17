package com.mmall.util;

import com.mmall.common.ConstValue;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @program: mmall
 * @description: FTP 工具类
 * @author: ypwang
 * @create: 2018-05-17 07:47
 **/
public class FTPUtil {
    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty(ConstValue.FTPSERVERIP, ConstValue.FTPSERVERIPDEFAULTVALUE);
    private static String ftpPort = PropertiesUtil.getProperty(ConstValue.FTPSERVERIPPORT, ConstValue.FTPSERVERPORTDEFAULTVALUE);
    private static String ftpUser = PropertiesUtil.getProperty(ConstValue.FTPUSER);
    private static String ftpPass = PropertiesUtil.getProperty(ConstValue.FTPPASS);

    public FTPUtil() {
    }

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    private boolean connectFTPServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user, pwd);

        } catch (IOException e) {
            logger.error("连接FTP服务器失败", e);
            e.printStackTrace();
        }
        return isSuccess;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean isUploaded = false;
        FileInputStream fis = null;
        // 连接FTP服务器
        if (connectFTPServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();

                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (Exception ex) {
                logger.error("上传文件异常", ex);
                ex.printStackTrace();
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
            isUploaded = true;
        }
        return isUploaded;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, Integer.parseInt(ftpPort), ftpUser, ftpPass);
        logger.info("开始连接FTP服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接FTP服务器，结束上传，上传结果：{}", result);
        return result;
    }
}
