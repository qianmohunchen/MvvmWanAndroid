package cn.segi.wanandroid.demo.base.network.exception;

/**
 * 服务器无数据返回错误
 *
 * @author liangzx
 * @version 1.0
 * @time 2018-08-08 22:51
 **/
public class HttpNoResponseException extends HttpRequestFailException {

    public HttpNoResponseException(String message) {
        super(message);
    }
}
