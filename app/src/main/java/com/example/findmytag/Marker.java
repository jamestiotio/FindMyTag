package com.example.findmytag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class Marker extends SubsamplingScaleImageView {
    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();
    private final PointF tempvPin = new PointF();
    private PointF sPin;
    private Bitmap pin;
    private Float vX, vY;
    //mine
    private PointF sNode;
    private Bitmap tempPin;
    private Float tempX, tempY;

    public Marker(Context context) {
        this(context, null);
    }

    public Marker(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        initialise();
        invalidate();
    }
    //--- my stuff

    public void createNode(PointF sNode) {
        this.sNode = sNode;
        init2();
        invalidate();
    }

    private void init2() {
        float density = getResources().getDisplayMetrics().densityDpi;
        tempPin = BitmapFactory.decodeResource(this.getResources(), R.drawable.pointer2);

        float w = (density / 420f) * tempPin.getWidth();
        float h = (density / 420f) * tempPin.getHeight();
        tempPin = Bitmap.createScaledBitmap(tempPin, (int) w / 15, (int) h / 15, true);
    }

    public PointF getCoords() {
        return sPin;
    }

    //---


    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.pointer2);

        float w = (density / 420f) * pin.getWidth();
        float h = (density / 420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int) w / 15, (int) h / 15, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        paint.setAntiAlias(true);

        if (sPin != null && pin != null) {
            sourceToViewCoord(sPin, vPin);
            vX = vPin.x - (pin.getWidth() / 2);
            vY = vPin.y - pin.getHeight();
            canvas.drawBitmap(pin, vX, vY, paint);
        }

        //mine
        if (sNode != null && tempPin != null) {
            sourceToViewCoord(sNode, tempvPin);
            tempX = tempvPin.x - (tempPin.getWidth() / 2);
            tempY = tempvPin.y - tempPin.getHeight();
            canvas.drawBitmap(pin, tempX, tempY, paint);
        }


    }

}

