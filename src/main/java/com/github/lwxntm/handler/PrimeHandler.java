package com.github.lwxntm.handler;

/**
 * @author lei
 */
public class PrimeHandler {
    private static int nthPrime(int n) {
        int i = 3, j = 1;
        while (true) {
            j = j + 2;
            if (j * j > i) {
                n = n - 1;
                if (n == 1) {
                    break;
                }
                i = i + 2;
                j = 1;
            } else if (i % j == 0) {
                i = i + 2;
                j = 1;
            }
        }
        return i;
    }

    public static String primeI(int n) {
        long s = System.currentTimeMillis();
        int result = nthPrime(n);
        long time = System.currentTimeMillis() - s;
        return "第" + n + "个素数的值是:" + result + " 耗时" + time + " ms";
    }
}
