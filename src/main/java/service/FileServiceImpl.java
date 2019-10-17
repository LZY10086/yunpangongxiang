package service;
import Util.FileTypeUtil;
import dao.FileDao;
import dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pojo.MyFile;
import pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class FileServiceImpl implements FileService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FileDao fileDao;
    @Override
    public void uploadFile(MultipartFile[] files, String currentpath, HttpServletRequest request) {
        for(MultipartFile file:files) {
            User user = (User) request.getSession().getAttribute("user");
            String username = user.getUsername();
            String path = request.getServletContext().getRealPath("/");
            path = path + File.separator + "WEB-INF" + File.separator + "files"
                    + File.separator + username + File.separator;
            File f = new File(path, file.getOriginalFilename());
            if (!f.exists()) {
                f.mkdirs();
                try {
                    file.transferTo(f);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //更新user表中的countSize信息
                String countSize1 = user.getCountSize();
                double d1 = Double.valueOf(countSize1.split("B")[0]);
                double d2 = d1 + f.length();
                String countSize2 = d2 + "B";
                user.setCountSize(countSize2);
                System.out.println("user = " + user);
                userDao.updateSize(user);
                //更新数据库中file表的信息
                MyFile mf = new MyFile();
                mf.setUserName(username);
                mf.setFilePath(username+File.separator+file.getOriginalFilename());
                fileDao.insertfile(mf);
            }
        }
    }
    @Override
    public List<MyFile> listFiles(String username, HttpSession session) {
        SimpleDateFormat slf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String path=session.getServletContext().getRealPath("/");
        path=path+File.separator+"WEB-INF"+File.separator+"files"+File.separator+username+File.separator;
        File[] files = new File(path).listFiles();
        List<MyFile> lists = new ArrayList<MyFile>();
        for (File file : files) {
            MyFile f = new MyFile();
            f.setFileName(file.getName());
            f.setLastTime(slf.format(file.lastModified()));
            f.setFilePath(file.getPath());
            f.setCurrentPath(path);
            if (file.isDirectory()) {
                f.setFileSize("-");
            } else {
                f.setFileSize(file.length()+"");
            }
            f.setFileType(getFileType(file));
            lists.add(f);
        }
        return lists;
    }
    private   String getFileType(File file) {
        if(file.isDirectory()){
            return "folder-open";
        }
        String fileName = file.getPath();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return suffix;
    }
    @Override//文件删除
    public void delDirectory(HttpServletRequest request, String currentpath, String[] directoryName) {
        User user = (User) request.getSession().getAttribute("user");
        String path = request.getServletContext().getRealPath("/");
        String username = user.getUsername();
        for (String s : directoryName) {
            path = path + File.separator + "WEB-INF" + File.separator + "files" +
                    File.separator + username + File.separator + currentpath + s;
            File file = new File(path);
            System.out.println("path = " + path);
            //更新user表中的countSize信息
            String countSize1 = user.getCountSize();
            double d1 = Double.valueOf(countSize1.split("B")[0]);
            double d2 = d1 + file.length();
            String countSize2 = d2 + "B";
            user.setCountSize(countSize2);
            userDao.updateSize(user);
            //更新数据库中file表的信息
            MyFile mf = new MyFile();
            mf.setUserName(username);
            mf.setFilePath(username + File.separator + s);
            fileDao.delfile(mf);
            boolean delete = file.delete();
        }
    }
    @Override
    public void rnameDirectory(HttpServletRequest request, String srcName, String destName) {
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getUsername();
        String filepath=request.getServletContext().getRealPath("/")+File.separator
                + "WEB-INF" + File.separator + "files" + File.separator + username + File.separator ;
        String filename=filepath+srcName;
        File file = new File(filename);
        filename=filepath+destName;
        File file1=new File(filename);
        file.renameTo(file1);
        MyFile myFile = new MyFile();
        myFile.setUserName(username);
        myFile.setFilePath(username+File.separator+destName);
        myFile.setSrcName(username+File.separator+srcName);
        fileDao.updatefile(myFile);
    }
    @Override
    public void addDirectory(String currentPath, String directoryName, HttpSession session) {
        System.out.println("currentPath = " + currentPath);
        String username = ((User) session.getAttribute("user")).getUsername();
        String path=session.getServletContext().getRealPath("/")+File.separator+"WEB-INF"+File.separator
                +"files"+File.separator+username;
        if(currentPath==null){
            currentPath="/";
        }
        path=path+currentPath;
        System.out.println("path = " + path);
        File file = new File(path, directoryName);
        if(!file.exists()){
            file.mkdirs();
        }
    }
    //根据文件格式分类，，查找某一类型的全部文件
    @Override
    public List<MyFile> searchFile(HttpServletRequest request, String regType) {
        //文件信息链表
        List<MyFile> list=new ArrayList<>();
        //格式化时间
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //拼接文件存放地址
        String path = request.getServletContext().getRealPath("/");
        //获取用户名
        User user= (User) request.getSession().getAttribute("user");
        String username=user.getUsername();
        path=path+ File.separator+"WEB-INF"+File.separator+"files"+File.separator+username;
        //从path路径下取出所有文件，存放到数组files
        File[] files = new File(path).listFiles();

        if (files!=null){
            //遍历文件数
            for (File file:files) {
                //获取文件后缀
                String type=getFileType(file);
                if (type==null){
                    type="file";
                }

                //判断该文件是否为要查找的文件类型
                if ((FileTypeUtil.fileTypeMap.get(type)).equals(regType)){
                    //将文件信息取得，并存放到fileCustom对象，使所有对象形成list
                    MyFile fileCustom = new MyFile();
                    fileCustom.setFileName(file.getName());
                    fileCustom.setLastTime(time.format(file.lastModified()));
                    fileCustom.setFilePath(file.getPath());
                    fileCustom.setCurrentPath(path);
                    if (file.isDirectory()){
                        fileCustom.setFileSize("-");
                    }else {
                        fileCustom.setFileSize(file.length()+"");
                    }
                    fileCustom.setFileType(getFileType(file));
                    list.add(fileCustom);
                }
            }
        }
        return list;
    }

}
