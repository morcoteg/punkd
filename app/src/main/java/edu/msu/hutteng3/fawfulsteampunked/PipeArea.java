package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Tyler on 10/13/2015.
 */
public class PipeArea {

    /**
     * Pipe bitmaps
     */
    private Bitmap pipeStraight;
    private Bitmap pipeCap;
    private Bitmap pipe90;
    private Bitmap pipeTee;
    public Vector<Bitmap> order=new Vector<Bitmap>();

    public PipeArea(Context context) {


        // Load the start pipes
        pipeStraight = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeCap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cap);
        pipe90 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe90);
        pipeTee = BitmapFactory.decodeResource(context.getResources(), R.drawable.tee);



        order.setSize(5);
        order.add(0,pipeCap);
        order.add(1,pipe90);
        order.add(2,pipe90);
        order.add(3,pipeTee);
        order.add(4, pipeStraight);
    }



    private int cwidth;
    private int cheight;



    public void draw(Canvas canvas) {

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();


        //used for on touch
        cwidth=wid;
        cheight=hit;



        int orentation=Math.max(hit, wid);


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        if (discard && touchedPipePos!=-1) {
            discardPipe();
            discard = false; //neew to set to false because you can oly generate 1 new pipe at a time
            touchedPipePos =- 1;
        }

        for (int count = 0; count < 5; count++){
            Bitmap pipe = order.elementAt(count);
            if (pipe.sameAs(pipeStraight))
                drawStraightPipe(canvas,count, pipe,wid, hit, orentation, paint);
            else
                drawNotStraightPipe(canvas,count, pipe,wid, hit, orentation, paint);
        }




    }









    public void drawNotStraightPipe(Canvas canvas, int offset, Bitmap pipe,int wid, int hit, int orentation, Paint paint){

        pipe= Bitmap.createScaledBitmap(pipe, Math.max(wid/5,hit/5),Math.min(wid, hit), false);
        if (orentation==wid)
            canvas.drawBitmap(pipe, offset*wid/5, 0, paint);
        //we are in landscape
        else
            canvas.drawBitmap(pipe, 0, offset * hit / 5, paint);

    }


    public void drawStraightPipe(Canvas canvas, int offset, Bitmap pipe,int wid, int hit, int orentation, Paint paint){

        pipe = Bitmap.createScaledBitmap(pipe, Math.max(wid/5,hit/5),Math.min(wid, hit), false);
        canvas.save();
        if (orentation == wid)
            pipe = Bitmap.createScaledBitmap(pipe,hit, wid/5, false);//<need to resize due to rotation

        canvas.rotate(90, pipe.getWidth() / 2, pipe.getHeight() / 2);

        //we are in portrait
        if (orentation == wid) {
            //subtract (pipeStraight.getHeight()-hit)/2 due to the roatation and the translation done
            canvas.drawBitmap(pipe, -(pipe.getHeight() - hit) / 2, -offset * wid / 5 - (pipe.getHeight()-hit)/2, null);
        }
        //we are in landscape
        else
            canvas.drawBitmap(pipe,  offset * hit / 5,0, null);
        canvas.restore();
    }










    /**
     * Handle a touch event from the view.
     *
     * @param view  The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        //
        // Convert an x,y location to a relative location in the
        // puzzle.
        //

        float relX = (event.getX(0) )/ cwidth;
        float relY = (event.getY(0)) / cheight;

        return onTouched(relX, relY);


    }



    public Bitmap touchedPipe;
    public int touchedPipePos=-1;


    /**
     * Handle a touch message. This is when we get an initial touch
     *
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {

        // Check each piece to see if it has been hit
        // We do this in reverse order so we find the pieces in front

        float pressedLayout;

        if(cwidth> cheight)
            pressedLayout=x;
        else
            pressedLayout=y;

        if(pressedLayout<=0.2f)
            touchedPipePos=0;

        else if (pressedLayout>0.2f && pressedLayout<=0.4f)
            touchedPipePos=1;

        else if (pressedLayout>0.4f && pressedLayout<=0.6f)
            touchedPipePos=2;

        else if (pressedLayout>0.6f && pressedLayout<=0.8f)
            touchedPipePos=3;

        else if (pressedLayout>0.8f && pressedLayout<=1)
            touchedPipePos=4;


        touchedPipe = order.elementAt(touchedPipePos);

        if(!discard)
            touchedPipePos=-1;
        return true;
    }


    public Bitmap getTouched(){return touchedPipe;}



    public void setTouchedPipePos(int pos){touchedPipePos=pos;}

    public boolean discard=false;

    public void setDiscard(boolean disc){discard=disc;}


    public void discardPipe(){
        Random rand = new Random();
        int randPipe = rand.nextInt(5);



        Bitmap newPipe=order.elementAt(touchedPipePos);

        //put in a while to ensure the pipe thats generated is different from the one that was there
        while ((order.elementAt(touchedPipePos).sameAs(newPipe))) {
            if (randPipe == 0)
                newPipe = pipe90;
            else if (randPipe == 1)
                newPipe = pipeCap;
            else if (randPipe == 2)
                newPipe = pipeStraight;
            else
                newPipe = pipeTee;

            randPipe = rand.nextInt(5);

        }

        //set the new pipe to the old location
        order.set(touchedPipePos, newPipe);

    }



}