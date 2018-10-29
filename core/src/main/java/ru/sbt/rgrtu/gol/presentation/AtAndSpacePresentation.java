package ru.sbt.rgrtu.gol.presentation;

import ru.sbt.rgrtu.gol.game.BoardService;

public class AtAndSpacePresentation implements Presentation {

    private final BoardService board;

    public AtAndSpacePresentation(BoardService board) {
        this.board = board;
    }

    @Override
    public void show() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("===== %1$05d =====", board.getGeneration())).append("\n");
        for (int y = 0; y < board.getSizeY(); y++) {
            for (int x = 0; x < board.getSizeX(); x++) {
                out.append(board.getPoint(x,y) ? "@" : " ");
            }
            out.append("\n");
        }
        System.out.println(out);
    }
}
