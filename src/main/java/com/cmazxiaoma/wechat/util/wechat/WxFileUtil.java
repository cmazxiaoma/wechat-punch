package com.cmazxiaoma.wechat.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmazxiaoma.wechat.core.AccessTokenCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Slf4j
public class WxFileUtil {

    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    //微信上传素材,返回media_id
    public static String upload(String filePath, String type) {
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
           log.info("文件不存在");
        }
        String url = UPLOAD_URL.replace("ACCESS_TOKEN", AccessTokenCache.get()).replace("TYPE", type);
        String result = null;

        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            //设置请求头信息
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");

            //设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");

            byte[] head = sb.toString().getBytes("utf-8");

            //获得输出流
            OutputStream out = new DataOutputStream(con.getOutputStream());
            //输出表头
            out.write(head);

            //文件正文部分
            //把文件以流的方式 写入到url中
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];

            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            //结尾部分
            //定义最后数据分隔线
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n")
                    .getBytes("utf-8");

            out.write(foot);

            out.flush();
            out.close();

            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = null;

            try {

                //定义BufferedReader输入流来读取URL的响应
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                result = buffer.toString();

            } catch (IOException e) {
                log.error("获取url返回的结果失败 = {}", e.getMessage());
            } finally {
                close(reader);
            }

        } catch (Exception e) {
            log.error("未知异常 = {}", e.getMessage());
        }

        log.info("result = {}", result);
        return JSON.parseObject(result).getString("media_id");
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            log.error("关闭流失败 = {}", e.getMessage());
        }
    }



}
