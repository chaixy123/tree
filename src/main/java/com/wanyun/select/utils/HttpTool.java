package com.wanyun.select.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTool {

    private static final int BYTE_LEN = 102400; // 100KB

    private static final String CHARSET = "UTF-8";  // 编码格式


    /**
     * get请求
     *
     * @param url 请求地址（get请求时参数自己组装到url上）
     * @return 响应文本
     */
    public static String get(String url) {
        // 请求地址，以及参数设置
        HttpGet get = new HttpGet(url);
        // 执行请求，获取相应
        return getRespString(get);
    }

    public static String sendGet(String strURL) throws Exception {
        String result = "";
        BufferedReader in = null;
        String realURL = strURL;
        URL url = new URL(realURL);
        URLConnection conn = url.openConnection();
        conn.connect();
        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += "" + line;
        }
        return result;
    }


    /**
     * get请求
     *
     * @param url       请求地址（get请求时参数自己组装到url上）
     * @param headerMap 请求头
     * @return 响应文本
     */
    public static String get(String url, Map<String, String> headerMap) {
        // 请求地址，以及参数设置
        HttpGet get = new HttpGet(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                get.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 执行请求，获取相应
        return getRespString(get);
    }

    /**
     * post 请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应文本
     */
    public static String post(String url, Map<String, String> params, Map<String, String> headerMap) {
        // 构建post请求
        HttpPost post = new HttpPost(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 构建请求参数
        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
            }
        }
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(pairs, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post.setEntity(entity);
        return getRespString(post);
    }

    /**
     * post 请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应文本
     */
    public static String post(String url, Map<String, String> params) {
        // 构建post请求
        HttpPost post = new HttpPost(url);
        // 构建请求参数
        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(pairs, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(entity);
        return getRespString(post);
    }

    public static String postJson(String url, Map<String, String> params) {
        String result = "";
        //CloseableHttpClient实现了HttpClient接口
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        //创建HttpClientBuilder设置属性
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(6000)
                .setSocketTimeout(6000)
                .setConnectTimeout(6000).build()).setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));

        //设置请求头信息
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json, text/plain, */*");
        map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9");
        map.put("Connection", "keep-alive");
        map.put("Content-Type", "application/json;charset=UTF-8");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.5024.400 QQBrowser/10.0.932.400");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        //传递参数为json数据
        String x = JSONObject.toJSONString(params);
        JSONObject jsonObject = JSONObject.parseObject(x);
        //创建指定内容和编码的字符串实体类
        StringEntity entity = new StringEntity(jsonObject.toString(), Consts.UTF_8);
        //设置请求参数
        httpPost.setEntity(entity);

        // 创建HttpClient对象，CloseableHttpClient实例的生成器
        httpClient = httpClientBuilder.build();

        try {
            // 发送HttpPost请求，获取返回值
            CloseableHttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //释放资源
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String postJson(String url, Map<String, String> params, Map<String, String> hadelMap) {
        String result = "";
        //CloseableHttpClient实现了HttpClient接口
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        //创建HttpClientBuilder设置属性
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(6000)
                .setSocketTimeout(6000)
                .setConnectTimeout(6000).build()).setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));

        //设置请求头信息
        hadelMap.put("Accept", "application/json, text/plain, */*");
        hadelMap.put("Accept-Encoding", "gzip, deflate");
        hadelMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        hadelMap.put("Connection", "keep-alive");
        hadelMap.put("Content-Type", "application/json;charset=UTF-8");
        hadelMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.5024.400 QQBrowser/10.0.932.400");
        for (Map.Entry<String, String> entry : hadelMap.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        //传递参数为json数据
        String x = JSONObject.toJSONString(params);
        JSONObject jsonObject = JSONObject.parseObject(x);
        //创建指定内容和编码的字符串实体类
        StringEntity entity = new StringEntity(jsonObject.toString(), Consts.UTF_8);
        //设置请求参数
        httpPost.setEntity(entity);

        // 创建HttpClient对象，CloseableHttpClient实例的生成器
        httpClient = httpClientBuilder.build();

        try {
            // 发送HttpPost请求，获取返回值
            CloseableHttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //释放资源
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void main(String[] args) {
        String url = "http://39.104.114.245:9980/login";
        Map<String, String> map = new HashMap<String, String>();
        map.put("UserName", "nnbp");
        map.put("Password", "666666");
        Map<String, String> hademap = new HashMap<String, String>();
        hademap.put("Content-Type", "application/json");
        String a = postJson(url, map);
        System.out.println(a);
    }


    public static String postBody(String url, String params) {
        // 构建post请求
        HttpPost post = new HttpPost(url);
        // 构建请求参数
        StringEntity s = null;
        try {
            s = new StringEntity(params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");
        post.setEntity(s);
        return getRespString(post);
    }

    /**
     * 文件下载
     */
    public static void getFile(String url, String name) {
        // 图片地址
        HttpGet get = new HttpGet(url);
        // 执行请求，获取响应流
        InputStream in = getRespInputStream(get);
        // InputStream 转 File，保存在当前工程中
        File file = new File(name);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte b[] = new byte[BYTE_LEN];
            int j = 0;
            while ((j = in.read(b)) != -1) {
                fos.write(b, 0, j);
            }
            fos.close();
        } catch (Exception e) {
        }
    }


    /**
     * 获取响应信息（String）
     */
    public static String getRespString(HttpUriRequest request) {
        // 获取响应流
        InputStream in = getRespInputStream(request);

        StringBuilder sb = new StringBuilder();
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = sb.toString();
        return str;
    }


    /**
     * 获取响应信息（InputStream）
     */
    public static InputStream getRespInputStream(HttpUriRequest request) {
        // 获取响应对象
        HttpResponse response = null;
        try {
            response = HttpClients.createDefault().execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null) {
            return null;
        }
        // 获取Entity对象
        HttpEntity entity = response.getEntity();
        // 获取响应信息流
        InputStream in = null;
        if (entity != null) {
            try {
                in = entity.getContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return in;
    }


    /**
     * 根据ip判断当前ip是否能够ping通  
     *
     * @param ip
     * @return
     */
    public static boolean isConnectIp(String ip) {
        boolean bool = false;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("ping " + ip);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                // 优化速度
                if (line.indexOf("请求超时") >= 0) {
                    // System.out.println(ip + "网络断开，时间 " + new Date());
                    return false;
                }
            }
            is.close();
            isr.close();
            br.close();

            if (null != sb && !sb.toString().equals("")) {
                if (sb.toString().indexOf("TTL") > 0) {
                    // 网络畅通
                    // System.out.println(ip + "网络正常 ，时间" + new Date());
                    bool = true;
                } else {
                    // 网络不畅通
                    // System.out.println(ip + "网络断开，时间 " + new Date());
                    bool = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    public static String doPost(String httpUrl) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

    //post请求参数放到body中
    public static String httpPost(String url, Map map, Map<String, Object> hadeMap) {
        // 返回body
        String body = null;
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        // 2、创建一个HttpPost请求
        HttpPost post = new HttpPost(url);
        // 5、设置header信息
        /**header中通用属性*/
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Encoding", "gzip, deflate");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        /**业务参数*/
        for (Map.Entry<String, Object> entry : hadeMap.entrySet()) {//把每个类型的数据获取到从新分装
            String value = (String) entry.getValue();
            String key = (String) entry.getKey();
            post.setHeader(key, value);
        }


        // 设置参数
        if (map != null) {
            try {
                StringEntity entity1 = new StringEntity(JSON.toJSONString(map), "UTF-8");
                entity1.setContentEncoding("UTF-8");
                entity1.setContentType("application/json");
                post.setEntity(entity1);
                // 7、执行post请求操作，并拿到结果
                httpResponse = httpClient.execute(post);
                // 获取结果实体
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    // 按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, "UTF-8");
                }
                try {
                    httpResponse.close();
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return body;
    }


}
