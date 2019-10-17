package controller;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pojo.MyFile;
import pojo.Result;
import pojo.User;
import service.FileServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {
            @Autowired
            private FileServiceImpl fileService;
            @RequestMapping("/upload.action")
            @ResponseBody
            public Result upload(MultipartFile[] files, String currentpath, HttpServletRequest request) {
                fileService.uploadFile(files, currentpath, request);
                Result result = new Result();
                result.setSuccess(true);
        return result;
    }
    @RequestMapping("/getFiles.action")
    @ResponseBody
    public Result getFiles(String path, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String username = user.getUsername();
        List<MyFile> list = fileService.listFiles(username, session);
        Result result = new Result();
        result.setData(list);
        result.setSuccess(true);
        System.out.println("result = " + result);
        return result;
    }

    @RequestMapping("/download.action")
    public ResponseEntity<byte[]> dowload(String downPath, HttpServletRequest request) {
        try {
            String path = request.getServletContext().getRealPath("/");
            User user = (User) request.getSession().getAttribute("user");
            downPath = path + File.separator + "WEB-INF" + File.separator + "files"
                    + File.separator + user.getUsername() + File.separator + downPath;
            File downloadFile = new File(downPath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String filename = null;
            filename = new String(downloadFile.getName().getBytes("utf-8"),
                    "iso-8859-1");
            headers.setContentDispositionFormData("attachment", filename);
            byte[] fileToByArry = FileUtils.readFileToByteArray(downloadFile);
            return new ResponseEntity<byte[]>(fileToByArry,headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //真的
    @RequestMapping("delDirectory.action")
    @ResponseBody
    public Result delDirectory(HttpServletRequest request,
                               String currentPath, String[] directoryName){
        fileService.delDirectory(request, currentPath, directoryName);
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }
    @RequestMapping("renameDirectory.action")
    @ResponseBody
    public Result enameDirectory(HttpServletRequest request,
                                 String currentPath, String srcName,String destName){
        fileService.rnameDirectory(request,srcName,destName);
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }
    @RequestMapping("addDirectory.action")
    @ResponseBody
    public Result addDirectory(String currentPath, String directoryName, HttpSession session){
        fileService.addDirectory(currentPath, directoryName, session);
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }
    @RequestMapping("searchFile.action")
    @ResponseBody
    public Result  searchFile(HttpServletRequest request,String regType ){
        List<MyFile> list = fileService.searchFile(request,regType);
        Result result = new Result();
        result.setSuccess(true);
        result.setData(list);

        return result;
    }
}
