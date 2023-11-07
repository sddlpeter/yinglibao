package com.powernode;

import org.junit.Test;

public class TestQ1 {

    @Test
    public void function() {
        int res = solution(99);
        System.out.println(res);
    }

    public int solution(int n) {
        int sum = helper(n);
        int target = sum * 2;
        for (int i = n + 1; i < Integer.MAX_VALUE; i++) {
            if (helper(i) == target) {
                return i;
            }
        }
        return -1;
    }

    public int helper(int n) {
        int ans = 0;
        while (n > 0) {
            ans += n % 10;
            n /= 10;
        }
        return ans;
    }
}
