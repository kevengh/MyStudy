package com.android.keven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Paul on 2015/12/28.
 */
public class LockCircle{
    private final String TAG = "KEVEN";
    private final int Radiu = 40;
    //    private int sum;
    private int rx, ry;
    private Boolean OnTouched = false;
    private Paint mp1, mp2;
    private boolean errStatus = false;

    public LockCircle() {
        initPaint();
    }

    public LockCircle(Context context, AttributeSet attrs) {
        initPaint();
    }

    private void initPaint() {
        if(mp1== null){
            mp1 = new Paint();
            mp1.setAntiAlias(true);
            mp1.setStrokeWidth(4);
            mp1.setStyle(Paint.Style.STROKE);
        }

        if(mp2== null){
            mp2 = new Paint();
            mp2.setAntiAlias(true);
            mp2.setStyle(Paint.Style.FILL);
        }
    }


    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        Log.i(TAG, "LockCircle invalidate");
        if(errStatus){
            mp1.setColor(Color.RED);
            mp2.setColor(Color.RED);
        }else{
            if(OnTouched){
                mp1.setColor(Color.GREEN);
                mp2.setColor(Color.RED);

            }else {
                mp1.setColor(Color.GRAY);
                mp2.setColor(Color.GRAY);
            }
        }

        canvas.drawCircle(rx,ry,Radiu,mp1);
        canvas.drawCircle(rx,ry,20,mp2);
    }

//    public void setSum(int sum){
//        this.sum = sum;
//    }
//
//    public int getSum() {
//        return sum;
//    }

    public void setRy(int ry) {
        this.ry = ry;
    }

    public int getRy() {
        return ry;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }
    public int getRx() {
        return rx;
    }

    public void setOnTouched(Boolean onTouched) {
        OnTouched = onTouched;
    }

    public Boolean getOnTouched() {
        return OnTouched;
    }

    public void setErrStatus(boolean errStatus) {
        this.errStatus = errStatus;
    }

    public boolean isPointIn(int x,int y){
        int mR = (int)Math.sqrt(Math.pow((x-rx),2) + Math.pow((y - ry),2));
        return (mR < Radiu)?true:false;
    }
}
