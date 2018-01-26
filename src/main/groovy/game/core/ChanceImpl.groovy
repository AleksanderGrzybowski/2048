package game.core

class ChanceImpl implements Chance {

    private static final double CHANCE_FOR_TWO = 0.66
    
    private final Random random = new Random()

    @Override
    Position roll(int gridSize) {
        int first = random.nextInt(gridSize)
        int second = random.nextInt(gridSize)
        return Position.pos(first, second)
    }

    @Override
    int nextTile() {
        return (random.nextDouble() < CHANCE_FOR_TWO) ? 2 : 4
    }
}
