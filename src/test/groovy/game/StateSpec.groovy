package game

import spock.lang.Specification

import static game.GridSwipeDirection.*
import static game.Position.pos

class StateSpec extends Specification {

    Chance random

    def setup() {
        random = Stub(Chance)
    }

    def "should create new game state with two starting tiles with value 2"() {
        when:
        random.roll(4) >> { pos(0, 0) } >> { pos(0, 1) }
        State state = new State(4, random)

        then:
        state.at(pos(0, 0)) == 2
        state.at(pos(0, 1)) == 2
    }

    def "in new game, after swiping left (no collapsing) should put new tile"() {
        given:
        random.roll(4) >> { pos(0, 0) } >> { pos(1, 1) } >> { pos(3, 3) }
        State state = new State(4, random)

        when:
        state = state.swipe(LEFT)

        then:
        state.at(pos(0, 0)) == 2
        state.at(pos(1, 0)) == 2
        state.at(pos(3, 3)) == 2
    }

    def "in new game, after swiping right (no collapsing) should put new tile"() {
        given:
        random.roll(4) >> { pos(0, 0) } >> { pos(1, 1) } >> { pos(0, 0) }
        State state = new State(4, random)

        when:
        state = state.swipe(RIGHT)

        then:
        state.at(pos(0, 3)) == 2
        state.at(pos(1, 3)) == 2
        state.at(pos(0, 0)) == 2
    }

    def "in new game, after swiping top (no collapsing) should put new tile"() {
        given:
        random.roll(4) >> { pos(3, 0) } >> { pos(3, 1) } >> { pos(3, 3) }
        State state = new State(4, random)

        when:
        state = state.swipe(TOP)

        then:
        state.at(pos(0, 0)) == 2
        state.at(pos(0, 1)) == 2
        state.at(pos(3, 3)) == 2
    }

    def "in new game, after swiping bottom (no collapsing) should put new tile"() {
        given:
        random.roll(4) >> { pos(0, 0) } >> { pos(0, 1) } >> { pos(3, 3) }
        State state = new State(4, random)

        when:
        state = state.swipe(BOTTOM)

        then:
        state.at(pos(3, 0)) == 2
        state.at(pos(3, 1)) == 2
        state.at(pos(3, 3)) == 2
    }
}
