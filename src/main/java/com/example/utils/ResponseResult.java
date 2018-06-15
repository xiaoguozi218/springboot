package com.example.utils;

/**
 * Created by gsh on 18/1/22.
 */
public class ResponseResult<T> {
    private boolean success;
    private String errMsg;
    private String errCode;
    private T data;

    @SuppressWarnings("rawtypes")
    public static ResponseResult success() {
        return new ResponseResult<>(true);
    }

    @SuppressWarnings("rawtypes")
    public static <T> ResponseResult success(T data) {
        return new ResponseResult<>(true, data);
    }

    @SuppressWarnings("rawtypes")
    public static ResponseResult err(String errMsg) {
        return new ResponseResult<>(false, errMsg);
    }

    public ResponseResult(boolean success, T data) {
        super();
        this.success = success;
        this.data = data;
    }

    public ResponseResult(boolean success) {
        super();
        this.success = success;
    }

    public ResponseResult(boolean err, String errMsg) {
        super();
        this.success = err;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
