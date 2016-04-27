package team5818.robot.util;

/**
 * This is a Kalman filter designed for estimating gyro angle measurements. It
 * was originally written by Kristian Lauszus, TKJ Electronics, then translated
 * to Java by Yoseph Alabdulwahab. This code is protected under the General
 * public license.
 * 
 * Copyright (C) 2012 Kristian Lauszus, TKJ Electronics. All rights reserved.
 * 
 * This software may be distributed and modified under the terms of the GNU
 * General Public License version 2 (GPL2) as published by the Free Software
 * Foundation and appearing in the file GPL2.TXT included in the packaging of
 * this file. Please note that GPL2 Section 2[b] requires that all works based
 * on this software must also be made publicly available under the terms of the
 * GPL2 ("Copyleft").
 */
public class Kalman {

    // Process noise variance for the accelerometer.
    private double Q_angle = 0.001;
    // Process noise variance for the gyro bias.
    private double Q_bias = 0.003;
    // Measurement noise variance
    private double R_measure = 0.03;

    // The angle calculated by the Kalman Filter - part of the 2x1 state vector.
    private double angle;
    // The gyro bias calculated by the filter - part of the 2x1 state vector.
    private double bias;
    // Unbias rate calculated from the rate and the calculated bias - you may
    // call getAngle to update the rate.
    private double rate;
    // The angle difference.
    private double y;
    // The estimated error.
    private double s;

    // Error estimate matrix - this is a 2x2 matrix.
    private double[][] P;
    // Kalman gain - this is a 2x1 vector.
    private double[] K;

    public Kalman() {
        angle = 0;
        bias = 0;
        rate = 0;
        y = 0;
        s = 0;

        P = new double[2][2];
        K = new double[2];
    }

    /**
     * This is the core of the kalman filter. It should be called as fast as
     * possible for better accuracy. It will compute the next best angle guess
     * based on the most recent input. It will also update the matrices and
     * vectors to continue calculation.
     * 
     * @param newAngle
     *            the new input from the gyro angle in degrees.
     * @param newRate
     *            the rate of the angle change in degrees.
     * @param dt
     *            the change in time in seconds.
     * @return the new best estimate angle.
     */
    public double getAngle(double newAngle, double newRate, double dt) {
        // Update X-Hat - Project the state ahead.
        rate = newRate - bias;
        angle += dt * rate;

        // Update estimated error covariance. Project the error covariance
        // ahead.
        P[0][0] += dt * (dt * P[1][1] - P[0][1] - P[1][0] + Q_angle);
        P[0][1] -= dt * P[1][1];
        P[1][0] -= dt * P[1][1];
        P[1][1] += Q_bias * dt;

        // Calculate the Kalman gain.
        s = P[0][0] + R_measure;
        K[0] = P[0][0] / s;
        K[1] = P[1][0] / s;

        // Calculate the angle bias.
        y = newAngle - angle;

        // update estimates with new angle estimates.
        angle += K[0] * y;
        bias += K[1] * y;

        // Calculate estimation error covariance.
        P[0][0] -= K[0] * P[0][0];
        P[0][1] -= K[0] * P[0][1];
        P[1][0] -= K[1] * P[0][0];
        P[1][1] -= K[1] * P[0][1];

        return angle;
    }

    public void setAngle(double newAngle) {
        angle = newAngle;
    }

    /**
     * @return the unbias rate in units of degrees per second.
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param newQ_angle
     *            updates the Q_angle Kalman constant.
     */
    public void setQangle(double newQ_angle) {
        Q_angle = newQ_angle;
    }

    /**
     * 
     * @param newQ_bias
     *            updates the Q_bias Kalman constant
     */
    public void setQbias(double newQ_bias) {
        Q_bias = newQ_bias;
    }

    /**
     * 
     * @param newR_measurme
     *            updates the R_measure Kalman constant.
     */
    public void setRmeasurment(double newR_measurme) {
        R_measure = newR_measurme;
    }

    /**
     * @return the Q_angle Kalman constant.
     */
    public double getQangle() {
        return Q_angle;
    }

    /**
     * @return the Q_bias kalman constant.
     */
    public double getQbias() {
        return Q_bias;
    }

    /**
     * @return the R_measure Kalman constant.
     */
    public double getRmeasure() {
        return R_measure;
    }
}
