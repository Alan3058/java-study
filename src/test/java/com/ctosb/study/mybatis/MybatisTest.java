
package com.ctosb.study.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.ctosb.study.mybatis.mapper.UserMapper;
import com.ctosb.study.mybatis.model.User;

/**
 * mybatis test
 * @author liliangang-1163
 * @date 2018/3/11 9:40
 * @see
 */
public class MybatisTest {

	@Test
	public void testMybatis() throws IOException {
		InputStream inputStream = Resources.getResourceAsStream("com/ctosb/study/mybatis/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		int result = mapper.insert(new User().setUserName("test").setPassword("123456"));
		sqlSession.commit();
		Assert.assertTrue(result > 0);
	}
}
