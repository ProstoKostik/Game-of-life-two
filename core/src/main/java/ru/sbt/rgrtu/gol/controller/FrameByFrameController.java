package ru.sbt.rgrtu.gol.controller;

import ru.sbt.rgrtu.gol.game.BoardService;
import ru.sbt.rgrtu.gol.game.Gol;
import ru.sbt.rgrtu.gol.presentation.Presentation;

import java.util.Scanner;

/**
 * Runs simulation one generation at a time and waits for user's input.
 */
public class FrameByFrameController implements Controller {

    private final Gol gol;
    private final Presentation presentation;

    public FrameByFrameController(Gol gol, Presentation presentation) {
        this.gol = gol;
        this.presentation = presentation;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (!"q".equals(scanner.nextLine())) {
            gol.nextStep();
            presentation.show();
        }
    }
}
