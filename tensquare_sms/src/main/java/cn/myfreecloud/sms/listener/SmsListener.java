package cn.myfreecloud.sms.listener;

import cn.myfreecloud.sms.util.SmsUtil;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;
    @Value("${aliyun.sms.sign_name}")
    private String sign_name;

    @RabbitHandler
    public void getMessage(Map<String, Object> map) {
        //电话号码
        String mobile = map.get("mobile").toString();
        System.out.println("电话号码:" + mobile);
        //短信验证码
        String code = map.get("code").toString();
        System.out.println("短信验证码:" + code);


        try {
            //发送短信
            smsUtil.sendSms(mobile, template_code, sign_name,"{\"code\":\"" + code + "\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }

}
