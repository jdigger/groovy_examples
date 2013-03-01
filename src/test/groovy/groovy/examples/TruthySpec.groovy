package groovy.examples

import spock.lang.Specification

/**
 * http://groovy.codehaus.org/Groovy+Truth
 */
class TruthySpec extends Specification {

    @SuppressWarnings(["GroovyResultOfObjectAllocationIgnored", "GroovyMethodWithMoreThanThreeNegations"])
    def 'truthiness1'() {
        when:
        URI.create('http://groovy.codehaus.org/Groovy+Truth')

        then:
        342342           // any number but 0 is true
        !0               // 0 is false like in all C-ish languages
        new Object()     // non-null object instances are true
        !null            // null is false
        'string is true'
        !''              // an empty string is false
        ['a', 'b']
        ![]              // empty collection is false
        ![:]             // empty map counts as an empty collection
    }

    /**
     * Our own class that defines how to convert itself into a boolean
     */
    static class TruthyClass {
        int age


        boolean asBoolean() {
            age >= 21
        }
    }


    def 'truthiness'() {
        expect:
        new TruthyClass(age: 26)
        !new TruthyClass(age: 16)
    }

    /**
     * Use a "Category" (http://groovy.codehaus.org/Category+and+Mixin+transformations)
     * to provide a custom implementation of asBoolean for java.lang.String.
     */
    static class HasGroovyCategory {
        static boolean asBoolean(String str) {
            str != null && str.contains('Groovy')
        }
    }


    def 'truthiness3'() {
        expect:
        use(HasGroovyCategory) {
            assert !'Only Java'
            assert 'But lots of Groovy!'
            true // this is because Spock wants the use() to return a boolean
        }
    }

}
