import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Equals {

    /**
     * Will be false only if one of the data fields do not match
     */
    private boolean equals;

    /**
     * String set of all of the names of data fields that failed to match. It is
     * a set so no values are repeated.
     */
    private Set<String> failures;

    /**
     * No-Arg constructor that initializes the data fields
     */
    private Equals() {
        equals = true;
        failures = new HashSet<>();
    }

    /**
     * Adds the failure to the failures list
     *
     * @param failure
     */
    private void add(String failure) {
        this.failures.add(failure);
    }

    /**
     * Combines another Equals object with this one
     *
     * @param eq The other Equals object
     */
    public void combine(Equals eq) {
        // AND the two equals booleans
        equals &= eq.equals;

        // Combine the set of failures for each
        for (String failure : eq.failures) {
            failures.add(failure);
        }
    }

    /**
     * Performs a deep check of each field and returns an Equals object
     * detailing what all failed to match in each field of two objects of the
     * same class.
     *
     * @param <T> The type of each object.
     * @param a The first object
     * @param b The second object
     * @return The equals object containing equality information
     */
    public static <T> Equals check(T a, T b) {
        // Create a list to add the equals object of each field to
        List<Equals> list = new ArrayList<>();

        // Avoid null pointer exceptions
        if (a == null) {
            // If both objects are null, then they are equal
            if (b == null) {
                return new Equals();
            } else { // They are not
                Equals e = new Equals();

                e.notEqual();

                return e;
            }
        }

        // Iterate through each data field in the object
        for (Field field : a.getClass().getDeclaredFields()) {
            // Get the class of the objects
            Class type = field.getType();

            // Create an equals object to store findings
            Equals e = new Equals();

            // Depending on class type, take different actions
            switch (type.getSimpleName()) {
                case "String":
                    try {
                        // Get the value for the data field
                        String first = (String) field.get(a),
                                second = (String) field.get(b);

                        // See if they are a mismatch 
                        if (first == null ? second != null : !first.equals(second)) {
                            // Add the field name to the list
                            e.add(field.getName());

                            // Make the Equals object false
                            e.notEqual();
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        System.out.println("Error: " + ex);
                    }
                    break;
                case "boolean":
                    try {
                        if (field.getBoolean(a) != field.getBoolean(b)) {
                            e.add(field.getName());
                            e.notEqual();
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        System.out.println("Error: " + ex);
                    }
                    break;
                case "int":
                    try {
                        int first = field.getInt(a), second = field.getInt(b);
                        if (first != second) {
                            e.add(field.getName());
                            e.notEqual();
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        System.out.println("Error: " + ex);
                    }
                    break;
                case "char":
                    try {
                        char first = field.getChar(a), second = field.getChar(b);
                        if (first != second) {
                            e.add(field.getName());
                            e.notEqual();
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        System.out.println("Error: " + ex);
                    }
                    break;
                case "double":
                    try {
                        double first = field.getDouble(a), second = field.getDouble(b);
                        if (first != second) {
                            e.add(field.getName());
                            e.notEqual();
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        System.out.println("Error: " + ex);
                    }
                    break;
                default:
                    try {
                        e = check(field.get(a), field.get(b));
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        System.out.println("Error: " + ex);
                    }
            }

            // Add this specific Equals object to the greater list
            list.add(e);
        }

        // Create the final equals object
        Equals eq = new Equals();

        // Combine all equals objects in the list together
        for (Equals x : list) {
            // They only matter when they came back false
            if (!x.isEquals()) {
                eq.notEqual();

                // Add each failure to the combined Equals object
                for (String failure : x.getFailures()) {
                    eq.add(failure);
                }
            }
        }

        return eq;
    }

    /**
     * @return Whether or not the objects match
     */
    public boolean isEquals() {
        return equals;
    }

    /**
     * @return The list of failure for the check
     */
    public Set<String> getFailures() {
        return failures;
    }

    /**
     * Sets the equality to false.
     */
    private void notEqual() {
        this.equals = false;
    }

    /**
     * @return The formatted string representation. [true/false] -> [list]
     */
    @Override
    public String toString() {
        return String.format("%s -> %s\n", equals, failures);
    }
}
