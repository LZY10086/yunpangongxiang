package service;
import org.springframework.web.multipart.MultipartFile;
import pojo.MyFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
public interface FileService {
    public void uploadFile(MultipartFile[] files, String currentpath, HttpServletRequest request);
    public List<MyFile> listFiles(String username, HttpSession session);
    public void delDirectory(HttpServletRequest request,String currentpath,String[] directoryName);
    public void rnameDirectory(HttpServletRequest request,String srcName,String destName);
    public void addDirectory(String currentPath,String directoryName,HttpSession session);
    public  List<MyFile> searchFile(HttpServletRequest request, String regType);
}
