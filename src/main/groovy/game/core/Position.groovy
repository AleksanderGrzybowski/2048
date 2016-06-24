package game.core

class Position {
    int row, col
    
    private Position(int row, int col) {
        this.row = row
        this.col = col
    }
    
    public static Position pos(int row, int col) {
        return new Position(row, col)
    }
}
