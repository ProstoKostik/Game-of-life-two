package ru.sbt.rgrtu.gol.presentation;

import ru.sbt.rgrtu.gol.game.BoardService;

import java.io.UnsupportedEncodingException;

public class SmilePresentation implements Presentation {

    private static final String SMILE = getSmile();
    private final BoardService board;

    private static String getSmile() {
        try {
            return new String(new byte[]{(byte)0xf0, (byte)0x9f, (byte)0x98, (byte)0x80},
                    "UTF-8"
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ":)";
    }

    public SmilePresentation(BoardService board) {
        this.board = board;
    }

    @Override
    public void show() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("===== %1$05d =====", board.getGeneration())).append("\n");
        for (int y = 0; y < board.getSizeY(); y++) {
            for (int x = 0; x < board.getSizeX(); x++) {
                out.append(board.getPoint(x,y) ? SMILE : " ");
            }
            out.append("\n");
        }
        System.out.println(out);
    }
}
