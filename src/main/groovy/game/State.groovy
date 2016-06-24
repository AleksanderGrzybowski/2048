package game

import static game.GridSwipeDirection.*
import static game.RowSwipeDirection.HEAD
import static game.RowSwipeDirection.TAIL

class State {
    private int[][] grid
    private Chance random

    public State(int gridSize, Chance random) {
        this.random = random
        grid = new int[gridSize][gridSize]

        Position p1 = random.roll(gridSize)
        grid[p1.row][p1.col] = random.nextTile()
        Position p2 = random.roll(gridSize)
        grid[p2.row][p2.col] = random.nextTile()
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

    private Position findPlaceForNewTile() {
        if (grid.every { int[] row -> !(0 in row) }) {
            return null
        }

        Position pos
        while (true) {
            pos = random.roll(size)
            if (grid[pos.row][pos.col] == 0) {
                break
            }
        }
        return pos
    }

    // may do nothing
    public State placeNewTile() {
        Position newTile = findPlaceForNewTile()
        State newState = copy()

        if (newTile == null) {
            return newState
        }

        newState.grid[newTile.row][newTile.col] = random.nextTile()
        return newState
    }
    
    public boolean isGameOver() {
        possibleMoves.empty
    }

    public State iterate(GridSwipeDirection direction) {
        if (possibleMoves.empty) {
            throw new GameOverException()
        }
        return swipe(direction).placeNewTile()
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false
        }

        State other = obj as State
        return (0..<grid.length).every { int row -> Arrays.equals(grid[row], other.grid[row]) }
    }

    @Override
    String toString() {
        String result = ""

        grid.each { int[] row ->
            result += "+------" * size + "+\n"
            result += "|      " * size + "|\n"
            row.each { int value -> result += "| " + (value == 0 ? '    ' : String.format("%4d", value)) + " " }
            result += "|\n"
            result += "|      " * size + "|\n"
        }
        result += "+------" * size + "+\n"

        return result
    }
}
