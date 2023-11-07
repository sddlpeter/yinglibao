package com.powernode;

import org.junit.Test;

public class TestQ3 {

    @Test
    public void function() {
        int[] arr = new int[] {3, -7, 3, -7, 3};
        int num = solution(arr);

        System.out.println(num);
    }

    private int solution(int[] arr) {
        if (arr.length == 1) return 1;
        int odd = arr[0], even = arr[1];
        int start = 0, max_len = 0;
        for (int i = 2; i < arr.length; ++i) {
            if (i % 2 == 0 && arr[i] != odd) {
                max_len = Math.max(max_len, i - start);
                start = i - 1;
                odd = arr[i];
                even = arr[i - 1];
            } else if (i % 2 == 1 && arr[i] != even) {
                max_len = Math.max(max_len, i - start);
                start = i - 1;
                odd = arr[i - 1];
                even = arr[i];
            }
        }
        return Math.max(max_len, arr.length - start);
    }
}
