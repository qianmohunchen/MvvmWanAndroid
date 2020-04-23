package cn.segi.wanandroid.demo.base.network.exception;

/**
 * HTTP中401或403编码对应的错误<br>
 * 授权失败
 *
 * @author liangzx
 * @version 1.0
 * @time 2018-08-08 22:51
 **/
public class HttpAuthorizationFailException extends HttpRequestFailException {

    public HttpAuthorizationFailException(String message) {
        super(message);
    }
}
