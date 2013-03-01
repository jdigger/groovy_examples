package groovy.examples

import spock.lang.Specification

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@SuppressWarnings("GroovyUncheckedAssignmentOfMemberOfRawType")
class AnnotationSpec extends Specification {

    /**
     * Actually doing invariants, preconditions, etc. is MUCH better done with
     * https://github.com/andresteingress/gcontracts/wiki
     */
    class InvarientedBase {

        void setProperty(String propName, Object value) {
            def meta = this.metaClass.getMetaProperty(propName)
            if (meta) {
                checkInvarient()
                meta.setProperty(this, value)
                checkInvarient()
            }
            else {
                throw new MissingPropertyException(propName, this.class)
            }
        }


        def checkInvarient() {
            def invariantAnnotation = this.class.getAnnotation(Invariant)
            Class closureClass = invariantAnnotation.value()
            Closure check = closureClass.newInstance(this, this)
            check.delegate = this
            if (!check.call()) {
                throw new IllegalStateException("Invarient violated")
            }
        }

    }


    @Invariant({ number >= 0 })
    class Distance extends InvarientedBase {
        float number
        String unit
    }


    def 'validate invariant0'() {
        given:
        def invariantAnnotation = Distance.getAnnotation(Invariant)
        Class closureClass = invariantAnnotation.value()

        def d = new Distance(number: 10, unit: "meters")

        when:
        Closure check = closureClass.newInstance(d, d)

        then:
        check(d)
    }


    def 'validate invariant'() {
        given:
        def d = new Distance(number: 10, unit: "meters")

        when:
        d.number = -2

        then:
        thrown(IllegalStateException)
    }

}

@Retention(RetentionPolicy.RUNTIME)
@interface Invariant {
    Class value() // will hold a closure class
}
