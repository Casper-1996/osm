package com.icbc.exam.resource.feignClient;


import com.icbc.exam.entity.pojo.mail.IcbcMail;
import com.icbc.exam.entity.pojo.sms.GroupSMSPhoneMsg;
import com.icbc.exam.entity.pojo.sms.SinglePointPhoneMsg;
import com.icbc.exam.entity.pojo.user.Admin;
import com.icbc.exam.entity.pojo.user.Organization;
import com.icbc.exam.entity.pojo.user.R;
import com.icbc.exam.entity.pojo.user.UserDetailInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @author cyt
 * @title: ucenterClient
 * @projectName plm_mgmt_npl
 * @description: 用户中心 FeignClient
 * @date 2020/6/5 16:48
 */
@FeignClient(name="pes-mgmt-system")
public interface UcenterClient {

    /**
     * 根据级别获取组织列表
     */
    @GetMapping(path = "/organization/findByLevel/{levelCode}",produces = "application/json")
    R<List<Organization>> findByLevel(@PathVariable("levelCode") String levelCode);


    @GetMapping(value = "/getUserInfo/{userName}",produces = "application/json")
    R<UserDetailInfo> getUserInfo(@PathVariable("userName") String userName);

    /**
    * 发送邮件
    **/
    @PostMapping(value = "/utils/sendIcbcMail",produces = "application/json")
    R<Object> sendIcbcMail(@RequestBody IcbcMail icbcMail);

    @GetMapping(path = "/getUserByRoleId/{roleId}",produces = "application/json")
    R<List<Admin>> getUserByRoleId(@PathVariable("roleId") Integer roleId);

    @GetMapping(path = "/getUserByRoleAndArea/role/{roleId}/area/{areaCode}",produces = "application/json")
    R<List<Admin>> selectByRoleAndArea(@PathVariable("roleId") Integer roleId,
                                       @PathVariable("areaCode") String areaCode);

    /**
     * 一对一发短信
     **/
    @PostMapping(path = "utils/sendSingleMsg",produces = "application/json")
    R<Object> sendSingleMsg(@RequestBody SinglePointPhoneMsg phoneMsg);

    /**
     * 群发短信
     **/
    @PostMapping(path = "utils/sendGroupMsg",produces = "application/json")
    R<Object> sendGroupMsg(@RequestBody GroupSMSPhoneMsg phoneMsg);

}
