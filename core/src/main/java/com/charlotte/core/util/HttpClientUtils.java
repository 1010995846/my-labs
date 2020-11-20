package com.charlotte.core.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

//import com.alibaba.fastjson.JSON;

public class HttpClientUtils {

    public static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.displayName();

//	private static CookieStore cookieStore = new BasicCookieStore();


    public static HttpClientContext clientContext = null;

    static {
        clientContext = HttpClientContext.create();  //创建上下文.用于共享sessionid
    }

    /**
     * @param url
     * @param pairs
     * @param httpClientContext
     * @return
     */
    public static HttpResponse post(String url, List<NameValuePair> pairs, HttpClientContext httpClientContext) {
        if (httpClientContext == null) {
            httpClientContext = clientContext;
        }
        HttpClient client = (HttpClient) HttpClients.createDefault(); //获取链接对象.
        HttpPost post = new HttpPost(url); //创建表单.
        try {
            //对表单数据进行url编码
            if (pairs != null) {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, DEFAULT_CHARSET);
                post.setEntity(urlEncodedFormEntity);
            }
            HttpResponse response = client.execute(post, httpClientContext);//发送数据.提交表单
            CookieStore cookieStore = httpClientContext.getCookieStore(); //获取cookie 第一步
            List<Cookie> cookies = cookieStore.getCookies();  //获取所有的cookie
            System.out.println("HttpPost---------cookies.size = " + cookies.size());
            for (Cookie cookie :
                    cookies) {
                System.out.println("---------name = " + cookie.getName() + "; value = " + cookie.getValue());
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse postJson(String url, Object object, HttpClientContext httpClientContext) {
        if (httpClientContext == null) {
            httpClientContext = clientContext;
        }
        HttpClient client = (HttpClient) HttpClients.createDefault(); //获取链接对象.
        HttpPost post = new HttpPost(url); //创建表单.
        post.setHeader("Content-Type", "application/json");
        try {
            //对表单数据进行url编码
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(object), DEFAULT_CHARSET);
            stringEntity.setContentType("text/json");
            post.setEntity(stringEntity);

            HttpResponse response = client.execute(post, httpClientContext);//发送数据.提交表单
            CookieStore cookieStore = httpClientContext.getCookieStore(); //获取cookie 第一步
            List<Cookie> cookies = cookieStore.getCookies();  //获取所有的cookie
            System.out.println("HttpPost---------cookies.size = " + cookies.size());
            for (Cookie cookie :
                    cookies) {
                System.out.println("---------name = " + cookie.getName() + "; value = " + cookie.getValue());
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse get(String url, HttpClientContext httpClientContext) {
        HttpClient client = (HttpClient) HttpClients.createDefault(); //获取链接对象.


        HttpGet get = new HttpGet(url); //创建表单.
        try {
            HttpResponse response = client.execute(get, httpClientContext);//发送数据.提交表单
            CookieStore cookieStore = httpClientContext.getCookieStore(); //获取cookie 第一步
            List<Cookie> cookies = cookieStore.getCookies();  //获取所有的cookie
            System.out.println("gyqtest---------cookies.size = " + cookies.size());
            for (Cookie cookie :
                    cookies) {
                System.out.println("gyqtest---------name = " + cookie.getName() + "; value = " + cookie.getValue());
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
