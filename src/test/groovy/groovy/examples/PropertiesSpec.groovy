package groovy.examples

import spock.lang.Specification

import java.lang.reflect.Modifier

@SuppressWarnings(["GroovyGetterCallCanBePropertyAccess", "GroovySetterCallCanBePropertyAccess", "GroovyUnresolvedAccess"])
class PropertiesSpec extends Specification {

    /**
     * A simple class defining a single property, 'property1'
     */
    static class SimpleClass {
        String property1
    }


    def 'defining properties'() {
        // Groovy encourages the Uniform Access Principle (http://martinfowler.com/bliki/UniformAccessPrinciple.html)
        // If you do not specify otherwise, it turns a "member field" declaration into a private field and creates
        //   the appropriate getter and setter methods.
        expect:
        Modifier.isPrivate(SimpleClass.getDeclaredField('property1').modifiers)
        SimpleClass.getMethod('getProperty1')
        SimpleClass.getMethod('setProperty1', String)
    }


    def 'setting properties'() {
        final simpleClassTraditional = new SimpleClass()
        simpleClassTraditional.setProperty1('fuzzy bunny')

        final simpleClassSimplified = new SimpleClass(property1: 'fuzzy bunny')

        expect:
        simpleClassTraditional.getProperty1() == simpleClassSimplified.property1
    }


    def 'calling properties'() {
        final propertyName = 'property1'
        final propertyNumber = 1

        final simpleClass = new SimpleClass(property1: 'fuzzy bunny')

        expect:
        simpleClass.property1 == 'fuzzy bunny'
        simpleClass.getProperty1() == 'fuzzy bunny'
        simpleClass[propertyName] == 'fuzzy bunny'
        simpleClass."property${propertyNumber}" == 'fuzzy bunny'

    }

    /**
     * A simple class defining a single property, 'property1'
     */
    static class SimpleClassWithGetter {
        String property1


        String getProperty1() {
            "getter ${property1}"
        }
    }


    def 'explicit properties'() {
        final simpleClass = new SimpleClassWithGetter(property1: 'fuzzy bunny')

        expect:
        simpleClass.property1 == 'getter fuzzy bunny'
        simpleClass.@property1 == 'fuzzy bunny' // direct field access
    }

}
