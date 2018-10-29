package ru.sbt.rgrtu.gol.controller;

import ru.sbt.rgrtu.gol.game.Gol;
import ru.sbt.rgrtu.gol.presentation.Presentation;

import java.util.Scanner;

/**
 * Runs simulation without user input, changing generations by timer.
 */
public class TimedController implements Controller {

    private final Gol gol;
    private final Presentation presentation;
    private volatile boolean stopped;

    public TimedController(Gol gol, Presentation presentation) {
        this.gol = gol;
        this.presentation = presentation;
    }

    @Override
    public void run() {
        new Thread(new Input()).start();

        while (!stopped) {
            gol.nextStep();
            presentation.show();

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Input implements Runnable {

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (!stopped) {
                scanner.nextLine();
                stopped = true;
            }
        }
    }
}
