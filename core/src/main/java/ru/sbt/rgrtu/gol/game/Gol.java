package ru.sbt.rgrtu.gol.game;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Game of Life.
 * <p> 1. Any live cell with fewer than two live neighbors dies, as if by under population.
 * <p> 2. Any live cell with two or three live neighbors lives on to the next generation.
 * <p> 3. Any live cell with more than three live neighbors dies, as if by overpopulation.
 * <p> 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 */
public class Gol {

    private final BoardService board;

    @Autowired(required = false)
    private ExecutorService executor;

    /**
     * Create an instance with a given game field.
     *
     * @param board game board
     */
    @Autowired
    public Gol(BoardService board) {
        this.board = board;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public void nextStep() {
        List<Future> f = new LinkedList<>();
        for (BigInteger y = BigInteger.ZERO; !y.equals(board.getSizeY()); y = y.add(BigInteger.ONE)) {
            if (executor == null) calculateRow(y);
            else {
                final BigInteger yy = y;
                f.add(executor.submit(() -> calculateRow(yy)));
            }
        }
        Iterator<Future> i = f.iterator();
        while (i.hasNext()) {
            try {
                i.next().get();
                i.remove();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        board.applyNewValues();
    }

    private void calculateRow(BigInteger y) {
        for (BigInteger x = BigInteger.ZERO; !x.equals(board.getSizeX()); x = x.add(BigInteger.ONE)) {
            int neighbours = countNeighbours(x.intValue(), y.intValue());
            board.setPoint(x, y, calculateNewValue(board.getPoint(x, y), neighbours));
        }
    }

    private int countNeighbours(int x, int y) {
        int count = 0;
        for (int m = x - 1; m <= x + 1; m++) {
            for (int n = y - 1; n <= y + 1; n++) {
                if (m == x && n == y) continue;
                if (board.getPoint(BigInteger.valueOf(m), BigInteger.valueOf(n))) count++;
            }
        }
        return count;
    }

    private boolean calculateNewValue(boolean old, int n) {
        if (n == 3) return true; // rules 2 and 4
        return old && n == 2; // rules 1, 2 and 3
    }

}
