public class BadCodeExample {
    public static void main(String[] args) {
        // Initialize an array with values
        int[] a = {1, 2, 3, 4, 5};

        // Calculate sum of the array elements
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }

        // Print result
        System.out.println("Sum is: " + sum);

        // Array to store results
        int[] results = new int[10];

        // Fill the array with values
        for (int i = 0; i < results.length; i++) {
            results[i] = i * 2; // Arbitrary values
        }

        // Print all results
        for (int i = 0; i < results.length; i++) {
            System.out.println("Result " + i + ": " + results[i]);
        }
    }
}
