package game.core

class Position {

    final int row, col

    private Position(int row, int col) {
        this.row = row
        this.col = col
    }

    static Position pos(int row, int col) {
        new Position(row, col)
    }
}
