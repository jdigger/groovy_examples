package groovy.examples

import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Inspiration from https://github.com/mrhaki/Groovy-Goodness/blob/a429f0809083bebfec391caa49339454e11d388b/Blog/Posts/A_Bit_of_metaClass_DSL/sample.groovy
 */
@Stepwise
@Mixin(MetaClassMixin)
@SuppressWarnings("GroovyAssignabilityCheck")
class MetaClassMagicSpec extends Specification {

    def cleanup() {
        savedMetaClasses.each { clazz, metaClass ->
            GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
        }
    }


    static class A {
        def first
        def last

        /**
         * the "implicit method"
         */
        def call(Closure closure) {
            closure.delegate = this
            closure.call()
        }
    }


    def "closure on instance"() {
        given:
        A a = new A()

        when:
        def b = a {
            first = 'Jim'
            last = 'Moore'
            1
        }

        then:
        a.first == 'Jim'
        b == 1
    }


    def "change meta class"() {
        given:
        metaClassFor(String) {
            or << { String s -> plus(' or ').plus(s) }
            or << { List l -> findAll("(${l.join('|')})") }
            and { String s -> plus(' and ').plus(s) }
            'static' {
                groovy { 'Yeah man!' }
            }
        }

        expect:
        'Groovy or Java?' == ("Groovy" | "Java?")
        ['o', 'o', 'y'] == ("Groovy" | ['o', 'y'])
        'Groovy and Java!' == ("Groovy" & "Java!")
        'Yeah man!' == String.groovy()
    }


    def "check that metaClass was reverted"() {
        when:
        'Yeah man!' == String.groovy()

        then:
        thrown(MissingMethodException)
    }

    static class F {
        static m1(String str) {
            '(' + str + ')'
        }
    }


    def 'Category'() {
        expect:
        use(F) {
            "Here I am".m1() == "(Here I am)"
        }
    }

}
