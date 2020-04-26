import isel.leic.pg.Console;

public class Grid {

    private static final double PROB_OF_4 = 0.10;
    private static final int ANIM_DELAY = 50;

    private static boolean animate = false;

    private static int score;
    private static int moves;

    private static int[][] tiles;

    public static void initGrid() {
        Panel.updateMoves(moves = 0);
        Panel.updateScore(score = 0);
        tiles = new int[Game2048.COLS][Game2048.LINES];
        placeRandom(); placeRandom();
        animate();
    }

    public static void moveLeft() {
        move(0, 0, +1, +1, -1, 0);
        displayGrid();
    }

    public static void moveRight() {
        move(Game2048.COLS - 1, 0, -1, +1, +1, 0);
        displayGrid();
    }

    public static void moveUp() {
        move(0, 0, +1, +1, 0, -1);
        displayGrid();
    }

    public static void moveDown() {
        move(0, Game2048.LINES-1, +1, -1, 0, +1);
        displayGrid();
    }

    private static void move(int xStart, int yStart, int xIncr, int yIncr, int dx, int dy) {
        boolean shifted = false;
        while (slideFrom(xStart, yStart, xIncr, yIncr, dx, dy, false)) shifted = true;
        boolean joined = slideFrom(xStart, yStart, xIncr, yIncr, dx, dy, true);
        if (joined) while (slideFrom(xStart, yStart, xIncr, yIncr, dx, dy, false));
        if (shifted || joined) {
            Panel.updateMoves(++moves);
            placeRandom();
            animate();
        }
    }

    private static boolean slideFrom(int xStart, int yStart, int xIncr, int yIncr, int dx, int dy, boolean join) {
        boolean changed = join ? joinTile(xStart, yStart, dx, dy) : shiftTile(xStart, yStart, dx, dy);
        if (colInGrid(xStart + xIncr))
            return slideFrom(xStart + xIncr, yStart, xIncr, yIncr, dx, dy, join) || changed;
        else if (lineInGrid(yStart + yIncr))
            return slideFrom(Game2048.COLS - 1 - xStart, yStart + yIncr, xIncr, yIncr, dx, dy, join) || changed;
        return changed;
    }

    private static boolean shiftTile(int x, int y, int dx, int dy) {
        if (!existsTile(x, y) || !tileEmpty(x+dx, y+dy)) return false;
        tiles[x+dx][y+dy] = tiles[x][y];
        tiles[x][y] = 0;
        return true;
    }

    private static boolean joinTile(int x, int y, int dx, int dy) {
        if (!existsTile(x, y) || !existsTile(x+dx, y+dy) || tiles[x][y] != tiles [x+dx][y+dy]) return false;
        Panel.updateScore(score += tiles[x+dx][y+dy] = 2*tiles[x][y]);
        tiles[x][y] = 0;
        return true;
    }

    private static boolean existsTile(int x, int y) {
        return coordsInGrid(x, y) && tiles[x][y] != 0;
    }

    private static boolean tileEmpty(int x, int y) {
        return coordsInGrid(x, y) && tiles[x][y] == 0;
    }

    private static boolean coordsInGrid(int x, int y) {
        return colInGrid(x) && lineInGrid(y);
    }

    private static boolean colInGrid (int x) {
        return (x >= 0 && x < Game2048.COLS);
    }

    private static boolean lineInGrid (int y) {
        return (y >= 0 && y < Game2048.LINES);
    }

    private static void displayGrid() {
        for (int col = 0; col < Game2048.LINES; ++col)
            for (int line = 0; line < Game2048.COLS; ++line)
                if (tiles[col][line] == 0) Panel.hideTile(line, col);
                else Panel.showTile(line, col, tiles[col][line]);
    }

    private static void placeRandom() {
        int free = numFreeSpaces();
        if (free == 0) return;
        int pos = (int) (Math.random()*free + 1);
        int line = 0, col = 0;
        for (; col < Game2048.COLS && pos > 0; ++col)
            for (line = 0; line < Game2048.LINES && pos > 0; ++line)
                if (tiles[col][line] == 0) --pos;
        tiles[col-1][line-1] = (Math.random() < PROB_OF_4) ? 4 : 2;
    }

    private static int numFreeSpaces() {
        int count = 0;
        for (int[] col : tiles)
            for (int tile : col)
                if (tile == 0) ++count;
        return count;
    }

    public static int getScore() {
        return score;
    }

    public static void toggleAnimate() {
        animate = !animate;
    }

    private static void animate() {
        if (animate) Console.sleep(ANIM_DELAY);
        displayGrid();
    }

    public static void undo() {
        if (moves == 0) return;
    }
}