package groovy.examples

import groovy.transform.ToString
import spock.lang.Specification

@SuppressWarnings("GroovyMethodWithMoreThanThreeNegations")
class StringSpec extends Specification {

    def 'is it a number?'() {
        expect:
        '23'.isInteger()
        !'46.5'.isInteger()
        '46.5'.isDouble()
    }


    @ToString(includePackage = false)
    static class Person {
        String name
    }


    def 'append with left shift'() {
        final person = new Person(name: 'Jim')
        final str = 'Hello, ' << person

        expect:
        str instanceof StringBuffer
        str.toString() == 'Hello, StringSpec$Person(Jim)'
    }


    def 'double quote vs single quote'() {
        given:
        final var = 'variable substitution'
        final doubleQuotes = "These are 'GString' (Groovy Strings) that support ${var}"
        final singleQuotes = 'These are regular Strings that do not support ${var}'

        String str = doubleQuotes

        expect:
        doubleQuotes.contains(var)
        doubleQuotes instanceof GString
        !(doubleQuotes instanceof String)

        str.contains(var)
        !(str instanceof GString)
        str instanceof String

        !singleQuotes.contains(var)
        singleQuotes.contains('${var}')
        singleQuotes instanceof String
        !(singleQuotes instanceof GString)
    }


    def '"doc" quotes'() {
        final descriptor = 'nonsense'

        final longLine = '''
This is a long, multiline
block of text that can just
get done without the need for
lots of \n"+" nonsense.
'''
        final longGLine = """
This is a long, multiline
block of text that can just
get done without the need for
lots of \n"+" ${descriptor}.
"""

        expect:
        longLine == longGLine
    }


    def 'read lines'() {
        final longLine = '''
This is a long, multiline
block of text.
'''

        expect:
        longLine.readLines() == ['', 'This is a long, multiline', 'block of text.']
    }

}
