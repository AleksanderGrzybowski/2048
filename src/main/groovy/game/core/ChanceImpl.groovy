package game.core

class ChanceImpl implements Chance {
    
    private Random random = new Random()
   
    @Override
    Position roll(int gridSize) {
        return Position.pos(random.nextInt(gridSize), random.nextInt(gridSize))
    }

    @Override
    int nextTile() {
        return (random.nextDouble() < 0.66) ? 2 : 4
    }
}
