package com.powernode;

import org.junit.Test;

public class TestQ2 {
    @Test
    public void function() {
        int[] a = new int[] {1000000000, 1, 2, 2, 1000000000, 1, 1000000000};
        int[] b = new int[] {1, 2, 3, 3, 2, 1, 5};

        int ans = solution(b);
        System.out.println(ans);
    }

    public int solution(int[] arr) {
        long sum = 0;
        int i = 0;
        while (i < arr.length - 1 && arr[i + 1] > arr[i]) {
            i++;
        }

        sum += arr[i];

        for (int j = i + 1; j < arr.length; j++) {
            if (arr[j] > arr[j - 1]) {
                sum += arr[j] - arr[j - 1];
            }
        }

        if (sum > 1000000000) {
            sum %= 1000000000;
        }

        return (int) sum;
    }


}
