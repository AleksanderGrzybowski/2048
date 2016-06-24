package game;

public interface Chance {
    Position roll(int gridSize);
    int nextTile()
}
