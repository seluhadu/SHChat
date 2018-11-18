package com.seluhadu.shchat.models;import java.util.HashMap;// testpublic class UserMessage extends BaseMessage {    private String message = "";    UserMessage(HashMap<String, Object> obj) {        super(obj);        this.message = obj.get("message").toString();    }    static HashMap<String, Object> build(String userId, String receiverId, String message, String msgType, long createdAt, long updatedAt, long messageId) {        HashMap<String, Object> obj = new HashMap<>();        obj.put("userId", userId);        obj.put("receiverId", receiverId);        obj.put("message", message);        obj.put("msgType", msgType);        obj.put("createdAt", createdAt);        obj.put("messageId", messageId);        obj.put("updatedAt", updatedAt);        return obj;    }    HashMap<String, Object> toJson() {        HashMap<String, Object> obj = super.toJson();        obj.put("msgType", "MESG");        obj.put("data", "data");        obj.put("customType", "customType");        obj.put("message", this.message);        return obj;    }    public String getMessage() {        return message;    }    public void setMessage(String message) {        this.message = message;    }}