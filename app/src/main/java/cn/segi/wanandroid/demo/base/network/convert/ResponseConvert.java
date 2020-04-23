package cn.segi.wanandroid.demo.base.network.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import cn.segi.wanandroid.demo.base.network.response.BaseResponse;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class ResponseConvert<T> implements Converter<ResponseBody, T> {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    public ResponseConvert(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String responce = value.string();
        BaseResponse re = gson.fromJson(responce, BaseResponse.class);
        if (re.getErrorCode() != 0) {
            value.close();
//            throw new ApiException(re.getErrorCode(), re.getErrorMessage());
        }
        MediaType mediaType = value.contentType();
        Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
        ByteArrayInputStream bis = new ByteArrayInputStream(responce.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(bis, charset);
        JsonReader jsonReader = gson.newJsonReader(inputStreamReader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
