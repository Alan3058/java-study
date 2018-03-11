package com.ctosb.study.mybatis.mapper;

import java.util.List;

import com.ctosb.study.mybatis.model.User;


public interface UserMapper {

	int insert(User user);

	int insertAll(List<User> users);

	int delete(int id);

	int deleteByUserName(String userName);

	List<User> getByUserName(String userName);

}
