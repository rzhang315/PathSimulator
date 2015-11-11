package util;

/**
 * Math functions not available in Java API
 * @author Kairi Kozuma
 * @version 1.0
 */
public class MathUtil {

    /**
     * Factorial function
     * @param  N number to find factorial
     * @return   long N!
     */
    public static long factorial(int N) {
        if (N == 0) {
            return 1;
        }
        long multi = 1;
        for (int i = 1; i <= N; i++) {
            multi = multi * i;
        }
        return multi;
    }
}
