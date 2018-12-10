package ru.sbt.rgrtu.gol.game;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.rgrtu.gol.config.Configuration;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Random;

/**
 * A {@link BoardService} implementation that stores current and next generation in memory.
 */
public class InMemoryBoardService implements BoardService {

    private final ConfigurationProvider configurationProvider;

    private long seed;
    private BigInteger sizeX;
    private BigInteger sizeY;
    private int generation;

    /**
     * Current values of points on the game field.
     */
    private boolean[][] current;
    /**
     * Values of points on the game field for the next generation.
     */
    private boolean[][] next;

    @Autowired
    public InMemoryBoardService(ConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    @PostConstruct
    public void init() {
        Configuration configuration = configurationProvider.getConfiguration();
        this.seed = configuration.getSeed();
        this.sizeX = configuration.getSizeX();
        this.sizeY = configuration.getSizeY();

        current = createGeneration();
        next = createGeneration();

        Random random = new Random(seed);
        for (BigInteger y = BigInteger.ZERO; !y.equals(sizeY); y = y.add(BigInteger.ONE)) {
            for (BigInteger x = BigInteger.ZERO; !x.equals(sizeX); x = x.add(BigInteger.ONE)) {
                current[x.intValue()][y.intValue()] = random.nextBoolean();
            }
        }
    }

    @Override
    public void setUserId(String userId){

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
        next[x.intValue()][y.intValue()] = alive;
    }

    @Override
    public boolean getPoint(BigInteger x, BigInteger y) {
        return current[(sizeX.add(x)).remainder(sizeX).intValue()][(sizeY.add(y)).remainder(sizeY).intValue()];
    }

    @Override
    public void applyNewValues() {
        current = next;
        next = createGeneration();
        generation++;
    }

    @Override
    public int getGeneration() {
        return generation;
    }

    private boolean[][] createGeneration() {
        return new boolean[sizeX.intValue()][sizeY.intValue()];
    }
}
