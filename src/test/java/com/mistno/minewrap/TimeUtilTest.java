package com.mistno.minewrap;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Seconds;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class TimeUtilTest {

    private static final int LONG_INTERVAL = 24;
    private static final int MEDIUM_INTERVAL = 6;
    private static final int SHORT_INTERVAL = 1;

    private static final int[][] LONG_INTERVALS_EXPECTED_VALUES = new int[][]{
            {0, (int) new DateTime(0).plusHours(23).plusMinutes(55).getMillis() / 1000},
            {1, (int) new DateTime(0).plusHours(22).plusMinutes(55).getMillis() / 1000},
            {2, (int) new DateTime(0).plusHours(21).plusMinutes(55).getMillis() / 1000},
            {3, (int) new DateTime(0).plusHours(20).plusMinutes(55).getMillis() / 1000},
            {4, (int) new DateTime(0).plusHours(19).plusMinutes(55).getMillis() / 1000},
            {5, (int) new DateTime(0).plusHours(18).plusMinutes(55).getMillis() / 1000},
            {6, (int) new DateTime(0).plusHours(17).plusMinutes(55).getMillis() / 1000},
            {7, (int) new DateTime(0).plusHours(16).plusMinutes(55).getMillis() / 1000},
            {8, (int) new DateTime(0).plusHours(15).plusMinutes(55).getMillis() / 1000},
            {9, (int) new DateTime(0).plusHours(14).plusMinutes(55).getMillis() / 1000},
            {10, (int) new DateTime(0).plusHours(13).plusMinutes(55).getMillis() / 1000},
            {11, (int) new DateTime(0).plusHours(12).plusMinutes(55).getMillis() / 1000},
            {12, (int) new DateTime(0).plusHours(11).plusMinutes(55).getMillis() / 1000},
            {13, (int) new DateTime(0).plusHours(10).plusMinutes(55).getMillis() / 1000},
            {14, (int) new DateTime(0).plusHours(9).plusMinutes(55).getMillis() / 1000},
            {15, (int) new DateTime(0).plusHours(8).plusMinutes(55).getMillis() / 1000},
            {16, (int) new DateTime(0).plusHours(7).plusMinutes(55).getMillis() / 1000},
            {17, (int) new DateTime(0).plusHours(6).plusMinutes(55).getMillis() / 1000},
            {18, (int) new DateTime(0).plusHours(5).plusMinutes(55).getMillis() / 1000},
            {19, (int) new DateTime(0).plusHours(4).plusMinutes(55).getMillis() / 1000},
            {20, (int) new DateTime(0).plusHours(3).plusMinutes(55).getMillis() / 1000},
            {21, (int) new DateTime(0).plusHours(2).plusMinutes(55).getMillis() / 1000},
            {22, (int) new DateTime(0).plusHours(1).plusMinutes(55).getMillis() / 1000},
            {23, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000}
    };

    @Test
    public void longIntervals() {
        for (int index = 0; index < LONG_INTERVALS_EXPECTED_VALUES.length; index++) {
            DateTimeUtils.setCurrentMillisFixed(new DateTime(2015, 8, 1, LONG_INTERVALS_EXPECTED_VALUES[index][0], 0).getMillis());
            Seconds result = TimeUtil.getSecondsUntilNextInterval(LONG_INTERVAL);
            assertThat(result.getSeconds(), Matchers.is(LONG_INTERVALS_EXPECTED_VALUES[index][1]));
        }
    }

    private static final int[][] MEDIUM_INTERVALS_EXPECTED_VALUES = new int[][]{
            {0, (int) new DateTime(0).plusHours(5).plusMinutes(55).getMillis() / 1000},
            {1, (int) new DateTime(0).plusHours(4).plusMinutes(55).getMillis() / 1000},
            {2, (int) new DateTime(0).plusHours(3).plusMinutes(55).getMillis() / 1000},
            {3, (int) new DateTime(0).plusHours(2).plusMinutes(55).getMillis() / 1000},
            {4, (int) new DateTime(0).plusHours(1).plusMinutes(55).getMillis() / 1000},
            {5, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {6, (int) new DateTime(0).plusHours(5).plusMinutes(55).getMillis() / 1000},
            {7, (int) new DateTime(0).plusHours(4).plusMinutes(55).getMillis() / 1000},
            {8, (int) new DateTime(0).plusHours(3).plusMinutes(55).getMillis() / 1000},
            {9, (int) new DateTime(0).plusHours(2).plusMinutes(55).getMillis() / 1000},
            {10, (int) new DateTime(0).plusHours(1).plusMinutes(55).getMillis() / 1000},
            {11, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {12, (int) new DateTime(0).plusHours(5).plusMinutes(55).getMillis() / 1000},
            {13, (int) new DateTime(0).plusHours(4).plusMinutes(55).getMillis() / 1000},
            {14, (int) new DateTime(0).plusHours(3).plusMinutes(55).getMillis() / 1000},
            {15, (int) new DateTime(0).plusHours(2).plusMinutes(55).getMillis() / 1000},
            {16, (int) new DateTime(0).plusHours(1).plusMinutes(55).getMillis() / 1000},
            {17, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {18, (int) new DateTime(0).plusHours(5).plusMinutes(55).getMillis() / 1000},
            {19, (int) new DateTime(0).plusHours(4).plusMinutes(55).getMillis() / 1000},
            {20, (int) new DateTime(0).plusHours(3).plusMinutes(55).getMillis() / 1000},
            {21, (int) new DateTime(0).plusHours(2).plusMinutes(55).getMillis() / 1000},
            {22, (int) new DateTime(0).plusHours(1).plusMinutes(55).getMillis() / 1000},
            {23, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000}
    };

    @Test
    public void mediumIntervals() {
        for (int index = 0; index < MEDIUM_INTERVALS_EXPECTED_VALUES.length; index++) {
            DateTimeUtils.setCurrentMillisFixed(new DateTime(2015, 8, 1, MEDIUM_INTERVALS_EXPECTED_VALUES[index][0], 0).getMillis());
            Seconds result = TimeUtil.getSecondsUntilNextInterval(MEDIUM_INTERVAL);
            assertThat(result.getSeconds(), Matchers.is(MEDIUM_INTERVALS_EXPECTED_VALUES[index][1]));
        }
    }

    private static final int[][] SHORT_INTERVALS_EXPECTED_VALUES = new int[][]{
            {0, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {1, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {2, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {3, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {4, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {5, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {6, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {7, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {8, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {9, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {10, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {11, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {12, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {13, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {14, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {15, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {16, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {17, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {18, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {19, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {20, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {21, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {22, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000},
            {23, (int) new DateTime(0).plusHours(0).plusMinutes(55).getMillis() / 1000}
    };

    @Test
    public void shortIntervals() {
        for (int index = 0; index < SHORT_INTERVALS_EXPECTED_VALUES.length; index++) {
            DateTimeUtils.setCurrentMillisFixed(new DateTime(2015, 8, 1, SHORT_INTERVALS_EXPECTED_VALUES[index][0], 0).getMillis());
            Seconds result = TimeUtil.getSecondsUntilNextInterval(SHORT_INTERVAL);
            assertThat(result.getSeconds(), Matchers.is(SHORT_INTERVALS_EXPECTED_VALUES[index][1]));
        }
    }
}