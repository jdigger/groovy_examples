package groovy.examples

import java.awt.Point
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import spock.lang.Specification

@SuppressWarnings(["GroovyLabeledStatement", "GroovyUnusedAssignment", "GroovyAccessibility", "GroovyVariableNotAssigned"])
class ClosureSpec extends Specification {

    def 'closure definitions'() {
        expect:
        ({ 12 }.call() == 12)                                      // invoked as a Closure object
        ({ 12 }() == 12)                                           // invoked as if the closure itself was a method
        ({-> 12 }.call() == 12)                                    // closure declared to take no args
        ({ it + 1 }.call(11) == 12)                                // "it" is the implicit name of the sole argument
        ({ a -> a + 1 }.call(11) == 12)                            // give an explicit name to the argument
        ({ a, b -> a + b }.call(11, 12) == 23)                     // give an explicit name to the argument
        ({ String a, Number b -> a + b }.call('11', 12) == '1112') // give explicit types
    }


    def 'bad closure definitions'(Object[] args, Closure closure) {
        when:
        closure.call(* args)  // "spread" the array across the parameters

        then:
        thrown(Exception)

        where:
        args | closure
        []   | {it + 1}          // NullPointerException: no arguments were provided
        []   | {-> it + 1}       // MissingPropertyException: closure declared that it takes no args
        [11] | {-> 1}            // MissingMethodException: tried pass args to a no-arg "method"
        [11] | { a -> it }       // MissingPropertyException: "it" is undefined because an explicit variable was defined
        [11] | { String a -> a } // MissingMethodException: passed args don't match signature
    }


    def 'lexical scoping'() {
        when:
        def a = 1
        Closure b = {def c = a + 2; c} // 'a' is brought in from the surrounding context

        then:
        b(1) == 3

        when:
        Closure d = {def e = f + 2; e}
        d()

        then:
        thrown(MissingPropertyException) // 'f' was not defined, but didn't error until evaluation...
    }


    def 'late evaluation'() {
        given:
        def a = 1
        Closure b = {c -> def d = a + c + 2; d}
        a = 'hello'

        expect:
        b(1) == 'hello12'
    }


    def 'delegate'() {
        when:
        Closure d = {def e = x + 2; e}
        d.delegate = new Point(12, 13)
        def f = d()

        then:
        f == 14
    }


    def 'setting owner'() {
        when:
        Closure d = {def e = x + 2; e}
        d.owner
        d.owner = new Point(12, 13)

        then:
        thrown(MissingPropertyException) // owner is a read-only property
    }


    static class B {
        def x = 'B'
        Closure d = {x -> owner.x + delegate.x + this.x + x}
        Closure e = {x}
        Closure s = {x + y}

        private String name = 'bName'

        def getClosure(argName) {
            { name -> "${name} - ${owner.name} - ${argName}"}
        }
    }


    def 'owner/delegate/this'() {
        when:
        Closure d = new B().d
        d.delegate = new Point(12, 13)

        then:
        d(4) == 'B12.0B4'

        when:
        def argName = 'lName'
        Closure t = new B().getClosure('pName')

        then:
        t('myName') == 'myName - bName - pName'
    }


    def 'missing owner property'() {
        when:
        Closure d = {owner.x}
        d.delegate = new Point(12, 13)
        d(4)

        then:
        thrown(MissingPropertyException) // the containing object (owner) is the Spec, which doesn't have 'x'
    }


    def 'resolve strategy'() {
        when:
        Closure e = new B().e
        e.delegate = new Point(12, 13)
        e.resolveStrategy = Closure.DELEGATE_FIRST
        def f = e()

        then:
        f == 12.0

        when:
        Closure e2 = new B().e
        e2.delegate = new Point(12, 13)
        e2.resolveStrategy = Closure.OWNER_FIRST  // default
        def g = e2()

        then:
        g == 'B'

        when:
        Closure s = new B().s
        s.delegate = new Point(12, 13)
        s.resolveStrategy = Closure.DELEGATE_FIRST
        def h = s()

        then:
        h == 25
    }


    def 'multi-args'() {
        given:
        def c = {a, Object[] b -> a + b}
        def d = {a, Object... b -> a + b}

        expect:
        c('a') == 'a[]'
        c('a', 'b') == 'a[b]'
        c('a', 'b', 'c') == 'a[b, c]'
        d('a', 'b', 'c') == 'a[b, c]'
        d('a') == 'a[]'
    }


    static class C {
        def m1(Closure c) {
            c.call()
        }

        def binary(a, b, Closure c) {
            c.call(a, b)
        }
    }


    def 'closure to method'() {
        given:
        C c = new C()

        expect:
        c.m1({ 'hello' }) == 'hello'           // regular parameter
        c.m1() { 'hello' } == 'hello'          // a closure right after a method call is the same as passing it as
        //   the last param, so it's identical to the line above
        c.m1 { 'hello' } == 'hello'            // same as above
        c.binary(9, 2, {l, r -> l - r }) == 7
        c.binary(1, 2) {l, r -> l + r } == 3   // same as above
    }


    def 'as single method interface'() {
        given:
        ExecutorService executor = Executors.newSingleThreadExecutor()

        when: "doing things the 'Java' way"
        final int a = 34
        final int[] b = new int[1] // have to put in an array because Java can only use final vars from context
        Future<Integer> c = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() {
                b[0] = a;  // dereference the array to write to it
                return a * a;
            }
        });
        waitFor c

        then:
        b[0] == 34  // dereference the array again to read it
        c.get() == 1156

        when: "doing things the 'Groovy' way"
        int d = 34
        int e
        def f = executor.submit({-> e = d; d * d } as Callable<Integer>)
        waitFor f

        then:
        e == 34
        f.get() == 1156
    }


    static void waitFor(Future future) {
        while (!future.done) {
            Thread.sleep(50)
        }
    }


    def 'as ad-hoc class in a Map'() {
        given:
        def obj = [aMethod: {'a'}, bMethod: {v -> 'b' + v}]

        expect:
        obj.aMethod() == 'a'
        obj.bMethod('1') == 'b1'
    }


    def 'as multi-method interface'() {
        given:
        boolean addCalled = false
        boolean getCalled = false
        List obj = [add: {addCalled = true}, get: {getCalled = true}] as List

        when:
        obj.add('a')
        obj.get(2)

        then:
        addCalled
        getCalled
    }


    static class D {
        D(String val) {
            // don't care; just need to make sure there's no no-arg constructor
        }

        def m1() {
            1
        }
    }


    def 'as multi-method class'() {
        given:
        boolean addCalled = false
        boolean notifyCalled = false
        Observable obj = [addObserver: {addCalled = true}, notifyObservers: {notifyCalled = true}] as Observable

        when:
        obj.addObserver(null)
        obj.notifyObservers()

        then:
        addCalled
        notifyCalled

        expect:
        obj.deleteObserver(null)

        when:
        D d = [m1: {-> 2}] as D

        then:
        thrown(ArrayIndexOutOfBoundsException) // confusing error; caused because there's no no-arg constructor
    }

}
