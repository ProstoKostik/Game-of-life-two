package ru.sbt.rgrtu.gol.game;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.rgrtu.gol.config.Configuration;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * A {@link BoardService} implementation that stores current and next generation in memory.
 */
public class InMemoryBoardService implements BoardService {

    private final ConfigurationProvider configurationProvider;

    private long seed;
    private int sizeX;
    private int sizeY;
    private long generation;

    /** Current values of points on the game field. */
    private boolean[][] current;
    /** Values of points on the game field for the next generation. */
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
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                current[x][y] = random.nextBoolean();
            }
        }
    }

    @Override
    public int getSizeX() {
        return sizeX;
    }

    @Override
    public int getSizeY() {
        return sizeY;
    }

    @Override
    public void setPoint(int x, int y, boolean alive) {
        next[x][y] = alive;
    }

    @Override
    public boolean getPoint(int x, int y) {
        return current[(sizeX + x) % sizeX][(sizeY + y) % sizeY];
    }

    @Override
    public void applyNewValues() {
        current = next;
        next = createGeneration();
        generation++;
    }

    @Override
    public long getGeneration() {
        return generation;
    }

    private boolean[][] createGeneration() {
        return new boolean[sizeX][sizeY];
    }
}
