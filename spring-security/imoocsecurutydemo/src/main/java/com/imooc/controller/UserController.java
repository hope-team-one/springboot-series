package com.imooc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.imooc.dto.User;
import com.imooc.dto.UserQueryCondition;
import com.imooc.exception.UserNotExsitException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController//我这个类可以提供restful服务
public class UserController {
//    @Autowired
//    private ProviderSignInUtils providerSignInUtils;
//    @PostMapping("/user/regist")
//    public void regist(User user, HttpServletRequest request){
//        //注册用户
//        //不管注册用户还是绑定用户 都会拿到用户的唯一标示
//        String userId = user.getUsername();
//        //将userid传给social，入库到userconnect这个表
//        providerSignInUtils.doPostSignUp(userId,new ServletWebRequest(request));
//
//    }

    @DeleteMapping("/user/{id:\\d+}")
    public void delete(@PathVariable(name = "id") String id){
        System.out.println("成功删除id为"+id+"的数据");
    }

    @PutMapping("/user/{id:\\d+}")//和上面的效果一样
    public User update(@Valid @RequestBody User user,BindingResult errors){//模拟mvc的测试用例传入的是username和password正好属于user对象的属性 所以可以用user对象接
        if(errors.hasErrors()){
            //只要是校验出错了就会走到这里来 走到这里了我们就可以获取到错误的信息 也就是属性上面定义的message 然后封转到一个对象里面 返回给前端
            errors.getAllErrors().stream().forEach(error -> {//当校验出错了的时候  这里获得错误，然后我们将错误信息返回给前端 在这里return具体的信息给前端 前端提示就行了
                System.out.println(error.getDefaultMessage());
            });
        }
//        System.out.println("id:"+id);
        System.out.println(user.getUsername()+":"+user.getPassword());
        System.out.println(user.getBirthday());
        return user;
    }

    @PostMapping("/user")
    public User create(@Valid @RequestBody User user, BindingResult errors){//模拟mvc的测试用例传入的是username和password正好属于user对象的属性 所以可以用user对象接
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach(error -> System.out.println(error.getDefaultMessage()));
        }
        System.out.println(user.getUsername()+":"+user.getPassword());
        System.out.println(user.getBirthday());
        user.setId("1");
        return user;
    }
    //处理get请求路径为user的服务端
//    @RequestMapping(value = "/user",method = RequestMethod.GET)
    @GetMapping("/user")//和上面的效果一样
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询服务")
    public List<User> getUser(@ApiParam("用户confition") UserQueryCondition condition, @PageableDefault(page = 2,size = 17,sort = "username,asc") Pageable pageable){//required为false说明请求不必须带着个属性
        System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));
        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getSort());
        List<User> users = new ArrayList<User>();
        users.add(new User("贺武康","88888888"));
        users.add(new User("刘佳丽","88888888"));
        users.add(new User("贺雅萍","88888888"));
        //将user这个list流一下，流成存放id的list，过滤这个id的list，也就是循环这个id的list，判断当id等于11的时候就采集到一个新的list中，作为返回结果
        //map是将某个对象重新组装成一个指定的对象，然后filter可以循环这个对象，去过滤掉不符合条件的值然后采集，map就是重新映射成一个指定的对象
//        List<String> lis = users.stream().map(User::getId).filter(id -> id.equals("11")).collect(Collectors.toList());
        return users;
    }

//    @RequestMapping(value = "/user/{id:\\d+}",method = RequestMethod.GET)//只处理get请求
    @GetMapping("/user/{id:\\d+}")//和上面的效果一样
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable(name = "id") String id){
        throw new UserNotExsitException("user is not exsit");
//        System.out.println(id);
//        System.out.println("进入getInfo服务");
//        User user = new User();
//        user.setUsername("tom");
//        return user;
    }

    @RequestMapping(value = "/social",method = RequestMethod.GET)
    public void so(){
        System.out.println("social");
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void sos(){
        System.out.println("test");
    }
}
