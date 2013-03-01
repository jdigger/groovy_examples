package groovy.examples

import spock.lang.Specification

/**
 * Parts taken from or inspired-by http://www.slideshare.net/arturoherrero/functional-programming-with-groovy
 *
 * Avoiding Mutable State
 * Side-Effect-Free Functions
 * Referential Transparency
 * First-Class Citizens
 * Higher-Order Functions
 * Lambdas and Closures
 * Lazy Evaluation
 * Recursion
 *
 * @mfeathers: "OO makes code understandable by encapsulating moving parts. FP makes code understandable by minimizing
 * moving parts."
 */
@SuppressWarnings(["GroovyLabeledStatement", "GroovyAssignabilityCheck", "GroovyUntypedAccess", "GroovyUnresolvedAccess"])
class ClosureCompositionSpec extends Specification {

    def 'closure composition - time'() {
        given:
        def minutesToSeconds = {it * 60}
        def hoursToMinutes = {it * 60}
        def daysToHours = {it * 24}

        // '<<' below is the same as f(x) << g(x), or f(g(x))
        def hoursToSeconds = minutesToSeconds << hoursToMinutes // {x -> minutesToSeconds(hoursToMinutes(x)) }
        def daysToSeconds = hoursToSeconds << daysToHours // {x -> hoursToSeconds(daysToHours(x)) }

        expect:
        daysToSeconds(1) == 86400
    }


    def 'closure composition - strings'() {
        given:
        def upper = { it.toUpperCase() }
        def firstLetter = { it.charAt(0) }

        def words = ['don\'t', 'repeat', 'yourself']
        def acronym = words.collect(firstLetter >> upper).join() // {x -> upper(firstLetter(x)) }

        // same as...
        words.collect(firstLetter).collect(upper).join()
        // ...but more concise and efficient

        expect:
        acronym == 'DRY'
    }


    def 'currying'() {
        given:
        def surrounder = {String begin, String end, String middle -> begin + middle + end}
        def joiner = {String delim, List list -> list.join(delim)}

        def xmlBracket = surrounder.curry('<', '/>')
        def squiglyBracket = surrounder.curry('{', '}')

        def commaDelim = joiner.curry(',')
        def xmlDelim = joiner.curry('/><')

        def list = ['a', 'b', 'c']

        when:
        def dashed = joiner '-', list
        def parens = surrounder '(', ')', 'hello'
        def csv = commaDelim list
        def xml = xmlBracket xmlDelim(list)
        def json = squiglyBracket commaDelim(list)

        then:
        dashed == 'a-b-c'
        parens == '(hello)'
        csv == 'a,b,c'
        xml == '<a/><b/><c/>'
        json == '{a,b,c}'
    }


    def 'currying "joiner"'() {
        given:
        def joiner = {String delim, List list -> list.join(delim)}
        def commaDelim = joiner.curry(',')

        def list = ['a', 'b', 'c']

        expect:
        'a-b-c' == joiner('-', list)
        'a,b,c' == commaDelim(list)
    }


    def 'currying "surrounder"'() {
        given:
        def surrounder = {String begin, String end, String middle -> begin + middle + end}
        def xmlBracket = surrounder.curry('<', '/>')

        expect:
        '(hello)' == surrounder('(', ')', 'hello')
        '<hello/>' == xmlBracket('hello')
    }

    // Functional programming is all about composing complex functionality from
    // simple functionality in a very "building blocks" kind of way.
    def '"currying" and "composition"'() {
        given:
        def joiner = {String delim, List list -> list.join(delim)}
        def commaDelim = joiner.curry(',')

        def surrounder = {String begin, String end, String middle -> begin + middle + end}
        def squiglyBracket = surrounder.curry('{', '}')

        def json = commaDelim >> squiglyBracket  // {x -> squiglyBracket( commaDelim(x) ) }

        def list = ['a', 'b', 'c']

        expect:
        '{a,b,c}' == json(list)
    }

}
