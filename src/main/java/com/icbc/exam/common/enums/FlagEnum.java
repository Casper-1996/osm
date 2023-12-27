package com.icbc.exam.common.enums;

/**
 * @descption: 通用是否
 * @projectName plm_mgmt_npl
 * @date 2020/5/15 11:47
 */
public enum FlagEnum {

    //否
    NO("0","否"),
    //是
    YES("1","是");


    public String code;
    public String chName;

    FlagEnum(String code, String chName) {
        this.code = code;
        this.chName = chName;
    }

    public String getCode() {
        return code;
    }

    public String getChName() {
        return chName;
    }

    public static String getCodeByChName(String chName) {
        for (FlagEnum enumitem : FlagEnum.values()) {
            if (enumitem.getChName().equals(chName)) {
                return enumitem.getCode();
            }
        }
        return null;
    }

    public static String getChNameByCode(String code) {
        for (FlagEnum enumitem : FlagEnum.values()) {
            if (enumitem.getCode().equals(code)) {
                return enumitem.getChName();
            }
        }
        return null;
    }

}
