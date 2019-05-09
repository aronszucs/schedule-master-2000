package com.codecool.web.dao;

import com.codecool.web.dto.TaskDto;
import com.codecool.web.model.Task;
import com.codecool.web.service.exception.ServiceException;

import java.sql.SQLException;
import java.util.List;

public interface TaskDao {

    List<Task> findAll() throws SQLException;

    Task findById(int id) throws SQLException;

    List<Task> findByUserId(int userId) throws SQLException;

    List<TaskDto> findDtosByScheduleId(int scheduleId) throws SQLException;

    List<Task> findUnscheduledDtosByScheduleId(int scheduleId) throws SQLException;

    TaskDto findDtoById(int scheduleId, int taskId) throws SQLException;

    int add(String title, String content) throws SQLException;

    void update(int id, String title, String content) throws SQLException;

    void updateLink(int scheduleId, int taskId, int day, int hourStart, int hourEnd) throws SQLException;

    void attachTaskToSchedule(int scheduleId, int taskId, int day, int hourStart, int hourEnd) throws SQLException;

    List<String> findDtoByTaskId(int taskId) throws SQLException;
}
