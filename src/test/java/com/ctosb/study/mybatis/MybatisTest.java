
package com.ctosb.study.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
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

	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;

	@Before
	public void before() throws IOException {
		InputStream inputStream = Resources.getResourceAsStream("com/ctosb/study/mybatis/mybatis-config.xml");
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		sqlSession = sqlSessionFactory.openSession();
		testInsert();
	}

	@Test
	public void testInsert() throws IOException {
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		int result = mapper.insert(new User().setUserName("test").setPassword("123456"));
		sqlSession.commit();
		Assert.assertTrue(result > 0);
	}

	@Test
	public void testCache() throws IOException {
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		// 测试缓存
		List<User> users = mapper.getByUserName("test");
		// 这里不会发起sql
		users = mapper.getByUserName("test");
		Assert.assertTrue(users.size() > 0);
	}

	@Test
	public void testAnnotation() throws IOException {
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		// 测试缓存
		List<User> users = mapper.selectAll();
		// 这里不会发起sql
		Assert.assertTrue(users.size() > 0);
	}
}
