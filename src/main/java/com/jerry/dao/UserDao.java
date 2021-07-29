package com.jerry.dao;

import com.jerry.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Cjl
 * @date 2021/7/22 14:05
 */

public interface UserDao {
    Integer insert(User user);

    Integer deleteByCardId(Integer cardId);

    Integer updateBalance(@Param("cardId") Integer cardId, @Param("balance") double balance);

    Integer updatePersonalInfo(User user);

    Integer updateAllInfos(User user);

    User findByCardId(Integer cardId);

    List<User> findAll();

    User findByUsername(String username);
}
