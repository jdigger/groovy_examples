package groovy.examples

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import spock.lang.Specification

@SuppressWarnings("GroovyUncheckedAssignmentOfMemberOfRawType")
class AnnotationSpec extends Specification {

    /**
     * Example taken from http://docs.codehaus.org/display/GROOVY/Groovy+1.8+release+notes
     */
    @Invariant({ number >= 0 })
    class Distance {
        float number
        String unit
    }


    def 'validate invariant'() {
        given:
        def invariantAnnotation = Distance.getAnnotation(Invariant)
        Class closureClass = invariantAnnotation.value()

        def d = new Distance(number: 10, unit: "meters")

        when:
        Closure check = closureClass.newInstance(d, d)

        then:
        check(d)
    }

}

@Retention(RetentionPolicy.RUNTIME)
@interface Invariant {
    Class value() // will hold a closure class
}
