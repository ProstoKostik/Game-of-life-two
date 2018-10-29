package ru.sbt.rgrtu.gol.presentation;

import ru.sbt.rgrtu.gol.game.BoardService;

public class ColoredPresentation implements Presentation {

    private final BoardService board;

    public ColoredPresentation( BoardService board) {
        this.board = board;
    }

    @Override
    public void show() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("===== %1$05d =====", board.getGeneration())).append("\n");
        for (int y = 0; y < board.getSizeY(); y++) {
            for (int x = 0; x < board.getSizeX(); x++) {
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
