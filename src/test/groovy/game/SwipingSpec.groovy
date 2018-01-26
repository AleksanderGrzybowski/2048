package game

import spock.lang.Specification

import static game.core.RowSwipeDirection.HEAD
import static game.core.RowSwipeDirection.TAIL
import static game.core.SwipeSeq.swipeSeq

class SwipingSpec extends Specification {
    def "should not move anything if row is empty or filled with non-collapsible tiles"() {
        expect:
        swipeSeq(input as int[], direction) == output as int[]

        where:
        input        | direction | output
        [0, 0, 0, 0] | HEAD      | [0, 0, 0, 0]
        [1, 2, 1, 2] | HEAD      | [1, 2, 1, 2]
    }

    def "should not move a single tile if it is on the edge"() {
        expect:
        swipeSeq(input as int[], direction) == output as int[]

        where:
        input        | direction | output
        [2, 0, 0, 0] | HEAD      | [2, 0, 0, 0]
        [0, 0, 0, 2] | TAIL      | [0, 0, 0, 2]
    }

    def "should move non-collapsing tiles to the edge"() {
        expect:
        swipeSeq(input as int[], direction) == output as int[]

        where:
        input        | direction | output
        [0, 2, 0, 0] | HEAD      | [2, 0, 0, 0]
        [0, 2, 0, 0] | TAIL      | [0, 0, 0, 2]

        [0, 2, 1, 0] | HEAD      | [2, 1, 0, 0]
        [0, 2, 1, 0] | TAIL      | [0, 0, 2, 1]
    }

    def "should collapse two adjacent tiles with the same number"() {
        expect:
        swipeSeq(input as int[], direction) == output as int[]

        where:
        input        | direction | output
        // on the edge
        [2, 2, 0, 0] | HEAD      | [4, 0, 0, 0]
        [0, 0, 2, 2] | TAIL      | [0, 0, 0, 4]

        // in the middle
        [0, 2, 2, 0] | HEAD      | [4, 0, 0, 0]
        [0, 2, 2, 0] | TAIL      | [0, 0, 0, 4]

        // with zeros between
        [0, 2, 0, 2] | HEAD      | [4, 0, 0, 0]
        [0, 2, 0, 2] | TAIL      | [0, 0, 0, 4]
        [2, 0, 2, 0] | HEAD      | [4, 0, 0, 0]
        [2, 0, 2, 0] | TAIL      | [0, 0, 0, 4]

        // with some numbers between
        [0, 2, 2, 1] | HEAD      | [4, 1, 0, 0]
        [1, 2, 2, 0] | HEAD      | [1, 4, 0, 0]
        [0, 2, 2, 1] | TAIL      | [0, 0, 4, 1]
        [1, 2, 2, 0] | TAIL      | [0, 0, 1, 4]
    }

    def "should collapse a double pair of adjacent tiles with the same number"() {
        expect:
        swipeSeq(input as int[], direction) == output as int[]

        where:
        input        | direction | output
        // same numbers
        [2, 2, 2, 2] | HEAD      | [4, 4, 0, 0]
        [2, 2, 2, 2] | TAIL      | [0, 0, 4, 4]

        // different numbers
        [2, 2, 3, 3] | HEAD      | [4, 6, 0, 0]
        [2, 2, 3, 3] | TAIL      | [0, 0, 4, 6]
    }
}
