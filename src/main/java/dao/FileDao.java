package dao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pojo.MyFile;
@Mapper
@Repository
public interface FileDao {
    public void insertfile(MyFile file);
    public void delfile(MyFile file);
    public void updatefile(MyFile file);
}
