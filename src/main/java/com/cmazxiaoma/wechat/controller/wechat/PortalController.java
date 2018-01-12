package com.cmazxiaoma.wechat.controller.wechat;

import com.cmazxiaoma.wechat.core.BaseController;
import com.cmazxiaoma.wechat.dto.LocationMessage;
import com.cmazxiaoma.wechat.http.HttpAPIService;
import com.cmazxiaoma.wechat.util.wechat.MessageUtil;
import com.cmazxiaoma.wechat.util.wechat.WechatCheckUtil;
import com.cmazxiaoma.wechat.constant.WechatMessage;
import com.cmazxiaoma.wechat.util.wechat.WechatHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class PortalController extends BaseController {

    @Resource
    private HttpAPIService httpAPIService;
    /**
     * 验证消息的确来自微信服务器
     * signature： 微信加密签名，signature结合开发者填写的token参数和请求中的timestamp参数、nonce参数
     * timestamp: 时间戳
     * nonce: 随机数
     * echostr: 随机字符串
     * 开发者通过验证signature对请求进行校验，若确认此GET请求来自微信服务器，请原样返回echostr参数，则接入生效，否则接入失败。
     * 1.将token、timestamp、nonce三个参数进行字典排序
     * 2.将三个参数拼接成一个字符串，并且进行sha1加密
     * 3.开发者获得加密后的字符串可与signature对比，标识该请求是否来自微信
     */

    @GetMapping("/portal")
    public void potal() throws IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        log.info("signature = {}", signature);
        log.info("timestamp = {}", timestamp);
        log.info("nonce = {}", nonce);
        log.info("echostr = {}", echostr);

        response.getWriter().print(WechatCheckUtil.checkSignature(signature, timestamp, nonce) ? echostr : null);
    }

    @PostMapping("/portal")
    public void sendTextMessage() throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String, String> map = MessageUtil.xml2Map(request);
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");
        String msgType = map.get("MsgType");

        log.info("MsgType = {}", msgType);

        String message = null;
        String userOpenId = fromUserName;

        if (WechatMessage.TEXT.equalsIgnoreCase(msgType)) {
            String content = map.get("Content");
            message = MessageUtil.initTextMessage(fromUserName, toUserName, content);

            if (content.equals("1")) {
                message = MessageUtil.initTextMessage(fromUserName, toUserName, MessageUtil.introduceTeam());
            }

            if (content.equals("2")) {
                message = MessageUtil.initTextMessage(fromUserName, toUserName, MessageUtil.introduceGood());
            }

            if (content.equals("3")) {
                message = MessageUtil.initTextMessage(fromUserName, toUserName, MessageUtil.linkToPunch());

            }

            if (content.equals("小功能")) {
                message = MessageUtil.initTextMessage(fromUserName, toUserName, MessageUtil.showFunction());
            }

            //发送该公众号的关注者列表
            if (content.equalsIgnoreCase("a")) {
                message = MessageUtil.initTextMessage(fromUserName, toUserName, WechatHttpUtil.getInstance().queryWechatUserList());
            }

            //发送当前用户的基础信息
            if (content.equalsIgnoreCase("b")) {
                message = MessageUtil.initTextMessage(fromUserName, toUserName, WechatHttpUtil.getInstance().queryCurrentUserInfo(userOpenId));
            }

            //发送公众号的二维码
            if (content.equalsIgnoreCase("c")) {
                message = MessageUtil.initImageMessage(fromUserName, toUserName, WechatHttpUtil.getInstance().getQRCodeMediaId());
            }

            //发送单图文消息
            if (content.equalsIgnoreCase("d")) {
                message = MessageUtil.initSingleNewsMessage(fromUserName, toUserName);
            }

            //发送多图文消息
            if (content.equalsIgnoreCase("e")) {
                message = MessageUtil.initMultiNewsMessage(fromUserName, toUserName);
            }

            //发送公众号的二维码url
            if (content.equalsIgnoreCase("f")) {
                message = MessageUtil.initTextMessage(fromUserName, toUserName, WechatHttpUtil.getInstance().getQRCodeUrl());
            }

        }

        if (msgType.equals(WechatMessage.EVENT)) {
            String eventType = map.get("Event");

            log.info("Event = {}", eventType);

            if (eventType.equals(WechatMessage.EVENT_SUBSCRIBE)) {
                message = MessageUtil.initTextMessage(fromUserName, toUserName, MessageUtil.subscribeText());
            }

            if (eventType.equals(WechatMessage.EVENT_UNSUBSCRIBE)) {
                //todo
            }

            if (eventType.equals(WechatMessage.EVENT_CLICK)) {
                //todo
            }

            if (eventType.equals(WechatMessage.EVENT_VIEW)) {
                //todo
            }

            //通过用户授权，获取用户地理信息
            if (eventType.equalsIgnoreCase(WechatMessage.EVENT_LOCATION)) {

                //获取纬度
                String latitude = map.get("Latitude");
                //获取经度
                String longitude = map.get("Longitude");
                //获取精确度
                String precision = map.get("Precision");

                log.info("Latitude = {}", latitude);
                log.info("Longitude = {}", longitude);
                log.info("Precision = {}", precision);

                StringBuffer stringBuffer = new StringBuffer();

                message = MessageUtil.initTextMessage(fromUserName, toUserName,
                        stringBuffer.append("latitude:" + latitude + "\n")
                                .append("longitude:" + longitude + "\n")
                                .append("precision:" + precision + "\n")
                                .toString());
            }

            //用户点击上传地理信息
            if (eventType.equalsIgnoreCase(WechatMessage.EVENT_LOCATION_SELECT)) {
                //事件KEY值
                String eventKey = map.get("EventKey");
                String location_X = map.get("Location_X");
                String location_Y = map.get("Location_Y");
                //精度
                String scale = map.get("Scale");
                //地理位置的字符串信息
                String label = map.get("Label");
                //朋友圈@的名字
                String poiname = map.get("Poiname");

                log.info("eventKey={}\n"
                                + "location_x={}\n"
                                + "location_y={}\n"
                                + "scale={}\n"
                                + "label={}\n"
                                + "poiname={}\n"
                        , eventKey, location_X
                        , location_Y, scale, label, poiname);

                //接受到用户的地理信息，我们也可以返回自己的地理信息给用户
                LocationMessage locationMessage = new LocationMessage();
                locationMessage.setFromUserName(toUserName);
                locationMessage.setToUserName(fromUserName);
                locationMessage.setCreateTime(new Date().getTime());
                locationMessage.setMsgType(WechatMessage.EVENT_LOCATION);
                locationMessage.setLocation_X(location_X);
                locationMessage.setLocation_Y(location_Y);
                locationMessage.setScale(scale);
                locationMessage.setLabel(label);
                locationMessage.setMsgId(String.valueOf(System.currentTimeMillis()));

                message = MessageUtil.locationMessage2Xml(locationMessage);
            }
        }

        if (msgType.equals(WechatMessage.IMAGE)) {
            //todo
        }

        if (msgType.equals(WechatMessage.VIDEO)) {
            //todo
        }

        if (msgType.equals(WechatMessage.VOICE)) {
            //todo
        }


        log.info("Message = {}", message);

        response.getWriter().print(message);
    }
}
