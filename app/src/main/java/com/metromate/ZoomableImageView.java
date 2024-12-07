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

    // States
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    private ScaleGestureDetector scaleDetector;

    // Dragging
    private final PointF last = new PointF();
    private float dy;

    public ZoomableImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);

        PointF current = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last.set(current);
                mode = DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    float dx = current.x - last.x;
                    dy = current.y - last.y;

                    // 현재 드래그 위치 계산
                    matrix.postTranslate(dx, dy);
                    setImageMatrix(matrix);
                    last.set(current.x, current.y);
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();

            // 현재 스케일 값을 얻기 위해 Matrix 값 계산
            matrix.getValues(matrixValues);
            float currentScale = matrixValues[Matrix.MSCALE_X];

            // 줌 범위를 제한
            // Zooming
            // 최소 줌 비율
            float minScale = 1f;
            // 최대 줌 비율
            float maxScale = 5f;
            if ((scaleFactor > 1 && currentScale * scaleFactor <= maxScale) || // 확대 조건
                    (scaleFactor < 1 && currentScale * scaleFactor >= minScale)) { // 축소 조건
                matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            }

            // Matrix 적용
            setImageMatrix(matrix);
            return true;
        }

    }
}
