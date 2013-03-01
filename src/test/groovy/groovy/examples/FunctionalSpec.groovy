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
 *
 * http://steve-yegge.blogspot.com/2006/03/execution-in-kingdom-of-nouns.html
 */
@SuppressWarnings(["GroovyLabeledStatement", "GroovyAssignabilityCheck"])
class FunctionalSpec extends Specification {

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

    final numbers = [1, 2, 3, 4]

    final person1 = new Person('Arturo', 26)
    final person2 = new Person('Luis', 61)
    final person3 = new Person('Laura', 19)
    final family = [person1, person2, person3]


    def 'list filtering'() {
        when: 'imperitive style'
        // note the amount of state we need to keep track of
        def filteredList = []
        numbers.each {
            if (it > 2) {
                filteredList << it
            }
        }

        then:
        filteredList == [3, 4]

        expect: 'functional style'
        // note we simply state *what* we want to find, not *how* to do it
        numbers.findAll {it > 2} == [3, 4]
    }


    def 'mapping/transformation'() {
        when: 'imperitive style'
        def transformedList = []
        numbers.each {
            transformedList << it * 2
        }

        then:
        transformedList == [2, 4, 6, 8]

        then: 'functional style'
        numbers.collect {it * 2} == [2, 4, 6, 8]

        then: 'functional style for Persons'
        family.collect {it.name} == ['Arturo', 'Luis', 'Laura']
    }


    @SuppressWarnings("GroovyResultOfAssignmentUsed")
    def 'folding/reducing/accumulation'() {
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

        and: 'functional style with default head'
        // if you don't specify a starting value, it assumes the head (first) item of the list
        numbers.inject {accumulator, n -> accumulator + n} == 10
        ['J', 'i', 'm'].inject {a, c -> a + c} == 'Jim'
    }


    @SuppressWarnings("GroovyBreak")
    def 'find first matching value'() {
        when:
        def firstValue = null
        for (int n : numbers) {
            if (n > 1) {
                firstValue = n
                break
            }
        }

        then:
        firstValue == 2

        expect: 'functional style'
        numbers.find {it > 1} == 2
    }


    def 'find every matching value'() {
        when:
        def peopleFound = []
        for (Person person : family) {
            if (person.age > 21) {
                peopleFound.add(person)
            }
        }

        then:
        peopleFound == [person1, person2]

        expect: 'functional style'
        family.findAll {it.age > 21} == [person1, person2]
    }


    @SuppressWarnings(["GroovyUntypedAccess", "GroovyUnresolvedAccess"])
    def 'max with collect and spread-operator'() {
        expect:
        family.max {it.age}.name == 'Luis' // iterates over list looking for max as defined and returns that object
        family.collect {it.age}.max() == 61 // -> [26, 61, 19].max()
        family*.age.max() == 61 // -> family.collect {it?.age}.max(); essentially the same as above
    }


    @SuppressWarnings(["GroovyBreak", "GroovyMissingReturnStatement"])
    def 'existence'() {
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
    }


    def 'groupBy'() {
        expect:
        family.groupBy {it.name[0].toLowerCase()} == ['a': [person1], 'l': [person2, person3]]
    }

}
