public class LinearSearch {
    public static int linearSearch(int[] array, int elementToSearch) {
        for (int index = 0; index < array.length; index++) {
            if (array[index] == elementToSearch) {
                return index;
            }
        }
        return -1;
    }
}
