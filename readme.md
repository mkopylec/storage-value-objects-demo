## Storage demo
The project demonstrates the benefits of using terms and values from [domain ubiquitous language](https://martinfowler.com/bliki/UbiquitousLanguage.html) in the application source code.

### The domain
The project implements a simple application for managing storage containers for storing various items.

### The problem
One way to implement a domain model is to use custom classes only for aggregates (i.e. container) and for collection types inside aggregates (i.e. item).
Any other values are represented as primitive or built-in type.
Domain invariant violations are also represented as built-in types (exceptions).
This kind of implementation can have several issues:
- computations on domain objects can be split into multiple methods invocations causing potential result inconsistency:
  - calculating container items total weight is split into "calculate weight value" and "calculate weight unit";
  the developer has to remember that these methods needs to be invoked one after another to prevent items modification during total weight calculation
- a single domain invariant can be forced to be checked multiple times:
  - weight value is checked in container and item
  - weight unit is checked in container and item
- methods and constructors arguments can be accidentally passed in wrong order because many of them have the same type:
  - container identifier can be passed as maximum weight unit when constructing the container 
  - item name can be passed as container identifier when loading container by identifier
- it's impossible to catch only domain invariant violations in application logic layer because the exception type used for the violations can also be the type of exception thrown from elsewhere:
  - container repository finding method can throw `IllegalArgumentException`
- domain object classes can have a lot of long and similarly named fields:
  - container has "maximum weight value" and "maximum weight unit" fields 

The goal is to eliminate the usage of primitive and built-in types from the application

Use cases and invariants:

- add container (input: daily price, max weight; output: identifier)
  - identifier must be a generated unique value (UUID is not human readable but for domain simplicity it will be sufficient)
  - price amount must be greater than 0
  - price currency can be PLN or EURO
  - weight quantity must be greater than zero
  - weight unit can be kg or t
  - w newly created container is not rented to anyone thus has no items inside
- rent container (input: tenant identity card number, container identifier, rental period; output: total price for rent)
  - tenant identity card number must be in format AAA000000
  - container must exist
  - container can be rented only per days
  - rental period first day must be in the future
  - rental period last day must not be before first day
- insert item in container (input: container identifier, tenant identity card number, item name, item weight; output: number of items in container, container items total weight)
  - tenant identity card number must be in format AAA000000
  - container must exist
  - item name must not be empty
  - every item in the container must have different name
  - if putting an item will result in exceeding container's max weight, the item cannot be put into container

Box:
Identifier:
- must contain 8 uppercase letters - must be unique for all boxes Max weight:
    
