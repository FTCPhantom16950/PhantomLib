package org.firstinspires.ftc.teamcode.own.camera;

import static org.opencv.core.CvType.CV_64F;

import android.annotation.SuppressLint;
import android.graphics.Canvas;


import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для обнаружеия элементов сезона 2025-2026, артифактов
 * Наследуется от интерфейса {@link VisionProcessor}
 */
public class ArtifactProcessor implements VisionProcessor {
    /// Внутренний конструктор класса, чтобы случайно не создать неиспользуемый класс
    private ArtifactProcessor() {
    }

    /**
     * Метод для создания нового экземпляра класса @link ArtifactProcessor
     *
     * @return новый экземпляр {@link ArtifactProcessor}
     */
    public static Builder newBuilder() {
        new ArtifactProcessor();
        return new Builder();
    }

    /**
     * Класс для создания нового экземпляра класса {@link ArtifactProcessor}
     */
    public static class Builder {
        /// Экземпляр класса {@link ArtifactProcessor}
        private final ArtifactProcessor processor;

        /// Внутренний конструктор класса, чтобы случайно не создать неиспользуемый класс
        private Builder() {
            processor = new ArtifactProcessor();
        }

        /**
         * Установка размера матрицы в пикселях
         *
         * @param pixelCameraHeight размер матрицы в пикселях
         * @return сборщик
         */
        public Builder setPixelCameraHeight(int pixelCameraHeight) {
            processor.pixelCameraHeight = pixelCameraHeight;
            return this;
        }

        /**
         * Установка параметров камеры
         *
         * @param x смещение от центра робота в мм по координате x
         * @param y смещение от центра робота в мм по координате y
         * @param z смещение от центра робота в мм по координате z
         * @return сборщик
         */
        public Builder setCameraPos(float x, float y, float z) {
            processor.cameraPos[0] = x;
            processor.cameraPos[1] = y;
            processor.cameraPos[2] = z;
            return this;
        }

        /**
         * Установка вращения камеры
         *
         * @param yaw   угол поворота вокруг камерв вокруг оси z
         * @param pitch угол поворота вокруг камерв вокруг оси x
         * @param roll  угол поворота вокруг камерв вокруг оси y
         * @return
         */
        public Builder setCameraRot(float pitch, float roll, float yaw) {
            processor.cameraRot[0] = pitch;
            processor.cameraRot[1] = roll;
            processor.cameraRot[2] = yaw;
            return this;
        }

        /**
         * Установка параметров матрицы камеры
         *
         * @param x размер камеры в мм по x
         * @param y размер камеры в мм по y
         * @return сборщик
         */
        public Builder setCameraMatrix(float x, float y) {
            processor.c = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            return this;
        }

        public Builder setRazmer(float razmer) {
            processor.razmer = razmer;
            return this;
        }

        public Builder setF(float f) {
            processor.f = f;
            return this;
        }

        public Builder setDistProperties(boolean usingDist, float minDist, float maxDist) {
            processor.usingDist = usingDist;
            processor.minDist = minDist;
            processor.maxDist = maxDist;
            return this;
        }

        public Builder setOtnProperties(boolean usingOtn, float minOtn, float maxOtn) {
            processor.usingOtn = usingOtn;
            processor.minOtn = minOtn;
            processor.maxOtn = maxOtn;
            return this;
        }

        public Builder setSquareProperties(boolean usingSquare, float minSquare, float maxSquare) {
            processor.usingSquare = usingSquare;
            processor.minSquareOnScreen = minSquare;
            processor.maxSquareOnScreen = maxSquare;
            return this;
        }

        public Builder addTelemetry(boolean usingTelemetry, Telemetry telemetry) {
            processor.usingTelemetry = usingTelemetry;
            processor.telemetry = telemetry;
            return this;
        }
        public Builder setDilateElement(Mat dilateElement) {
            processor.dilateElement = dilateElement;
            return this;
        }
        public Builder setDilateElement(int dilateHeight, int dilateWidth) {
            processor.dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(dilateWidth, dilateHeight));
            return this;
        }
        public Builder setErodeElement(Mat erodeElement) {
            processor.erodeElement = erodeElement;
            return this;
        }
        public Builder setErodeElement(int erodeHeight, int erodeWidth) {
            processor.erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erodeWidth, erodeHeight));
            return this;
        }
        public Builder setBlurSize(Size blurSize) {
            processor.blurSize = blurSize;
            return this;
        }
        public Builder setBlurSize(int blurHeight, int blurWidth) {
            processor.blurSize = new Size(blurWidth, blurHeight);
            return this;
        }
        public Builder setMinValues(Scalar minValues) {
            processor.minValues = minValues;
            return this;
        }
        public Builder setMaxValues(Scalar maxValues) {
            processor.maxValues = maxValues;
            return this;
        }
        public Builder setMinValues(int r, int g, int b) {
            processor.minValues = new Scalar(r,g,b);
            return this;
        }
        public Builder setMaxValues(int r, int g, int b) {
            processor.maxValues = new Scalar(r,g,b);
            return this;
        }
        public ArtifactProcessor createWithDefaults() {
            processor.usingOtn = true;
            processor.usingDist = true;
            processor.usingSquare = true;
            processor.usingTelemetry = false;
            processor.cameraPos[0] = 0;
            processor.cameraPos[1] = 0;
            processor.cameraPos[2] = 0;
            processor.cameraRot[0] = 0;
            processor.cameraRot[1] = 0;
            processor.cameraRot[2] = 0;
            processor.c = (float) Math.sqrt(Math.pow(3.58F, 2) + Math.pow(2.02F, 2));
            processor.razmer = 49f;
            processor.f = 4f;
            processor.pixelCameraHeight = 960;
            processor.dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(50, 50));
            processor.erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30, 30));
            processor.minValues = new Scalar(7, 60, 60);
            processor.maxValues = new Scalar(15, 255, 255);
            processor.blurSize = new Size(3, 3);
            return processor;
        }

        public ArtifactProcessor build() {
            if (Float.isNaN(processor.razmer)){
                throw new IllegalArgumentException("Object height is not set");
            }
            if (Float.isNaN(processor.c)){
                throw new IllegalArgumentException("Camera diagonal is not set");
            }
            if (Float.isNaN(processor.f)){
                throw new IllegalArgumentException("Focal length is not set");
            }
            if (Float.isNaN(processor.pixelCameraHeight)) {
                throw new IllegalArgumentException("Pixel camera height is not set");
            }
            if (processor.dilateElement == null) {
                processor.dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(50, 50));
            }
            if (processor.erodeElement == null) {
                processor.erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30, 30));
            }
            if (processor.minValues == null) {
                processor.minValues = new Scalar(7, 60, 60);
            }
            if (processor.maxValues == null) {
                processor.maxValues = new Scalar(15, 255, 255);
            }
            if (processor.blurSize == null) {
                processor.blurSize = new Size(3, 3);
            }
            if (Float.isNaN(processor.cameraPos[0])){
                processor.cameraPos[0] = 0;
                processor.cameraPos[1] = 0;
                processor.cameraPos[2] = 0;
            }
            if (Float.isNaN(processor.cameraRot[0])){
                processor.cameraRot[0] = 0;
                processor.cameraRot[1] = 0;
                processor.cameraRot[2] = 0;
            }
            return processor;
        }
    }

    private float maxDist,
            minDist,
            minOtn,
            maxOtn,
            razmer,
            f,
            pixelCameraHeight,
            c;
    private float[] cameraPos = new float[]{0, 0, 0},
            cameraRot = new float[]{0, 0, 0};
    private float h,
            centerOfSquare,
            x,
            y,
            z,
            rectSizeOnCamera,
            convers,
            a,
            b,
            otn;
    private boolean usingOtn,
            usingSquare,
            usingDist,
            usingTelemetry;
    private float squareOnScreen,
            minSquareOnScreen,
            maxSquareOnScreen;
    Mat K = new Mat(3, 3, CV_64F);
    List<MatOfPoint> contours = new ArrayList<>();
    Mat openingImage = new Mat();
    Mat closingOutput = new Mat();
    Mat hierarchy = new Mat();
    Mat blurredImage = new Mat();
    Mat hsvImage = new Mat();
    Mat mask = new Mat();
    Mat morphOutput = new Mat();
    Mat dilateElement;
    Mat erodeElement;
    Scalar minValues;
    Scalar maxValues;
    MatOfPoint2f[] contoursPoly;
    Telemetry telemetry;
    Rect[] rects;
    Size blurSize;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        convers = (c / pixelCameraHeight);
        K.put(0, 0, calibration.focalLengthX);
        K.put(0, 1, 0);
        K.put(0, 2, calibration.principalPointX);
        K.put(1, 0, 0);
        K.put(1, 1, calibration.focalLengthY);
        K.put(1, 2, calibration.principalPointY);
        K.put(2, 0, 0);
        K.put(2, 1, 0);
        K.put(2, 2, 1);
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.blur(frame, blurredImage, blurSize);
        Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hsvImage, minValues, maxValues, mask);

        Imgproc.erode(mask, openingImage, erodeElement);
        Imgproc.dilate(openingImage, morphOutput, dilateElement);

        Imgproc.dilate(mask, closingOutput, dilateElement);
        Imgproc.erode(closingOutput, morphOutput, erodeElement);

        Imgproc.findContours(morphOutput, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        contoursPoly = new MatOfPoint2f[contours.size()];
        rects = new Rect[contours.size()];
        for (int idx = 0; idx < contours.size(); idx++) {
            if (contours.get(idx).toArray() != null) {
                contoursPoly[idx] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(idx).toArray()), contoursPoly[idx], 3, true);
                rects[idx] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[idx].toArray()));
                a = (float) (-rects[idx].tl().x + rects[idx].br().x);
                b = (float) (-rects[idx].tl().y + rects[idx].br().y);
                otn = a / b;
                h = (a * convers);
                rectSizeOnCamera = ((f * razmer) / h);
                squareOnScreen = (a * a);
                boolean screenProperty = !usingSquare || (squareOnScreen >= minSquareOnScreen && squareOnScreen <= maxSquareOnScreen),
                        distProperty = !usingDist || (rectSizeOnCamera >= minDist && rectSizeOnCamera <= maxDist),
                        otnProperty = !usingOtn || (otn >= minOtn && otn <= maxOtn);
                if (screenProperty && otnProperty && distProperty) {
                    centerOfSquare = (a / 2);
                    Imgproc.rectangle(frame, rects[idx].tl(), rects[idx].br(), new Scalar(255, 0, 0), 2);
                    Point centerPoint = new Point(rects[idx].tl().x + centerOfSquare, rects[idx].tl().y + centerOfSquare);
                    float[] angles = calculateAngle((float) centerPoint.x, (float) centerPoint.y);
                    float D = rectSizeOnCamera;
                    float phi = angles[1];
                    float theta = angles[0];
                    float xc = (float) (D * Math.cos(theta) * Math.cos(phi));
                    float yc = (float) (D * Math.cos(theta) * Math.sin(phi));
                    float zc = (float) (D * Math.sin(theta));
                    float[] Pc_coords = {xc, yc, zc};
                    Pc_coords = convertCameraToRobot(cameraPos, cameraRot, Pc_coords);
                    x = Pc_coords[0];
                    y = Pc_coords[1];
                    z = Pc_coords[2];
                    @SuppressLint("DefaultLocale")
                    String text = String.format("dist: %.1f, x: %.1f, y: %.1f, z: %.1f", D, x, y, z);
                    Imgproc.putText(frame, text, rects[idx].tl(), 1, 1, new Scalar(255, 255, 0));
                    Imgproc.drawMarker(frame, new Point(rects[idx].tl().x + centerOfSquare, rects[idx].tl().y + centerOfSquare), new Scalar(255, 0, 0));
                }
            }
            contoursPoly[idx].release();
        }
//        morphOutput = frame;
        Imgproc.drawMarker(frame, new Point(K.get(0, 2)[0], K.get(1, 2)[0]), new Scalar(255, 0, 255));
        if (usingTelemetry && telemetry != null) {
            telemetry.update();
        }
        blurredImage.release();
        hsvImage.release();
        mask.release();
        openingImage.release();
        closingOutput.release();
        contours.clear();
        hierarchy.release();
        contours.clear();
        return new float[]{x, y, z};
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
    }

    private float[] convertCameraToRobot(float[] cameraCord, float[] angeles, float[] objectPos) {
        Mat T = new Mat(3, 1, CV_64F);
        T.put(0, 0, cameraCord[0]);
        T.put(1, 0, cameraCord[1]);
        T.put(2, 0, cameraCord[2]);
        Mat Pc = new Mat(3, 1, CV_64F);
        Pc.put(0, 0, objectPos[0]);
        Pc.put(1, 0, objectPos[1]);
        Pc.put(2, 0, objectPos[2]);
        angeles[0] = (float) Math.toRadians(angeles[0]);
        angeles[1] = (float) Math.toRadians(angeles[1]);
        angeles[2] = (float) Math.toRadians(angeles[2]);
        Mat Rx = Mat.eye(3, 3, CV_64F);
        Rx.put(1, 1, Math.cos(angeles[0]));
        Rx.put(1, 2, -Math.sin(angeles[0]));
        Rx.put(2, 2, Math.cos(angeles[0]));
        Mat Ry = Mat.eye(3, 3, CV_64F);
        Ry.put(1, 1, Math.cos(angeles[1]));
        Ry.put(1, 2, Math.sin(angeles[1]));
        Ry.put(2, 2, Math.cos(angeles[1]));
        Mat Rz = Mat.eye(3, 3, CV_64F);
        Rz.put(1, 1, Math.cos(angeles[2]));
        Rz.put(1, 2, -Math.sin(angeles[2]));
        Rz.put(2, 2, Math.cos(angeles[2]));

        Mat R_temp = new Mat();
        Core.gemm(Rz, Ry, 1.0, new Mat(), 0.0, R_temp);
        Mat R_c_to_r = new Mat();
        Core.gemm(R_temp, Rx, 1.0, new Mat(), 0.0, R_c_to_r);

        Mat RPc = new Mat();
        Core.gemm(R_c_to_r, Pc, 1.0, new Mat(), 0.0, RPc);

        Mat Pr = new Mat();
        Core.add(RPc, T, Pr);

        float x_r = (float) Pr.get(0, 0)[0];
        float y_r = (float) Pr.get(1, 0)[0];
        float z_r = (float) Pr.get(2, 0)[0];
        T.release();
        Pc.release();
        Rx.release();
        Ry.release();
        Rz.release();
        R_temp.release();
        R_c_to_r.release();
        RPc.release();
        Pr.release();
        return new float[]{x_r, y_r, z_r};
    }

    private float[] calculateAngle(float x, float y) {
        double fx = K.get(0, 0)[0];
        double fy = K.get(1, 1)[0];
        double cx = K.get(0, 2)[0];
        double cy = K.get(1, 2)[0];
        double x_norm = (x - cx) / fx;
        double y_norm = (y - cy) / fy;
        double phi_rad = Math.atan(x_norm);
        double theta_rad = Math.atan(y_norm / Math.sqrt(1.0 + x_norm * x_norm));
        return new float[]{(float) theta_rad, (float) phi_rad};
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

}
