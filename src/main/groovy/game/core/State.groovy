package game.core

import static RowSwipeDirection.HEAD
import static RowSwipeDirection.TAIL
import static game.core.GridSwipeDirection.*
import static game.core.SwipeSeq.swipeSeq

class State {

    private int[][] grid
    private Chance random

    State(int gridSize, Chance random) {
        this.random = random
        this.grid = new int[gridSize][gridSize]

        2.times {
            Position pos = random.roll(gridSize)
            grid[pos.row][pos.col] = random.nextTile()
        }
    }

    State(int[][] startingGrid) {
        this.grid = new int[startingGrid.length][]
        (0..<startingGrid.length).each { int row ->
            this.grid[row] = startingGrid[row].clone()
        }
    }
    
    private State() {}

    int getSize() {
        grid.length
    }

    int at(Position pos) {
        grid[pos.row][pos.col]
    }

    Collection<GridSwipeDirection> getPossibleMoves() {
        //noinspection UnnecessaryQualifiedReference
        GridSwipeDirection.values().findAll { GridSwipeDirection dir -> swipe(dir) != this }
    }

    private State copy() {
        int[][] newGrid= new int[size][size]
        (0..<size).each { int row ->
            newGrid[row] = grid[row].clone()
        }
        
        return new State(random: random, grid: newGrid)
    }

    private int[] extractCol(int col) {
        (0..<size).collect { int row -> grid[row][col] }
    }

    private void replaceCol(int col, int[] content) {
        (0..<size).each { int i -> grid[i][col] = content[i] }
    }

    State swipe(GridSwipeDirection direction) {
        State newState = copy()
        
        if (direction in [LEFT, RIGHT]) {
            (0..<size).each { int row ->
                newState.grid[row] = swipeSeq(newState.grid[row], direction == LEFT ? HEAD : TAIL)
            }
        }
        if (direction in [TOP, BOTTOM]) {
            (0..<size).each { int col ->
                newState.replaceCol(col, swipeSeq(newState.extractCol(col), direction == TOP ? HEAD : TAIL))
            }
        }

        return newState
    }

    private Position findPlaceForNewTile() {
        if (grid.every { int[] row -> !(0 in row) }) {
            return null
        }

        Position pos
        while (true) { // very naive, but okay in most cases
            pos = random.roll(size)
            if (grid[pos.row][pos.col] == 0) {
                break
            }
        }
        
        return pos
    }

    // may do nothing
    State placeNewTile() {
        Position newTile = findPlaceForNewTile()

        if (newTile == null) {
            return this
        }

        State newState = copy()
        newState.grid[newTile.row][newTile.col] = random.nextTile()
        return newState
    }

    boolean isGameOver() {
        possibleMoves.empty
    }

    State iterate(GridSwipeDirection direction) {
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
