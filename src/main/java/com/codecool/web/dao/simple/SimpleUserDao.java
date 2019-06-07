package com.codecool.web.dao.simple;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.Role;
import com.codecool.web.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleUserDao extends AbstractDao implements UserDao {

    public SimpleUserDao(Connection connection) {
        super(connection);
    }

    public List<User> findAllExceptCurrent(int id) throws SQLException {
        String sql = "SELECT id, name, email, role FROM users WHERE id != ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                users.add(fetchUserWithoutPw(rs));
            }
        }
        return users;
    }

    @Override
    public User findById(int id) throws SQLException {
        String sql = "SELECT id, name, email, role FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            rs.next();
            return fetchUserWithoutPw(rs);
        }
    }

    @Override
    public User add(String name, String password, String email, Role role,int workLoad) throws SQLException {
        if (name == null || "".equals(name)) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (password == null || "".equals(password)) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (email == null || "".equals(email)) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO users (email, password, name, role) VALUES (?, crypt(?, gen_salt('bf', ?)), ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setInt(3, workLoad);
            statement.setString(4, name);
            statement.setInt(5, role.getValue());
            executeInsert(statement);
            int id = fetchGeneratedId(statement);
            connection.commit();
            return new User(id, name, password, email, role);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public User findByEmailPassword(String email, String password) throws SQLException {
        if (email == null || "".equals(email)) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        String sql = "SELECT * FROM Users where email = ? AND password = crypt(?, password)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchUser(resultSet);
                }
            }
        }
        return null;
    }

    private User fetchUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        Role role = resultSet.getInt("role") == 1 ? Role.ADMIN : Role.REGULAR;
        return new User(id, name, password, email, role);
    }

    private User fetchUserWithoutPw(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String name = resultSet.getString("name");
        Role role = resultSet.getInt("role") == 1 ? Role.ADMIN : Role.REGULAR;
        return new User(id, name, email, role);
    }

}
