package com.android.keven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;

/**
 * Created by Paul on 2015/12/28.
 */
public class LockView extends View {
    private final String TAG = "KEVEN";
    private final int SUM = 9;
    private final int LINE_WIDTH = 16;
    private int w, h;
    private float touchX, touchY;
    private Paint mPen = null;

    LockCircle[] LockCircles = new LockCircle[SUM];
    int[] mSelectArray = new int[SUM];
    int[] mCheckArray = {0, 1, 4, 7, 8};
    boolean mCheckResultError = false;
    boolean mSelectEnded = false;

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        w = this.getResources().getDisplayMetrics().widthPixels;
//        h = this.getResources().getDisplayMetrics().heightPixels;
//        w = getMeasuredWidth();
//        h = getMeasuredHeight();
//        onMeasure(w,h);
        initPaint();
        resetSelectArray();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        onMeasure(w,h);
        w = getMeasuredWidth();
        h = getMeasuredHeight();
        int offx = w / 6;
        int offy = h / 6;

        for (int i = 0; i < SUM; i++) {
            LockCircles[i] = new LockCircle();
            LockCircles[i].setRx(((i % 3) * 2 + 1) * offx);
            LockCircles[i].setRy(((i / 3) * 2 + 1) * offy);
            LockCircles[i].setOnTouched(false);
        }
    }

    private void initPaint() {
        mPen = new Paint();
        mPen.setAntiAlias(true);
        mPen.setStrokeWidth(LINE_WIDTH);
    }


    private void resetSelectArray() {
        for (int i = 0; i < SUM; i++) {
            mSelectArray[i] = SUM;
        }
    }

    private void addSelectArray(int sum) {
        if (sum != SUM) {
            for (int i = 0; i < SUM; i++) {
                if (mSelectArray[i] == sum) {//ря╢Ф
                    break;
                }

                if (mSelectArray[i] == SUM) {
                    mSelectArray[i] = sum;
                    break;
                }
            }
        }
    }

    private int getSelectArrayLength() {
        for (int i = 0; i < SUM; i++) {
            if (mSelectArray[i] == SUM)
                return i;
        }
        return SUM;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < SUM; i++) {
            LockCircles[i].onDraw(canvas);
        }
        Log.i(TAG, "LockView invalidate");
        drawLine(canvas);
        super.onDraw(canvas);
    }

    private void drawLine(Canvas canvas) {
        if(mSelectEnded){
            if (mCheckResultError)
                mPen.setColor(Color.RED);
            else
                mPen.setColor(Color.GREEN);
        }else
            mPen.setColor(Color.GRAY);

        int len = getSelectArrayLength();
        Log.i(TAG, "len = " + len);
        if (len == 0) {
            return;
        } else {
            int select = mSelectArray[0];
            int x0 = LockCircles[select].getRx();
            int y0 = LockCircles[select].getRy();
            Log.i(TAG, "P" + select + "[" + x0 + "," + y0 + "]");
            canvas.drawCircle(x0, y0, LINE_WIDTH/2, mPen);
            int x1 = x0;
            int y1 = y0;
            if (len == 1) {
                canvas.drawLine(x0, y0, x1, y1, mPen);
            } else {
                for (int i = 1; i < len; i++) {
                    select = mSelectArray[i];
                    x1 = LockCircles[select].getRx();
                    y1 = LockCircles[select].getRy();
                    Log.i(TAG, "P" + select + "[" + x1 + "," + y1 + "]");
                    canvas.drawLine(x0, y0, x1, y1, mPen);
                    canvas.drawCircle(x1, y1, LINE_WIDTH / 2, mPen);
                    x0 = x1;
                    y0 = y1;
                }
            }
            if (touchY != -1 && touchY != -1) {
                Log.i(TAG, "T" + "[" + x1 + "," + y1 + "]");
                canvas.drawLine(x0, y0, touchX, touchY, mPen);
                canvas.drawCircle(touchX, touchY, LINE_WIDTH / 2, mPen);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        touchX = event.getX();
        touchY = event.getY();
        Log.i(TAG, "touch" + "[" + touchX + "," + touchY + "]");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCheckResultError = false;
                mSelectEnded = false;
                resetSelectArray();
                resetCirCleArray();
            case MotionEvent.ACTION_MOVE:
                int select = checkTouchCircles(touchX, touchY);
                if (select != SUM) {
                    addSelectArray(select);
                    LockCircles[select].setOnTouched(true);
                }
                break;

            case MotionEvent.ACTION_UP:
                mSelectEnded = true;
                touchX = -1;
                touchY = -1;
                boolean checkResult = checkLockNum();
                if (!checkResult) {
                    mCheckResultError = true;
                    setCirClesStatusError(true);
                }
                break;
            default:
                break;

        }
        invalidate();
        return true;
    }

    private void setCirClesStatusError(boolean b) {
        for (int i = 0; i < SUM; i++) {
            if (mSelectArray[i] != SUM)
                LockCircles[mSelectArray[i]].setErrStatus(b);
        }
    }

    private void resetCirCleArray() {
        for (int i = 0; i < SUM; i++) {
            LockCircles[i].setOnTouched(false);
            LockCircles[i].setErrStatus(false);
        }
    }

    private int checkTouchCircles(float x, float y) {
        int msum = SUM;
        for (int i = 0; i < SUM; i++) {
            if (LockCircles[i].isPointIn((int) x, (int) y)) {
                msum = i;
                break;
            }
        }

        return msum;
    }

    private boolean checkLockNum() {
        if (getSelectArrayLength() == mCheckArray.length) {
            for (int i = 0; i < mCheckArray.length; i++)
                if (mCheckArray[i] != mSelectArray[i])
                    return false;
            return true;
        }
        return false;
    }
}
