package com.beiming.uhf_test.utils.pic;

import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码

    public static String post(String path, List<Bitmap> imgs, List<String> paths) throws Exception {
        StringBuilder output = new StringBuilder();
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--";
        String LINE_END = "\r\n";
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(TIME_OUT);
        conn.setConnectTimeout(TIME_OUT);
        conn.setDoInput(true);  //允许输入流
        conn.setDoOutput(true); //允许输出流
        conn.setUseCaches(false);  //不允许使用缓存
        conn.setRequestMethod("POST");  //请求方式
        conn.setRequestProperty("Charset", CHARSET);  //设置编码
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
        conn.connect();

        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        //把所有文件类型的实体数据发送出来  

        for (int i = 0; i < imgs.size(); i++) {
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append(PREFIX);
            fileEntity.append(BOUNDARY);
            fileEntity.append(LINE_END);
            fileEntity.append("Content-Disposition: form-data;name=\"img\";filename=\"" + paths.get(i) + "\"" + LINE_END);
            fileEntity.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
            fileEntity.append(LINE_END);
            outStream.write(fileEntity.toString().getBytes());
            output.append(fileEntity);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgs.get(i).compress(Bitmap.CompressFormat.PNG, 100, baos);
            InputStream inStream = new ByteArrayInputStream(baos.toByteArray());

            if (inStream != null) {
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer, 0, 1024)) != -1) {
                    outStream.write(buffer, 0, len);
                    output.append(buffer);
                }
                inStream.close();
            }
            outStream.write(LINE_END.getBytes());
            output.append(LINE_END.getBytes());
        }
        //下面发送数据结束标志，表示数据已经结束  
        outStream.write(endline.getBytes());
        output.append(endline.getBytes());
        System.out.println(output);
        outStream.flush();
        /**
         * 获取响应码  200=成功
         * 当响应成功，获取响应的流  
         */
        int res = conn.getResponseCode();
        if (res == 200) {
            InputStream input = conn.getInputStream();
            StringBuffer sb1 = new StringBuffer();
            int ss;
            while ((ss = input.read()) != -1) {
                sb1.append((char) ss);
            }
            result = sb1.toString();
//            System.out.println(result);
        }
        return result;
    }

    /**
     *单文件上传  
     * 提交数据到服务器  
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)  
     * @param params 请求参数 key为参数名,value为参数值  
     * @param file 上传文件  
     */  
    /*public static String post(String path, File file) throws Exception{  
       return post(path, new File[]{file});  
    } */
}