package groovy.examples

import spock.lang.Specification

@SuppressWarnings("GroovyMethodWithMoreThanThreeNegations")
class RandomFeaturesSpec extends Specification {

    def '== and identity'() {
        final name = 'Jim'
        final str1 = 'Hello, Jim'
        final str2 = "Hello, ${name}".toString()

        expect:
        str1 == str2   // equivalent to equals(..) in Java
        !str1.is(str2) // checks object identity (i.e., the == operator in Java)
    }


    String method1() {
        if (4 > 5) {
            return 'Huuuuh ??!?'
        }
        'Okey-dokey'
    }


    def 'implicit return value'() {
        expect:
        method1() == 'Okey-dokey'
    }

}
