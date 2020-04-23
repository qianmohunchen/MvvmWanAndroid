package cn.segi.wanandroid.demo.base.network.exception;

/**
 * HTTP CODE大于等于400小于600的异常<br>
 * 表示服务忙或接口不存在
 * @author liangzx
 * @version 1.0
 * @time 2018-08-08 22:55
 **/
public class HttpBusyServiceException extends HttpRequestFailException {

    public HttpBusyServiceException(String message) {
        super(message);
    }
}
