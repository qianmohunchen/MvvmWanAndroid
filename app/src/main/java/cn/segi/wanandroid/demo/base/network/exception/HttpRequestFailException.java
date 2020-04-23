package cn.segi.wanandroid.demo.base.network.exception;

/**
 * HTTP中非401、非403、非400<=code<600的编码对应的错误<br>
 * 网络请求失败
 *
 * @author liangzx
 * @version 1.0
 * @time 2018-08-08 22:51
 **/
public class HttpRequestFailException extends Exception {

    public HttpRequestFailException(String message) {
        super(message);
    }
}
