package com.icbc.exam.common.util.ftp;


import com.icbc.exam.common.config.FtpConfig;
import com.icbc.exam.common.constant.DailyConstant;
import com.icbc.exam.entity.pojo.ftp.FtpInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author cxk
 * @title:
 * @projectName: plm_mgmt_npl
 * @description:
 * @date: 2020/7/6 11:01
 */
@Component
@Slf4j
public class FtpUtils {
    // FTP 登录用户名
    private String userName;
    // FTP 登录密码
    private String password;
    // FTP 服务器地址IP地址
    private String ip;
    // FTP 端口
    private int port;
    //上传\下载路径
    private String path;
    // 默认超时60秒
    private int DEFAULT_TIMEOUT = 60 * 1000;

    private int DATA_TIMEOUT = 60 * 1000;
    //连接时间
    private int DEFAULT_CONNECTTIME = 60*1000;
    //字符集
    private String DEFAULT_CHARSET = DailyConstant.DEFAULT_ENCODE;

    private List replyStrings = new ArrayList();
    //ftpClient对象
    private FTPClient ftp;

    @Autowired
    private FtpConfig ftpConfig;

    public FtpUtils() {
    }

    private void initFtp(String ftpType) {
        FtpInfo ftpInfo = ftpConfig.getFtp().get(ftpType);
        ftp = new FTPClient();
        this.userName = ftpInfo.getFtpUsername();
        this.password = ftpInfo.getFtpPassword();
        this.ip = ftpInfo.getFtpIp();
        this.port = Integer.valueOf(ftpInfo.getFtpPort());
        this.path = ftpInfo.getFtpPath();
        ftp.setControlEncoding(DEFAULT_CHARSET);
        if (StringUtils.isNotBlank(ftpInfo.getDataTimeOut())) {
            this.DATA_TIMEOUT = Integer.parseInt(ftpInfo.getDataTimeOut());
        }
        if (StringUtils.isNotBlank(ftpInfo.getConnectTimeOut())) {
            this.DEFAULT_CONNECTTIME = Integer.parseInt(ftpInfo.getConnectTimeOut());
        }
    }

    public FtpUtils(FtpInfo ftpInfo) {
        ftp = new FTPClient();
        this.userName = ftpInfo.getFtpUsername();
        this.password = ftpInfo.getFtpPassword();
        this.ip = ftpInfo.getFtpIp();
        this.port = Integer.valueOf(ftpInfo.getFtpPort());
        this.path = ftpInfo.getFtpPath();
        ftp.setControlEncoding(ftpInfo.getFtpEncode());
        if (StringUtils.isNotBlank(ftpInfo.getDataTimeOut())) {
            this.DATA_TIMEOUT = Integer.parseInt(ftpInfo.getDataTimeOut());
        }
        if (StringUtils.isNotBlank(ftpInfo.getConnectTimeOut())) {
            this.DEFAULT_CONNECTTIME = Integer.parseInt(ftpInfo.getConnectTimeOut());
        }
    }

    /**
     * 连接FTP服务器
     *
     * @return true 成功 false 失败
     **/
    public boolean connectServer() {
        replyStrings.clear();
        boolean flag = true;
        if (ftp == null) {
            log.error("登录ftp服务器失败,连接超时！");
            return false;
        }
        try {
            ftp.setDefaultPort(port);
            ftp.connect(ip);
            ftp.login(userName, password);
            ftp.setDataTimeout(DATA_TIMEOUT);
            ftp.setConnectTimeout(DEFAULT_CONNECTTIME);
            ftp.setDefaultTimeout(DEFAULT_TIMEOUT);
        } catch (IOException e) {
            log.error("登录ftp服务器:" + ip + " 失败，FTP服务器无法打开！", e);
            return false;
        }
        if (log.isDebugEnabled())
            log.debug("登陆ftp服务器成功. server:" + ip);
        return flag;
    }

    /**
     * 断开FTP连接
     **/
    public void closeConnect() {
        replyStrings.clear();
        try {
            if (ftp != null && ftp.isConnected()) {
                ftp.logout();
                ftp.disconnect();
                replyStrings.addAll(Arrays.asList(ftp.getReplyStrings()));
            }
            log.debug("关闭ftp服务器");
        } catch (Exception e) {
            log.warn("断开连接出错！,出错原因：{}", e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param content  上传文件输入内容
     * @param fileName 上传文件名
     * @param encode   编码格式
     **/
    public void storeFile(String ftpType, String content, String fileName, String encode) {
        initFtp(ftpType);
        connectServer();
        boolean storeSuccess = false;
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(content.getBytes(encode));
            ftp.changeWorkingDirectory(path);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setControlEncoding(encode);
            storeSuccess = ftp.storeFile(new String(fileName.getBytes(encode), "iso-8859-1"), in);
            if (!storeSuccess) {
                throw new IOException("不能上传文件 ‘" + fileName + "'到FTP.请检查FTP权限和路径");
            }
            closeConnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(in);
        }
    }

    /**
     * 该方法需要手动断开FTP及在关闭inputStream
     *
     * @param remotePath 需要下载的文件相对路径(./: 上一级目录)
     * @param fileName   需要读取的文件名
     * @return 返回一个输入流
     **/
    public InputStream downStream(String remotePath, String fileName) {
        connectServer();
        replyStrings.clear();
        InputStream is = null;
        try {
            changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            replyStrings.addAll(Arrays.asList(ftp.getReplyStrings()));
            ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            replyStrings.addAll(Arrays.asList(ftp.getReplyStrings()));
            is = ftp.retrieveFileStream(fileName);
            replyStrings.addAll(Arrays.asList(ftp.getReplyStrings()));
            return is;
        } catch (IOException e) {
            log.error("download file " + path + remotePath + "/" + fileName + " error.", e);
        }
        return is;
    }

    /**
     * 一行行读取，并操作
     * @param ftpType ftp类型
     * @param fileName 文件名
     * @param function 处理读行流，无返回值
     */
    public void readLineConsumer(String ftpType, String fileName, Consumer<BufferedReader> function) {
        initFtp(ftpType);
        connectServer();
        changeWorkingDirectory(path);// 转移到FTP服务器目录
        try {
            ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            log.error("setFileType error:", e);
        }
        try (InputStream is = ftp.retrieveFileStream(fileName);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(isr)){
            function.accept(bufferedReader);
        } catch (IOException e) {
            log.error("download file " + path  + "/" + fileName + " error.", e);
        }
        closeConnect();
    }

    /**
     * 一行行读取，并操作
     * @param ftpType ftp类型
     * @param fileName 文件名
     * @param function 处理每行数据操作，无返回值
     */
    public void readLineDataConsumer(String ftpType, String fileName, Consumer<String> function) {
        initFtp(ftpType);
        connectServer();
        changeWorkingDirectory(path);// 转移到FTP服务器目录
        try {
            ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            log.error("setFileType error:", e);
        }
        try (InputStream is = ftp.retrieveFileStream(fileName);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(isr)){
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                function.accept(lineTxt);
            }

        } catch (IOException e) {
            log.error("download file " + path  + "/" + fileName + " error.", e);
        }
        closeConnect();
    }

    /**
     * 一行行读取，并操作
     * @param ftpType ftp类型
     * @param fileName 文件名
     * @param function 处理读行流，有返回值
     */
    public Object readLineFunction(String ftpType, String fileName, Function<BufferedReader, Object> function) {
        initFtp(ftpType);
        connectServer();
        changeWorkingDirectory(path);// 转移到FTP服务器目录
        Object obj = null;
        try {
            ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
           log.error("setFileType error:", e);
        }
        try (InputStream is = ftp.retrieveFileStream(fileName);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader bufferedReader = new BufferedReader(isr)){
            obj = function.apply(bufferedReader);
        } catch (IOException e) {
            log.error("download file " + path  + "/" + fileName + " error.", e);
        }
        closeConnect();
        return obj;
    }

    /**
     * @param ftpSubpath 你需要查看的目录名
     * @param cmdParams  需要执行cmd执行
     * @return 获取你输入的路径下所有的文件名 windows下有默认文件. ..
     **/
    public String[] getFileNameList(String ftpSubpath, String cmdParams) {
        connectServer();
        String[] fileArr = null;
        FTPClient ftpClient = ftp;
        if (ftpClient != null) {
            try {
                ftpClient.changeWorkingDirectory(ftpSubpath);
                fileArr = ftpClient.listNames(cmdParams);
            } catch (IOException e) {
                log.error("获取文件名称异常", e);
            } finally {
                closeConnect();
            }
        }
        return fileArr;
    }

    /**
     * 判断
     **/
    private boolean changeWorkingDirectory(String path) {
        boolean flag = true;
        try {
            flag = ftp.changeWorkingDirectory(path);
            if (flag) {
            } else {
                log.warn("进入文件夹={},失败！", path);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return flag;
    }

    /**
     * 关闭流
     **/
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("关闭文件流出错！出错原因:{},详情如下", e.getMessage());
            }
        }
    }


}
