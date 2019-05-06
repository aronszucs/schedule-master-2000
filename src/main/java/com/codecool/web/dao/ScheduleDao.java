package com.codecool.web.dao;

import com.codecool.web.model.Schedule;

import java.sql.SQLException;
import java.util.List;

public interface ScheduleDao {

    List<Schedule> findAll() throws SQLException;

    Schedule findById(int userId, int scheduleId) throws SQLException;

    Schedule add(int userId, String name) throws SQLException;

    void add(int scheduleId, int... scheduleIds) throws SQLException;

    void updateSchedule(int userId, int scheduleId, String newName) throws SQLException;

    void deleteSchedule(int userId, int scheduleId) throws SQLException;
}
