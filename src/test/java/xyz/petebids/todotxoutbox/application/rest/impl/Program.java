package xyz.petebids.todotxoutbox.application.rest.impl;

class Program {
    public static int binarySearch(int[] array, int target) {
        int left = 0, right = array.length - 1;
        int mid;
        while (left <= right) {
            mid = (left + right) / 2;
            if (array[mid] == target) {
                return mid;
            }
            if (target < array[mid]) {
                right = mid - 1;
            }
            if (target > array[mid]) {
                left = mid + 1;
            }

        }
        return -1;
    }

}