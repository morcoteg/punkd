package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class PipeSelectView extends View {
    public PipeSelectView(Context context) {
        super(context);
        init(null, 0);
    }

    public PipeSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PipeSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }



    private PipeArea pipeSelect;
    private void init(AttributeSet attrs, int defStyle) {

        pipeSelect = new PipeArea(getContext());

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pipeSelect.draw(canvas);

    }




    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        pipeSelect.onTouchEvent(this,event);
        gameBoardView.setPipeToAdd(pipeSelect.getTouched());
        invalidate();
        return false;
    }




    public void setGameBoardView(GameBoardView view){gameBoardView=view;};
    private GameBoardView gameBoardView;


    public void setDiscard(boolean discard){pipeSelect.setDiscard(discard);}

}