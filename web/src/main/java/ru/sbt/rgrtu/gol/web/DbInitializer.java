package ru.sbt.rgrtu.gol.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {

    private static DbInitializer instance;

    public static DbInitializer getInstance(DataSource ds) {
        if (instance == null)
            instance = new DbInitializer(ds);
        return instance;
    }

    private DbInitializer(DataSource ds) {
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
    }
}
