## Storage - value objects demo
The project demonstrates the benefits of using terms and values from [domain ubiquitous language](https://martinfowler.com/bliki/UbiquitousLanguage.html) in the application source code.

### The domain
The project implements a simple application for managing storage containers for storing various items.
Every item is named and weight.
Every container has limited capacity described by the maximum weight, therefore a limited number of items can be inserted in the container.
There are three use cases implemented:
- adding a container
- inserting an item in a container
- loading a container

### The problem
One way to implement the storage domain model is to use custom classes only for aggregate (container) and for collection type inside aggregate (item).
Any other values can be represented as primitive or built-in types.
Domain invariant violations can also be represented as built-in types (exceptions).
This kind of implementation suffers from several issues:

##### Issue 1
Computations on domain objects can be split into multiple methods invocations causing the result to be inconsistent:
- [calculating container items total weight is split into "calculate weight value" and "calculate weight unit"](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Container.kt#L28-L32);
[these methods needs to be invoked one after another to prevent items modification during total weight calculation](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/ContainerLoader.kt#L18-L19)

##### Issue 2
Some domain logic can be duplicated:
- weight value invariant is checked in [container](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Container.kt#L19) and [item](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Item.kt#L21)
- weight unit invariant is checked in [container](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Container.kt#L20) and [item](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Item.kt#L22)

##### Issue 3
Methods and constructors arguments can be accidentally passed in wrong order because many of them have the same type:
- [container identifier can be passed as maximum weight unit when constructing the container](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Containers.kt#L11)
- [item name can be passed as container identifier when loading container by identifier](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Containers.kt#L15)
- and so on...

##### Issue 4
It's hard to catch only domain invariant violations in application logic layer because the exception type used for the violations can also be the type of exception thrown from elsewhere:
- when invariant is violated an `IllegalArgumentException` is thrown but [the container repository implementation can also throw it](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/ContainerRepository.kt#L5-L6)

##### Issue 5
It's hard to catch only use case violations in the api layer because the exception type used for the violations can also be the type of exception thrown from elsewhere:
- when use case is violated an `IllegalStateException` is thrown but [the container repository implementation can also throw it](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/ContainerRepository.kt#L5-L6)

##### Issue 6
It's hard to map domain invariant violation to some meaningful error code because all the violations are of the same type: 
- all invariant violations are represented by `IllegalArgumentException`

##### Issue 7
Domain object classes can have a lot of long and similarly named fields:
- [container has "maximum weight value" and "maximum weight unit" fields](https://github.com/mkopylec/storage-value-objects-demo/blob/step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Container.kt#L10-L11)
