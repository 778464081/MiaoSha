package com.miaoshaproject.contronll;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.miaoshaproject.contronll.viewobject.UserVo;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(value = "telphone")String telphone,
                                     @RequestParam(value = "otpCode")String otpCode,
                                     @RequestParam(value = "name")String name,
                                     @RequestParam(value = "gender")String gender,
                                     @RequestParam(value = "age")Integer age,
                                     @RequestParam(value = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpCode相符合
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证不符合");
        }
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterModel("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Base64.Encoder encoder = Base64.getEncoder();
        String newstr = encoder.encodeToString(md5.digest(str.getBytes("utf-8")));

        return newstr;
    }

    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType logig(@RequestParam(value = "telphone")String telphone,
                                  @RequestParam(value = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)||
                StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validdateLogin(telphone, this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }


    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(value = "telphone") String telphone){
        //需要按照一定的要求生成验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt+=10000;
        String otpCode = String.valueOf(randomInt);


        //将otp验证码同对应的用户的手机关联
        httpServletRequest.getSession().setAttribute(telphone,otpCode);

        //将otp验证码通过短信通道发给用户，省略
        System.out.println("telphone:" + telphone+ "&otpCode : " +otpCode  );

        return CommonReturnType.create(null);
    }
    
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id ) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        if (userModel==null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //返回只给前端用户需要看到的信息
        UserVo userVo = convertFromModel(userModel);

        //返回通用对象
        return CommonReturnType.create(userVo);
    }

    private UserVo convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }

        UserVo userVo = new UserVo();

        BeanUtils.copyProperties(userModel,userVo);
        return userVo;
    }
}
