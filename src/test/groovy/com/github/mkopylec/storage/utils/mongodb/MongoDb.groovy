package com.github.mkopylec.storage.utils.mongodb

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class MongoDb {

    private ReactiveMongoTemplate mongoDb

    private MongoDb(ReactiveMongoTemplate mongoDb) {
        this.mongoDb = mongoDb
    }

    void clear() {
        mongoDb.collectionNames.flatMap { mongoDb.remove(new Query(), it) }.blockLast()
    }
}
