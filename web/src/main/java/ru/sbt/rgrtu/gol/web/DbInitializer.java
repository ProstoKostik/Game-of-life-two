package ru.sbt.rgrtu.gol.web;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {

    @Autowired
    DbInitializer(DataSource ds) {
        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement()
        ){
            statement.execute("drop table if exists Board");
            statement.execute("create table Board (\n" +
                    "       id integer not null IDENTITY,\n" +
                    "       generation integer not null,\n" +
                    "       x integer not null,\n" +
                    "       y integer not null,\n" +
                    "       alive integer not null,\n" +
                    "       primary key (id)\n" +
                    "    )");
            statement.execute("create unique index if not exists pk_board_index on Board\n" +
                    "(generation, x, y)");
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
