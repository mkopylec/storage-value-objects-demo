package com.github.mkopylec.storage.utils.concurrent

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

import java.util.concurrent.Callable
import java.util.function.Function

@Component
class ConcurrentExecutor {

    private ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor().tap {
        corePoolSize = 10
        maxPoolSize = 10
        initialize()
    }

    def <R> List<R> executeConcurrently(int times, Function<Integer, R> action) {
        (1..times).collect { i ->
            executor.submit(new Callable<R>() {

                @Override
                R call() throws Exception {
                    return action(i)
                }
            })
        }.collect { it.get() }
    }
}
