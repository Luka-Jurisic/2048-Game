import isel.leic.pg.Console;

import java.awt.event.KeyEvent;

public class Game2048 {

    static final int LINES = 4, COLS = 4;
    static final int MIN_VALUE = 2, MAX_VALUE = 2048;

    private static TopScores top = new TopScores();

    private static boolean exit = false;
    private static int key;

    public static void main(String[] args) {
        Panel.open();
        init();
        for (; ; ) {
            key = Console.waitKeyPressed(0);
            if (!processKey(key)) break;
            while (Console.isKeyPressed(key)) ;
        }
        Panel.close();
        top.saveToFile();
    }

    private static void init() {
        Panel.showMessage("Use cursor keys to play");
        Grid.initGrid();
        for (int i = 0; i < top.getNumOfRows(); ++i) {
            Score s = top.getRow(i);
            Panel.updateBestRow(i+1, s.name, s.points);
        }
    }

    private static boolean processKey(int key) {
        switch (key) {
            case 'A':                       Grid.toggleAnimate(); break;
            case KeyEvent.VK_BACK_SPACE:    Grid.undo();          break;
            case KeyEvent.VK_UP:            Grid.moveUp();        break;
            case KeyEvent.VK_DOWN:          Grid.moveDown();      break;
            case KeyEvent.VK_LEFT:          Grid.moveLeft();      break;
            case KeyEvent.VK_RIGHT:         Grid.moveRight();     break;
            case KeyEvent.VK_ESCAPE:
            case 'Q':                       quitGame();           break;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_ADD:
            case KeyEvent.VK_MINUS:
            case KeyEvent.VK_SUBTRACT:
        }
        return !exit;
    }

    private static void quitGame() {
        int resp = Panel.questionChar("Exit game (Y/N)");
        if (resp == 'Y') {
            exit = true;
            int score = Grid.getScore();
            if (score != 0 && top.canAdd(score)) {
                String n = Panel.questionName("New High Score! Your name");
                top.addRow(n, score);
            }
        }
        key = 0;
    }

}