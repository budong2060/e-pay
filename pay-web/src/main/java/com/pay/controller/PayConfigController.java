package com.pay.controller;

import com.pay.common.Pager;
import com.pay.domain.PayConfig;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import com.pay.service.PayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/**
 * Created by admin on 2016/7/8.
 */
@RestController
@RequestMapping("/pay/config")
public class PayConfigController {

    @Autowired
    private PayConfigService payConfigService;

    /**
     * 添加第三方支付账户配置接口
     * @param payConfig
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object save(@Valid @RequestBody PayConfig payConfig) {
//        PayConfig payConfig = new PayConfig();
//        payConfig.setId(1);
//        payConfig.setMchName("宁远科技");
//        payConfig.setMchId("100");
//        payConfig.setPayWay(10);
//        payConfig.setPayScene(1);
//        payConfig.setStatus(1);
//        payConfig.setConfig(new HashMap<String, String>(){
//            {
//                put("pay.alipay.partner", "2088911130299423");
//                put("pay.alipay.key", "yiptzwcwk3nsbh2h3h3s0eiv4kynulcp");
//                put("pay.alipay.seller.email", "BDFLY6@163.COM");
//                put("pay.alipay.account.name", "湖南省蓝蜻蜓网络科技有限公司");
//            }
//        });
        payConfigService.save(payConfig);

        return payConfig;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable Integer id, @Valid @RequestBody PayConfig payConfig) {
        if(null == id) {
            throw new PayException(PayResultEnum.ILLEGAL_ARGUMENTS);
        }
        payConfig.setId(id);
        return payConfigService.update(payConfig);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable Integer id) {
        if(null == id) {
            throw new PayException(PayResultEnum.ILLEGAL_ARGUMENTS);
        }
        return payConfigService.delete(id);
    }
//
//    @JmxMonitor
//    @ResponseBody
//    @RequestMapping(value = "/{account}/{payWay}", method = RequestMethod.GET)
//    public Object find(@PathVariable String account, @PathVariable int payWay, Integer payScene) {
//        PayConfig payConfig = payConfigService.findOnly(account, payWay, Enabled.Y.toString(), payScene);
//        Result<Object> result = new Result<>();
//        result.setData(payConfig);
//        result.setResult(PayCons.ResultCons.SUCCESS);
//        return result;
//    }
//
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object findByPage(Integer page, Integer size, String accountNo) {
        if (null == page || page <= 0) {
            page = 0;
        }
        if (null == size || size <= 0) {
            size = Pager.DEFAULT_PAGE_NUM;
        }
        return payConfigService.getByPage(accountNo, page, size);
    }
//
//    @ResponseBody
//    @RequestMapping(value = "/way/scene", method = RequestMethod.GET)
//    public Result<Object> findPayWays() {
//        Result<Object> result = new Result<>();
//        Map<String, List<KvEntity>> map = new HashMap<String, List<KvEntity>>() {{
//           put("payWay", PayWayType.getList());
//           put("payScene", PayScene.getList());
//        }};
//        result.setResult(PayCons.ResultCons.SUCCESS);
//        result.setData(map);
//        return result;
//    }
//
//    /**
//     * 参数校验异常处理
//     * @param error
//     * @return
//     */
//    @ResponseBody
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
//        BaseResult result = new BaseResult(PayResultEnum.ILLEGAL_ARGUMENTS);
//        result.setDescription(error.getMessage());
//        BindingResult bindingResult = error.getBindingResult();
//        if(null != bindingResult) {
//            FieldError fieldError = bindingResult.getFieldError();
//            if(null != fieldError) {
//                result.setDescription(fieldError.getDefaultMessage());
//            }
//        }
//        return result;
//    }

}
