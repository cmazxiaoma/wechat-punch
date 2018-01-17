package com.cmazxiaoma.wx.controller;

import com.cmazxiaoma.wx.VO.WxUserTimeSheetVO;
import com.cmazxiaoma.wx.core.ResultCode;
import com.cmazxiaoma.wx.core.ResultVO;
import com.cmazxiaoma.wx.core.ResultVOGenerator;
import com.cmazxiaoma.wx.dto.WxUserTimeSheetDTO;
import com.cmazxiaoma.wx.model.PunchRole;
import com.cmazxiaoma.wx.model.SysUser;
import com.cmazxiaoma.wx.model.WxUser;
import com.cmazxiaoma.wx.model.WxUserTimeSheet;
import com.cmazxiaoma.wx.service.PunchRoleService;
import com.cmazxiaoma.wx.service.WxUserService;
import com.cmazxiaoma.wx.service.WxUserTimeSheetService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;

/**
* Created by cmazxiaoma on 2018/01/15.
*/
@RestController
@RequestMapping("/wx_user_time_sheet")
@Slf4j
public class WxUserTimeSheetController{
    @Resource
    private WxUserTimeSheetService wxUserTimeSheetService;

    @Resource
    private WxUserService wxUserService;

    @Resource
    private PunchRoleService punchRoleService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Valid WxUserTimeSheetDTO wxUserTimeSheetDTO, BindingResult bindingResult) throws ParseException {
        ResultVO resultVO = null;

        if (bindingResult.hasErrors()) {
            resultVO = new ResultVO.Builder()
                    .code(ResultCode.ILLEGAL_PARAMETERS.getCode())
                    .data("null")
                    .msg(bindingResult.getFieldError().getDefaultMessage())
                    .build();
            return resultVO;
        }

        String openId = wxUserTimeSheetDTO.getOpenId();
        WxUser wxUser = wxUserService.findBy("openid", openId);

        if (wxUser != null) {
            Integer userId = wxUser.getId();

            //判断用户是否凌晨打卡, 我设置的区间是0-5点, 如果是凌晨打卡，那么punch_date就是上一天的。
            Date punchTime = wxUserTimeSheetDTO.getPunchTime();

            //避免后面的setDate()改变wxUserTimeSheetDTO.getPunchTime()的值
            Date punchTimeBackup = (Date) punchTime.clone();
            Date punchDate = null;

            //比如我15号早上08:52:00上班打卡, 2018-01-16 00:50:54下班打卡
            //那么currentPunchTimeHour = 0, currentPunchTimeDay=16
            int currentPunchTimeHour = punchTime.getHours();
            int currentPunchTimeDay = punchTime.getDate();

            if (currentPunchTimeHour >= 0 && currentPunchTimeHour <= 5) {
                punchTime.setDate(currentPunchTimeDay - 1);
                punchDate = punchTime;
            } else {
                //如果是5点以后, 那么punch_date就是当天的
                punchDate = wxUserTimeSheetDTO.getPunchTime();
            }

            WxUserTimeSheet dbWxUserTimeSheet = wxUserTimeSheetService.findByOpenIdAndPunchDate(openId, punchDate);

            log.info("dbWxUserTimeSheet = {}", dbWxUserTimeSheet);
            PunchRole punchRole = punchRoleService.findById(1);

            if (punchRole == null) {
                throw new NullPointerException();
            }
            //上班开始时间
            String goworkValue = punchRole.getGoworkDate();
            //下班开始时间
            String offworkValue = punchRole.getOffworkDate();
            //加班开始时间
            String overtimeStartDateValue = punchRole.getOvertimeStartDate();

            Date goworkDate = DateUtils.parseDate(goworkValue, "HH:mm:ss");
            int goworkDateTotalSeconds = goworkDate.getHours() * 3600 + goworkDate.getMinutes() * 60 + goworkDate.getSeconds();

            Date offworkDate = DateUtils.parseDate(offworkValue, "HH:mm:ss");
            int offDateTotalSeconds = offworkDate.getHours() * 3600 + offworkDate.getMinutes() * 60 + offworkDate.getSeconds();

            Date overtimeStartDate =  DateUtils.parseDate(overtimeStartDateValue, "HH:mm:ss");
            int overtimeStartDateTotalSeconds =  overtimeStartDate.getHours() * 3600 + overtimeStartDate.getMinutes() * 60 + overtimeStartDate.getSeconds();

            //判断是否为加班的flag 加班开始时间 - 正常下班时间
            int isOverTimeflag = overtimeStartDateTotalSeconds - offDateTotalSeconds;

            //如果不存在当天打卡的记录，那么就添加记录(上班打卡)
            if (dbWxUserTimeSheet == null) {
                WxUserTimeSheet wxUserTimeSheet = new WxUserTimeSheet();
                wxUserTimeSheet.setUserId(userId);
                wxUserTimeSheet.setOpenId(openId);
                wxUserTimeSheet.setLocationLatitude(wxUserTimeSheetDTO.getLocationLatitude());
                wxUserTimeSheet.setLocationLongitude(wxUserTimeSheetDTO.getLocationLongitude());
                wxUserTimeSheet.setLocationInfo(wxUserTimeSheetDTO.getLocationInfo());
                wxUserTimeSheet.setFirstPunchTime(wxUserTimeSheetDTO.getPunchTime());
                wxUserTimeSheet.setLastPunchTime(wxUserTimeSheetDTO.getPunchTime());
                wxUserTimeSheet.setPunchDate(
                        DateUtils.parseDate(
                                DateFormatUtils.format(wxUserTimeSheetDTO.getPunchTime(), "yyyy-MM-dd")
                        , "yyyy-MM-dd"));

                //判断是否迟到
                Date currentPunchDate = wxUserTimeSheetDTO.getPunchTime();
                int currentPunchDateTotalSeconds = currentPunchDate.getHours() * 3600 + currentPunchDate.getMinutes() * 60 + currentPunchDate.getSeconds();

                if (currentPunchDateTotalSeconds <= goworkDateTotalSeconds) {
                    log.info("上班没有迟到");
                    //第一次上班打卡的时候，我们把第一次打卡时间和最后一次打卡时间都设置为第一次上班打卡时间
                    //所以我们把考勤状态设置为早退，当我们在合法时间内进行下班打卡，再把考勤状态设置为正常
                    //firststatus:0 代表正常
                    //laststatus:2 代表早退
                    wxUserTimeSheet.setFirstStatus(0);
                    wxUserTimeSheet.setLastStatus(2);
                } else {
                    log.info("上班迟到");
                    //status:1 代表迟到
                    wxUserTimeSheet.setFirstStatus(1);
                    wxUserTimeSheet.setLastStatus(2);
                }

                wxUserTimeSheetService.save(wxUserTimeSheet);
            } else {
                //当天第N次打卡，那么更新数据
                dbWxUserTimeSheet.setLastPunchTime(punchTimeBackup);
                Date currentPunchDate = punchTimeBackup;
                int currentPunchDateTotalSeconds = currentPunchDate.getHours() * 3600 + currentPunchDate.getMinutes() * 60 + currentPunchDate.getSeconds();

                //获取上班打卡日期
                Date dbPunchDate = dbWxUserTimeSheet.getPunchDate();
                //获取上班打卡是几号
                int punchDateDay = dbPunchDate.getDate();

                //获取当前打卡(或者是下班打卡)是几号, 比如上班是15号,我加班到16号的02:39:00
                int currentPunchDateDay = currentPunchDate.getDate();

                //加班到第二天凌晨的情况
                if (currentPunchDateDay - punchDateDay > 0) {
                    //考勤状态设置为加班
                    dbWxUserTimeSheet.setLastStatus(3);
                } else {
                    //正常下班的情况

                    //将考勤状态设置为加班
                    if (currentPunchDateTotalSeconds - offDateTotalSeconds >= isOverTimeflag) {
                        dbWxUserTimeSheet.setLastStatus(3);
                    }

                    //将考勤状态设置为正常
                    if (currentPunchDateTotalSeconds - offDateTotalSeconds < isOverTimeflag) {
                        dbWxUserTimeSheet.setLastStatus(0);
                    }

                    //将考勤状态设置为早退
                    if (currentPunchDateTotalSeconds < offDateTotalSeconds) {
                        dbWxUserTimeSheet.setLastStatus(2);
                    }
                }

                wxUserTimeSheetService.update(dbWxUserTimeSheet);
            }
        }

        resultVO = ResultVOGenerator.genSuccessResult("打卡成功");
        return resultVO;
    }

    /**
     *
     * @param param openid或者是nickName
     * @param type 查询的类型
     *             1.如果type=0,那么对应的param就是openid
     *             2.如果type=1,那么对应的param就是nickName
     * @param firstDate 开始时间
     * @param lastDate  结束时间
     *                  1.如果firstDate与lastDate都是当天的，那么就是查询当天打卡记录
     *                  2.如果firstDate与lastDate为空，那么就是查询所有打卡记录
     *                  3.如果指定firstDate与lastDate，那么就是查询firstDate - lastDate期间的打卡记录
     * @param page 页码
     * @param size 每页显示的数据
     * @return
     */
    @GetMapping("/search")
    public ResultVO search(@RequestParam(value = "param") String param,
                           @RequestParam(value = "type") String type,
                           @RequestParam(value = "first_date") String firstDate,
                           @RequestParam(value = "last_date") String lastDate,
                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "size", defaultValue = "0") Integer size) {
        ResultVO resultVO = null;

        if (StringUtils.isEmpty(param) || StringUtils.isEmpty(type)
                || (StringUtils.equals(type, "0") && (StringUtils.equals(type, "1")))) {
            resultVO = ResultVOGenerator.genCustomResult(ResultCode.ILLEGAL_PARAMETERS);
            return resultVO;
        }

        //校验firstDate与lastDate在不为''和null的情况下是否为正常参数
        if (!StringUtils.isEmpty(firstDate) || !StringUtils.isEmpty(lastDate)) {
            try {
                DateUtils.parseDate(firstDate, "yyyy-MM-dd");
                DateUtils.parseDate(lastDate, "yyyy-MM-dd");
            } catch (ParseException e) {
                resultVO = ResultVOGenerator.genCustomResult(ResultCode.ILLEGAL_PARAMETERS);
                return resultVO;
            }
        }

        List<WxUserTimeSheet> wxUserTimeSheetList = null;

        Map<String, String> params = new HashMap<>();
        log.info("firstDate = {}", firstDate);
        log.info("lastDate = {}", lastDate);

        params.put("firstDate", firstDate);
        params.put("lastDate", lastDate);
        params.put("param", param);
        params.put("type", type);
        PageHelper.startPage(page, size);

        wxUserTimeSheetList = wxUserTimeSheetService.findByOpenIdOrNickName(params);

        log.info("wxUserTimeSheetList = {}", wxUserTimeSheetList);

        List<WxUserTimeSheetVO> wxUserTimeSheetVOList = new ArrayList<>();

        if (!ListUtils.isEmpty(wxUserTimeSheetList)) {
            for (WxUserTimeSheet wxUserTimeSheet : wxUserTimeSheetList) {
                wxUserTimeSheetVOList.add(new WxUserTimeSheetVO(
                        wxUserTimeSheet.getNickName(), wxUserTimeSheet.getLocationInfo(),
                        wxUserTimeSheet.getFirstPunchTime(), wxUserTimeSheet.getLastPunchTime(),
                        wxUserTimeSheet.getPunchDate(), wxUserTimeSheet.getFirstStatus(), wxUserTimeSheet.getLastStatus()));
            }

            PageInfo pageInfo = new PageInfo(wxUserTimeSheetVOList);
            resultVO = ResultVOGenerator.genSuccessResult(pageInfo);
        } else {
            return ResultVOGenerator.genCustomResult(ResultCode.QUERY_NO_DATA);
        }

        return resultVO;
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable Integer id) {
        wxUserTimeSheetService.deleteById(id);
        return ResultVOGenerator.genSuccessResult();
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody WxUserTimeSheet wxUserTimeSheet) {
        wxUserTimeSheetService.update(wxUserTimeSheet);
        return ResultVOGenerator.genSuccessResult();
    }

    @GetMapping("/detail/{id}")
    public ResultVO detail(@PathVariable Integer id) {
        WxUserTimeSheet wxUserTimeSheet = wxUserTimeSheetService.findById(id);
        return ResultVOGenerator.genSuccessResult(wxUserTimeSheet);
    }

    @GetMapping("/list")
    public ResultVO list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<WxUserTimeSheet> list = wxUserTimeSheetService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultVOGenerator.genSuccessResult(pageInfo);
    }
}
