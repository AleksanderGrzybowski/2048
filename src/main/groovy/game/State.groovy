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

    public State(int[][] startingGrid) {
        this.grid = new int[startingGrid.length][]
        (0..<startingGrid.length).each { int row ->
            this.grid[row] = startingGrid[row].clone()
        }
    }

    public int getSize() {
        grid.length
    }

    public int at(Position pos) {
        grid[pos.row][pos.col]
    }

    public Collection<GridSwipeDirection> getPossibleMoves() {
        //noinspection UnnecessaryQualifiedReference
        GridSwipeDirection.values().findAll { GridSwipeDirection dir -> swipe(dir) != this }
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


    public State swipe(GridSwipeDirection direction) {
        State newState = copy()
        if (direction in [LEFT, RIGHT]) {
            (0..<size).each { int row ->
                newState.grid[row] = SwipeSeq.swipeSeq(newState.grid[row], direction == LEFT ? HEAD : TAIL)
            }
        }
        if (direction in [TOP, BOTTOM]) {
            (0..<size).each { int col ->
                newState.replaceCol(col, SwipeSeq.swipeSeq(newState.extractCol(col), direction == TOP ? HEAD : TAIL))
            }
        }

        return newState
    }
    
    public State placeNewTile() {
        Position newTile = random.roll(size)
        
        State newState = copy()
        newState.grid[newTile.row][newTile.col] = START_VALUE
        return newState
    }

    public State iterate(GridSwipeDirection direction) {
        swipe(direction).placeNewTile()
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false
        }

        State other = obj as State
        return (0..<grid.length).every { int row -> Arrays.equals(grid[row], other.grid[row]) }
    }
}
