package io.github.ftcphantom16950.phantomlib.utils;


public class PhantomMath {
    public static final double FIELD_SIZE_INCHES = 144.0;
    public static final double HALF_FIELD = FIELD_SIZE_INCHES / 2.0; // 72.0

    public static final double EPSILON = 0.0f;
    public static double servoCRPowerToDegrees(double input,double max){
        double resultPower;
        if (max <= EPSILON) {
            throw new IllegalArgumentException("Максимальное должно быть больше 0");
        }
        if (input > max || input < 0) {
            throw new IllegalArgumentException(String.format("Угол %.2f вне диапазона [0, %.2f]", input, max));
        }
        resultPower = input / (max / 2) - 1;
        return resultPower;
    }
    public static double makeLinearToCubic(double input){
        return Math.pow(input, 3);
    }
    public static double makeLinearToCubic(double input, int a){
        return Math.pow(input, 3 * a);
    }
    public static double xPedroToFtcX(double x){
        return HALF_FIELD - x;
    }
    public static double yPedroToFtcY(double y){
        return y - HALF_FIELD;
    }
    public static double convertToRPM(double info, int encoderTicks){
        return (info * 60) / encoderTicks ;
    }
}
