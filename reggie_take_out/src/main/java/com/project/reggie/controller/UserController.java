package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.reggie.common.R;
import com.project.reggie.entity.User;
import com.project.reggie.service.UserService;
import com.project.reggie.utils.SMSUtils;
import com.project.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //get phone number
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)) {
            //generate verification code
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //call aliyun sms service
            //SMSUtils.sendMessage("瑞吉外卖","", phone, code);

            //save generated code
            session.setAttribute(phone, code);

            //将生成的验证码缓存到redis中，并设置时效5分钟
//            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success("SMS MSg Send Success!");
        }

        return R.error("Failure");


    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //get phone number
        String phone = map.get("phone").toString();

        //get verification code
//        String code = map.get("code").toString();

        //get code from Session
//        Object codeInSession = session.getAttribute(phone);

        //从redis中获取缓存的验证码
//        Object codeInsession = redisTemplate.opsForValue().get(phone);

        //compare verification code
//        if (codeInSession != null && codeInSession.equals(code)) {
            //judge if phone is new user, if yes auto register
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            //如果成功删除redis缓存
//            redisTemplate.delete(phone);


            return R.success(user);

//        }


//        return R.error("Login Failure!");


    }
}
