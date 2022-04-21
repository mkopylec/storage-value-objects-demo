package com.github.mkopylec.storage.specification

import com.github.mkopylec.storage.Application
import com.github.mkopylec.storage.utils.api.StorageHttpClient
import com.github.mkopylec.storage.utils.concurrent.ConcurrentExecutor
import com.github.mkopylec.storage.utils.mongodb.MongoDb
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles('e2e')
@SpringBootTest(classes = Application, webEnvironment = RANDOM_PORT)
abstract class BasicSpec extends Specification {

    @Autowired
    protected StorageHttpClient storage
    @Autowired
    protected ConcurrentExecutor executor
    @Autowired
    private MongoDb mongoDb

    void cleanup() {
        mongoDb.clear()
    }
}
