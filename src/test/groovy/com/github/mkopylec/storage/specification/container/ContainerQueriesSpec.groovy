package com.github.mkopylec.storage.specification.container

import com.github.mkopylec.storage.specification.BasicSpec
import com.github.mkopylec.storage.utils.api.requests.ContainerToAdd
import com.github.mkopylec.storage.utils.api.requests.ContainerToLoad
import com.github.mkopylec.storage.utils.api.requests.ItemToInsert

import static org.springframework.http.HttpStatus.OK

class ContainerQueriesSpec extends BasicSpec {

    def "Should load a newly added container"() {
        given:
        def containerToAdd = new ContainerToAdd(identifier: 'ABCDEFGH', maximumWeightValue: 2, maximumWeightUnit: 't')
        storage.addContainer(containerToAdd)
        def containerToLoad = new ContainerToLoad(identifier: containerToAdd.identifier)

        when:
        def response = storage.loadContainer(containerToLoad)

        then:
        with(response) {
            status == OK
            with(body) {
                identifier == containerToAdd.identifier
                maximumWeightInKilograms == containerToAdd.maximumWeightValue * 1000
                itemsQuantity == 0
                items == []
            }
            violation == null
        }
    }

    def "Should load a nonempty container"() {
        given:
        def containerToAdd = new ContainerToAdd(identifier: 'ABCDEFGH', maximumWeightValue: 2, maximumWeightUnit: 't')
        storage.addContainer(containerToAdd)
        def itemsToInsert = 2
        itemsToInsert.times {
            def itemToInsert = new ItemToInsert(containerIdentifier: containerToAdd.identifier, name: "item-$it", weightValue: it, weightUnit: 'kg')
            storage.insertItemInContainer(itemToInsert)
        }
        def containerToLoad = new ContainerToLoad(identifier: containerToAdd.identifier)

        when:
        def response = storage.loadContainer(containerToLoad)

        then:
        with(response) {
            status == OK
            with(body) {
                identifier == containerToAdd.identifier
                maximumWeightInKilograms == containerToAdd.maximumWeightValue * 1000
                itemsQuantity == itemsToInsert
                items.size() == itemsToInsert
                itemsToInsert.times { i ->
                    with(items[i]) {
                        identifier != null
                        name == "item-$i"
                        weightInKilograms == i
                    }
                }
            }
            violation == null
        }
    }

    def "Should not load nonexistent container"() {
        given:
        def containerToLoad = new ContainerToLoad(identifier: 'ABCDEFGH')

        when:
        def response = storage.loadContainer(containerToLoad)

        then:
        with(response) {
            status == OK
            body == null
            violation == null
        }
    }
}
