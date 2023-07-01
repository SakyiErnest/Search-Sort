import java.util.Arrays;

public class BinarySearch {
    public static int binarySearch(int[] array, int elementToSearch) {
        // Check if the array is sorted
        if (!isSorted(array)) {
            // Sort the array if it's not sorted
            Arrays.sort(array);
        }

        // Perform binary search
        int firstIndex = 0;
        int lastIndex = array.length - 1;

        while (firstIndex <= lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;

            if (array[middleIndex] == elementToSearch) {
                return middleIndex;
            } else if (array[middleIndex] < elementToSearch) {
                firstIndex = middleIndex + 1;
            } else {
                lastIndex = middleIndex - 1;
            }
        }

        return -1; // Element not found
    }

    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
