package ru.sbt.rgrtu.gol.web;

import ru.sbt.rgrtu.gol.game.Gol;
import ru.sbt.rgrtu.gol.monitoring.MonitoringService;
import ru.sbt.rgrtu.gol.web.presentation.HtmlPresentation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GolServlet extends HttpServlet {

    @Override
    public synchronized void init() throws ServletException {
        super.init();
//        initGol("simpleContext.xml");
    }

    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ((HtmlPresentation)session.getAttribute("presentation")).setResponse(response);
        ((MonitoringService)session.getAttribute("monitoring")).calculationsStarts();
        ((Gol)session.getAttribute("gol")).nextStep();
        ((HtmlPresentation)session.getAttribute("presentation")).show();
    }

}
