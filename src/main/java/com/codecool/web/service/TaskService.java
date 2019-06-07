package com.codecool.web.service;

import com.codecool.web.dto.TaskDto;
import com.codecool.web.model.Task;
import com.codecool.web.service.exception.ServiceException;

import java.sql.SQLException;
import java.util.List;

public interface TaskService {
    Task findById(String id) throws SQLException, ServiceException;

    List<Task> findByUserId(String userId) throws SQLException, ServiceException;

    List<Task> findByUserId(int userId) throws SQLException;

    List<TaskDto> findDtosByScheduleId(String scheduleId) throws SQLException, ServiceException;

    TaskDto findDtoById(String scheduleId, String id) throws SQLException, ServiceException;

    int add(int userId, String title, String content) throws SQLException;

    void attachTaskToSchedule(String scheduleId, String taskId, String day, String hourStart, String hourEnd)
            throws SQLException, ServiceException;

    void detachTaskFromSchedule(String scheduleId, String taskId) throws SQLException, ServiceException;

    void update(String id, String title, String content) throws SQLException, ServiceException;

    void deleteByIds(String ids) throws SQLException, ServiceException;

    List<String> findDtoByTaskId(int taskId) throws SQLException;

    void updateLink(String scheduleId, String taskId, String day, String hourStart, String hourEnd) throws SQLException, ServiceException;
}
