package com.icbc.exam.entity.pojo.excel;

import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * @author cxk
 * @title: ${CLASS}
 * @projectName: osm-mgmt-exam
 * @description:
 * @date: 2021/3/31 17:56
 */
@Data
public class RowNumObject<T> extends BaseRowModel {
    private Integer rowNum;

    private T Object;
}
