package ru.sbt.rgrtu.gol.game;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.rgrtu.gol.config.Configuration;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;

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

    private long seed;
    private BigInteger sizeX;
    private BigInteger sizeY;
    private long generation;

    private final List<Future> futures = new LinkedList<>();

    @Autowired
    public JdbcBoardService(ConfigurationProvider configurationProvider, DataSource dataSource) {
        this.configurationProvider = configurationProvider;
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        Configuration configuration = configurationProvider.getConfiguration();
        this.seed = configuration.getSeed();
        this.sizeX = configuration.getSizeX();
        this.sizeY = configuration.getSizeY();

        Random random = new Random(seed);
        for (BigInteger y = BigInteger.ZERO; !y.equals(sizeY); y = y.add(BigInteger.ONE)) {
            for (BigInteger x = BigInteger.ZERO; !x.equals(sizeX); x = x.add(BigInteger.ONE)) {
                if (random.nextBoolean()) setAlive(generation, x, y);
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "insert into Board (generation, x, y, alive) values (?, ?, ?, 1) ")
        ) {
            statement.setInt(1, (int) generation);
            statement.setInt(2, x.intValue());
            statement.setInt(3, y.intValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setDead(long generation, int x, int y) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "delete from Board where generation=? and x=? and y=?")
        ) {
            statement.setInt(1, (int) generation);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isAlive(long generation, BigInteger x, BigInteger y) {
        boolean result = false;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select alive from Board where generation=? and x=? and y=?")
        ) {
            statement.setInt(1, (int) generation);
            statement.setInt(2, x.intValue());
            statement.setInt(3, y.intValue());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                result = rs.getBoolean("alive");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    private void clearGeneration(long generation) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "delete from Board where generation=?")
        ) {
            statement.setInt(1, (int) generation);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
