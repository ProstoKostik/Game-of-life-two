package ru.sbt.rgrtu.gol.presentation;

import ru.sbt.rgrtu.gol.game.BoardService;

import java.math.BigInteger;

public class AtAndSpacePresentation implements Presentation {

    private final BoardService board;

    public AtAndSpacePresentation(BoardService board) {
        this.board = board;
    }

    @Override
    public void show() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("===== %1$05d =====", board.getGeneration())).append("\n");
        for (BigInteger y = BigInteger.ZERO; !y.equals(board.getSizeY()); y = y.add(BigInteger.ONE)) {
            for (BigInteger x = BigInteger.ZERO; !x.equals(board.getSizeX()); x = x.add(BigInteger.ONE)) {
                out.append(board.getPoint(x, y) ? "@" : " ");
            }
            out.append("\n");
        }
        System.out.println(out);
    }
}
