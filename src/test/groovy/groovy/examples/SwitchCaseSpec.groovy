package groovy.examples

import groovy.transform.EqualsAndHashCode
import spock.lang.Specification

@SuppressWarnings(["GroovyMultipleReturnPointsPerMethod", "GroovyUntypedAccess", "GroovyAssignabilityCheck"])
class SwitchCaseSpec extends Specification {

    /**
     * A simple class defining a single property, 'property1' with equals(..) defined
     */
    @EqualsAndHashCode
    static class AnotherSimpleClass {
        String property1
    }


    def switchCase(arg) {
        final map = [key1: 'val1', key2: 'val2']
        final asc = new AnotherSimpleClass(property1: 'fooble')

        switch (arg) {
            case 1: return 'one'
            case 50..60: return arg + 1
            case [23, 'a value', 0.3]: return "Found $arg in a list"
            case map: return "Found ${map[arg]} in a map"
            case ~/.*Groovy.*/: return "!! ${arg} !!"
            case {it instanceof Integer && it < 50}: return "closure matching $arg"
            case asc: return "another simple class"
            default: throw new IllegalArgumentException(arg)
        }
    }


    def 'switch/case'() {
        expect:
        switchCase(1) == 'one'
        switchCase(54) == 55
        switchCase("It's all Groovy") == "!! It's all Groovy !!"
        switchCase("a value") == "Found a value in a list"
        switchCase("key1") == "Found val1 in a map"
        switchCase(23) == "Found 23 in a list"
        switchCase(47) == "closure matching 47"
        switchCase(new AnotherSimpleClass(property1: 'fooble')) == "another simple class"
    }

}
