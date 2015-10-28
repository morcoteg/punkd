package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
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
    public PipeArea getPipeArea(){return pipeSelect;}


    private void init(AttributeSet attrs, int defStyle) {

        pipeSelect = new PipeArea(getContext());
        pipeSelect.setPipeSelectView(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pipeSelect.draw(canvas);

    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        pipeSelect.onTouchEvent(this, event);
        invalidate();

        return false;
    }


    public void setTouchedPipe(Bitmap selected){
        gameBoardView.setPipeToAdd(selected);
    }

    public void newTurn(){
        gameBoardView.newTurn();
    }

    public void setGameBoardView(GameBoardView view){gameBoardView=view;}

    private GameBoardView gameBoardView;

    public void setDiscard(boolean discard){pipeSelect.setDiscard(discard);}

    public void clear(){
        pipeSelect=null;
    }

}