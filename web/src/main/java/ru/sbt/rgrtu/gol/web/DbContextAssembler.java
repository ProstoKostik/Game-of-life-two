package ru.sbt.rgrtu.gol.web;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.sbt.rgrtu.context.Context;
import ru.sbt.rgrtu.gol.config.ConfigurationPropertiesLoader;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;
import ru.sbt.rgrtu.gol.game.BoardService;
import ru.sbt.rgrtu.gol.game.Gol;
import ru.sbt.rgrtu.gol.game.JdbcBoardService;
import ru.sbt.rgrtu.gol.game.ThreadLocalCachingBoardService;
import ru.sbt.rgrtu.gol.monitoring.MonitoringService;
import ru.sbt.rgrtu.gol.monitoring.MonitoringServiceImpl;
import ru.sbt.rgrtu.gol.presentation.Presentation;
import ru.sbt.rgrtu.gol.web.presentation.HtmlPresentation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DbContextAssembler {

    private static DbContextAssembler instance;
    private boolean firstPool = false;

    private DbContextAssembler() {
    }

    public static DbContextAssembler getInstance() {
        if (instance == null)
            instance = new DbContextAssembler();
        return instance;
    }

    public Context assembleContext() {
        ConfigurationProvider cpl = new ConfigurationPropertiesLoader("config.properties");
//        DataSource dataSource = getDataSource();
        DataSource dataSource = getPooledDataSource();
        BoardService board = new JdbcBoardService(cpl, dataSource);
        ((JdbcBoardService) board).init();
        board = new ThreadLocalCachingBoardService(board, 15);
        Gol gol = new Gol(board);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        gol.setExecutor(executor);
        MonitoringService monitoring = new MonitoringServiceImpl();
        Presentation presentation = new HtmlPresentation(board, true, monitoring);

        Context context = new Context();
        context.add(gol);
        context.add(presentation);
        context.add(monitoring);
        context.add(board);

        return context;
    }

    private DataSource getPooledDataSource() {
        HikariConfig config = new HikariConfig();
        config.setConnectionTestQuery("VALUES 1");
        config.setJdbcUrl("jdbc:hsqldb:mem:gol");
        config.setUsername("sa");
        config.setPassword("");
        HikariDataSource ds = new HikariDataSource(config);
        if (firstPool == false)
            initTable(ds);
        return ds;
    }

 /*   private DataSource getDataSource() {
        JDBCDataSource ds = new JDBCDataSource();
        ds.setURL("jdbc:hsqldb:mem:gol");
        ds.setUser("sa");
        ds.setPassword("sa");
        initTable(ds);
        return ds;
    }*/

    private void initTable(DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("drop table if exists Board");
        jdbcTemplate.execute("create table Board (\n" +
                "       id integer not null IDENTITY,\n" +
                "       generation integer not null,\n" +
                "       x integer not null,\n" +
                "       y integer not null,\n" +
                "       alive integer not null,\n" +
                "       user_id varchar(50) not null,\n" +
                "       primary key (id)\n" +
                "    )");
        jdbcTemplate.execute("create unique index if not exists pk_board_index on Board\n" +
                "(generation, x, y, user_id)");
        firstPool = true;
    }
}
