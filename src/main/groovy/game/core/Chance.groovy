package game.core

public interface Chance {
    Position roll(int gridSize);
    int nextTile()
}
