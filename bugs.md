## Errors encountered upon first compilation (Part 0):

[ERROR] /Users/jonathanchen/col/swe/jonathan-chen-miniproject/IndividualProject/src/main/java/dev/coms4156/project/individualproject/controller/RouteController.java:[93,3] missing return statement

Added return statement to appropriate method, with correct value.

[ERROR] /Users/jonathanchen/col/swe/jonathan-chen-miniproject/IndividualProject/src/main/java/dev/coms4156/project/individualproject/model/BOOK.java:[268,20] incompatible types: java.lang.Object cannot be converted to dev.coms4156.project.individualproject.model.BOOK

Added typecast to ensure Object is converted to Book before assignment.

## Errors encountered before test suite creation (Part 2):

[ERROR] /Users/jonathanchen/col/swe/jonathan-chen-miniproject/IndividualProject/src/main/java/dev/coms4156/project/individualproject/model/Book.java:[190,3] missing return statement

Added return value to language proprety getter method.

## Bugs found during bug fixing (Part 3):

`Book` class:

1. Usage of non-interface types in properties, namely `ArrayList` used when interface `List` should be used.

ex. `private ArrayList<String> authors` --> `private List<String> authors`

2. Usage of non-interface types in getter method return types, similar to the first bug.

ex. `public ArrayList<String> getAuthors()` --> `public List<String> getAuthors()`

3. Usage of non-interface types in setter method parameter types, similar to first bug.

ex. `public void setAuthors(ArrayList<String> authors)` --> `public void setAuthors(List<String> authors)`

4. `hasCopies()` method should check for `copiesAvailable > 0`, not `copiesAvailable >= 0`

5. References are used in setter/getter methods. For example, `setAuthors` should copy the input List before setting it, as using the reference directly would allow modification of the `authors` List without using the setter. Similarly, `getAuthors` returns a reference to the inner List, so that can be used to modify the inner values without using the setter.

6. `setAuthors` and `setSubjects` do not check for null inputs.

7. `setShelvingLocation` sets the property to a constant string, not the input.

8. `deleteCopy()` return values are swapped.

9. `checkoutCopy()` amountOfTimesCheckedOut is decremented instead of incremented.

10. `returnCopy()` only checks returnDates if the list is empty, not when it has elements.

`RouteController` class:

1. `getAvailableBooks` does not return the constructed `availableBooks` list.

2. `getAvailableBooks` uses the OK HTTP status code when an exception occurs, and should be using the INTERNAL_SERVER_ERROR HTTP status code.

3. `addCopy` has an unused `currBookId` that isn't necessary.

4. `addCopy` uses the I_AM_A_TEAPOT HTTP status code when it should be using the NOT_FOUND status code.

5. `addCopy` uses the BAD_REQUEST HTTP status code when it should be using the INTERNAL_SERVER_ERROR status code.

6. `getAvailableBooks` uses `ArrayList` as an assigned type, when it should use the `List` interface instead.

`MockApiService` class:

1. `updateBook` re-assigns `this.books` to itself, instead of `tmpBooks`

2. `updateBook` uses `ArrayList` as an assigned type, when it should use the `List` interface instead.

3. Usage of non-interface types in properties. 

4. Usage of non-interface types in method return values.

5. `MockApiService` constructor error message is not printed because it is commented out.

6. Unused `bags` property.
