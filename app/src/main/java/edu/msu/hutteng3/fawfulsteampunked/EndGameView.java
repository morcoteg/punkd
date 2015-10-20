package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


public class EndGameView extends View {





    private Bitmap pipe;
    private Bitmap steam;

    public EndGameView(Context context) {
        super(context);
       // pipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        //steam = BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);

        int x=0;

        init(null, 0);
    }

    public EndGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
       // pipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        //steam = BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);
        init(attrs, 0);
    }

    public EndGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
       // pipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
       // steam = BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);





        int wid = canvas.getWidth();
        int hit=canvas.getHeight();




        int orentation=Math.min(wid, hit);

        //wid=orentation;
        //hit=orentation;


      //  pipe = Bitmap.createScaledBitmap(pipe, Math.max(wid / 6, hit / 6), Math.min(wid/6, hit/6), false);
      //  steam = Bitmap.createScaledBitmap(steam, Math.max(wid / 6, hit / 6), Math.min(wid/6, hit/6), false);
       // canvas.save();


        //canvas.rotate(90, canvas.getWidth() / 3 - 3 * pipe.getWidth() / 2, 5*canvas.getHeight() / 12-pipe.getHeight());

        //canvas.drawBitmap(pipe, canvas.getWidth() / 3 -  pipe.getWidth() , 5 * canvas.getHeight() / 24-pipe.getHeight(), paint);
        //canvas.drawBitmap(pipe, canvas.getWidth() / 3 -  pipe.getWidth() , 5 * canvas.getHeight() / 24-pipe.getHeight()-wid/1.9f, paint);
        //we are in portrait
     //   if (orentation == hit) {
            //subtract (pipeStraight.getHeight()-hit)/2 due to the rotation and the translation done
     //       canvas.drawBitmap(pipe, -(pipe.getHeight() - hit) / 2 ,  wid / 5 - (pipe.getHeight()-hit)/2, null);
      //  }
        //we are in landscape
      //  else
        //     canvas.drawBitmap(pipe,  hit / 5, 0, null);
       // canvas.restore();






        paint.setTextSize(wid/20);
        canvas.drawText("Winner: " + winner, canvas.getWidth() / 3, 5*canvas.getHeight() / 12, paint);



        paint.setTextSize(wid/30);
        canvas.drawText("Loser: "+ loser, canvas.getWidth()/2.5f, 7*canvas.getHeight() / 12, paint);


    }




    public String winner;
    public String loser;

    public void setWinner(String toSet){
        winner = toSet;
    }

    public void setLoser(String toSet){
        loser = toSet;
    }


}
