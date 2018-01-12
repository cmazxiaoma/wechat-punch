package com.cmazxiaoma.wechat.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmazxiaoma.wechat.VO.WechatUserListVO;
import com.cmazxiaoma.wechat.dto.*;
import com.cmazxiaoma.wechat.VO.WechatQRTicketVO;
import com.cmazxiaoma.wechat.constant.WechatConfig;
import com.cmazxiaoma.wechat.constant.WechatMessage;
import com.cmazxiaoma.wechat.http.HttpAPIService;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class MessageUtil {

    public static Map<String, String> xml2Map(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream is = null;

        try {
            is = request.getInputStream();
            Document doc = reader.read(is);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();

            for (Element e : list) {
                /*
                <xml>
                    <ToUserName><![CDATA[gh_e136c6e50636]]></ToUserName>
                    <FromUserName><![CDATA[oMgHVjngRipVsoxg6TuX3vz6glDg]]></FromUserName>
                    <CreateTime>1408091189</CreateTime>
                    <MsgType><![CDATA[event]]></MsgType>
                    <Event><![CDATA[location_select]]></Event>
                    <EventKey><![CDATA[6]]></EventKey>
                    <SendLocationInfo>
                        <Location_X><![CDATA[23]]></Location_X>
                        <Location_Y><![CDATA[113]]></Location_Y>
                        <Scale><![CDATA[15]]></Scale>
                        <Label><![CDATA[ 广州市海珠区客村艺苑路 106号]]></Label>
                        <Poiname><![CDATA[]]></Poiname>
                    </SendLocationInfo>
                 </xml>
                 如果返回这种的XML，我们要获取 <SendLocationInfo></SendLocationInfo>这个节点下面的所有子节点
                 */
                if (e.getName().equals("SendLocationInfo")) {
                    List<Element> sendLocationInfoList = e.elements();
                    for (Element sendLocationInfo : sendLocationInfoList) {
                        map.put(sendLocationInfo.getName(), sendLocationInfo.getText());
                    }
                }

                map.put(e.getName(), e.getText());
            }

        } catch (Exception e) {
            log.error("error = {}", e.getMessage());

        } finally {
            close(is);
        }

        return map;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                log.error("error = {}", e.getMessage());
            }
        }
    }


    public static String initTextMessage(String fromUserName, String toUserName, String content) {
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(WechatMessage.TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent(content);
        text.setMsgID(String.valueOf(System.currentTimeMillis()));

        return MessageUtil.textMessage2Xml(text);
    }

    /**
     * 图文上传格式
     * <xml>
        <ToUserName>oTyMJxBQ2mqjPC8tEdssdVriapC0</ToUserName>
        <FromUserName>gh_3fd26f0cb080</FromUserName>
        <CreateTime>1515657834273</CreateTime>
        <MsgType>news</MsgType>
        <ArticleCount>1</ArticleCount>
        <Articles>
            <item>
            <Title>小马打卡</Title>
            <Description>练手项目</Description>
            <PicUrl>http://upload-images.jianshu.io/upload_images/4636177-3b31fc01ee3cf663.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240</PicUrl>
            <Url>http://cmazxiaoma.tunnel.echomod.cn/wechat_punch/employee/info/list</Url>
            </item>
        </Articles>
     </xml>
     *
     * @param fromUserName
     * @param toUserName
     * @return
     */
    public static String initSingleNewsMessage(String fromUserName, String toUserName) {
        String message = null;
        List<News> newsList = new ArrayList<>();
        NewsMessage newsMessage = new NewsMessage();
        newsList.add(new News().Title("小马打卡").Description("练手项目")
                .PicUrl("http://upload-images.jianshu.io/upload_images/4636177-3b31fc01ee3cf663.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240")
                .Url("http://cmazxiaoma.tunnel.echomod.cn/wechat_punch/employee/info/list"));
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(WechatMessage.NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());
        message = newsMessage2Xml(newsMessage);

        return message;
    }

    public static String initMultiNewsMessage(String fromUserName, String toUserName) {
        String message = null;
        List<News> newsList = new ArrayList<>();
        NewsMessage newsMessage = new NewsMessage();
        newsList.add(new News().Title("小马打卡").Description("练手项目")
                .PicUrl("http://suo.im/46wQxz")
                .Url("http://cmazxiaoma.tunnel.echomod.cn/wechat_punch/employee/info/list"));
        newsList.add(new News().Title("五月天").Description("勇敢")
                .PicUrl("http://suo.im/126MSD")
                .Url("http://cmazxiaoma.tunnel.echomod.cn/wechat_punch/employee/info/list"));
        newsList.add(new News().Title("五月天").Description("宣传照")
                .PicUrl("http://suo.im/N4e6W")
                .Url("http://cmazxiaoma.tunnel.echomod.cn/wechat_punch/employee/info/list"));
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(WechatMessage.NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());
        message = newsMessage2Xml(newsMessage);

        return message;
    }

    /**
     * 图片上传格式
     * <xml>
        <ToUserName>oTyMJxBQ2mqjPC8tEdssdVriapC0</ToUserName>
        <FromUserName>gh_3fd26f0cb080</FromUserName>
        <CreateTime>1515657666518</CreateTime>
        <MsgType>image</MsgType>
        <Image>
            <MediaId>6_Xaucy5Pvf0lEsaryNkcteQyRlN8I7caDAtgxYExXrCNz9j4aaq091yF_XjWDGF</MediaId>
        </Image>
     </xml>
     * @param fromUserName
     * @param toUserName
     * @param mediaId
     * @return
     */
    public static String initImageMessage(String fromUserName, String toUserName, String mediaId) {
        ImageMessage imageMessage = new ImageMessage();
        Image image = new Image();
        image.MediaId(mediaId);
        imageMessage.setFromUserName(toUserName);
        imageMessage.setToUserName(fromUserName);
        imageMessage.setMsgType(WechatMessage.IMAGE);
        imageMessage.setCreateTime(new Date().getTime());
        imageMessage.setImage(image);

        return MessageUtil.imageMessage2Xml(imageMessage);
    }

    //当用户关注微信公众号，显示该信息
    public static String subscribeText() {
        StringBuffer content = new StringBuffer();
        return content.append("欢迎关注小马打卡\n")
                .append("1、回复1，团队介绍\n")
                .append("2、回复2, 产品介绍\n")
                .append("3、回复3，自动打卡\n")
                .toString();
    }

    //回复1，团队介绍
    public static String introduceTeam() {
        return new StringBuffer().append("cmazxiaoma\n")
                .append("wtmjldev\n")
                .toString();
    }

    //回复2, 产品介绍
    public static String introduceGood() {
        return new StringBuffer().append("这是一个练手项目，小马打卡")
                .toString();
    }

    //回复3，自动打卡
    public static String linkToPunch() {
        return new StringBuffer().append("http://cmazxiaoma.tunnel.echomod.cn/wechat_punch/employee/info/list")
                .toString();
    }

    //微信公众号的小功能
    public static String showFunction() {
        StringBuffer content = new StringBuffer();
        return content.append("本公众号小功能如下\n")
                .append("1、回复a，查询该公众号关注者列表\n")
                .append("2、回复b，查询当前用户基础信息\n")
                .append("3、回复c，获取推广二维码\n")
                .append("4、回复d，查看最新单图文消息\n")
                .append("5、回复e，查看最新多图文消息\n")
                .append("6、回复f，获取推广二维码URL\n")
                .toString();
    }

    public static String imageMessage2Xml(ImageMessage message) {
        XStream xStream = new XStream();
        xStream.alias("xml", message.getClass());

        return xStream.toXML(message);
    }

    public static String newsMessage2Xml(NewsMessage message) {
        XStream xStream = new XStream();
        xStream.alias("xml", message.getClass());
        xStream.alias("item", new News().getClass());

        return xStream.toXML(message);
    }

    public static String locationMessage2Xml(LocationMessage message) {
        XStream xStream = new XStream();
        xStream.alias("xml", message.getClass());

        return xStream.toXML(message);
    }

    public static String textMessage2Xml(TextMessage message) {
        XStream xStream = new XStream();
        xStream.alias("xml", message.getClass());

        return xStream.toXML(message);
    }

}
