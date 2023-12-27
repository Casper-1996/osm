package com.icbc.exam.common.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelListener<T extends BaseRowModel> extends AnalysisEventListener<T> {

    private List<T> rows = new ArrayList<>();

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
    public void onException(Exception exception,AnalysisContext context ){
        log.error("解析失败，继续解析下一行:{}",exception.getMessage());
        if(exception instanceof ExcelDataConvertException){
            ExcelDataConvertException excelDataConvertException =  (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析错误异常",excelDataConvertException.getRowIndex()+1,excelDataConvertException.getColumnIndex()+1);
        }
    }

}
