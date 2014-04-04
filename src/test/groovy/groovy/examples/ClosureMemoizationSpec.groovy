package groovy.examples

import spock.lang.Specification

@SuppressWarnings("GroovyUntypedAccess")
class ClosureMemoizationSpec extends Specification {

    def 'closure no memoization'() {
        given:
        int counter = 0
        final calculation = {counter++; it + 2}

        when:
        final val = (1..1000).collect {it % 2}.collect(calculation)

        then:
        val.size() == 1000
        counter == 1000
    }


    def 'closure with memoization'() {
        given:
        int counter = 0
        final calculation = {counter++; it + 2}.memoize()

        when:
        final val = (1..1000).collect {it % 2}.collect(calculation)

        then:
        val.size() == 1000
        counter == 2
    }

}
