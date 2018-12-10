package ru.sbt.rgrtu.gol.game;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.rgrtu.gol.config.ConfigurationProvider;
import ru.sbt.rgrtu.gol.entity.Board;
import ru.sbt.rgrtu.gol.repository.BoardRepository;

import java.math.BigInteger;

public class ImplBoardService extends BDBoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    public ImplBoardService(ConfigurationProvider configurationProvider) {
        super(configurationProvider);
    }

    protected void setAlive(int generation, BigInteger x, BigInteger y) {
        Board b = new Board();
        b.setGeneration(generation);
        b.setX(x.intValue());
        b.setY(y.intValue());
        b.setAlive(1);
        b.setUserID(getUserId());

        boardRepository.save(b);
    }

    protected boolean isAlive(int generation, BigInteger x, BigInteger y) {
        Board b = boardRepository.findTop1ByGenerationAndXAndYAndUserID(generation, x.intValue(), y.intValue(), getUserId());
        boolean result = false;
        if (b != null)
            result = b.getAlive() == 1;
        return result;
    }

    public void clearGeneration(int generation) {
        Board b = new Board();
        b.setUserID(getUserId());
        b.setGeneration(generation);
        boardRepository.delete(b);
    }
}
