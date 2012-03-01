package groovy.examples

import spock.lang.Specification
import spock.lang.Unroll

class CoffeeSpec extends Specification {

    def 'coffee DSL with order'() {
        given:
        CoffeeOrder order = new CoffeeOrder()

        when:
        order.add {
            triple venti nonfat caramel_macchiato
        }
        order.add {
            soy venti caramel_macchiato
        }
        order.add {
            caramel_macchiato nonfat
        }
        order.add {
            caramel_macchiato
        }

        then:
        order[0].message == 'A triple venti nonfat caramel macchiato is yummy!'
        order[1].message == 'A venti soy caramel macchiato is yummy!'
        order[2].message == 'A nonfat caramel macchiato is yummy!'
        order[3].message == 'A caramel macchiato is yummy!'
    }


    def 'coffee DSL using properties'() {
        when:
        def coffee = new Coffee().triple.grande.nonfat.caramel_macchiato

        then:
        coffee.message == 'A triple grande nonfat caramel macchiato is yummy!'
    }


    def 'coffee DSL using constructor properties'() {
        when:
        def coffee = new Coffee(shots: 'triple', size: 'grande', milk: 'nonfat', name: 'caramel macchiato')

        then:
        coffee.message == 'A triple grande nonfat caramel macchiato is yummy!'
    }


    def 'coffee DSL using with{}'() {
        when:
        def coffee = new Coffee().with {
            triple grande nonfat caramel_macchiato
        }

        then:
        coffee.message == 'A triple grande nonfat caramel macchiato is yummy!'
    }


    @Unroll({"coffee DSL using '${message}'"})
    def 'coffee DSL using with{Closure}'(String message, Closure block) {
        when:
        def coffee = new Coffee().with(block)

        then:
        coffee.message == message

        where:
        message                                              | block
        'A triple grande nonfat caramel macchiato is yummy!' | { triple grande nonfat caramel_macchiato }
        'A venti soy Pike\'s roast is yummy!'                | { venti soy pikes_roast }
    }

}
