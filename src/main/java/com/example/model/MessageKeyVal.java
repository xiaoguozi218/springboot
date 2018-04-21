package com.example.model;

/**
 * @filename:MessageKeyVal.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:rpc服务映射容器
 * @author gsh
 * @version 1.0
 *
 */

import java.util.Map;

public class MessageKeyVal {

    private Map<String, Object> messageKeyVal;

    public void setMessageKeyVal(Map<String, Object> messageKeyVal) {
        this.messageKeyVal = messageKeyVal;
    }

    public Map<String, Object> getMessageKeyVal() {
        return messageKeyVal;
    }
}