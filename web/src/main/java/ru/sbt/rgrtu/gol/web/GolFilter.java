package ru.sbt.rgrtu.gol.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.sbt.rgrtu.context.Context;
import ru.sbt.rgrtu.gol.game.Gol;
import ru.sbt.rgrtu.gol.monitoring.MonitoringService;
import ru.sbt.rgrtu.gol.web.presentation.HtmlPresentation;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GolFilter implements Filter {

    private void initGol(String location, HttpServletRequest req) {
        //       Context context = DbContextAssembler.getInstance().assembleContext();
        //       Context context = new SimpleContextAssembler().assembleContext();
        ApplicationContext context = new ClassPathXmlApplicationContext(location);
        //       req.getSession().setAttribute("gol", context.get(Gol.class));
        //      req.getSession().setAttribute("presentation", context.get(HtmlPresentation.class));
        //       req.getSession().setAttribute("monitoring", context.get(MonitoringService.class));
        req.getSession().setAttribute("gol", context.getBean(Gol.class));
        req.getSession().setAttribute("presentation", context.getBean(HtmlPresentation.class));
        req.getSession().setAttribute("monitoring", context.getBean(MonitoringService.class));
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getSession() == null || req.getSession().getAttribute("gol") == null) {
            initGol("heavyContext.xml", req);
            //        initGol("simpleContext.xml");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
