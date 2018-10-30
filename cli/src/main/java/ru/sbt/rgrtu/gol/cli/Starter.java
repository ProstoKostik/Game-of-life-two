package ru.sbt.rgrtu.gol.cli;

import ru.sbt.rgrtu.gol.config.Configuration;
import ru.sbt.rgrtu.gol.config.ConfigurationPropertiesLoader;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;
import ru.sbt.rgrtu.gol.controller.Controller;
import ru.sbt.rgrtu.gol.controller.FrameByFrameController;
import ru.sbt.rgrtu.gol.game.BoardService;
import ru.sbt.rgrtu.gol.game.Gol;
import ru.sbt.rgrtu.gol.controller.TimedController;
import ru.sbt.rgrtu.gol.game.InMemoryBoardService;
import ru.sbt.rgrtu.gol.presentation.AtAndSpacePresentation;
import ru.sbt.rgrtu.gol.presentation.ColoredPresentation;
import ru.sbt.rgrtu.gol.presentation.Presentation;
import ru.sbt.rgrtu.gol.presentation.SmilePresentation;

import java.math.BigInteger;

public class Starter {

    public static void main(String[] args) {
//        ConfigurationProvider cpl = createHardCodedConfigurationProvider();
//        ConfigurationProvider cpl = createInlineConfigurationProvider();
        ConfigurationProvider cpl = createConfigurationPropertiesLoader();
        BoardService board = new InMemoryBoardService(cpl);
        ((InMemoryBoardService) board).init();
        Gol gol = new Gol(board);
//        Presentation presentation = new AtAndSpacePresentation(gol);
        Presentation presentation = new SmilePresentation(board);
//        Presentation presentation = new ColoredPresentation(gol);

//        Controller controller = new FrameByFrameController(gol, presentation);
        Controller controller = new TimedController(gol, presentation);
        controller.run();
    }

    private static ConfigurationProvider createConfigurationPropertiesLoader() {
        return new ConfigurationPropertiesLoader("config.properties");
    }

    private static ConfigurationProvider createHardCodedConfigurationProvider() {
        return new HardCodedConfigurationProvider();
    }

    private static ConfigurationProvider createInlineConfigurationProvider() {
        return () -> {
            Configuration configuration = new Configuration();
            configuration.setSeed(20180921L);
            configuration.setSizeX(BigInteger.valueOf(150));
            configuration.setSizeY(BigInteger.valueOf(35));
            return configuration;
        };
    }
}
