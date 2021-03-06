package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Custom view class for our Puzzle.
 */
public class GameBoardView extends View {


    //Hi It's Gabe testing a commit :)


    /**
     * The actual grid
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

    private void init(@SuppressWarnings("UnusedParameters") AttributeSet attrs, @SuppressWarnings("UnusedParameters") int defStyle) {
        playingArea= new PlayingArea(getContext());
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        invalidate();
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
       // GameBoard host = (GameBoard) this.getContext();

        return playingArea.onTouchEvent(this,event);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        playingArea.draw(canvas);

       // invalidate();
    }



    /* Basically all the functions from here down act as a gateway from the activity to the playing area*/
    public void newTurn(){
        GameBoard gameBoard = (GameBoard) this.getContext();
        gameBoard.switchTurn();
    }


    public void setAddPipe(boolean on){playingArea.setAddPipe(on);}

    public void setPipeToAdd(Bitmap pipe){
        playingArea.setPipeToAdd(pipe);
        invalidate();
    }

    public void setPlayer1name(String name){
        playingArea.setPlayer1Name(name);
    }

    public void setPlayer2name(String name){
        playingArea.setPlayer2Name(name);
    }

    public void setCurrentPlayer(String player){playingArea.setCurrentPlayer(player);}

    public void setGridSize(int scale){
        playingArea.setGridSize(scale);
    }

    public PlayingArea getPlayingArea(){return playingArea;}

    public void setOpened(@SuppressWarnings("SameParameterValue") boolean toOpen){playingArea.setOpened(toOpen);}

    public boolean addToGrid(Context context){return playingArea.addToGrid(context,this);}

    public boolean checkForLeaks(String player){return playingArea.checkForLeaks(player);}

    public void clear(){
        playingArea=null;
    }


    public void setWon(@SuppressWarnings("SameParameterValue") boolean won){playingArea.setWon(won);}



   }
