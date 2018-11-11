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
public class JdbcBoardService implements BoardService {

    private final ConfigurationProvider configurationProvider;
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    private long seed;
    private BigInteger sizeX;
    private BigInteger sizeY;
    private long generation;
    private String userId = "";

    @Autowired
    public JdbcBoardService(ConfigurationProvider configurationProvider, DataSource dataSource) {
        this.configurationProvider = configurationProvider;
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void init() {
        Configuration configuration = configurationProvider.getConfiguration();
        this.seed = configuration.getSeed();
        this.sizeX = configuration.getSizeX();
        this.sizeY = configuration.getSizeY();

        if (userId != "") {
            Random random = new Random(seed);
            for (BigInteger y = BigInteger.ZERO; !y.equals(sizeY); y = y.add(BigInteger.ONE)) {
                for (BigInteger x = BigInteger.ZERO; !x.equals(sizeX); x = x.add(BigInteger.ONE)) {
                    if (random.nextBoolean()) setAlive(generation, x, y);
                }
            }
        }
    }

    @Override
    public BigInteger getSizeX() {
        return sizeX;
    }

    @Override
    public BigInteger getSizeY() {
        return sizeY;
    }

    @Override
    public void setPoint(BigInteger x, BigInteger y, boolean alive) {
        if (alive) setAlive(generation + 1, x, y);
    }

    @Override
    public boolean getPoint(BigInteger x, BigInteger y) {
        return isAlive(generation, (sizeX.add(x)).remainder(sizeX), (sizeY.add(y)).remainder(sizeY));
    }

    @Override
    public void applyNewValues() {
        clearGeneration(generation);
        generation++;
    }

    @Override
    public long getGeneration() {
        return generation;
    }

    private void setAlive(long generation, BigInteger x, BigInteger y) {
        jdbcTemplate.update("insert into Board (generation, x, y, alive, user_id) values (?, ?, ?, 1, ?)"
                , (int) generation, x.intValue(), y.intValue(), userId);
    }

    private boolean isAlive(long generation, BigInteger x, BigInteger y) {
        boolean result = false;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select alive from Board where generation=? and x=? and y=? and user_id = ?")
        ) {
            statement.setInt(1, (int) generation);
            statement.setInt(2, x.intValue());
            statement.setInt(3, y.intValue());
            statement.setString(4, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                result = rs.getBoolean("alive");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    /*    return jdbcTemplate.query("select alive from Board where generation=? and x=? and y=?"
                , new Object[]{(int) generation, x.intValue(), y.intValue()}, new ResultSetExtractor<Boolean>() {
                    @Override
                    public Boolean extractData(ResultSet rs) throws SQLException,
                            DataAccessException {
                        return rs.next() ? rs.getBoolean("alive") : false;
                    }
                });
        List<Boolean> results = jdbcTemplate.query("select alive from Board where generation=? and x=? and y=?"
                , new Object[]{(int) generation, x.intValue(), y.intValue()}, new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getBoolean(1);
                    }
                });
        if (!results.isEmpty())
            result = results.get(0);*/
    }

    public void clearGeneration(long generation) {
        jdbcTemplate.update("delete from Board where generation=? and user_id = ?", (int) generation, userId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
