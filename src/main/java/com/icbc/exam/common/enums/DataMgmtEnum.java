package com.icbc.exam.common.enums;

/**
 * @author cyt
 * @title: DataMgmtEnum
 * @projectName pes-mgmt-main
 * @description:
 * @date 2021/1/28 12:14
 */

public enum DataMgmtEnum {

    PES_CUST_BASE_INFO("PES_CUST_BASE_INFO", "客户基本信息表", 0),
    PES_ORDER_RECORD_M("PES_ORDER_RECORD_M", "订单记录表", 1);



    private String code;
    private String name;
    private Integer type;

    private DataMgmtEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private DataMgmtEnum(String code, String name, Integer type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getType() {
        return type;
    }

    public static String getNameByCode(String code) {
        for (DataMgmtEnum enumitem : DataMgmtEnum.values()) {
            if (enumitem.getCode().equals(code)) {
                return enumitem.getName();
            }
        }
        return null;
    }

}
