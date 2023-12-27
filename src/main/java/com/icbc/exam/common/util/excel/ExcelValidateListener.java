package com.icbc.exam.common.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ExcelValidateListener<T extends BaseRowModel> extends AnalysisEventListener<T> {


    private List<T> rows = new ArrayList<>();
    private Class<T> clazz;

    public ExcelValidateListener(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * object是每一行数据映射的对象
     * @param object
     * @param context
     */
    @Override
    public void invoke(T object, AnalysisContext context) {
         rows.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //解析结束销毁不用的资源
        //rows.clear();
        log.info("read {} rows %n", rows.size());
    }

    public List<T> getRows() {

        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public void onException(Exception exception,AnalysisContext context ) throws Exception {
        log.error("解析失败，继续解析下一行:{}",exception.getMessage());
        if(exception instanceof ExcelDataConvertException){
            ExcelDataConvertException excelDataConvertException =  (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析错误异常",excelDataConvertException.getRowIndex()+1,excelDataConvertException.getColumnIndex()+1);
        } else if (exception instanceof ExcelAnalysisException) {
            super.onException(exception, context);
        }
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
        if (clazz != null) {
            try {
                Map<Integer, String> indexNameMap = getIndexNameMap(clazz);
                Set<Integer> keySet = headMap.keySet();
                if (headMap.size() > indexNameMap.size()) {
                    throw new ExcelAnalysisException("解析excel出错，请传入正确格式的excel");
                }
                for (Integer key : keySet) {
                    if (StringUtils.isEmpty(headMap.get(key))) {
                        throw new ExcelAnalysisException("解析excel出错，请传入正确格式的excel");
                    }
                    if (!headMap.get(key).equals(indexNameMap.get(key))) {
                        throw new ExcelAnalysisException("解析excel出错，请传入正确格式的excel");
                    }
                }
            } catch (NoSuchFieldException e) {
                log.error("解析表头错误",e);
            }
        }
    }

    private Map<Integer,String> getIndexNameMap(Class<T> clazz) throws NoSuchFieldException {
        Map<Integer, String> result = new HashMap<>();
        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            field = clazz.getDeclaredField(fields[i].getName());
            field.setAccessible(true);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                int index = excelProperty.index();
                String[] values = excelProperty.value();
                StringBuilder value = new StringBuilder();
                for (String v : values) {
                    value.append(v);
                }
                result.put(index, value.toString());
            }
        }
        return result;
    }
}
