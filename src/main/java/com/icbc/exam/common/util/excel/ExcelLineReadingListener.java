package com.icbc.exam.common.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.BaseRowModel;
import com.icbc.exam.entity.pojo.excel.RowNumObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Data
public class ExcelLineReadingListener<T extends BaseRowModel> extends AnalysisEventListener<T> {

    List<RowNumObject<T>> datas = new ArrayList<>();

    @Override
    public void invoke(T data, AnalysisContext context) {
        RowNumObject<T> rowNumObject = new RowNumObject<>();
        rowNumObject.setObject(data);
        rowNumObject.setRowNum(context.getCurrentRowNum());
        datas.add(rowNumObject);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("read {} rows %n", datas.size());
    }
}
