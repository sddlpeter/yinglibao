package com.amazon.oa_1022;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestQ2 {
    @Test
    public void function() {

        List<Integer> center = new ArrayList<>(Arrays.asList(-1, 0, 1, 2, 3));
        int d = 22;

        int res = suitableLocations(center, d);
        System.out.println(res);
    }

    public static int suitableLocations(List<Integer> center, long d) {

        Collections.sort(center);
        int n = center.size();
        d = d / 2;
        long temp = (long) center.get(n / 2);
        if (!cal(center, temp, d)) {
            return 0;
        }

        long left = temp;
        long right = (long) center.get(n - 1) + d;
        while (left <= right) {
            long mid = left + (right - left) / 2;
            if (cal(center, mid, d)) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        long s = left - 1;
        left = center.get(0) - d;
        right = temp;
        while (left <= right) {
            long mid = left + (right - left) / 2;
            if (cal(center, mid, d)) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return (int) Math.max(s - right, 0);
    }


    public static boolean cal(List<Integer> center, long position, long limit) {
        long remainingLimit = limit;
        for (int c : center) {
            remainingLimit -= (long) Math.abs(position - c);
            if (remainingLimit < 0) {
                return false;
            }
        }
        return true;
    }
}
