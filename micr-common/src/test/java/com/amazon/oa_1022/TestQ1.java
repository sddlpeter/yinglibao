package com.amazon.oa_1022;

import org.junit.Test;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestQ1 {
    @Test
    public void function() {

        List<Integer> center = new ArrayList<>(Arrays.asList(-2, 1, 0));
        int d = 8;

        int res = suitableLocations(center, d);
        System.out.println(res);
    }

    public int suitableLocations(List<Integer> center, long d) {
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int c : center) {
            min = Math.min(min, c);
            max = Math.max(max, c);
        }

        int distance = (int) d / 2;

        min =  min - distance;
        max =  max + distance;

        int res = 0;

        System.out.println(min);
        System.out.println(max);


        for (int i = min; i < max; i++) {
            int dis = computeDistance(center, i);
            if (dis <= distance) {
                res++;
            }
        }

        return res;
    }

    public int computeDistance(List<Integer> center, int location) {
        int sum = 0;
        for (int c : center) {
            sum += Math.abs(c - location);
        }

        return sum;
    }
}
