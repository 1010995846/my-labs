package cn.cidea.framework.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@Slf4j
public abstract class HttpRequestBuilder {

    protected String url;

    protected RequestMethod method;

    protected Object param;

    @Accessors(chain = true, fluent = true)
    protected String charset;

    @Accessors(chain = true, fluent = true)
    protected List<Header> headerList = new ArrayList<>();

    @Accessors(chain = true, fluent = true)
    protected Integer connectionTimeout = 3000;

    @Accessors(chain = true, fluent = true)
    protected Integer socketTimeout = 30000;

    private HttpRequestBuilder() {
    }

    /**
     * post
     * @param url
     * @return
     */
    public static Post post(String url) {
        Post builder = new Post();
        builder.url = url;
        builder.method = RequestMethod.POST;
        return builder;
    }

    /**
     * 预设的一些编码
     */
    public HttpRequestBuilder utf8() {
        this.charset = Charset.forName("UTF-8").displayName();
        return this;
    }
    public HttpRequestBuilder gb2313() {
        this.charset = Charset.forName("GB2312").displayName();
        return this;
    }
    public HttpRequestBuilder gbk() {
        this.charset = Charset.forName("GBK").displayName();
        return this;
    }

    /**
     * 添加Header
     */
    public HttpRequestBuilder addHeader(String name, String value){
        this.headerList.add(new Header(name, value));
        return this;
    }
    public HttpRequestBuilder addHeader(List<Header> headerList){
        this.headerList.addAll(headerList);
        return this;
    }
    public HttpRequestBuilder addHeader(Map<String, String> map){
        List<Header> headerList = map.entrySet().stream()
                .map(e -> new Header(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        this.headerList.addAll(headerList);
        return this;
    }

    private String execute() {
        HttpMethod httpMethod = buildHttpMethod();
        HttpMethodParams httpMethodParams = httpMethod.getParams();
        if (charset != null) {
            httpMethodParams.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        }

        for (Header header : headerList) {
            httpMethod.addRequestHeader(header);
        }

        HttpClient client = new HttpClient();
        HttpConnectionManagerParams connectionManagerParams = client.getHttpConnectionManager().getParams();
        connectionManagerParams.setConnectionTimeout(connectionTimeout);
        connectionManagerParams.setSoTimeout(socketTimeout);

        try {
            int status = client.executeMethod(httpMethod);

            String responseBody = httpMethod.getResponseBodyAsString();
            if (status != HttpStatus.SC_OK) {
                log.error("HTTP调用异常: {}", responseBody);
                throw new RuntimeException("HTTP调用异常: " + responseBody);
            }
            return responseBody;
        } catch (IOException ioe) {
            throw new RuntimeException("HTTP调用IO异常: " + ioe.getMessage());
        }
    }

    abstract HttpMethod buildHttpMethod();

    public enum RequestMethod {
        POST,
    }

    @Slf4j
    public static class Post extends HttpRequestBuilder {

        protected ContentType contentType;

        private Post() {
        }

        public Post json(Object param) {
            this.contentType = ContentType.JSON;
            this.param = param;
            return this;
        }

        public Post formUrlencoded(Object param) {
            this.contentType = ContentType.FORM_URLENCODED;
            Map<String, Object> paramMap;
            if (param instanceof Map) {
                paramMap = (Map) param;
            } else {
                paramMap = JSONObject.parseObject(JSONObject.toJSONString(this.param));
            }
            List<NameValuePair> nameValuePairList = paramMap.entrySet().stream()
                    .map(e -> new NameValuePair(e.getKey(), e.getValue().toString()))
                    .collect(Collectors.toList());
            this.param = nameValuePairList;
            return this;
        }

        private Post formData(Object param) {
            this.contentType = ContentType.FORM_DATA;
            // TODO
            return this;
        }

        @Override
        public HttpMethod buildHttpMethod() {
            PostMethod httpMethod = new PostMethod(url);
            try {
                switch (contentType) {
                    case FORM_URLENCODED:
                        List<NameValuePair> list = (List<NameValuePair>) param;
                        NameValuePair[] array = new NameValuePair[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            array[i] = list.get(i);
                        }
                        httpMethod.setRequestBody(array);
                        break;
                    case JSON:
                        if(!(param instanceof String)){
                            param = JSONObject.toJSONString(param);
                        }
                        StringRequestEntity requestEntity = new StringRequestEntity(
                                (String) param,
                                contentType.value(),
                                charset);
                        httpMethod.setRequestEntity(requestEntity);
                        break;
                    default:
                        throw new RuntimeException("暂不支持此类型");
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("HTTP参数封装异常: " + e.getMessage());
            }
            return httpMethod;
        }

        @AllArgsConstructor
        public enum ContentType {
            FORM_DATA("multipart/form-data"),
            FORM_URLENCODED("application/x-www-form-urlencoded"),
            JSON("application/json"),
            XML("application/xml"),
            ;

            private String value;

            public String value() {
                return value;
            }
        }

    }
}
