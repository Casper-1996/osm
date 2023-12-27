package com.icbc.exam.common.constant;


/**
 * @author cyt
 * @description: 用到的常量
 * @date 2019/4/30 14:50
 */
public interface UcenterConstant {

    String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    String PATTERN_DATE = "yyyy-MM-dd";
    String PATTERN_TIME = "HH:mm:ss";

    /**
     * 角色前缀
     */
    String SECURITY_ROLE_PREFIX = "ROLE_";

    /**
     * token前缀
     */
    String TOKEN_PREFIX = "ucenter:token:";

    /**
     * 订单号前缀
     */
    String SERIAL_NO_PREFIX = "pes:tran_serial_no_";

    /**
     * 状态[0:失效,1:正常]
     */
    int DB_STATUS_DEL = 0;
    int DB_STATUS_OK = 1;

    /**
     * 用户锁定状态
     */
    int DB_ADMIN_NON_LOCKED = 0;
    int DB_ADMIN_LOCKED = 1;

    /**
     * 菜单
     */
    int RESOURCE_TYPE_MENU = 0;

    /**
     * 系统资源
     */
    String SYETME_RESOURCE = "0";

    /**
     * 应用资源
     */
    String APP_RESOURCE = "1";


}
