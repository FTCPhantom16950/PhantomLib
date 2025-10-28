package io.github.ftcphantom16950.phantomlib.camera;

import static org.opencv.core.CvType.CV_64F;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

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
 * Класс для обнаружения элементов сезона 2025-2026, артефактов
 * Наследуется от интерфейса {@link VisionProcessor}
 */
public class ArtifactProcessor implements VisionProcessor {

    /**
     * Класс для хранения координат обнаруженного артефакта.
     */
    public static class Artifact {
        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public final float x, y, z;

        public Artifact(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Рассчитывает расстояние до другого артефакта.
         * @param other Другой артефакт.
         * @return Расстояние между объектами.
         */
        public double distanceTo(Artifact other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2) + Math.pow(this.z - other.z, 2));
        }
    }

    private final List<Artifact> detectedArtifacts = new ArrayList<>();

    public List<Artifact> getDetectedArtifacts() {
        return detectedArtifacts;
    }


    /// Внутренний конструктор класса, чтобы случайно не создать неиспользуемый класс
    private ArtifactProcessor() {
    }

    /**
     * Метод для создания нового экземпляра класса @link ArtifactProcessor
     *
     * @return новый экземпляр {@link ArtifactProcessor}
     */
    public static Builder newBuilder() {
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
        // установка параметров определения

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
         * @param yaw   угол поворота вокруг камеры вокруг оси z
         * @param pitch угол поворота вокруг камеры вокруг оси x
         * @param roll  угол поворота вокруг камеры вокруг оси y
         * @return сборщик
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

        /**
         * Установка реального размера объекта
         *
         * @param size реальный размер
         * @return сборщик
         */
        public Builder setSize(float size) {
            processor.size = size;
            return this;
        }

        /**
         * Установка фокусного расстояния в мм
         *
         * @param f фокусное расстояние (мм)
         * @return сборщик
         */
        public Builder setF(float f) {
            processor.f = f;
            return this;
        }
        // Устновка параметров отсеивания

        /**
         * Установка параметров для отсеивания лишних объектов по дистанции
         *
         * @param usingDist используется ли проверка на дистанцию
         * @param minDist   минимальная дистанция до объекта(мм)
         * @param maxDist   максимальная дистанция до объекта(мм)
         * @return сборщик
         */
        public Builder setDistProperties(boolean usingDist, float minDist, float maxDist) {
            processor.usingDist = usingDist;
            processor.minDist = minDist;
            processor.maxDist = maxDist;
            return this;
        }

        /**
         * Установка параметров для отсеивания лишних объектов по отношению сторон (длинны к высоте)
         *
         * @param usingOtn используется ли проверка на отношение сторон
         * @param minOtn   минимальное соотношение сторон
         * @param maxOtn   максимальное соотношение сторон
         * @return сборщик
         */
        public Builder setOtnProperties(boolean usingOtn, float minOtn, float maxOtn) {
            processor.usingOtn = usingOtn;
            processor.minOtn = minOtn;
            processor.maxOtn = maxOtn;
            return this;
        }

        /**
         * Установка параметров для отсеивания лишних объектов по площади
         *
         * @param usingSquare используется ли проверка на площадь
         * @param minSquare   минимальная площадь объекта
         * @param maxSquare   максимальная площадь объекта
         * @return сборщик
         */
        public Builder setSquareProperties(boolean usingSquare, float minSquare, float maxSquare) {
            processor.usingSquare = usingSquare;
            processor.minSquareOnScreen = minSquare;
            processor.maxSquareOnScreen = maxSquare;
            return this;
        }

        /**
         * Добавление телеметрии
         *
         * @param usingTelemetry используется ли телеметрия
         * @param telemetry      телеметрия
         * @return сборщик
         */
        public Builder addTelemetry(boolean usingTelemetry, Telemetry telemetry) {
            processor.usingTelemetry = usingTelemetry;
            processor.telemetry = telemetry;
            return this;
        }
        // Установка параметров морфологических операций

        /**
         * <p>Установка параметра операции расширения светлых областей (Dilation) для обработки изображений
         * <p>Смотреть {@link Imgproc#dilate(Mat, Mat, Mat)}
         * или <a href="https://docs.opencv.org/4.x/db/df6/tutorial_erosion_dilatation.html#autotoc_md565">Документацию по OpenCV</a>
         *
         * @param dilateElement размер элемента для расширения области {@link Imgproc#getStructuringElement(int, Size)}
         * @return сборщик
         */
        public Builder setDilateElement(Mat dilateElement) {
            processor.dilateElement = dilateElement;
            return this;
        }

        /**
         * <p>Установка параметра операции расширения светлых областей (Dilation) для обработки изображений
         * <p>Смотреть {@link Imgproc#dilate(Mat, Mat, Mat)}
         * или <a href="https://docs.opencv.org/4.x/db/df6/tutorial_erosion_dilatation.html#autotoc_md565">Документацию по OpenCV</a>
         *
         * @param dilateHeight высота элемента для расширения области
         * @param dilateWidth  ширина элемента для расширения области
         * @return сборщик
         */
        public Builder setDilateElement(int dilateHeight, int dilateWidth) {
            processor.dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(dilateWidth, dilateHeight));
            return this;
        }

        /**
         * <p>Установка параметра операции уменьшения светлых областей (Erosion) для обработки изображений
         * <p>Смотреть {@link Imgproc#erode(Mat, Mat, Mat)}
         * или <a href="https://docs.opencv.org/4.x/db/df6/tutorial_erosion_dilatation.html#autotoc_md566">Документацию по OpenCV</a>
         *
         * @param erodeElement размер элемента для сужения области {@link Imgproc#getStructuringElement(int, Size)}
         * @return сборщик
         */
        public Builder setErodeElement(Mat erodeElement) {
            processor.erodeElement = erodeElement;
            return this;
        }

        /**
         * <p>Установка параметра операции уменьшения светлых областей (Erosion) для обработки изображений
         * <p>Смотреть {@link Imgproc#erode(Mat, Mat, Mat)}
         * или <a href="https://docs.opencv.org/4.x/db/df6/tutorial_erosion_dilatation.html#autotoc_md566">Документацию по OpenCV</a>
         *
         * @param erodeHeight высота элемента для сужения области
         * @param erodeWidth  ширина элемента для сужения области
         * @return сборщик
         */
        public Builder setErodeElement(int erodeHeight, int erodeWidth) {
            processor.erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erodeWidth, erodeHeight));
            return this;
        }
        // Установка параметров блюра

        /**
         * Установка параметров блюра
         *
         * @param blurSize размер блюра
         * @return сборщик
         */
        public Builder setBlurSize(Size blurSize) {
            processor.blurSize = blurSize;
            return this;
        }

        /**
         * Установка параметров блюра
         *
         * @param blurHeight высота пикселя блюра
         * @param blurWidth  ширина пикселя блюра
         * @return сборщик
         */
        public Builder setBlurSize(int blurHeight, int blurWidth) {
            processor.blurSize = new Size(blurWidth, blurHeight);
            return this;
        }
        // Установка ограничений по цвету (В оригинале сказано, что тут используются цвета HSV однако
        // по факту используется RGB)


        /**
         * Максимальный порог зеленого цвета
         *
         * @param maxValues Минимальные значения в Scalar см.{@link Scalar}
         * @return сборщик
         */
        public Builder setMaxGreenValues(Scalar maxValues) {
            processor.maxValuesGreen = maxValues;
            return this;
        }

        /**
         * Максимальный порог зеленого цвета
         *
         * @param r Минимальное значение красного
         * @param g Минимальное значение зеленого
         * @param b Минимальное значение синего
         * @return сборщик
         */
        public Builder setMaxGreenValues(int r, int g, int b) {
            processor.maxValuesGreen = new Scalar(r, g, b);
            return this;
        }

        /**
         * Максимальный порог фиолетового цвета
         *
         * @param maxValues Минимальные значения в Scalar см.{@link Scalar}
         * @return сборщик
         */
        public Builder setMaxPurpleValues(Scalar maxValues) {
            processor.maxValuePurple = maxValues;
            return this;
        }

        /**
         * Минимальный порог фиолетового цвета
         *
         * @param r Минимальное значение красного
         * @param g Минимальное значение зеленого
         * @param b Минимальное значение синего
         * @return сборщик
         */
        public Builder setMaxPurpleValues(int r, int g, int b) {
            processor.maxValuePurple = new Scalar(r, g, b);
            return this;
        }

        /**
         * Минимальный порог зеленого цвета
         *
         * @param r Минимальное значение красного
         * @param g Минимальное значение зеленого
         * @param b Минимальное значение синего
         * @return сборщик
         */
        public Builder setMinGreenValues(int r, int g, int b) {
            processor.minValuesGreen = new Scalar(r, g, b);
            return this;
        }

        /**
         * Максимальный порог зеленого цвета
         *
         * @param maxValues Минимальные значения в Scalar см.{@link Scalar}
         * @return сборщик
         */
        public Builder setMinGreenValues(Scalar maxValues) {
            processor.minValuesGreen = maxValues;
            return this;
        }

        /**
         * Минимальный порог фиолетового цвета
         *
         * @param r Минимальное значение красного
         * @param g Минимальное значение зеленого
         * @param b Минимальное значение синего
         * @return сборщик
         */
        public Builder setMinPurpleValues(int r, int g, int b) {
            processor.minValuesPurple = new Scalar(r, g, b);
            return this;
        }

        /**
         * Максимальный порог фиолетового цвета
         *
         * @param maxValues Минимальные значения в Scalar см.{@link Scalar}
         * @return сборщик
         */
        public Builder setMinPurpleValues(Scalar maxValues) {
            processor.minValuesPurple = maxValues;
            return this;
        }


        /**
         * НЕ ИСПОЛЬЗОВАТЬ СОЗДАНО ДЛЯ ПРОВЕРКИ В БУДУЩЕМ БУДЕТ ОБНОВЛЕНО И ДОБАВЛЕНО
         *
         * @return сборщик
         */
        public ArtifactProcessor createWithDefaults() {
            processor.usingOtn = true;
            processor.minOtn = 0.95f;
            processor.maxOtn = 1.05f;
            processor.usingDist = true;
            processor.minDist = 100;
            processor.maxDist = 1000;
            processor.usingSquare = false;
            processor.usingTelemetry = false;
            processor.cameraPos[0] = 0;
            processor.cameraPos[1] = 0;
            processor.cameraPos[2] = 0;
            processor.cameraRot[0] = 0;
            processor.cameraRot[1] = 0;
            processor.cameraRot[2] = 0;
            processor.c = (float) Math.sqrt(Math.pow(3.58F, 2) + Math.pow(2.02F, 2));
            processor.size = 115f;
            processor.f = 4f;
            processor.pixelCameraHeight = 960;
            processor.dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(50, 50));
            processor.erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30, 30));
            processor.minValuesGreen = new Scalar(7, 60, 60);
            processor.maxValuesGreen = new Scalar(15, 255, 255);
            processor.blurSize = new Size(3, 3);
            processor.minValuesPurple = new Scalar(0, 0, 0);
            processor.maxValuePurple = new Scalar(0, 0, 0);
            return processor;
        }

        /**
         * Сборка проекта
         * <p> В случае отсутствия параметров:
         * size, c, f, pixelCameraHeight
         * будет выброшено исключение {@link IllegalArgumentException}
         *
         * <p> По умолчанию берется значение позиции камеры 0 во всех осях
         *
         * <p> Цвет ограничивается по промежуткам от верхнего к нижнему значению
         *
         * @return Процессор со всеми параметрами
         */
        public ArtifactProcessor build() {
            if (Float.isNaN(processor.size)) {
                throw new IllegalArgumentException("Object height is not set");
            }
            if (Float.isNaN(processor.c)) {
                throw new IllegalArgumentException("Camera diagonal is not set");
            }
            if (Float.isNaN(processor.f)) {
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
            if (processor.minValuesGreen == null) {
                processor.minValuesGreen = new Scalar(7, 60, 60);
            }
            if (processor.maxValuesGreen == null) {
                processor.maxValuesGreen = new Scalar(15, 255, 255);
            }
            if (processor.minValuesPurple == null) {
                processor.minValuesPurple = new Scalar(7, 60, 60);
            }
            if (processor.maxValuePurple == null) {
                processor.maxValuePurple = new Scalar(15, 255, 255);
            }
            if (processor.blurSize == null) {
                processor.blurSize = new Size(3, 3);
            }
            if (Float.isNaN(processor.cameraPos[0])) {
                processor.cameraPos[0] = 0;
                processor.cameraPos[1] = 0;
                processor.cameraPos[2] = 0;
            }
            if (Float.isNaN(processor.cameraRot[0])) {
                processor.cameraRot[0] = 0;
                processor.cameraRot[1] = 0;
                processor.cameraRot[2] = 0;
            }
            return processor;
        }
    }

    /// Максимальная дистанция до объекта
    private float maxDist,
    /// Минимальная дистанция до объекта
    minDist,
    /// Минимальное соотношение сторон
    minOtn,
    /// Максимальное соотношение сторон
    maxOtn,
    /// Размер объекта
    size,
    /// Фокусное расстояние
    f,
    /// Размер сенсора в пикселях
    pixelCameraHeight,
    /// Диагональ сенсора в мм
    c;
    /// Позиция камеры в мм по x, y, z
    private float[] cameraPos = new float[]{0, 0, 0},
    /// Поворот камеры в градусах, Roll Yaw Pitch
    cameraRot = new float[]{0, 0, 0};
    /// Высота объекта на сенсоре камеры
    private float h,
    /// Центр прямоугольника ограничивающего объект
    centerOfSquare,
    /// Координата x
    x,
    /// Кордината y
    y,
    /// Координата z
    z,
    /// Расстояние до объекта
    distanceToTarget,
    /// Соотношение реального размера камеры к размеру в пикселях
    mmPerPixel,
    /// Высота объекта на картинке
    a,
    /// Ширина объекта на картинке
    b,
    /// Соотношение сторон объекта
    otn;
    /// Проверка используются ли соотношения сторон
    private boolean usingOtn,
    /// Проверка используются ли расстояния
    usingDist,
    /// Проверка используются ли площади
    usingSquare,
    /// Проверка используется ли телеметрия
    usingTelemetry;
    /// Площадь объекта на фото
    private float squareOnScreen,
    /// Минимальная площадь объекта на фото
    minSquareOnScreen,
    /// Максимальная площадь объекта на фото
    maxSquareOnScreen;
    /// Матрица параметров камер
    Mat K = new Mat(3, 3, CV_64F);
    /// Список контуров на картинке
    List<MatOfPoint> contours = new ArrayList<>();
    /// Матрица с фильтром открытия
    Mat openingImage = new Mat();
    /// Матрица с фильтром закрытия
    Mat closingOutput = new Mat();
    /// Матрица иерархии контуров
    Mat hierarchy = new Mat();
    /// Матрица заблюренного изображения
    Mat blurredImage = new Mat();
    /// Матрица фото в HSV
    Mat hsvImage = new Mat();
    /// Матрица после применения фильтра по зеленому цвету
    Mat green = new Mat(),
    /// Матрица после применения фильтра по фиолетовому цвету
    purple = new Mat();
    /// Матрица итогового изображение после фильтров по цветам
    Mat mask = new Mat();
    /// Матрица итогового изображения после всех обработок
    Mat morfOutput = new Mat();
    /// Матрица объекта для расширения светлых областей
    Mat dilateElement;
    /// Матрица объекта для уменьшения светлых областей
    Mat erodeElement;
    /// Минимальное значение зеленого цвета
    Scalar minValuesGreen,
    /// Минимальное значение фиолетового цвета
    minValuesPurple;
    /// Максимальное значение зеленого цвета
    Scalar maxValuesGreen,
    /// Максимальное значение фиолетового цвета
    maxValuePurple;
    /// Массив полигонов контуров
    MatOfPoint2f[] contoursPoly;
    /// Телеметрия
    Telemetry telemetry;
    /// Массив контурных прямоугольников
    Rect[] contourRects;
    /// Размер блюра
    Size blurSize;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        // устанавливаем соотношение мм к пикселям
        mmPerPixel = (c / pixelCameraHeight);
        // установка параметров камеры
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
        detectedArtifacts.clear();
        // блюрим изображение
        Imgproc.blur(frame, blurredImage, blurSize);
        // преобразуем к HSV формату
        Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);
        // фильтруем изображение по зеленому цвету
        Core.inRange(hsvImage, minValuesGreen, maxValuesGreen, green);
        // фильтруем изображение по фиолетовому цвету
        Core.inRange(hsvImage, minValuesPurple, maxValuePurple, purple);
        // совмещаем изображения
        Core.add(green, purple, mask);
        // применяем фильтр открытия
        Imgproc.erode(mask, openingImage, erodeElement);
        Imgproc.dilate(openingImage, morfOutput, dilateElement);
        // применяем фильтр закрытия
        Imgproc.dilate(mask, closingOutput, dilateElement);
        Imgproc.erode(closingOutput, morfOutput, erodeElement);
        // ищем контуры изображения
        Imgproc.findContours(morfOutput, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        // создаём объекты полигонов контуров
        contoursPoly = new MatOfPoint2f[contours.size()];
        // создаём объекты прямоугольников
        contourRects = new Rect[contours.size()];
        // отрисовка и создание прямоугольников
        for (int idx = 0; idx < contours.size(); idx++) {
            // если контур не пустой
            if (contours.get(idx).toArray() != null) {
                // создаем полигон для данного контура
                contoursPoly[idx] = new MatOfPoint2f();
                // аппроксимируем контур
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(idx).toArray()), contoursPoly[idx], 3, true);
                // создаём прямоугольник
                contourRects[idx] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[idx].toArray()));
                // ищем стороны прямоугольника на изображении
                a = (float) (-contourRects[idx].tl().x + contourRects[idx].br().x);
                b = (float) (-contourRects[idx].tl().y + contourRects[idx].br().y);
                // ищем отношение сторон
                otn = a / b;
                // высота объекта на сенсоре
                h = (a * mmPerPixel);
                // расчет дистанции до объекта
                distanceToTarget = ((f * size) / h);
                // площадь на изображении
                squareOnScreen = (a * a);
                // проверка параметров
                boolean screenProperty = !usingSquare || (squareOnScreen >= minSquareOnScreen && squareOnScreen <= maxSquareOnScreen),
                        distProperty = !usingDist || (distanceToTarget >= minDist && distanceToTarget <= maxDist),
                        otnProperty = !usingOtn || (otn >= minOtn && otn <= maxOtn);
                // проверка условий и отрисовка итоговых изображений
                if (screenProperty && otnProperty && distProperty) {

                    // центр диагонали центра прямоугольника
                    centerOfSquare = (a / 2);
                    // отрисовка прямоугольника
                    Imgproc.rectangle(frame, contourRects[idx].tl(), contourRects[idx].br(), new Scalar(255, 0, 0), 2);
                    // точка центра
                    Point centerPoint = new Point(contourRects[idx].tl().x + centerOfSquare, contourRects[idx].tl().y + centerOfSquare);
                    /// Массив с углами, хранит 2 значения: горизонтальный(1) и вертикальный(0)
                    float[] angles = calculateAngle((float) centerPoint.x, (float) centerPoint.y);
                    /// Координата x в системе камеры
                    float xc = (float) (distanceToTarget * Math.cos(angles[0]) * Math.cos(angles[1]));
                    /// Координата y в системе камеры
                    float yc = (float) (distanceToTarget * Math.cos(angles[0]) * Math.sin(angles[1]));
                    /// Координата z в системе камеры
                    float zc = (float) (distanceToTarget * Math.sin(angles[0]));
                    /// Массив координат камеры
                    float[] cameraCoordinates = {xc, yc, zc};
                    // перевод координат камеры в координаты робота
                    cameraCoordinates = convertCameraToRobot(cameraPos, cameraRot, cameraCoordinates);
                    x = cameraCoordinates[0];
                    y = cameraCoordinates[1];
                    z = cameraCoordinates[2];

                    // логика для добавления уникальных объектов
                    Artifact newArtifact = new Artifact(x, y, z);
                    boolean isNew = true;
                    for (Artifact existingArtifact : detectedArtifacts) {
                        if (newArtifact.distanceTo(existingArtifact) < 100) {
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) {
                        detectedArtifacts.add(newArtifact);
                    }

                    // вывод координат на изображение
                    @SuppressLint("DefaultLocale")
                    String text = String.format("dist: %.1f, x: %.1f, y: %.1f, z: %.1f", distanceToTarget, x, y, z);
                    Imgproc.putText(frame, text, contourRects[idx].tl(), 1, 1, new Scalar(255, 255, 0));
                    // отрисовка центра прямоугольника
                    Imgproc.drawMarker(frame, new Point(contourRects[idx].tl().x + centerOfSquare, contourRects[idx].tl().y + centerOfSquare), new Scalar(255, 0, 0));
                }
            }
            // освобождение полигона
            contoursPoly[idx].release();
        }
        // отрисовка центра камеры
        Imgproc.drawMarker(frame, new Point(K.get(0, 2)[0], K.get(1, 2)[0]), new Scalar(255, 0, 255));
        // освобождение памяти
        blurredImage.release();
        hsvImage.release();
        mask.release();
        openingImage.release();
        closingOutput.release();
        contours.clear();
        hierarchy.release();
        contours.clear();


        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
    }
    /// Конверсия координат от системы камеры в систему робота
    private float[] convertCameraToRobot(float[] cameraCord, float[] angeles, float[] objectPos) {
        /// Матрица координат камеры
        Mat T = new Mat(3, 1, CV_64F);
        T.put(0, 0, cameraCord[0]);
        T.put(1, 0, cameraCord[1]);
        T.put(2, 0, cameraCord[2]);
        /// Матрица координат объекта
        Mat Pc = new Mat(3, 1, CV_64F);
        Pc.put(0, 0, objectPos[0]);
        Pc.put(1, 0, objectPos[1]);
        Pc.put(2, 0, objectPos[2]);
        /// координата Roll
        angeles[0] = (float) Math.toRadians(angeles[0]);
        /// координата Yaw
        angeles[1] = (float) Math.toRadians(angeles[1]);
        /// координата Pitch
        angeles[2] = (float) Math.toRadians(angeles[2]);
        /// Матрица угла вращения вокруг оси x
        Mat Rx = Mat.eye(3, 3, CV_64F);
        Rx.put(1, 1, Math.cos(angeles[0]));
        Rx.put(1, 2, -Math.sin(angeles[0]));
        Rx.put(2, 2, Math.cos(angeles[0]));
        /// Матрица угла вращения вокруг оси y
        Mat Ry = Mat.eye(3, 3, CV_64F);
        Ry.put(1, 1, Math.cos(angeles[1]));
        Ry.put(1, 2, Math.sin(angeles[1]));
        Ry.put(2, 2, Math.cos(angeles[1]));
        /// Матрица угла вращения вокруг оси z
        Mat Rz = Mat.eye(3, 3, CV_64F);
        Rz.put(1, 1, Math.cos(angeles[2]));
        Rz.put(1, 2, -Math.sin(angeles[2]));
        Rz.put(2, 2, Math.cos(angeles[2]));
        /// Промежуточная матрица
        Mat R_temp = new Mat();
        Core.gemm(Rz, Ry, 1.0, new Mat(), 0.0, R_temp);
        /// Полная матрица вращения
        Mat R_c_to_r = new Mat();
        Core.gemm(R_temp, Rx, 1.0, new Mat(), 0.0, R_c_to_r);
        // аффинное преобразование
        // вращение
        Mat RPc = new Mat();
        Core.gemm(R_c_to_r, Pc, 1.0, new Mat(), 0.0, RPc);
        // смещение
        /// Итоговая матрица
        Mat Pr = new Mat();
        Core.add(RPc, T, Pr);
        /// Получаем координаты в системе робота
        float x_r = (float) Pr.get(0, 0)[0];
        float y_r = (float) Pr.get(1, 0)[0];
        float z_r = (float) Pr.get(2, 0)[0];
        // освобождение памяти
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
    /// Расчет вертикального и горизонтального углов
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

    public float[] getCameraPos() {
        return cameraPos;
    }

    public void setCameraPos(float[] cameraPos) {
        this.cameraPos = cameraPos;
    }

    public float[] getCameraRot() {
        return cameraRot;
    }

    public void setCameraRot(float[] cameraRot) {
        this.cameraRot = cameraRot;
    }

}
