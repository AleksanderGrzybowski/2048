package game

import spock.lang.Specification

class SampleSpec extends Specification {
    def "maximum of two numbers"() {
        expect:
        2 + 2 == 4
    }
}
