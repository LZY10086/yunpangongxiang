package Util;
import java.util.HashMap;
import java.util.Map;
public class FileTypeUtil {
    public static Map<String,String> fileTypeMap=new HashMap<>();
    static {
        fileTypeMap.put("jpg","image");//图片
        fileTypeMap.put("png","image");
        fileTypeMap.put("gif","image");
        fileTypeMap.put("jpeg", "image");
        fileTypeMap.put("bmp","image");
        fileTypeMap.put("doc","office"); //文档
        fileTypeMap.put("docx","office");
        fileTypeMap.put("mp4","vido");//视频vido
        fileTypeMap.put("3gp","vido");
        fileTypeMap.put("wmv","vido");
        fileTypeMap.put("avi","vido");
        fileTypeMap.put("mp3","audio");//音乐audio
        fileTypeMap.put("mpeg","audio");
        fileTypeMap.put("txt","docum");//文本docum
        //其他//file
        fileTypeMap.put("folder-open","file");
    }
}
