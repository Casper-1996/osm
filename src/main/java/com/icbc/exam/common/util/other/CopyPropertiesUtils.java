package com.icbc.exam.common.util.other;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author liurong
 * @title: CopyPropertiesUtils
 * @projectName plm_mgmt_npl
 * @description: 对象复制
 * @date 2020/6/11 16:40
 */
public class
CopyPropertiesUtils {

    /**
     * 从list<A> copy 到list<B>
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> copy(List<?> list,Class<T> clazz){
        String oldOb = JSON.toJSONString(list);
        return JSON.parseArray(oldOb, clazz);
    }

    /**
     * 从对象A copy 到对象B
     * @param ob
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T copy(Object ob,Class<T> clazz){
        String oldOb = JSON.toJSONString(ob);
        return JSON.parseObject(oldOb, clazz);
    }

}
