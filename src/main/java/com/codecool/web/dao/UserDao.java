package com.codecool.web.dao;

import com.codecool.web.model.Role;
import com.codecool.web.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao{

    List<User> findAllExceptCurrent(int id) throws SQLException;

    User findById(int id) throws SQLException;

    User add(String name, String password, String email, Role role, int workLoad) throws SQLException;

    User findByEmailPassword(String email, String password) throws SQLException;
}
