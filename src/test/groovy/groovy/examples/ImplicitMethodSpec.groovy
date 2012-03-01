package groovy.examples

import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
class ImplicitMethodSpec extends Specification {

    def 'implicit method missing with paren'() {
        given:
        def a = new Object()

        when:
        a(1)

        then:
        thrown(MissingMethodException) // missing a.call(Integer)
    }


    def 'implicit method missing no paren'() {
        given:
        def a = new Object()

        when:
        a 1

        then:
        thrown(MissingMethodException) // missing a.call(Integer)
    }


    static class Incrementer {
        def call(Number number) {
            number + 1
        }
    }


    def 'implicit method with paren'() {
        expect:
        new Incrementer()(2) == 3
        new Incrementer().call(2) == 3
    }


    def 'implicit method with no paren'() {
        given:
        Incrementer incr = new Incrementer()

        when:
        def result = incr 2

        then:
        result == 3
    }


    def 'implicit method no paren; bad precidence'() {
        when:
        new Incrementer() 2 == 3

        then:
        thrown(MissingMethodException) // looked for a.call(Boolean)
    }


    static class PassThrough {
        def call(Closure closure) {
            closure.call()
        }
    }


    def 'implicit method passed an inlined closure'() {
        given:
        PassThrough b = new PassThrough()

        when:
        def c = b {
            def f = 4 + 5
            "The number is ${f}"
        }

        then:
        c == 'The number is 9'
    }


    def 'implicit method passed an assigned closure'() {
        given:
        PassThrough b = new PassThrough()
        Closure e = {
            def f = 4 + 5
            "The number is ${f}"
        }

        when:
        def c = b e

        then:
        c == 'The number is 9'
    }

}
