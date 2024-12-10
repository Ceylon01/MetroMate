package com.metromate;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.appcompat.widget.AppCompatImageView;

public class ZoomableImageView extends AppCompatImageView {

    private final Matrix matrix = new Matrix();
    private final float[] matrixValues = new float[9];

    private float minZoom = 0.3f;
    private float maxZoom = 2.0f;

    private PointF lastTouch = new PointF();
    private ScaleGestureDetector scaleGestureDetector;

    public ZoomableImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        setScaleType(ScaleType.MATRIX);
        setImageMatrix(matrix);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastTouch.set(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - lastTouch.x;
                float dy = event.getY() - lastTouch.y;
                matrix.postTranslate(dx, dy);

                // 이미지가 화면 밖으로 벗어나지 않도록 제한
                limitMovement();

                setImageMatrix(matrix);
                lastTouch.set(event.getX(), event.getY());
                break;
        }
        return true;
    }

    private void limitMovement() {
        // 현재 매트릭스 값 가져오기
        matrix.getValues(matrixValues);

        // 이미지를 확대, 축소 후의 크기 계산
        float currentScale = matrixValues[Matrix.MSCALE_X]; // scaleX 값 (이미지 크기)
        float imageWidth = getDrawable().getIntrinsicWidth() * currentScale;
        float imageHeight = getDrawable().getIntrinsicHeight() * currentScale;

        // 화면의 여유 공간을 10%로 설정
        float marginX = getWidth() * 0.2f;
        float marginY = getHeight() * 0.2f;

        // 이미지의 이동 범위를 제한 (이미지가 화면을 벗어나지 않도록)
        float maxX = marginX;
        float maxY = marginY;
        float minX = getWidth() - imageWidth - marginX;
        float minY = getHeight() - imageHeight - marginY;

        // 이동을 제한
        if (matrixValues[Matrix.MTRANS_X] > maxX) {
            matrix.postTranslate(maxX - matrixValues[Matrix.MTRANS_X], 0);
        } else if (matrixValues[Matrix.MTRANS_X] < minX) {
            matrix.postTranslate(minX - matrixValues[Matrix.MTRANS_X], 0);
        }

        if (matrixValues[Matrix.MTRANS_Y] > maxY) {
            matrix.postTranslate(0, maxY - matrixValues[Matrix.MTRANS_Y]);
        } else if (matrixValues[Matrix.MTRANS_Y] < minY) {
            matrix.postTranslate(0, minY - matrixValues[Matrix.MTRANS_Y]);
        }
    }

    public void resetZoom() {
        matrix.reset();
        matrix.postScale(minZoom, minZoom);
        setImageMatrix(matrix);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();

            // 스케일 제한 (최소, 최대 줌 크기)
            if (scaleFactor * getCurrentScale() > maxZoom) {
                scaleFactor = maxZoom / getCurrentScale();
            } else if (scaleFactor * getCurrentScale() < minZoom) {
                scaleFactor = minZoom / getCurrentScale();
            }

            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            limitMovement();  // 이동 범위 제한
            setImageMatrix(matrix);

            return true;
        }
    }

    private float getCurrentScale() {
        matrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X]; // 현재 스케일 값
    }
}
