package game

class ChanceImpl implements Chance {
    
    private Random random = new Random()
   
    @Override
    Position roll(int gridSize) {
        return Position.pos(random.nextInt(gridSize), random.nextInt(gridSize))
    }
}
