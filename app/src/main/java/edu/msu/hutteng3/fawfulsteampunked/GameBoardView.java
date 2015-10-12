package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


/**
 * Custom view class for our Puzzle.
 */
public class GameBoardView extends View {





    //Hi It's Gabe testing a commit :)




    /**
     * The actual puzzle
     */
    private PlayingArea playingArea;

    public GameBoardView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameBoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

         playingArea= new PlayingArea(getContext());

        playingArea.setPlayer1Nmae("Hope");
        // linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // linePaint.setColor(0xff008000);
        // linePaint.setStrokeWidth(3);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        playingArea.draw(canvas);
    }




    public void setPlayer1name(String name){
        playingArea.setPlayer1Nmae(name);
    }

    public void setPlayer2name(String name){
        playingArea.setPlayer2Nmae(name);
    }


    public void setScale(int scale){
        playingArea.setScale(scale);

    }


}