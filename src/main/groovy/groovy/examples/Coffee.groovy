package groovy.examples

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

@Slf4j
@Canonical
class Coffee {
    String name

    String shots
    String size
    String milk

    static List<String> shotTypes = ['triple']
    static List<String> sizeTypes = ['venti', 'grande']
    static List<String> milkTypes = ['nonfat', 'soy']
    static List<String> modificationTypes = shotTypes + sizeTypes + milkTypes
    static Map<String, String> drinks = ['caramel_macchiato': 'caramel macchiato', 'pikes_roast': 'Pike\'s roast']


    Object methodMissing(String name, Object args) {
        // convert to a "property" request.
        this[name]
    }


    Object propertyMissing(String property) {
        groovy.examples.Coffee.log.debug("Adding ${property} to ${this}")
        if (property in shotTypes) {
            this.shots = property
            this
        }
        else if (property in sizeTypes) {
            this.size = property
            this
        }
        else if (property in milkTypes) {
            this.milk = property
            this
        }
        else if (property in drinks.keySet()) {
            this.name = drinks.get(property)
            this
        }
        else {
            throw new MissingPropertyException(property, Coffee)
        }
    }


    @Override
    String toString() {
        "Coffee[$name, $shots, $size, $milk]"
    }


    String getMessage() {
        // NOTE: this is a technique worth understanding...
        def mods = [shots, size, milk].findAll()
        "A ${mods.isEmpty() ? '' : mods.join(' ') + ' '}${name} is yummy!"
    }

}
