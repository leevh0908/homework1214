package cn.com.taiji.redis.test.controller;

import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
public class MsgController {

    @Autowired
    private StringRedisTemplate template;



    int i;

    @RequestMapping("/sendMsg")
    public String sendMsg(String phoneNum) {
        if (!template.hasKey(phoneNum)) {
            i = 1;
            String s = String.valueOf(i);
            template.opsForValue().append(phoneNum, s);  //设置手机号验证码获取次数 初始值为1




            String startTime = String.valueOf(System.currentTimeMillis());
            template.opsForValue().set(phoneNum+"startTime", startTime);


            System.out.println(template.opsForValue().get(phoneNum));
            System.out.println(startTime);

            System.out.println(template.opsForValue().get(phoneNum+"startTime"));
            return "发送成功";
        } else {

            i = Integer.parseInt(template.opsForValue().get(phoneNum));
            i++;
            if (i > 3) {
                long time1=Long.parseLong(template.opsForValue().get(phoneNum+"startTime"));
				long time =System.currentTimeMillis()-time1;
                System.out.println(time);
                if (time >= new Long(1000 * 60)) {
                    i = 1;
                    template.opsForValue().set(phoneNum, String.valueOf(i));
					template.opsForValue().set(phoneNum+"startTime", String.valueOf(System.currentTimeMillis()));
                    return "请求成功";
                } else {
                    return "请求次数过多";
                }


            } else {
                String s = String.valueOf(i);
                template.opsForValue().set(phoneNum, s); //第二次请求value值加 1
                //System.out.println( template.opsForValue().get(phoneNum));
                return "发送成功，已发送" + template.opsForValue().get(phoneNum);
            }


        }


    }
}