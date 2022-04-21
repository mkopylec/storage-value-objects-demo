package com.github.mkopylec.storage.specification.container

import com.github.mkopylec.storage.specification.BasicSpec
import com.github.mkopylec.storage.utils.api.requests.ContainerToAdd
import com.github.mkopylec.storage.utils.api.requests.ItemToInsert

import static com.github.mkopylec.storage.utils.api.responses.ItemInsertingViolationType.CONCURRENT_CONTAINER_MODIFICATION
import static org.springframework.http.HttpStatus.CONFLICT
import static org.springframework.http.HttpStatus.OK

class ContainerCommandsSpec extends BasicSpec {

    def "Should insert multiple items in container"() {
        given:
        def containerToAdd = new ContainerToAdd(identifier: 'ABCDEFGH', maximumWeightValue: 2, maximumWeightUnit: 't')
        storage.addContainer(containerToAdd)
        def itemToInsert1 = new ItemToInsert(containerIdentifier: containerToAdd.identifier, name: "item-1", weightValue: 10, weightUnit: 'kg')
        def itemToInsert2 = new ItemToInsert(containerIdentifier: containerToAdd.identifier, name: "item-2", weightValue: 20, weightUnit: 'kg')

        when:
        def response = storage.insertItemInContainer(itemToInsert1)

        then:
        with(response) {
            status == OK
            with(body) {
                identifier != null
            }
            violation.violation == null
        }

        when:
        response = storage.insertItemInContainer(itemToInsert2)

        then:
        with(response) {
            status == OK
            with(body) {
                identifier != null
            }
            violation.violation == null
        }
    }

    def "Should not insert multiple items in container at the same time"() {
        given:
        def containerToAdd = new ContainerToAdd(identifier: 'ABCDEFGH', maximumWeightValue: 2, maximumWeightUnit: 't')
        storage.addContainer(containerToAdd)

        when:
        def responses = executor.executeConcurrently(10) {
            def itemToInsert = new ItemToInsert(containerIdentifier: containerToAdd.identifier, name: "item-$it", weightValue: it, weightUnit: 'kg')
            storage.insertItemInContainer(itemToInsert)
        }

        then:
        responses.any {
            it.status == CONFLICT
            it.body == null
            it.violation.violation == CONCURRENT_CONTAINER_MODIFICATION
        }
    }
}
