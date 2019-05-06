package com.codecool.web.dao.simple;


import com.codecool.web.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleTaskDao extends AbstractDao {
    public SimpleTaskDao(Connection connection) {
        super(connection);
    }

    private Task fetchTask(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String content = resultSet.getString("content");

        Task task = new Task(id, title, content);
        return task;
    }

    public List<Task> findAll() throws SQLException {
        String sql = "SELECT * FROM task";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
            List<Task> tasks = new ArrayList<>();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {

                tasks.add(fetchTask(rs));
            }
            return tasks;
        }
    }

    public List<Task> findByScheduleId(int scheduleId) throws SQLException{
        String sql = "SELECT * FROM task LEFT JOIN schedule_task ON task.id = task_id WHERE schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scheduleId);
            statement.execute();
            List<Task> tasks = new ArrayList<>();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                tasks.add(fetchTask(rs));
            }
            return tasks;
        }
    }

    public Task findById(int taskId) throws SQLException {
        String sql = "SELECT * FROM task WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {

                return fetchTask(rs);
            } else {
                return null;
            }
        }
    }

    public int add(String title, String content) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        String sql = "INSERT INTO task (title, content) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setString(1, title);
            statement.setString(2, content);
            statement.executeUpdate();
            return fetchGeneratedId(statement);
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

}
