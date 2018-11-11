package ru.sbt.rgrtu.gol.web;

import ru.sbt.rgrtu.gol.game.JdbcBoardService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent ev) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent ev) {
        JdbcBoardService jdbcBoardService = (JdbcBoardService) ev.getSession().getAttribute("service");
        jdbcBoardService.clearGeneration(jdbcBoardService.getGeneration());
    }
}
