package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.dto.TaskDto;
import com.codecool.web.model.Task;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.exception.ServiceException;

import java.sql.SQLException;
import java.util.List;

public class SimpleTaskService extends AbstractService implements TaskService {

    private TaskDao taskDao;

    public SimpleTaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public Task findById(String id) throws ServiceException, SQLException{
        int idVal = fetchInt(id, "id");
        return taskDao.findById(idVal);
    }

    public List<Task> findByUserId(String userId) throws SQLException, ServiceException {
        int idVal = fetchInt(userId, "userId");
        return taskDao.findByUserId(idVal);
    }

    public List<Task> findByUserId(int userId) throws SQLException {
        return taskDao.findByUserId(userId);
    }

    public List<Task> findAll() throws SQLException, ServiceException {
        return taskDao.findAll();
    }

    public List<TaskDto> findDtosByScheduleId(String scheduleId) throws SQLException, ServiceException {
        int scheduleIdVal = fetchInt(scheduleId, "scheduleId");
        return taskDao.findDtosByScheduleId(scheduleIdVal);
    }

    public TaskDto findDtoById(String scheduleId, String taskId) throws SQLException, ServiceException {
        int scheduleIdVal = fetchInt(scheduleId, "scheduleId");
        int taskIdVal = fetchInt(taskId, "taskId");
        return taskDao.findDtoById(scheduleIdVal, taskIdVal);
    }

    public int add(String title, String content) throws SQLException {
        return taskDao.add(title, content);
    }

    public void update(String id, String title, String content) throws SQLException, ServiceException {
        int idVal = fetchInt(id, "id");
        taskDao.update(idVal, title, content);
    }


    public void updateLink(String scheduleId, String taskId, String day, String hourStart, String hourEnd) throws ServiceException, SQLException {
        int scheduleIdVal = fetchInt(scheduleId, "scheduleId");
        int taskIdVal = fetchInt(taskId, "taskId");
        int hourStartVal = fetchInt(hourStart, "hourStart");
        int hourSEndVal = fetchInt(hourEnd, "hourEnd");
        int dayVal = fetchInt(day, "day");
        taskDao.updateLink(scheduleIdVal, taskIdVal, dayVal, hourStartVal, hourSEndVal);
    }

    public void attachTaskToSchedule(String scheduleId, String taskId, String day, String hourStart, String hourEnd) throws SQLException, ServiceException {
        int scheduleIdVal = fetchInt(scheduleId, "scheduleId");
        int taskIdVal = fetchInt(taskId, "taskId");
        int dayVal = fetchInt(day, "day");
        int hourStartVal = fetchInt(hourStart, "hourStart");
        int hourEndVal = fetchInt(hourEnd, "hourEnd") + 1;
        taskDao.attachTaskToSchedule(scheduleIdVal, taskIdVal, dayVal, hourStartVal, hourEndVal);

    }

    public void detachTaskFromSchedule(String scheduleId, String taskId) throws SQLException, ServiceException {
        int scheduleIdVal = fetchInt(scheduleId, "scheduleId");
        int taskIdVal = fetchInt(taskId, "taskId");
        taskDao.detachTaskFromSchedule(scheduleIdVal, taskIdVal);

    }

    public void deleteByIds(String idChainString) throws SQLException, ServiceException{
        String[] idsStr = idChainString.split(",");
        int[] ids = new int[idsStr.length];
        for (int i = 0; i < idsStr.length; i++) {
            ids[i] = Integer.parseInt(idsStr[i]);
        }

    }


    public List<String> findDtoByTaskId(int taskId) throws SQLException {
        return taskDao.findDtoByTaskId(taskId);
    }
}
