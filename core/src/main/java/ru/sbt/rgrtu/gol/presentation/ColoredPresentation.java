package ru.sbt.rgrtu.gol.presentation;

import ru.sbt.rgrtu.gol.game.BoardService;

import java.math.BigInteger;

public class ColoredPresentation implements Presentation {

    private final BoardService board;

    public ColoredPresentation( BoardService board) {
        this.board = board;
    }

    @Override
    public void show() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("===== %1$05d =====", board.getGeneration())).append("\n");
        for (BigInteger y = BigInteger.ZERO; !y.equals(board.getSizeY()); y = y.add(BigInteger.ONE)) {
            for (BigInteger x = BigInteger.ZERO; !x.equals(board.getSizeX()); x = x.add(BigInteger.ONE)) {
                out.append(board.getPoint(x,y)
                        ? "\033[42m" // Green background
                        : "\033[40m" // Black background
                ).append(" ");
            }
            out.append("\033[0m").append("\n"); // reset
        }
        System.out.println(out);
    }
}
