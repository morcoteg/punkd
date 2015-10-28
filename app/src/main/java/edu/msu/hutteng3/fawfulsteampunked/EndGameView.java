package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class EndGameView extends View {



    private Bitmap pipe;
    private Bitmap steam;

    @SuppressWarnings("CanBeFinal")
    private Paint paint = new Paint();

    public EndGameView(Context context) {
        super(context);
        pipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        steam = BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);

        init(null, 0);
    }

    public EndGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        steam = BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);
        init(attrs, 0);
    }

    public EndGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        pipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        steam = BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);
        init(attrs, defStyle);
    }

    @SuppressWarnings("EmptyMethod")
    private void init(@SuppressWarnings("UnusedParameters") AttributeSet attrs, @SuppressWarnings("UnusedParameters") int defStyle) {


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);


        int wid = canvas.getWidth();
        int hit=canvas.getHeight();


        pipe = Bitmap.createScaledBitmap(pipe, Math.max(wid / 6, hit / 6), Math.min(wid/6, hit/6), false);
        steam = Bitmap.createScaledBitmap(steam, Math.max(wid / 6, hit / 6), Math.min(wid / 6, hit / 6), false);


        canvas.drawBitmap(pipe, 0, 15*canvas.getHeight() / 32 - pipe.getHeight(), paint);
        canvas.drawBitmap(steam, 0, 15*canvas.getHeight() / 32- 3*pipe.getHeight()/2, paint);

        canvas.drawBitmap(pipe, canvas.getWidth() - pipe.getWidth(),  15*canvas.getHeight() / 32 - pipe.getHeight(), paint);
        canvas.drawBitmap(steam, canvas.getWidth() - pipe.getWidth(),  15*canvas.getHeight() / 32 - 3*pipe.getHeight()/2, paint);




        paint.setTextSize(wid/20);
        canvas.drawText(getResources().getString(R.string.winner) + winner, canvas.getWidth() / 3, 5*canvas.getHeight() / 12, paint);



        paint.setTextSize(wid/30);
        canvas.drawText(getResources().getString(R.string.loser)+ loser, canvas.getWidth()/2.5f, 7*canvas.getHeight() / 12, paint);


    }




    private String winner;
    private String loser;

    public void setWinner(String toSet){
        winner = toSet;
    }

    public void setLoser(String toSet){
        loser = toSet;
    }


}
