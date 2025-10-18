package org.example.UDPFiles;

import java.util.List;

/**
 * Given an array arr[] and an integer k, we need to calculate the maximum sum of a subarray having size exactly k.
 * <ul>
 *    <li> Input  : arr[] = [5, 2, -1, 0, 3], k = 3 </li>
 *    <li> <b> Output </b> : 6 </li>
 *    <li> <b> Explanation  </b>: We get maximum sum by considering the subarray [5, 2 , -1] </li>
 *    <li> Input  : arr[] = [1, 4, 2, 10, 23, 3, 1, 0, 20], k = 4 </li>
 *    <li> <b> Output </b> : 39 </li>
 *    <li> <b> Explanation </b> : We get maximum sum by adding subarray [4, 2, 10, 23] of size 4. </li>
 * </ul>
 */
public class SlidingWindow {
    public static void main(String[] args) {
        int[] arr = { 5, 2, -1, 0, 3 };
        int k = 3;

        System.out.println("TEST 1 - K = " + k + " O = " + slidingWindow(arr, k));

        int[] arr2 = {1, 4, 2, 10, 23, 3, 1, 0, 20};
        int k2 = 4;

        System.out.println("TEST 1 - K = " + k + " O = " + slidingWindow(arr2, k2));
    }

    // Suma maxima en un subarreglo. O(n x k)
    public static int maxSum(int[] arr, int n, int k) {
        int max = 0;

        for( int i = 0; i < n - k; i++ ) {
            int sum = 0;
            for( int j = 0; j < k; j++ ) {
                sum += arr[j + i];
            }
            if( sum > max ) {
                max = sum;
            }
        }

        return max;
    }

    public static int slidingWindow(int[] arr, int k) {
        int max_sum = 0;
        // Computamos la suma de la primer ventana.
        for( int i = 0; i < k; i++ )
            max_sum += arr[i];

        int window_sum = max_sum;
        // Comenzamos a calcular las siguientes sumas.
        for( int i = k; i < arr.length; i++ ) {
            window_sum += arr[i] - arr[i - k];
            max_sum = Math.max(max_sum, window_sum);
        }
        return max_sum;
    }
}
