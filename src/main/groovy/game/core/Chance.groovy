package game.core

interface Chance {
    Position roll(int gridSize)

    int nextTile()
}
