
package com.icbc.exam.entity.pojo.user;


import lombok.Data;

import java.util.List;

/**
 * 用户全量详细信息
 *
 */
@Data
public class UserDetailInfo {

    /**
     　* @description: 用户信息
     　*/
    private Admin user;

    /**
     　* @description: 部门组织信息
     　*/
    private Organization organization;

    /**
     　* @description: 角色列表信息
     　*/
    private List<Role> roleList;

    /**
     　* @description: 资源列表信息
     　*/
    private List<String> resourceList;
}
