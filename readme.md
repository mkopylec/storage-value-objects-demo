## Storage - value objects demo
The project demonstrates the benefits of using terms and values from [domain ubiquitous language](https://martinfowler.com/bliki/UbiquitousLanguage.html) in the application source code.

### The domain
The project implements a simple application for managing storage containers for storing various items.
Every item is named and has weight.
Every container has limited capacity described by its maximum weight, therefore a limited number of items can be inserted in the container.
There are three use cases implemented:
- adding a container
- inserting an item in a container
- loading a container

### The problem
One way to implement the storage domain model is to use custom classes only for aggregate (container) and for collection type inside aggregate (item).
Any other values can be represented as primitive or built-in types.
Domain invariant and use case violations can also be represented as built-in types (exceptions).
This kind of implementation suffers from several issues:

##### Issue 1
Computations on domain objects can be split into multiple methods invocations causing the result to be inconsistent:
- [calculating container items total weight is split into "calculate weight value" and "calculate weight unit"](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Container.kt#L28-L32);
  [these methods needs to be invoked one after another to prevent items modification during total weight calculation](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/ContainerLoader.kt#L18-L19)

##### Issue 2
Some domain logic can be duplicated:
- weight value invariant is checked in [container](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Container.kt#L19) and [item](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Item.kt#L21)
- weight unit invariant is checked in [container](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Container.kt#L20) and [item](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Item.kt#L22)

##### Issue 3
Methods and constructors arguments can be accidentally passed in wrong order because many of them have the same type:
- [container identifier can be passed as maximum weight unit when constructing the container](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Containers.kt#L11)
- [item name can be passed as container identifier when loading container by identifier](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Containers.kt#L15)
- [container maximum weight value can be passed as container items weight value](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/ContainerLoader.kt#L14)
- and so on...

##### Issue 4
It's hard to catch only domain invariant violations in application logic layer because the exception type used for the violations can also be the type of exception thrown from elsewhere:
- when invariant is violated an `IllegalArgumentException` is thrown but [the container repository implementation can also throw it](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/ContainerRepository.kt#L5-L6)

##### Issue 5
It's hard to catch only use case violations in the api layer because the exception type used for the violations can also be the type of exception thrown from elsewhere:
- when use case is violated an `IllegalStateException` is thrown but [the container repository implementation can also throw it](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/ContainerRepository.kt#L5-L6)

##### Issue 6
It's hard to map domain invariant violation to some meaningful error code because all the violations are of the same type:
- all invariant violations are represented by `IllegalArgumentException`

##### Issue 7
Domain object classes can have a lot of long and similarly named fields:
- [container has "maximum weight value" and "maximum weight unit" fields](/../step-01/src/main/kotlin/com/github/mkopylec/storage/core/container/Container.kt#L10-L11)

### The solution
These issues can be eliminated by using terms and values from domain ubiquitous language in the source code.
The following describes how the code can be refactored to achieve that.
The refactor is done iteratively, step by step, so you can compare what has been changed since previous step.
Each step has its own branch, from step-01 to step-08, so you can compare them using git diff.
If you want to quickly jump to the final implementation go to [step-08 branch](/../../tree/step-08).

##### Step 1 - start
The implementation uses custom classes only for aggregate (container) and for collection type inside aggregate (item).
All the issues are present at this stage.

##### Step 2 - complex value object
A complex value object (with multiple properties) is introduced to represent the weight term.
This way 1st, 2nd and 7th issue is gone.

##### Step 3 - simple value objects
A simple value object is introduced for every domain object class property.
No issue is eliminated at this stage but the step is necessary to proceed.

##### Step 4 - domain object classes arguments
The introduced simple value objects are exposed in domain objects classes APIs: constructors and methods.
The 3rd issue is partially resolved: container identifier can no more be passed as maximum weight unit when constructing the container.

##### Step 5 - secondary ports arguments
The introduced simple value object (container identifier) is used as argument in container repository.
The 3rd issue is further resolved but not yet eliminated: item name can no more be passed as container identifier when loading container by identifier.

##### Step 6 - DTO properties
Input DTOs exposes properties as simple value objects only.
Output DTOs can only be constructed from simple value objects.
The 3rd issue is finally gone.

##### Step 7 - invariant violations
Custom exceptions are introduced to represent domain invariants violations.
The step resolves the 4th and 6th issue.

##### Step 8 - use case violations
Custom exceptions are introduced to represent use case violations.
This eliminates the 5th issue.

### The conclusion
Using custom class for every term or value from domain ubiquitous language introduces some overhead.
That's because using a value object to replace every primitive and built-in type results in more lines of code.
But it's a marginal disadvantage comparing to all the issues described in [the problem](#the-problem) section.
In return, we get a fully type-safe code that speeds up the implementation because IDE auto-completion works excellent with it.
Value objects are powerful, they:
- encapsulate domain invariants in one place
- allow moving most of the entities' behaviour inside them
- are immutable, so they can be safely used in multithreaded environment
- have names that are known to domain experts, so the communication between developers and experts is easier

Naming exceptions using business names results in very descriptive stack traces.
Such a stack trace can even be understandable for a non-technical person:
```
com.github.mkopylec.storage.core.ContainerNotAdded: INVALID_CONTAINER_IDENTIFIER
	at ...
Caused by: com.github.mkopylec.storage.core.container.InvalidContainerIdentifier: identifier=i_am_invalid
	at ...
```
