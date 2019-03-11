package utils;

import java.util.Comparator;


public class ArrySortUtil implements Comparator<String> {
    private final static int UP = 1;

    private final static int DOWM = -1;

    private int state;

    public ArrySortUtil(int state) {
            this.state = state;
    }

    public ArrySortUtil() {

    }

    public int compare(String o1, String o2) {
            if (state == ArrySortUtil.DOWM) {
                    return sortDown(o1, o2);
            }
            return sortUp(o1, o2);
    }

    private int sortUp(String o1, String o2) {
            if (Integer.parseInt(o1) < Integer.parseInt(o2)) {
                    return -1;
            } else if (Integer.parseInt(o1) > Integer.parseInt(o2)) {
                    return 1;
            } else {
                    return 0;
            }
    }

    private int sortDown(String o1, String o2) {
            if (Integer.parseInt(o1) > Integer.parseInt(o2)) {
                    return -1;
            } else if (Integer.parseInt(o1) < Integer.parseInt(o2)) {
                    return 1;
            } else {
                    return 0;
            }
    }
}
