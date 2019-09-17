# Equals
Provides a Java class that allows deep checks of equality for objects with nested classes.

## Data Fields
`equals: boolean`
      Is only false if there is a mismatch between the object.
 
`failures: Set<String>`
      Contains a list of all the field names of the data fields that fail in this object and all of its nested classes.

## Public Methods
`static <T> check(T a, T b): Equals`
      Inputs in two generic objects of the same type. Will return an equals object containing all details of their equality.

`combine(Equals eq): void`
      Combines the information within the inputted Equals object and the current one.
  
`isEquals(): boolean`
      Returns whether or not there was a mismatch.
      
`getFailures(): Set<String>`
      Returns the list of all fields that failed to match.
  
`toString(): String`
      Returns the string representation of this Equals object with the format "true/false -> [failures]".
