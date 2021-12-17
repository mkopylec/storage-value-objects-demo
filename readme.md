## Storage - value objects demo
The project demonstrates the benefits of using terms and values from [domain ubiquitous language](https://martinfowler.com/bliki/UbiquitousLanguage.html) in the application source code.

### The domain
The project implements a simple application for managing storage containers for storing various items.
There are three use cases implemented:
- adding a container
- inserting an item in a container
- loading a container

### The problem
One way to implement the storage domain model is to use custom classes only for aggregate (container) and for collection type inside aggregate (item).
Any other values can be represented as primitive or built-in types.
Domain invariant violations can also be represented as built-in types (exceptions).
This kind of implementation suffers from several issues:
- computations on domain objects can be split into multiple methods invocations causing the result to be inconsistent:
  - calculating container items total weight is split into "calculate weight value" and "calculate weight unit";
  these methods needs to be invoked one after another to prevent items modification during total weight calculation
- some domain logic can be duplicated:
  - weight value invariant is checked in container and item
  - weight unit invariant is checked in container and item
- methods and constructors arguments can be accidentally passed in wrong order because many of them have the same type:
  - container identifier can be passed as maximum weight unit when constructing the container 
  - item name can be passed as container identifier when loading container by identifier
  - and so on...
- it's hard to catch only domain invariant violations in application logic layer because the exception type used for the violations can also be the type of exception thrown from elsewhere:
  - when invariant is violated an `IllegalArgumentException` is thrown but the container repository implementation can also throw it
- it's hard to catch only use case violations in the api layer because the exception type used for the violations can also be the type of exception thrown from elsewhere:
  - when use case is violated an `IllegalStateException` is thrown but the container repository implementation can also throw it
- it's hard to map domain invariant violation to some meaningful error code because all the violations are of the same type: `IllegalArgumentException`
- domain object classes can have a lot of long and similarly named fields:
  - container has "maximum weight value" and "maximum weight unit" fields
