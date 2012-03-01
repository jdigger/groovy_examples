package groovy.examples

import groovy.transform.TupleConstructor
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
@SuppressWarnings(["GroovyLabeledStatement", "GroovyAssignabilityCheck"])
class FunctionalSpec extends Specification {

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
        def surrounder = {String b, String e, String m -> b + m + e}
        def joiner = {String d, List l -> l.join(d)}

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


    @TupleConstructor
    static class Person {
        String name
        Integer age

        // the @TupleConstructor AST transformation creates the following for us automatically:
        //    public Person() {
        //        this.name = null
        //        this.age = null
        //    }
        //
        //    public Person(name) {
        //        this.name = name
        //        this.age = null
        //    }
        //
        //    public Person(name, age) {
        //        this.name = name
        //        this.age = age
        //    }
    }

    /**
     * Functional list operations: filter, map, fold
     * Groovy list operations: findAll, collect, inject
     *
     * Additional list operations: any, every, sort, min, sum ...
     */
    def 'list operations'() {
        given:
        def numbers = [1, 2, 3, 4]

        //
        // filtering
        //

        when: 'imperitive style'
        def filteredList = []
        numbers.each {
            if (it > 2) {
                filteredList << it
            }
        }

        then:
        filteredList == [3, 4]

        expect: 'functional style'
        numbers.findAll {it > 2} == [3, 4]

        //
        // mapping/transformation
        //

        when: 'imperitive style'
        def transformedList = []
        numbers.each {
            transformedList << it * 2
        }

        then:
        transformedList == [2, 4, 6, 8]

        expect: 'functional style'
        numbers.collect {it * 2} == [2, 4, 6, 8]

        //
        // folding/reducing/accumulation
        //

        when: 'imperitive style'
        def total = 0
        numbers.each {
            total += it
        }

        then:
        total == 10

        expect: 'functional style'
        numbers.inject(0) {accumulator, n -> accumulator + n} == 10

        ['J', 'i', 'm'].inject('') {a, c -> a + c} == 'Jim'

        //
        // find first matching value
        //

        when:
        def firstValue = null
        for (int n: numbers) {
            if (n > 1) {
                firstValue = n
                break
            }
        }

        then:
        firstValue == 2

        expect: 'functional style'
        numbers.find {it > 1} == 2

        //
        // max
        //

        when:
        def person1 = new Person('Arturo', 26)
        def person2 = new Person('Luis', 61)
        def person3 = new Person('Laura', 19)
        List<Person> family = [person1, person2, person3]

        then:
        family.max {it.age}.name == 'Luis' // iterates over list looking for max as defined and returns that object
        family.collect {it.age}.max() == 61 // -> [26, 61, 19].max()
        family*.age.max() == 61 // -> family.collect {it?.age}.max(); essentially the same as above

        //
        // existence
        //

        when: 'imperitive style'
        def exists = false
        for (person in family) {
            if (person.age > 60) {
                exists = true
                break
            }
        }

        then:
        exists

        expect: 'functional using "inject"'
        family.inject(false) {found, person -> found || person.age > 60}

        and: 'functional using "any"'
        family.any {it.age > 60}

        //
        // groupBy
        //

        and:
        family.groupBy {it.name[0].toLowerCase()} == ['a': [person1], 'l': [person2, person3]]
    }

}
