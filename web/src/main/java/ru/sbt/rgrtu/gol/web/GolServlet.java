package ru.sbt.rgrtu.gol.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.sbt.rgrtu.context.Context;
import ru.sbt.rgrtu.gol.game.Gol;
import ru.sbt.rgrtu.gol.monitoring.MonitoringService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GolServlet extends HttpServlet {

    private Gol gol;
    private HtmlPresentation presentation;
    private MonitoringService monitoring;

    @Override
    public synchronized void init() throws ServletException {
        super.init();
//        initGol("simpleContext.xml");
        initGol("heavyContext.xml");
    }

    private void initGol(String location) {
//        Context context = new DbContextAssembler().assembleContext();
//        Context context = new SimpleContextAssembler().assembleContext();
        ApplicationContext context = new ClassPathXmlApplicationContext(location);

        this.presentation = context.getBean(HtmlPresentation.class);
        this.gol = context.getBean(Gol.class);
        this.monitoring = context.getBean(MonitoringService.class);
    }

    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        presentation.setResponse(response);
        monitoring.calculationsStarts();
        gol.nextStep();
        presentation.show();
        System.out.println(String.format("Request completed in %1$d ms", monitoring.elapsed()));
    }

}
