package com.icbc.exam.common.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * @author lida
 * @title:
 * @projectName：plm_mgmt_npl
 * @description:
 * @date 2021/1/18
 */
public class MoneyStringConverter implements Converter<BigDecimal> {
    @Override
    public Class supportJavaTypeKey() {
        return BigDecimal.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public BigDecimal convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty,
                                    GlobalConfiguration globalConfiguration) throws Exception {
        CellDataTypeEnum type = cellData.getType();
        switch (type) {
            case NUMBER:
                return cellData.getNumberValue();
            case STRING:

                String value = cellData.getStringValue();
                value = value.replace(",","");
                if(StringUtils.isBlank(value)){
                    return new BigDecimal(0);
                }
                return new BigDecimal(value);
            default:
                return new BigDecimal(cellData.toString());
        }


    }

    @Override
    public CellData convertToExcelData(BigDecimal bigDecimal, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData(bigDecimal);
    }

}
