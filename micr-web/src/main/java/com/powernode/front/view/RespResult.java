package com.powernode.front.view;


import com.powernode.common.enums.RCode;

import java.util.List;

// 统一的应答结果类，controller返回的结果都是它
public class RespResult {
    private int code; // 应答码
    private String msg; // code的文字说明
    private String accessToken; // 访问 token
    private Object data; // 单个数据
    private List list; // 集合数据
    private PageInfo page; // 分页数据

    public static RespResult ok() {
        RespResult result = new RespResult();
        result.setRCode(RCode.SUCC);
        return result;
    }

    public static RespResult fail() {
        RespResult result = new RespResult();
        result.setRCode(RCode.UNKNOWN);
        return result;
    }

    public void setRCode(RCode rcode) {
        this.code = rcode.getCode();
        this.msg = rcode.getText();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
