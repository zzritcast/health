package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约处理
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

//在线体检预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //获取手机号
        String telephone = (String) map.get("telephone");
        //从redis中查询出保存的验证码  手机号 + redis 001
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取页面输入框验证码
        String validateCode = (String) map.get("validateCode");
        //将用户输入的验证码与redis中保存的验证码进行对比
        if(validateCodeInRedis != null && validateCode != null && validateCodeInRedis.equals(validateCode)){
            //设置预约类型 电话预约  微信预约
            map.put("orderType",Order.ORDERTYPE_WEIXIN);

            Result result = new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            try{
                //对比成功 调用服务完成预约业务处理
                result = this.orderService.order(map);  //通过dubbo远程调用服务实现在线预约业务处理
            }catch(Exception e){
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()){
                //预约成功  可以给用户回复短信  第二个模板code
                try{
                    //预约成功，您预约的口令是666666    报错 请用 VALIDATE_CODE  同一个模板
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, ValidateCodeUtils.generateValidateCode4String(6));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return  result;

        }else{
            //如果不成功，将信息返回页面
            return  new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
    //根据预约id查询当前预约详情
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map map = this.orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map );
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL );
        }



    }
}
