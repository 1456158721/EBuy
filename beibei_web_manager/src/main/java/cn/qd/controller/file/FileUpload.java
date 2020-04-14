package cn.qd.controller.file;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileUpload {

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.callback}")
    private String callback;

    @Autowired
    private OSSClient ossClient;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, String fileDir){

        String key = UUID.randomUUID() + file.getOriginalFilename();
        try{
            ossClient.putObject(bucketName, key, file.getInputStream());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return callback +"/" + key;
    }

}
