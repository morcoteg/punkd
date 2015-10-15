package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


public class EndGameView extends View {

    public EndGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public EndGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EndGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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


        paint.setTextSize(48f);


        canvas.drawText("Winner: "+ winner, canvas.getWidth()/4, canvas.getHeight() / 6, paint);
        canvas.drawText("Loser: "+ loser, canvas.getWidth()/4, canvas.getHeight() / 2, paint);


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
