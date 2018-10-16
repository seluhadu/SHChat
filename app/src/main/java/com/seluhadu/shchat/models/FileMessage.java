package com.seluhadu.shchat.models;import java.util.HashMap;public class FileMessage extends BaseMessage {    private User sender;    private String userId;    private String url;    private String name;    private int size;    private String type;    private String data;    private String customType;    public static HashMap<String, Object> build(User user,String userId, String receiverID, String url, String name, int size, String type, String data, String customType, long createdAt, long updatedAt, long messageId) {        HashMap<String, Object> obj = new HashMap<>();        obj.put("receiverId", receiverID);        obj.put("messageId", messageId);        obj.put("url", url);        obj.put("name", name);        obj.put("size", size);        obj.put("type", type);        obj.put("updatedAt", createdAt);        obj.put("createdAt", updatedAt);        if (customType != null) {            obj.put("customType", customType);        }        if (data != null) {            obj.put("custom", data);        }        return obj;    }    public FileMessage(HashMap<String, Object> obj) {        super(obj);        this.userId = obj.get("url").toString();        this.url = obj.get("url").toString();        this.name = obj.get("url").toString();        this.size = (int) obj.get("url");        this.type = obj.get("url").toString();        this.data = obj.get("url").toString();        this.customType = obj.get("url").toString();    }    HashMap<String, Object> toJson() {        HashMap<String, Object> file = new HashMap<>();        file.put("url", this.url);        file.put("name", this.name);        file.put("size", this.size);        file.put("data", this.data);        file.put("type", this.type);        HashMap<String, Object> obj = super.toJson();        obj.put("userId", this.userId);        obj.put("customType", this.customType);        obj.put("type", "FILE");        obj.put("file", file);        return obj;    }    public User getSender() {        return sender;    }    public void setSender(User sender) {        this.sender = sender;    }    @Override    public String getUserId() {        return userId;    }    @Override    public void setUserId(String userId) {        this.userId = userId;    }    public String getUrl() {        return url;    }    public void setUrl(String url) {        this.url = url;    }    public String getName() {        return name;    }    public void setName(String name) {        this.name = name;    }    public int getSize() {        return size;    }    public void setSize(int size) {        this.size = size;    }    public String getType() {        return type;    }    public void setType(String type) {        this.type = type;    }    public String getData() {        return data;    }    public void setData(String data) {        this.data = data;    }    public String getCustomType() {        return customType;    }    public void setCustomType(String customType) {        this.customType = customType;    }}