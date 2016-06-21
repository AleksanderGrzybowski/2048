package game

import static game.GridSwipeDirection.*
import static game.RowSwipeDirection.HEAD
import static game.RowSwipeDirection.TAIL

class State {
    public static final int START_VALUE = 2
    private int[][] grid
    private Chance random

    public State(int gridSize, Chance random) {
        this.random = random
        grid = new int[gridSize][gridSize]

        Position p1 = random.roll(gridSize)
        grid[p1.row][p1.col] = START_VALUE
        Position p2 = random.roll(gridSize)
        grid[p2.row][p2.col] = START_VALUE
    }

    private State() {}

    public int getSize() {
        grid.length
    }

    public int at(Position pos) {
        grid[pos.row][pos.col]
    }

    private State copy() {
        State cloned = new State()
        cloned.random = random
        cloned.grid = new int[size][size]
        (0..<size).each { int row ->
            cloned.grid[row] = grid[row].clone()
        }
        return cloned
    }

    private int[] extractCol(int col) {
        (0..<size).collect { int row -> grid[row][col] }
    }

    private void replaceCol(int col, int[] content) {
        (0..<size).each { int i -> grid[i][col] = content[i] }
    }

    public State swipe(GridSwipeDirection dir) {
        State newState = copy()

        if (dir in [LEFT, RIGHT]) {
            (0..<size).each { int row ->
                newState.grid[row] = SwipeSeq.swipeSeq(newState.grid[row], dir == LEFT ? HEAD : TAIL)
            }
        }
        if (dir in [TOP, BOTTOM]) {
            (0..<size).each { int col ->
                newState.replaceCol(col, SwipeSeq.swipeSeq(newState.extractCol(col), dir == TOP ? HEAD : TAIL))
            }
        }

        Position newTile = random.roll(size)
        newState.grid[newTile.row][newTile.col] = START_VALUE

        return newState
    }
}
