package com.projectgp.messenger.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Json字符串与各种类型的转换工具类
@Component
public class JSONConverter {

    // ObjectMapper（确保已正确注入或初始化）
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JSONConverter.class);

    static {
        // 启用接受单个值作为数组
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }
    // Map序列化为Json字符串方法
    public static String serializeMaptoJsonString(Map<String, Object> Maps) {
        try {
            return objectMapper.writeValueAsString(Maps);
        } catch (JsonProcessingException e) {
            logger.error("序列化 Maps 时发生错误", e);
            return null;
        }
    }

    //List序列化为Json字符串方法
     public static String serializeListtoJsonString(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            logger.error("序列化列表时发生错误", e);
            return null;
        }
    }

    // Json字符串反序列化为Map方法
    public static Map<String, Object> deserializeJsonStringtoMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            logger.error("反序列化 json 时发生错误", e);
            return new HashMap<>();
        }
    }

    // Json字符串反序列化为List方法
    // 反序列化方法
    public static List<String> deserializeJsonStringtoList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            logger.error("反序列化列表时发生错误", e);
            return new ArrayList<>();
        }
    }
}
