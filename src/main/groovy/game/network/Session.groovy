package game.network

import game.core.Chance
import game.core.ChanceImpl
import game.core.GridSwipeDirection
import game.core.State
import groovy.util.logging.Log

import static game.core.GridSwipeDirection.*

@Log
class Session {
    private final Chance random = new ChanceImpl()
    private final IO io

    private static final Map<String, GridSwipeDirection> MOVES = [a: LEFT, s: BOTTOM, w: TOP, d: RIGHT]
    private static final Set<String> POSSIBLE_KEYS = MOVES.keySet() + 'q'
    private static final String CLEAR_SCREEN = "\u001B[2J"

    public final String id = UUID.randomUUID().toString().substring(0, 6)

    Session(IO io) {
        this.io = io
    }

    void go() {
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

        log.info("${id} creating new grid size=${gridSize}.")

        io.write("\r\n*** Use WSAD keys to shift tiles around, 'q' to exit ***")
        io.read()

        while (true) {
            io.write(CLEAR_SCREEN)
            io.write("\r\n")
            io.write(state.toString().replace("\n", "\r\n") + "\r\n\r\n")

            if (state.gameOver) {
                io.write("Game over!\r\n")
                break
            }

            String line = io.read()
            if (!(line in POSSIBLE_KEYS)) {
                continue
            }

            log.info("${id} user input: ${line}.")

            io.write("\r\n\r\n")

            if (line == 'q') {
                io.write("Thank you for playing!\r\n")
                log.info("${id} ended game.")
                break
            }
            
            state = state.iterate(MOVES[line])
        }
    }

    private void printBanner() {
        System.getResourceAsStream("/banner.txt").readLines().each { io.write(it + "\r\n") }
    }
}
