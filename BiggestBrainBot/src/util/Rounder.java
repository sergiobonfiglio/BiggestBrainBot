package util;

public class Rounder {

    public static double truncate(double x, int n) {
	x = (int) (x * Math.pow(10, n));
	x = x / Math.pow(10, n);
	return x;

    }
}
