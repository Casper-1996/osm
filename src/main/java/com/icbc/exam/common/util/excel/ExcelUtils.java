package com.icbc.exam.common.util.excel;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.icbc.exam.entity.pojo.excel.MultipleChoiceExcelModel;
import com.icbc.exam.entity.pojo.excel.RowNumObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ExcelUtils {

    private static Sheet initSheet;

    static {
        initSheet = new Sheet(1, 0);
        initSheet.setSheetName("sheet");
        //设置自适应宽度
        initSheet.setAutoWidth(Boolean.TRUE);
    }

    /**
     * @param is   导入文件输入流
     * @param clazz Excel实体映射类
     * @return
     */
    public static <T extends BaseRowModel> List<T> readExcel(String sheetName ,int sheetNo, int headLineMun, InputStream is, final Class<? extends BaseRowModel> clazz){

        List<T> rowsList = null;
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(is);
            // 解析每行结果在listener中处理
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = EasyExcelFactory.getReader(bis, listener);
            if(sheetName != null){
                excelReader.getSheets().stream().filter(st -> sheetName.equals(st.getSheetName())).forEach(sheet -> {
                    excelReader.read(new Sheet(sheet.getSheetNo(), headLineMun, clazz));
                });
            }else{
                excelReader.read(new Sheet(sheetNo, headLineMun, clazz));
            }
            rowsList = listener.getRows();
            excelReader.finish();
        } catch (Exception e) {
            log.error("Excel解析" + e.getMessage(),e);
            return null;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("IO异常" + e.getMessage(),e);
                }
            }
        }


        return rowsList;
    }

    /**
    * @description: 行读
    * @author: cxk
    * @date: 2021/4/1 11:33
    **/
    public static <T extends BaseRowModel> List<T> validateHeadReadExcel( InputStream is, final Class<? extends BaseRowModel> clazz) throws Exception {
        ExcelLineReadingListener excelListener = new ExcelLineReadingListener();
        EasyExcelFactory.readBySax(is, new Sheet(1,1,clazz), excelListener);
        return excelListener.datas;

    }


    /**
     * @param is   导入文件输入流
     * @param clazz Excel实体映射类
     * @return
     */
    public static <T extends BaseRowModel> List<RowNumObject<T>> LineReadExcel(String sheetName ,int sheetNo,
                                                                         int headLineMun,InputStream is,
                                                                         final Class<? extends BaseRowModel> clazz) throws Exception {

        List<RowNumObject<T>> rowsList = null;
        try (BufferedInputStream bis =  new BufferedInputStream(is)) {
            // 解析每行结果在listener中处理
            ExcelLineReadingListener listener = new ExcelLineReadingListener();
            ExcelReader excelReader = EasyExcelFactory.getReader(bis, listener);
            EasyExcelFactory.readBySax(bis,new Sheet(1,1, clazz), listener);
            if(sheetName != null){
                excelReader.getSheets().stream().filter(st -> sheetName.equals(st.getSheetName())).forEach(sheet -> {
                    excelReader.read(new Sheet(sheet.getSheetNo(), headLineMun, clazz));
                });
            }else{
                excelReader.read(new Sheet(sheetNo, headLineMun, clazz));
            }
            rowsList = listener.getDatas();
            excelReader.finish();
        }


        return rowsList;
    }



    /**
     * 生成excle
     * @param filePath  绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
     * @param data 数据源
     * @param head 表头
     */
    public static void writeBySimple(String filePath, List<List<Object>> data, List<String> head){
        writeSimpleBySheet(filePath,data,head,null);
    }


    /**
     * 生成excle
     * @param filePath 绝对路径, 如：/Downloads/aaa.xlsx
     * @param data 数据源
     * @param sheet excle页面样式
     * @param head 表头
     */
    public static void writeSimpleBySheet(String filePath, List<List<Object>> data, List<String> head, Sheet sheet){
        sheet = (sheet != null) ? sheet : initSheet;

        if(head != null){
            List<List<String>> list = new ArrayList<>();
            head.forEach(h -> list.add(Collections.singletonList(h)));
            sheet.setHead(list);
        }

        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            outputStream = new FileOutputStream(filePath);
            writer = EasyExcelFactory.getWriter(outputStream);
            writer.write1(data,sheet);
        } catch (FileNotFoundException e) {
            log.error("找不到文件或文件路径错误, 文件：{}", filePath);
        }finally {
            try {
                if(writer != null){
                    writer.finish();
                }

                if(outputStream != null){
                    outputStream.close();
                }

            } catch (IOException e) {
                log.error("excel文件导出失败, 失败原因：{}", e);
            }
        }

    }


    /**
     *
     * @param os 文件输出流
     * @param clazz Excel实体映射类
     * @param data 导出数据
     * @return
     */
    public static Boolean writeExcel(OutputStream os, Class clazz, List<? extends BaseRowModel> data){

        BufferedOutputStream bos= null;
        try {
            bos = new BufferedOutputStream(os);
            ExcelWriter writer = new ExcelWriter(bos, ExcelTypeEnum.XLS);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(1, 0,clazz);
            writer.write(data, sheet1);
            writer.finish();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("IO异常" + e.getMessage(),e);
                }
            }
        }
        return true;
    }

    /**
     * ResponseEntity下载文件
     *
     * @param fileName
     * @param byteOutPutStream
     */
    public static ResponseEntity<byte[]> downloadExcel(String fileName, ByteArrayOutputStream byteOutPutStream) {

        //下载文件
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",
                    new String(fileName.getBytes("GBK"), "ISO8859-1"));// 文件名称

            ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(byteOutPutStream.toByteArray(), headers, HttpStatus.CREATED);
            return responseEntity;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;

    }


}
