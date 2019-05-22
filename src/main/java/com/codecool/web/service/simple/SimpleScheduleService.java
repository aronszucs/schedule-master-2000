package com.codecool.web.service.simple;


import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.exception.ServiceException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SimpleScheduleService extends AbstractService implements ScheduleService  {

    private final ScheduleDao scheduleDao;

    public SimpleScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }


    @Override
    public List<Schedule> findAll() throws SQLException {
        return scheduleDao.findAll();
    }

    @Override
    public List<Schedule> findByUser(String userId) throws SQLException {
        return scheduleDao.findByUser(Integer.parseInt(userId));
    }

    @Override
    public Schedule findById(String userId, String scheduleId) throws SQLException {
        return scheduleDao.findById(Integer.parseInt(userId), Integer.parseInt(scheduleId));
    }

    public Schedule fetchShared(String scheduleId) throws SQLException, ServiceException {
        Schedule schedule = scheduleDao.fetchShared(fetchInt(scheduleId, "scheduleId"));
        if (schedule == null) {
            throw new ServiceException("Unauthorized access!");
        }
        return schedule;
    }


    @Override
    public void add(int userId, String name, String date, String days) throws SQLException, ServiceException {
        int daysVal = fetchInt(days, "days");
        LocalDate dateVal = fetchDate(date, "date");
        scheduleDao.add(userId, name, dateVal, daysVal);
    }


    @Override
    public void delete(String scheduleIdChain, int userId) throws SQLException, ServiceException {
        String[] ids = scheduleIdChain.split(",");
        for (String id : ids) {
            scheduleDao.delete(userId, fetchInt(id, "id"));
        }

    }


    /*public void update(String scheduleIdChain, String newNameChain, String newStartChain, String newFinishChain, int userId) throws SQLException, ServiceException {
        String[] ids = scheduleIdChain.split(",");
        for (String id : ids) {
            scheduleDao.delete(userId, fetchInt(id, "id"));
        }

    }*/
    @Override
    public void update(String[] ids, String[] names, List<LocalDate> starting, List<LocalDate> finishing) throws SQLException, ServiceException {
        for(int i = 0; i < ids.length; i++) {
            scheduleDao.update(Integer.parseInt(ids[i]), names[i], starting.get(i), (int)ChronoUnit.DAYS.between(starting.get(i), finishing.get(i)));
        }
    }

}
