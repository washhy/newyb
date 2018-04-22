package com.yiban.erp.controller.warehouse;

import com.alibaba.fastjson.JSONObject;
import com.yiban.erp.constant.CheckPlanConstant;
import com.yiban.erp.dao.RepertoryCheckFormMapper;
import com.yiban.erp.dao.RepertoryCheckPlanDetailMapper;
import com.yiban.erp.dao.RepertoryCheckPlanMapper;
import com.yiban.erp.entities.*;
import com.yiban.erp.service.warehouse.RepertoryCheckPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repertory/check")
public class RepertoryCheckController {

    private static final Logger logger = LoggerFactory.getLogger(RepertoryCheckController.class);

    @Autowired
    private RepertoryCheckPlanService repertoryCheckPlanService;
    @Autowired
    private RepertoryCheckPlanMapper repertoryCheckPlanMapper;
    @Autowired
    private RepertoryCheckPlanDetailMapper repertoryCheckPlanDetailMapper;
    @Autowired
    private RepertoryCheckFormMapper repertoryCheckFormMapper;
    //根据条件获取入库单list
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> list(@AuthenticationPrincipal User user,
        @RequestParam(name = "checkType", required = false) Integer checkType,
        @RequestParam(name = "warehouseId",required = false) Integer warehouseId,
        @RequestParam(name = "startDate", required = false) Date startDate,
        @RequestParam(name = "endDate", required = false) Date endDate
    ){
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("companyId", user.getCompanyId());
        requestMap.put("checkType", checkType);
        requestMap.put("warehouseId", warehouseId);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        List<RepertoryCheckPlan> list = repertoryCheckPlanMapper.getCheckPlanList(requestMap);
        JSONObject result = new JSONObject();
        result.put("data", list);
        return ResponseEntity.ok().body(result.toJSONString());
    }



    //查询待执行的盘点计划
    @RequestMapping(value = "/doList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doList(@AuthenticationPrincipal User user,
                                       @RequestParam(name = "checkType", required = false) Integer checkType,
                                       @RequestParam(name = "warehouseId",required = false) Integer warehouseId,
                                       @RequestParam(name = "startDate", required = false) Date startDate,
                                       @RequestParam(name = "endDate", required = false) Date endDate
    ){
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("companyId", user.getCompanyId());
        //待处理计划状态=0
        requestMap.put("state", CheckPlanConstant.PLAN_UNCHECK);
        requestMap.put("checkType", checkType);
        requestMap.put("warehouseId", warehouseId);
        requestMap.put("startDate", startDate);
        requestMap.put("endDate", endDate);
        List<RepertoryCheckPlan> list = repertoryCheckPlanMapper.getCheckPlanList(requestMap);
        JSONObject result = new JSONObject();
        result.put("data", list);
        return ResponseEntity.ok().body(result.toJSONString());
    }

    //根据条件获取入库单list
    @RequestMapping(value = "/orderPartList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderPartList(@AuthenticationPrincipal User user,
        @RequestParam(name = "checkPlanId", required = true) long checkPlanId
    ){
        RepertoryCheckPlan checkinfo= repertoryCheckPlanMapper.selectByPrimaryKey(checkPlanId);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("checkPlanId", checkPlanId);
        List<RepertoryCheckForm>  partlist = repertoryCheckFormMapper.getCheckPlanFormList(requestMap);
        JSONObject result = new JSONObject();
        result.put("data", checkinfo);
        result.put("checkPartList", partlist);
        return ResponseEntity.ok().body(result.toJSONString());
    }

    //根据条件获取入库单list
    @RequestMapping(value = "/orderinfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderinfo(@AuthenticationPrincipal User user,
        @RequestParam(name = "checkPlanId", required = true) Long checkPlanId
    ) {
        RepertoryCheckPlan checkinfo= repertoryCheckPlanMapper.selectByPrimaryKey(checkPlanId);
        JSONObject result = new JSONObject();
        result.put("data", checkinfo);
        return ResponseEntity.ok().body(result.toJSONString());
    }

    //查询盘点单全部信息
    @RequestMapping(value = "/orderinfoList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderinfoList(@AuthenticationPrincipal User user,
                @RequestParam(name = "checkPlanId", required = true) Long checkPlanId
    ) throws Exception {
        JSONObject result = new JSONObject();
        result =repertoryCheckPlanService.getCheckPlanAndDetailJSON(checkPlanId);
        return ResponseEntity.ok().body(result.toJSONString());
    }


    //根据盘点计划和拼命查询选择数据
    @RequestMapping(value = "/orderinfoList4Search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderinfoList4Search(@AuthenticationPrincipal User user,
        @RequestParam(name = "checkPlanId", required = true) Long checkPlanId,
        @RequestParam(name = "goodSearch", required = false) String goodSearch
    ) throws Exception {
        JSONObject result = new JSONObject();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("checkPlanId", checkPlanId);
        requestMap.put("goodSearch", goodSearch);
        result = repertoryCheckPlanService.getCheckPlanDetail4SearchJSON(requestMap);
        return ResponseEntity.ok().body(result.toJSONString());
    }

    //查询某人执行的盘点单进度
    @RequestMapping(value = "/orderinfoListByUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderinfoListByUser(@AuthenticationPrincipal User user,
        @RequestParam(name = "checkPlanId", required = true) Long checkPlanId
    ) throws Exception {
        JSONObject result = new JSONObject();
        result =repertoryCheckPlanService.getCheckPlanAndDetailJSONByUser(user,checkPlanId);
        return ResponseEntity.ok().body(result.toJSONString());
    }

    //查询盘点单进度
    @RequestMapping(value = "/orderinfoListFormId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderinfoListFormId(@AuthenticationPrincipal User user,
        @RequestParam(name = "formId", required = true) Long formId
    ) throws Exception {
        JSONObject result = new JSONObject();
        result =repertoryCheckPlanService.getCheckPlanAndDetailJSONByFormId(user,formId);
        return ResponseEntity.ok().body(result.toJSONString());
    }



    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> add(@AuthenticationPrincipal User user,
        @RequestBody RepertoryCheckPlan repertoryCheckPlan)throws Exception  {
        logger.info("ADD new repertoryCheckPlan order, userId={}", user.getId());
        repertoryCheckPlanService.saveCheckPlan(user,repertoryCheckPlan);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/doCheckDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doCheckDetail(@AuthenticationPrincipal User user,
        @RequestBody RepertoryCheckPlanDetail repertoryCheckPlanDetail)throws Exception  {
        JSONObject result = new JSONObject();
        result=repertoryCheckPlanService.doCheckDetail(user,repertoryCheckPlanDetail);
        return ResponseEntity.ok().body(result.toJSONString());
    }

    //盘盈数据-可能包含本次盘点范围外的产品
    @RequestMapping(value = "/doCheckDetailMore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doCheckDetailMore(@AuthenticationPrincipal User user,
                                                @RequestBody RepertoryCheckPlanDetail repertoryCheckPlanDetail)throws Exception  {
        JSONObject result = new JSONObject();
        result=repertoryCheckPlanService.doCheckDetailMore(user,repertoryCheckPlanDetail);
        return ResponseEntity.ok().body(result.toJSONString());
    }
    //增加盘盈记录
    @RequestMapping(value = "/addPlanDetaile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addPlanDetaile(@AuthenticationPrincipal User user,
                                      @RequestBody RepertoryCheckPlanDetail repertoryCheckPlanDetail)throws Exception  {
        logger.info("ADD new repertoryCheckPlan order, userId={}", user.getId());

        repertoryCheckPlanService.saveCheckPlanDetail(user,repertoryCheckPlanDetail);
        return ResponseEntity.ok().build();
    }
    //查询盘点单全部信息
    @RequestMapping(value = "/getInfo4Pass", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getInfo4Pass(@AuthenticationPrincipal User user,
        @RequestParam(name = "checkPlanId", required = true) Long checkPlanId
    ) throws Exception {
        JSONObject result = new JSONObject();
        result =repertoryCheckPlanService.getInfo4PassJSON(checkPlanId);
        return ResponseEntity.ok().body(result.toJSONString());
    }



    @RequestMapping(value = "/checkPlanPass", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkPlanPass(@RequestBody Map requestMap,
                                         @AuthenticationPrincipal User user) throws Exception{

        //Integer state = (Integer) requestMap.get("state");
        String comment = (String) requestMap.get("comment");
        String manager = (String) requestMap.get("manager");
        String managerNote = (String) requestMap.get("managerNote");
        String finance = (String) requestMap.get("finance");
        String financeNote = (String) requestMap.get("financeNote");
        Long planId = new Long((String)requestMap.get("planId"));
        repertoryCheckPlanService.setCheckPlanPass(user,planId,comment,manager,managerNote,finance,financeNote);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/checkFormPass", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkFormPass(@AuthenticationPrincipal User user,
                                                @RequestBody RepertoryCheckForm checkForm
                                                ) throws Exception{
        repertoryCheckPlanService.setCheckFormPass(user,checkForm);
        return ResponseEntity.ok().build();
    }

}
