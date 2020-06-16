package com.imooc.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.imooc.controller.UserController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class MockServer {
    public static void main(String[] args) throws IOException {
        WireMock.configureFor(8062);//配置端口
        WireMock.removeAllMappings();//移除之前所有配置
        mock("/order/1","01");mock("/order/2","02");mock("/order/3","03"); }
    public static void mock(String url,String file) throws IOException{
        ClassPathResource resource = new ClassPathResource("mockResponse/"+file+".txt");
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(),"UTF-8"),"\n");
        WireMock.stubFor(//get请求
                WireMock.get(WireMock.urlPathEqualTo(url))//请求的url
                        .willReturn(WireMock.aResponse()//服务器返回的东西
                                .withBody(content).withStatus(200)));
    }
}
