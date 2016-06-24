package game

import static game.GridSwipeDirection.*

class Session {
    private Chance random = new ChanceImpl()
    private IO io

    private static Map<String, GridSwipeDirection> MOVES = [a: LEFT, s: BOTTOM, w: TOP, d: RIGHT]
    private static String CLEAR_SCREEN = "\u001B[2J"

    public Session(IO io) {
        this.io = io
    }

    public void go() {
        System.getResourceAsStream("/banner.txt").readLines().each { io.write(it + "\r\n") }
        io.write("\r\nWhich grid size? (2-9) ")
        
        int gridSize = Integer.parseInt(io.read() as String)
        State state = new State(gridSize, random)

        while (true) {
            io.write(CLEAR_SCREEN);
            io.write(state.toString().replace("\n", "\r\n") + "\r\n\r\n")
            String line = io.read()
            io.write("\r\n\r\n")
            
            if (line == 'q') {
                io.write("Thank you\r\n")
                break
            }
            
            state = state.iterate(MOVES[line])
        }
    }
}
