package com.icbc.exam.common.util.other;

import com.alibaba.fastjson.JSONObject;
import com.icbc.exam.common.constant.UcenterConstant;
import com.icbc.exam.common.enums.DictEnum;
import com.icbc.exam.common.enums.ResultEnum;
import com.icbc.exam.common.exception.AuthException;
import com.icbc.exam.entity.pojo.user.R;
import com.icbc.exam.entity.pojo.user.UserDetailInfo;
import com.icbc.exam.resource.feignClient.UcenterClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/8/17
 */

@Component
@Slf4j
public class UserUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DictUtils dictUtils;

    @Autowired
    private UcenterClient ucenterClient;

    /**
     　* @description: 根据token 读取redis 获取用户的编码、场所、场所级别信息
     　* @author cyt
     　* @date 2021/1/15 16:51
     　*/
    public UserDetailInfo getUserInfoByToken(String token){
        try {
            if(!stringRedisTemplate.hasKey(UcenterConstant.TOKEN_PREFIX+token)){
                throw new AuthException(ResultEnum.NOT_AUTH);
            }
            String userInfoStr = stringRedisTemplate.opsForValue().get(UcenterConstant.TOKEN_PREFIX+token);
            return JSONObject.parseObject(userInfoStr, UserDetailInfo.class);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        throw new AuthException(ResultEnum.NOT_AUTH);
    }



    public  UserDetailInfo getUserInfoByUserName(String userName){
        try {
            if(StringUtils.isEmpty(userName)){
                throw new AuthException(ResultEnum.NOT_AUTH);
            }
            R<UserDetailInfo> resultObj = ucenterClient.getUserInfo(userName);
            if(resultObj.getCode()==ResultEnum.SUCCESS.getCode()){
               return resultObj.getData();
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        throw new AuthException(ResultEnum.NOT_AUTH);
    }


    /**
     　* @description: 根据token 获取用户可以查看数据的地区号列表
       * @return  返回null 则所有区域都可以查看; 返回空集合，则所有数据都无法查看
     　* @author cyt
     　* @date 2020/5/14 16:51
     　*/
    public List<String> getAreaPermissByToken(String token){
        try {
            List<String> areaList=new ArrayList<>();

            UserDetailInfo userInfo = getUserInfoByToken(token);
            Integer organizationLevel = userInfo.getOrganization().getOrgLevel();

            String orgCode = userInfo.getOrganization().getCode();
            String orgName = userInfo.getOrganization().getName();
            String areaCode = dictUtils.getValue(DictEnum.AREA_TO_BANK.getCode(), orgCode);

            //一级分行人员
            if( Integer.valueOf(dictUtils.getKey(DictEnum.BANK_LEVEL.getCode(),"一级分行"))==organizationLevel){
                return null;
            }

            //二级分行人员
            if( Integer.valueOf(dictUtils.getKey(DictEnum.BANK_LEVEL.getCode(),"二级分行"))==organizationLevel){
                if(!StringUtils.isEmpty(areaCode)){
                    areaList.add(areaCode);
                }
                if("长治催收中心".equals(orgName)){
                    // 长治催收中心：晋城、晋中（榆次）、阳泉、长治、忻州
                    areaList.add("0506");
                    areaList.add("0508");
                    areaList.add("0503");
                    areaList.add("0505");
                    areaList.add("0512");
                }
                if("运城催收中心".equals(orgName)){
                    // 运城催收中心：运城、临汾、朔州、大同、吕梁（离石)
                    areaList.add("0511");
                    areaList.add("0510");
                    areaList.add("0507");
                    areaList.add("0504");
                    areaList.add("0509");
                }
                return areaList;
            }

            //支行人员
            if(Integer.valueOf(dictUtils.getKey(DictEnum.BANK_LEVEL.getCode(),"支行"))==organizationLevel){
                return new ArrayList<>();
            }

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException("用户登录信息异常");
        }
        return new ArrayList<>();
    }

    /**
     　* @description: 根据token 读取redis 获取用户的编码、场所、场所级别信息
     　* @author cyt
     　* @date 2020/5/14 16:51
     　*/
    public String getUserAreaCodeByToken(String token){
        try {
            UserDetailInfo userInfo = getUserInfoByToken(token);
            return  dictUtils.getKey(DictEnum.AREA_TO_BANK.getCode(),userInfo.getOrganization().getCode());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        throw new RuntimeException("用户登录信息异常");
    }


}
