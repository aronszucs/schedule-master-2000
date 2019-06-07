package com.codecool.web.servlet;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.simple.SimpleTaskDao;
import com.codecool.web.model.Task;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.exception.ServiceException;
import com.codecool.web.service.simple.SimpleTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/task")
public class TaskServlet extends AbstractServlet {
    private static final Logger logger = LoggerFactory.getLogger(TaskServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new SimpleTaskDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);

            String id = req.getParameter("id");

            Task task = taskService.findById(id);
            List<String> results = taskService.findDtoByTaskId(Integer.parseInt(id));
            results.add(0, id);
            results.add(1, task.getTitle());
            results.add(2, task.getContent());

            logger.info("Task displayed");
            sendMessage(resp, HttpServletResponse.SC_OK, results);
        } catch (ServiceException ex) {
            sendMessage(resp, HttpServletResponse.SC_OK, ex.getMessage());
            logger.error("error", ex);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
            logger.error("error, ex");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new SimpleTaskDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);

            String taskId = req.getParameter("id");
            String title = req.getParameter("title");
            String content = req.getParameter("content");

            taskService.update(taskId, title, content);
            logger.info("Task added");
            sendMessage(resp, HttpServletResponse.SC_OK, "Task (id = " + taskId +") updated.");
        } catch (ServiceException ex) {
            sendMessage(resp, HttpServletResponse.SC_OK, ex.getMessage());
            logger.error("error", ex);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
            logger.error("error", ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new SimpleTaskDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);

            String taskId = req.getParameter("id");
            String title = req.getParameter("title");
            String content = req.getParameter("content");

            taskService.update(taskId, title, content);
            logger.info("Task updated");
            sendMessage(resp, HttpServletResponse.SC_OK, "Task (id = " + taskId +") updated.");
        } catch (ServiceException ex) {
            sendMessage(resp, HttpServletResponse.SC_OK, ex.getMessage());
            logger.error("error", ex);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
            logger.error("error", ex);
        }
    }
}
