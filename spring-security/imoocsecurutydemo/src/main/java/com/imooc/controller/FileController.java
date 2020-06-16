package com.imooc.controller;

import com.imooc.dto.FileInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {
    private static String folder = "D:\\SpringSecurity\\imoocsecurutydemo\\src\\main\\java\\com\\imooc\\controller";

    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {
        System.out.println(file.getName());//传上来参数的名字
        System.out.println(file.getOriginalFilename());//原始文件名
        System.out.println(file.getSize());
        File localFile = new File(folder,new Date().getTime()+".txt");
        file.transferTo(localFile);//传入的文件写到本地
        return new FileInfo(localFile.getAbsolutePath());
    }

    @GetMapping("/{id}")
    public void downloac(@PathVariable String id ,HttpServletRequest request, HttpServletResponse response){
        try (//流声明在try括号里，我就不用去写流的关闭，jdk自动关 不用去写close()
                InputStream inputStream = new FileInputStream(new File(folder,id+".txt"));
                OutputStream outputStream = response.getOutputStream();
                ){
            //定义为下载
            response.setContentType("application/x-download");
            response.addHeader("Content-Dispasition","attachment;filename=test.txt");
            //把文件输入流copy到输出流里面
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
