package groovy.examples


class CoffeeOrder {
    private List<Coffee> coffees = []

    def add(drink) {
        if (drink instanceof Closure) {
            drink.delegate = new Coffee()
            coffees << (Coffee) drink.call()
        }
        else {
            coffees << drink
        }
        this
    }


    Coffee getAt(index) {
        (Coffee) coffees[index]
    }

}
