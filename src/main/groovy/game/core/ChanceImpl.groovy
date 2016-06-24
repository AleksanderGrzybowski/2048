package game.core

class ChanceImpl implements Chance {

    private Random random = new Random()

    @Override
    Position roll(int gridSize) {
        int first = random.nextInt(gridSize)
        int second = random.nextInt(gridSize)
        return Position.pos(first, second)
    }

    @Override
    int nextTile() {
        return (random.nextDouble() < 0.66) ? 2 : 4
    }
}
