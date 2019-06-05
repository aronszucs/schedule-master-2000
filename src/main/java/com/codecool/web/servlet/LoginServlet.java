package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.simple.SimpleUserDao;
import com.codecool.web.model.User;
import com.codecool.web.service.simple.SimpleLoginService;
import com.codecool.web.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends AbstractServlet {
    private static final Logger logger = LoggerFactory.getLogger(TaskServlet.class);



     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            UserDao userDao = new SimpleUserDao(connection);
            SimpleLoginService simpleLoginService = new SimpleLoginService(userDao);

            String email = req.getParameter("email");
            String password = req.getParameter("password");


            User user = simpleLoginService.loginUser(email, password);
            req.getSession().setAttribute("user", user);
            logger.info("Succesful Login ;"+user.getName());
            sendMessage(resp, HttpServletResponse.SC_OK, user);
        } catch (SQLException ex) {
            if (ex.getMessage().equals("ERROR: invalid salt")){
                sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, "Bad login: Invalid password");
                logger.error("Bad login: Invalid password", ex);
            }
            else {
                handleSqlError(resp, ex);
                logger.error("SQL EXCEPTION", ex);
            }

        } catch (ServiceException ex) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            logger.error("Bad login;"+req.getParameter("email"), ex);
        }
    }

}
