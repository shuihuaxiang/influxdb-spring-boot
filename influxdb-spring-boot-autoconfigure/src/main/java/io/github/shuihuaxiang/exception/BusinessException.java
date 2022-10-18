package io.github.shuihuaxiang.exception;

/**
 * @Author: kimli
 * @Date: 2022/10/14 10:45
 * @Description: 自定义运行异常类
 */
public class BusinessException extends RuntimeException {

    public BusinessException(ExceptionErrorCode errorCode) {
        super(errorCode.toString());
    }
}
