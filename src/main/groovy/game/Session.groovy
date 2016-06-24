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
        printBanner()
        io.write("\r\nWhich grid size? (2-9, 'q' to exit) > ")
        
        int gridSize
        while (true) {
            String input = io.read() as String
            if (input == 'q') {
                return
            }
            try {
                gridSize = Integer.parseInt(input)
                if (gridSize in (2..9)) {
                    break
                }
            } catch (NumberFormatException ignored) {
                // continue
            }
        } 
        State state = new State(gridSize, random)
        
        io.write("\r\n*** Use WSAD keys to shift tiles around, 'q' to exit ***")
        io.read()

        while (true) {
            io.write(CLEAR_SCREEN);
            io.write("\r\n")
            io.write(state.toString().replace("\n", "\r\n") + "\r\n\r\n")
            
            if (state.gameOver) {
                io.write("Game over!\r\n")
                break
            }
            
            String line = io.read()
            io.write("\r\n\r\n")
            
            if (line == 'q') {
                io.write("Thank you for playing!\r\n")
                break
            }
            
            if (MOVES[line] != null) {
                state = state.iterate(MOVES[line])
            }
        }
    }

    private void printBanner() {
        System.getResourceAsStream("/banner.txt").readLines().each { io.write(it + "\r\n") }
    }
}
