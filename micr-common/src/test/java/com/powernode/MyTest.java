package com.powernode;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class MyTest {

    @Test
    public void test01() {

        Date start = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DATE);
        System.out.println(start);


        Date end = DateUtils.truncate(new Date(), Calendar.DATE);
        System.out.println(end);
    }
}
