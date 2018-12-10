package ru.sbt.rgrtu.gol.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import ru.sbt.rgrtu.gol.config.Configuration;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;
import ru.sbt.rgrtu.gol.monitoring.MonitoringServiceImpl;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A {@link BoardService} implementation that stores current and next generation in database.
 * <p>A point on the board is considered alive if a record with corresponding coordinates exists
 * and contains {@code true}. Otherwise the point is considered to be dead.
 */
public class JdbcBoardService extends BDBoardService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcBoardService(ConfigurationProvider configurationProvider, DataSource dataSource) {
        super(configurationProvider);
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected void setAlive(int generation, BigInteger x, BigInteger y) {
        jdbcTemplate.update("insert into Board (generation, x, y, alive, user_id) values (?, ?, ?, 1, ?)"
                , (int) generation, x.intValue(), y.intValue(), getUserId());
    }

    protected boolean isAlive(int generation, BigInteger x, BigInteger y) {
        boolean result = false;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select alive from Board where generation=? and x=? and y=? and user_id = ?")
        ) {
            statement.setInt(1, (int) generation);
            statement.setInt(2, x.intValue());
            statement.setInt(3, y.intValue());
            statement.setString(4, getUserId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                result = rs.getBoolean("alive");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void clearGeneration(int generation) {
        jdbcTemplate.update("delete from Board where generation=? and user_id = ?", (int) generation, getUserId());
    }
}
