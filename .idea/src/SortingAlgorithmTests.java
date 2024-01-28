import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SortingAlgorithmTests {

    public static void main(String[] args) {
        Random random = new Random();
        long totalTime = 0;
        for (int i = 0; i < 500; i++) {
            int[] arr = random.ints(10000, 0, 10000).toArray();
            //int[] arr = random.ints(21, 0, 50).toArray();

            //int[] arr = {5, 3, 4};

            //System.out.println("Before sorting: ");
            //System.out.println(Arrays.toString(arr));
            Instant start = Instant.now();
            iterativeMergeSort(arr);
            Instant end = Instant.now();
            long total = Duration.between(start, end).toMillis();
            //System.out.println("After sorting: ");
            //System.out.println(Arrays.toString(arr));
            totalTime += total;
            System.out.print(total + ", ");
        }
        System.out.println("On average, sort took " + totalTime/500 + " MilliSeconds");
    }

    /**
     * Merge sort, but without recursion, not in place...
     * literally same effectiveness, but less overhead due to not
     * using recursion.
     */
    private static void iterativeMergeSort(int[] arr) {
        //outer looop determines the size of the sub arrays which will be iterated over
        //increments by being multiplied 2-fold because each iteration the size of the
        //arrays is doubled. (1 -> 2 -> 4 -> 8 -> 16 -> 36 -> 64...)
        //This allows to work with huge sets of data in logn time complexity.
        for (int subArraySize = 1;
             subArraySize < arr.length;
             subArraySize *= 2) {
            //inner loop determines the start of the left side in the array,
            //iterates by 2x sub array size because 1x is the start of the next array
            //in this comparison
            for (int subArrayStart = 0;
                 subArrayStart < arr.length;
                 subArrayStart += 2 * subArraySize) {
                //finding start points for left array and right array
                int leftStart = subArrayStart;
                int rightStart = subArrayStart + subArraySize;

                //makes sure that right array never starts out of bounds
                //this also solves the problem of case where there is only enough
                //space for 1 more array. in that case left array is just empty
                if (rightStart >= arr.length) {
                    rightStart = arr.length - 1;
                }
                //sets the size for right array, to make sure it's never above limit
                int rightSize = subArraySize;
                if ((rightStart + rightSize) > arr.length) {
                    rightSize = subArraySize - (rightStart + subArraySize - arr.length);
                }

                //creates left and right arrays to compare values and merge
                int[] leftArray = Arrays.copyOfRange(arr, leftStart, rightStart);
                int[] rightArray = Arrays.copyOfRange(arr, rightStart, (rightStart + rightSize));

                //merge process
                int leftArrayCounter = 0;
                int rightArrayCounter = 0;
                int mainArrayCounter = subArrayStart;

                while (leftArrayCounter < leftArray.length
                && rightArrayCounter < rightArray.length) {
                    if (leftArray[leftArrayCounter] < rightArray[rightArrayCounter]) {
                        arr[mainArrayCounter] = leftArray[leftArrayCounter];
                        leftArrayCounter++;
                    } else {
                        arr[mainArrayCounter] = rightArray[rightArrayCounter];
                        rightArrayCounter++;
                    }
                    mainArrayCounter++;
                }
                //leftovers from left array
                while (leftArrayCounter < leftArray.length) {
                    arr[mainArrayCounter] = leftArray[leftArrayCounter];
                    leftArrayCounter++;
                    mainArrayCounter++;
                }
                //leftovers from right array
                while (rightArrayCounter < rightArray.length) {
                    arr[mainArrayCounter] = rightArray[rightArrayCounter];
                    rightArrayCounter++;
                    mainArrayCounter++;
                }

            }
        }

    }
    /**
     * Recursive Merge Sort, not in place...
     * How it works: recursively divides the original array into small and smaller ones
     * until each sub-array is of length (1). Then it merges them back by comparing the
     * first elements of subarrays and choosing the smaller one to put back. If one
     * subarray runs out of values it gets fully transferred to the main array.
     * Time Complexity: O(nlogn), since it breaks array into halves recursively,
     * each time it halves the size of the problem (logn), however we still need to compare
     * each element (n), hence n*logn.
     * Space allocation: O(N) we create new array for each element, so more space is used.
     *
     */
    private static void mergeSort(int[] arr) {
        int left = 0;
        int right = arr.length -1;
        int mid = arr.length/2;
        if (left < right) {

            //create 2 new arrays
            int[] leftArray = new int[mid];
            int[] rightArray = new int[arr.length - mid];

            //fill them with data
            for (int i = 0; i < arr.length; i++) {
                if (i < leftArray.length) {
                    leftArray[i] = arr[i];
                } else {
                    rightArray[i - mid] = arr[i];
                }
            }

            //recursively break them
            mergeSort(leftArray);
            mergeSort(rightArray);

            //reunite them
            int leftArrayCounter = 0;
            int rightArrayCounter = 0;
            int mainArrayCounter = 0;
            while (leftArrayCounter < leftArray.length
            && rightArrayCounter < rightArray.length) {
                if (leftArray[leftArrayCounter] < rightArray[rightArrayCounter]) {
                    arr[mainArrayCounter] = leftArray[leftArrayCounter];
                    leftArrayCounter++;
                } else {
                    arr[mainArrayCounter] = rightArray[rightArrayCounter];
                    rightArrayCounter++;
                }
                mainArrayCounter++;
            }
            //leftovers from left array
            while (leftArrayCounter < leftArray.length) {
                arr[mainArrayCounter] = leftArray[leftArrayCounter];
                leftArrayCounter++;
                mainArrayCounter++;
            }
            //leftovers from right array
            while (rightArrayCounter < rightArray.length) {
                arr[mainArrayCounter] = rightArray[rightArrayCounter];
                rightArrayCounter++;
                mainArrayCounter++;
            }
        }
    }

    /**
     * Insertion sort:
     * How it works: start at the second value (index 1). Go backwards to the start
     * of the array. Each time you see a larger value, move it one index right.
     * Once you encounter a smaller value, INSERT your original value at the adjacent index.
     * Repeat until you reach the end of the array.
     * Time complexity: O(N^2), since we are still iterating over the array while already
     * iterating over it, just going backwards.
     * Space allocation: O(1), since this is also an in-place sorting algorithm.
     */
    private static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tempLocation = i;
            int tempValue = arr[i];
            for (int j = i - 1; j >= 0; j--) {
                if (arr[j] > tempValue) {
                    arr[j + 1] = arr[j];
                    tempLocation = j;
                }
                arr[tempLocation] = tempValue;
            }

        }
    }

    /**
     * Selection sort:
     * How it works - SELECT left most value in the array. Iterate over the remaining array,
     * remember the smallest value and swap it with the selected value. SELECT the next value
     * and repeat the process until "left most" value is the end.
     * Time complexity - O(N^2) since we iterate over the array for each of it's indeces.
     * Space allocation - O(1) since this is an in-place sorting algorithm.
     */
    private static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int tempMinLocation = i;
            for (int j = i; j < arr.length; j++) {
                if (arr[tempMinLocation] > arr[j]) {
                    tempMinLocation = j;
                }
            }
            swap(arr, i, tempMinLocation);
        }
    }

    /**
     * Bubble sort:
     * How it works: compare two adjacent values in the array (0 and 1, 1 and 2, 2 and 3...)
     * until you reach the end.
     * When comparing, make sure the larger value ends up on the right.
     * When you reach the end, repeat the process, but this time reduce the "end" by 1.
     * Repeat until end = beginning.
     * Time complexity: O(N^2)
     * It iterates over the array (n-1)/2 times for each n, so n * (n-1)/2 is about n^2.
     * Space allocation: O(1)
     * No additional space needed, it is in-place algorithm
     */
    private static void bubbleSort(int[] arr) {
        int j = 1;
        while (j < arr.length) {
            for (int i = 0; i < arr.length - j; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                }
            }
            j++;
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}