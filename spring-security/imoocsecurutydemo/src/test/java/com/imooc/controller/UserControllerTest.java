package com.imooc.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

//告诉spring这是springboot的测试用例 用springrunner来执行测试用例
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    //会在每个测试用例执行之前执行这个
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenUploadSuccess() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/file")
                .file(new MockMultipartFile("file", "test.txt", "multipart/form-data", "hrllo upload".getBytes("UTF-8"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }
//
//    @Test
//    public void whenQuerySuccess() {
//        try {//使用perform执行这个请求
//            String result = mockMvc.perform(MockMvcRequestBuilders.get("/user")//模拟发出get请求
//                    .param("username", "贺武康")
//                    .param("age", "18")
//                    .param("ageTo", "60")
//                    .param("anything", "hello")
////                    .param("size","15")
////                    .param("page","3")
////                    .param("sort","age,desc")//分页查询结果按照年龄降序排序
//                    .contentType(MediaType.APPLICATION_JSON_UTF8))//使用utf-8格式发送一个json的contenttype请求
//                    .andExpect(MockMvcResultMatchers.status().isOk())//期望返回结果的状态码是200
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))//针对json内容进行判断,返回的是集合 并且集合长度是2
//                    .andReturn().getResponse().getContentAsString();
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void whenGenInfoSuccess() {
//        try {
//            String result = mockMvc.perform(MockMvcRequestBuilders.get("/user/1")//模拟发出get请求
//                    .contentType(MediaType.APPLICATION_JSON_UTF8))//使用utf-8格式发送一个json的contenttype请求
//                    .andExpect(MockMvcResultMatchers.status().isOk())//期望返回结果的状态码是200
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("tom"))//针对json内容进行判断,返回的是集合 并且集合长度是2
//                    .andReturn().getResponse().getContentAsString();
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //测试是否可以传非数字
//    @Test
//    public void whenGetInfoFail() {
//        try {
//            String result = mockMvc.perform(MockMvcRequestBuilders.get("/user/o")//模拟发出get请求
//                    .contentType(MediaType.APPLICATION_JSON_UTF8))//使用utf-8格式发送一个json的contenttype请求
//                    .andExpect(MockMvcResultMatchers.status().is4xxClientError())//期望返回结果的状态码是4**
//                    .andReturn().getResponse().getContentAsString();
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void whenCreateSuccess() {
//        Date date = new Date();
//        System.out.println(date.getTime());//直接传时间戳
//        String content = "{\"username\":\"tom\",\"password\":null,\"birthday\":" + date.getTime() + "}";
//        try {
//            String result = mockMvc.perform(MockMvcRequestBuilders.post("/user")//模拟发出post请求
//                    .contentType(MediaType.APPLICATION_JSON_UTF8).content(content))//使用utf-8格式发送一个json的contenttype请求 post请求 不在url上带参数 通过post传到后台
//                    .andExpect(MockMvcResultMatchers.status().isOk())//期望返回结果的状态码是4**
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))//期望返回的json中包含id为1的数据
//                    .andReturn().getResponse().getContentAsString();
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void whenUpdateSuccess() {
//        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
//        System.out.println(date.getTime());//直接传时间戳
//        String content = "{\"id\":\"1\",\"username\":\"tom\",\"password\":null,\"birthday\":" + date.getTime() + "}";
//        try {
//            String result = mockMvc.perform(MockMvcRequestBuilders.put("/user/1")//模拟发出put请求
//                    .contentType(MediaType.APPLICATION_JSON_UTF8).content(content))//使用utf-8格式发送一个json的contenttype请求 post请求 不在url上带参数 通过post传到后台
//                    .andExpect(MockMvcResultMatchers.status().isOk())//期望返回结果的状态码是4**
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))//期望返回的json中包含id为1的数据
//                    .andReturn().getResponse().getContentAsString();
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void whenDeleteSuccess() {
//        try {
//            mockMvc.perform(MockMvcRequestBuilders.delete("/user/888")//模拟发出delete请求
//                    .contentType(MediaType.APPLICATION_JSON_UTF8))//使用utf-8格式发送一个json的contenttype请求 post请求 不在url上带参数 通过post传到后台
//                    .andExpect(MockMvcResultMatchers.status().isOk())//期望返回结果的状态码是4**
//            ;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
