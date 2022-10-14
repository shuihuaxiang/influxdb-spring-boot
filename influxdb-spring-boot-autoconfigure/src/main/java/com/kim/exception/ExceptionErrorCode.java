package com.kim.exception;

/**
 * @Author: kimli
 * @Date: 2022/10/14 10:46
 * @Description: 异常信息枚举
 */
public enum ExceptionErrorCode {

    PARAMETER_ERROR("001", "参数错误"),

    ILLEGAL_DATA("002", "数据不合法"),

    EMPTY_PARAMETER("003", "参数为空");

    private String state;

    private String value;

    ExceptionErrorCode(String state, String value) {
        this.state = state;
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BusinessException:[" + this.state + "]" + this.value;
    }
}
