package in.wordmug.trialar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.google.firebase.ml.vision.common.FirebaseVisionPoint;

import java.util.ArrayList;
import java.util.List;

public class Overlay extends AppCompatImageView {

    List<FirebaseVisionPoint> points = new ArrayList<>();
    Paint paint = new Paint();
    Bitmap bm1,bm2;

    Context context;

    public Overlay(Context context) {
        super(context);
        this.context = context;
    }

    public Overlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public Overlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(4);

        if(bm1 == null)
        {
            bm1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.earj);
        }

        if(points.size()>=2)
        {
            //left E
            Integer x = Math.round(points.get(0).getX()), y = Math.round(points.get(0).getY());
            Integer newX = (int) (x - bm1.getWidth()*0.5);
            int nWidth = (int)Math.abs((points.get(0).getX()-points.get(1).getX()))/7;
            float nHeight= bm1.getHeight()*nWidth/bm1.getWidth();
            if(nWidth>0 && nHeight>0)
            {
                Bitmap scaled = Bitmap.createScaledBitmap(bm1, nWidth, (int)nHeight, true);
                canvas.drawBitmap(scaled, x-(nWidth/2),y+5,null);
                x = Math.round(points.get(1).getX());
                y = Math.round(points.get(1).getY());
                //newX = (int) (x - nWidth*0.5);
                canvas.drawBitmap(scaled, x-(nWidth/2),y+5,null);
                //scaled.recycle();
            }
//            else
//            {
//                canvas.drawBitmap(bm1, newX, y, null);
//            }
        }

        if(points.size()>=3)
        {
            //NOSE point
            //canvas.drawPoint(points.get(2).getX(), points.get(2).getY(), paint);
            //vertical distance between nose and the line joining both ears
            int distance = (int)( ((points.get(0).getY()+points.get(1).getY())/2) - points.get(2).getY());
            int jY = (int)(points.get(2).getY()-(2.55*distance));
            int jX = Math.round(points.get(2).getX()); //use of X since it is already at the horizontal center of face

            if(bm2 == null)
            {
                bm2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.neckj);
            }
            int nWidth = (int)(1.5 * Math.abs((points.get(0).getX())-points.get(1).getX()) ); //distance between ears
            int nHeight = bm2.getHeight()*nWidth/bm2.getWidth();
            if(nHeight>0 && nWidth>0)
            {
                Bitmap scaled = Bitmap.createScaledBitmap(bm2, nWidth, nHeight,true);
                canvas.drawBitmap(scaled, jX-(scaled.getWidth()/2), jY, null);
                //scaled.recycle();
            }
//            else
//            {
//                canvas.drawBitmap(bm2, jX-(bm2.getWidth()/2), jY, null);
//            }
        }

    }

    public void setPoints(List<FirebaseVisionPoint> points)
    {
        this.points = points;
        invalidate();
    }

}
