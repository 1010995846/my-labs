package cn.cidea.framework.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author Charlotte
 * 暂未完整测验
 */
@Slf4j
@Deprecated
public class HttpSessionUtils {

    public static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.displayName();

//	private static CookieStore cookieStore = new BasicCookieStore();

    /**
     * 默认上下文.用于共享session
     */
    public static final HttpClientContext defaultClientContext = HttpClientContext.create();

    /**
     * @param url
     * @param pairs
     * @param httpClientContext
     * @return
     */
    public static HttpResponse postForm(String url, List<NameValuePair> pairs, HttpClientContext httpClientContext) throws IOException {
        HttpPost post = new HttpPost(url);
        //对表单数据进行url编码
        if (pairs != null) {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, DEFAULT_CHARSET);
            post.setEntity(urlEncodedFormEntity);
        }
        // 发送数据.提交表单
        return execute(post, null, httpClientContext);
    }

    public static HttpResponse postJson(String url, Object object, HttpClientContext httpClientContext) throws IOException {
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        // 对表单数据进行url编码
        StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(object), DEFAULT_CHARSET);
        stringEntity.setContentType("text/json");
        post.setEntity(stringEntity);

        return execute(post, null, httpClientContext);
    }

    public static HttpResponse get(String url, HttpClientContext httpClientContext) throws IOException {
        HttpGet get = new HttpGet(url);
        return execute(get, null, httpClientContext);
    }

    private static HttpResponse execute(HttpUriRequest request, Map<String, String> headers, HttpClientContext httpClientContext) throws IOException {
        if (httpClientContext == null) {
            httpClientContext = defaultClientContext;
        }
        if(headers != null){
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(request, httpClientContext);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if(statusCode != 200){

        }

        CookieStore cookieStore = httpClientContext.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            log.debug("http.cookies: name = {}; value = {}", cookie.getName(), cookie.getValue());
        }
        return response;
    }
}
