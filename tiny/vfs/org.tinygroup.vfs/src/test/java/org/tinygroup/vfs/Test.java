package org.tinygroup.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.tinygroup.commons.io.StreamUtil;

/**
 * Created by luoguo on 2014/5/30.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String url = "http://www.tinygroup.org/checkSignature.servicepagelet";
        String charset = "UTF-8";
        HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
        httpConnection.setRequestMethod("POST");
        httpConnection.setDoOutput(true); // Triggers POST.
        httpConnection.setDoInput(true);
        httpConnection.setRequestProperty("Accept-Charset", charset);
        httpConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + charset);
        httpConnection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
        OutputStream output = null;
        try {
            output = httpConnection.getOutputStream();
            byte[]bb=new byte[3];
            bb[1]=-1;
            bb[0]=0;
            bb[2]='\n';
//            output.write(bb);
			output.write("\"<xml><ToUserName>TinyFramework</ToUserName> <FromUserName>luog</FromUserName>  <CreateTime>1348831860</CreateTime><MsgType>text</MsgType> <Content>this is a test</Content> <MsgId>1234567890123456</MsgId> </xml>".getBytes(charset));
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException logOrIgnore) {
                }
        }
        InputStream response = httpConnection.getInputStream();
        System.out.println(StreamUtil.readText(response, charset, true));
    }
}
