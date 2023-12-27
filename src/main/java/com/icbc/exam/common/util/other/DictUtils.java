package com.icbc.exam.common.util.other;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cyt
 * @title: DictUtils
 * @projectName pes-mgmt-main
 * @description: 字典获取工具类
 * @date 2021/1/2 15:44
 */
@Component
@Slf4j
public class DictUtils {

    /**
     * 字典redis前缀
     */
    String DICT_PREFIX = "ucenter:dict:";

    /**
     * 字典redis前缀 key->value
     */
    String DICT_KtoV = "KtoV:";

    /**
     * 字典redis前缀 value->key
     */
    String DICT_VtoK = "VtoK:";


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
    　* @description: 根据字典类别和字典key，获取 字典value
      * @param dictType 字典类别
      * @param dictKey 字典key
    　*/
    public String getValue(String dictType,String dictKey){
        try {
            String rediskey= DICT_PREFIX+DICT_KtoV+dictType;
            if(!stringRedisTemplate.opsForHash().hasKey(rediskey,dictKey)){
                return "";
            }
            return   stringRedisTemplate.opsForHash().get(rediskey,dictKey).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return "";
    }

    /**
      * @description: 根据字典类别和字典value，获取 字典key
      * @param dictType 字典类别
      * @param dictValue 字典value
    　*/
    public String getKey(String dictType,String dictValue){
        try {
            String rediskey= DICT_PREFIX+DICT_VtoK+dictType;
            if(!stringRedisTemplate.opsForHash().hasKey(rediskey,dictValue)){
                return "";
            }
            return   stringRedisTemplate.opsForHash().get(rediskey,dictValue).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return "";
    }

    /**
     * @description: 根据字典类别 获取键值对 <key,value>
     * @param dictType 字典类别
    　*/
    public Map<String,String> getEntry(String dictType){
        Map<String,String> result=new HashMap<>();
        try {
            String rediskey= DICT_PREFIX+DICT_KtoV+dictType;
            if(!stringRedisTemplate.hasKey(rediskey)){
                return result;
            }
            Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(rediskey);
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                result.put(entry.getKey().toString(),entry.getValue().toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }

}
