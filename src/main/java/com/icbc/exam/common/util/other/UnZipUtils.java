package com.icbc.exam.common.util.other;

/**
 * @author cxk
 * @title: ${CLASS}
 * @projectName: osm-mgmt-exam
 * @description:
 * @date: 2021/4/9 17:04
 */

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


@Slf4j
public class UnZipUtils {

    /**
     * 解压文件到指定目录
     */
    @SuppressWarnings("rawtypes")
    public static String unZipFiles(File zipFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String uid = "";
        try {
            uid = UUID.randomUUID().toString().replace("-", "") + "/";
            //解决zip文件中有中文目录或者中文文件
            ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                if (zipEntryName.contains("/")) {
                    int i = zipEntryName.indexOf("/");
                    zipEntryName = zipEntryName.substring(i + 1, zipEntryName.length());
                }
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir + uid + zipEntryName).replaceAll("\\*", "/");

                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //输出文件路径信息
                log.info("解压文件名：{}", outPath);
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }

            return uid;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            uid = "";
        }
        return uid;

    }

    public static void main(String[] args) throws IOException {
        /**
         * 解压文件
         */
        File zipFile = new File("C:\\Users\\sxfh-yykfcs02\\Desktop\\测试合并读取数据\\测试1.zip");
        String path = "C:\\Users\\sxfh-yykfcs02\\Desktop\\测试合并读取数据\\";
        String uid = unZipFiles(zipFile, path);
        System.out.println(uid);
    }
}

