package game

import spock.lang.Specification

import static game.GridSwipeDirection.*
import static game.Position.pos

class StateSpec extends Specification {

    Chance random

    def setup() {
        random = Stub(Chance)
    }

    def "should create new game state with two starting tiles with two random values"() {
        when:
        random.roll(4) >> { pos(0, 0) } >> { pos(0, 1) }
        random.nextTile() >> { 2 } >> { 4 }
        State state = new State(4, random)

        then:
        state.at(pos(0, 0)) == 2
        state.at(pos(0, 1)) == 4
    }

    def "should swipe properly"() {
        expect:
        previousState.swipe(direction) == nextState

        where:

        previousState         | nextState | direction
        new State(
                [[0, 0, 0, 0],
                 [0, 1, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]) | new State(
                [[0, 0, 0, 0],
                 [1, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]
        )                                 | LEFT
        new State(
                [[0, 0, 0, 0],
                 [0, 1, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]) | new State(
                [[0, 0, 0, 0],
                 [0, 0, 0, 1],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]
        )                                 | RIGHT
        new State(
                [[0, 0, 0, 0],
                 [0, 1, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]) | new State(
                [[0, 1, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]
        )                                 | TOP
        new State(
                [[0, 0, 0, 0],
                 [0, 1, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]) | new State(
                [[0, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 1, 0, 0],
                ] as int[][]
        )                                 | BOTTOM
    }

    def "should not replace tiles when placing new tile - take another random position"() {
        given:
        random.roll(4) >> { pos(0, 0) } >> { pos(0, 1) }
        random.nextTile() >> { 4 }
        State state = new State(
                [[3, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]
        )
        state.random = random

        when:
        state = state.placeNewTile()

        then:
        state.at(pos(0, 0)) == 3
        state.at(pos(0, 1)) == 4
    }

    def "should not place new tile if there is no space left"() {
        given:
        State state = new State(
                [[3, 3, 3, 3],
                 [3, 3, 3, 3],
                 [3, 3, 3, 3],
                 [3, 3, 3, 3],
                ] as int[][]
        )

        when:
        State newState = state.placeNewTile()

        then:
        state == newState
    }

    def "should compare with equals - true"() {
        given:
        State stateA = new State(
                [[1, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]
        )
        State stateB = new State(
                [[1, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]
        )

        expect:
        stateA == stateB
    }

    def "should compare with equals - false"() {
        given:
        State stateA = new State(
                [[1, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 1, 0],
                 [0, 0, 0, 0],
                ] as int[][]
        )
        State stateB = new State(
                [[1, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                 [0, 0, 0, 0],
                ] as int[][]
        )

        expect:
        stateA != stateB
    }

    def "should check which moves are available"() {
        expect:
        new State(grid).possibleMoves as Set == possibleMoves as Set

        where:
        grid         | possibleMoves
        [[1, 0, 0, 0],
         [0, 0, 0, 0],
         [0, 0, 0, 0],
         [0, 0, 0, 0],
        ] as int[][] | [RIGHT, BOTTOM]
        [[0, 0, 0, 1],
         [0, 0, 0, 0],
         [0, 0, 0, 0],
         [0, 0, 0, 0],
        ] as int[][] | [LEFT, BOTTOM]
        [[0, 0, 0, 0],
         [0, 0, 0, 0],
         [0, 0, 0, 0],
         [0, 0, 0, 1],
        ] as int[][] | [LEFT, TOP]
        [[0, 0, 0, 0],
         [0, 0, 0, 0],
         [0, 0, 0, 0],
         [1, 0, 0, 0],
        ] as int[][] | [RIGHT, TOP]
        [[1, 2, 1, 2],
         [1, 3, 2, 1],
         [1, 2, 1, 2],
         [2, 1, 2, 1],
        ] as int[][] | [TOP, BOTTOM]
        [[1, 2, 1, 2],
         [2, 1, 2, 1],
         [1, 2, 1, 2],
         [2, 1, 2, 1],
        ] as int[][] | []
    }
}
