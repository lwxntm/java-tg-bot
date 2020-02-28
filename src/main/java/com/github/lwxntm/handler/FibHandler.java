package com.github.lwxntm.handler;

/**
 * @author lei
 */
public class FibHandler {
    private static long fib(int num){
       int n=0;
        long a=1;
        long b=0;
        while (n<num){
            long tmp=a;
            a=a+b;
            b=tmp;
            n++;
        }
        return a;
    }
    public static String fibI(int n){
        long st = System.currentTimeMillis();
        long res = fib(n);
        long costTime = System.currentTimeMillis()-st;
        return "第"+n+"个数是:"+res+", 耗时:"+costTime+" ms";
    }
}
