## Storage - value objects demo
The project demonstrates the benefits of using terms and values from [domain ubiquitous language](https://martinfowler.com/bliki/UbiquitousLanguage.html) in the application source code.

### The domain
The project implements a simple application for managing storage containers for storing various items.
Every item is named and has weight.
Every container has limited capacity described by its maximum weight, therefore a limited number of items can be inserted in the container.

### The implementation
This branch is a sample CQRS implementation of the above domain.
There are two commands:
- adding a container
- inserting an item in a container

and two queries:
- loading a container by identifier (immediately consistent query result)
- loading only large containers (eventually consistent query result)

The read model is updated by handling domain events.
The `Container` aggregate doesn't allow for concurrent modifications.
