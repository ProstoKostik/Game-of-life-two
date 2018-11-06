package ru.sbt.rgrtu.gol.web;

import ru.sbt.rgrtu.context.Context;
import ru.sbt.rgrtu.gol.config.ConfigurationPropertiesLoader;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;
import ru.sbt.rgrtu.gol.game.BoardService;
import ru.sbt.rgrtu.gol.game.Gol;
import ru.sbt.rgrtu.gol.game.InMemoryBoardService;
import ru.sbt.rgrtu.gol.monitoring.MonitoringService;
import ru.sbt.rgrtu.gol.monitoring.MonitoringServiceImpl;
import ru.sbt.rgrtu.gol.presentation.Presentation;
import ru.sbt.rgrtu.gol.web.presentation.HtmlPresentation;

public class SimpleContextAssembler {

    public Context assembleContext() {
        ConfigurationProvider cpl = new ConfigurationPropertiesLoader("config.properties");
        BoardService board = new InMemoryBoardService(cpl);
        ((InMemoryBoardService) board).init();
        Gol gol = new Gol(board);
        MonitoringService monitoring = new MonitoringServiceImpl();
        Presentation presentation = new HtmlPresentation(board, true, monitoring);

        Context context = new Context();
        context.add(gol);
        context.add(presentation);
        context.add(monitoring);

        return context;
    }
}
