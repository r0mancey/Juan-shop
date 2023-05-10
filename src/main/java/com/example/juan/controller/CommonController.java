package com.example.juan.controller;

import com.example.juan.common.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@Api(tags = {"文件上传下载模块"})
@RequestMapping("/common")
public class CommonController {

    @Value("${juan.upload-path}")
    private String uploadPath;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到其他位置，否则请求结束后将被删除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();

        //截取后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件重名导致覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(uploadPath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //前端需要此文件名，以便之后存入数据库
        return R.success(fileName);
    }


    /**
     * 上传文件回显
     * 不需要返回值，通过输出流返回浏览器
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(uploadPath+name));

            //通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ( (len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

