import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class tgen {

    @Test
    public void t1() throws IOException {
        UserDOMapper userDOMapper ;
        InputStream resource ;
        SqlSession sqlSession ;
        //1、使用MybatisConfig.xml 作为 一个 字符串，以后方便修改
        String config = "mapping/UserDOMapper.xml" ;
        //2、加载资源
        resource = Resources.getResourceAsStream(config);
        //3、构建 SqlSessionFactoryBuilder
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder() ;
        //4、构建 SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(resource);
        //5、获取 sqlSession
        sqlSession = sqlSessionFactory.openSession();
        //6、获取CountryMapper
        userDOMapper = sqlSession.getMapper(UserDOMapper.class);
        UserDO userDO =new UserDO();
        userDO.setAge(1);
        int i = userDOMapper.insertSelective(userDO);
        sqlSession.commit();

    }
}
