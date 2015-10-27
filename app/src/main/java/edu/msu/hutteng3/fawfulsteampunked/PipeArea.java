package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
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
    public Vector<Bitmap> order= new Vector<>();

    public PipeArea(Context context) {

        // Load the start pipes
        pipeStraight = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeCap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cap);
        pipe90 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe90);
        pipeTee = BitmapFactory.decodeResource(context.getResources(), R.drawable.tee);


        order.setSize(5);
        params.order.setSize(5);

        order.set(0, pipeCap);
        order.set(1, pipe90);
        order.set(2, pipe90);
        order.set(3, pipeTee);
        order.set(4, pipeStraight);
    }



    private int cWidth;
    private int cHeight;
    private static final int margin=20;


    public void draw(Canvas canvas) {

        int wid = canvas.getWidth()-margin;
        int hit = canvas.getHeight()-margin;

        int orientation=Math.max(hit, wid);

        //used for on touch, the real canvas size )may need to remove the argin addition, needs to be tested on the 4 and S
        cWidth=wid+margin;
        cHeight=hit+margin;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        if (params.discard && touchedPipePos!=-1) {
            discardPipe();
            params.discard = false; //need to set to false because you can oly generate 1 new pipe at a time
            touchedPipePos =- 1;
        }

        for (int count = 0; count < 5; count++){
            Bitmap pipe = order.elementAt(count);
            if (pipe.sameAs(pipeStraight))
                drawStraightPipe(canvas,count, pipe,wid, hit, orientation);
            else
                drawNotStraightPipe(canvas,count, pipe,wid, hit, orientation);
        }




    }









    public void drawNotStraightPipe(Canvas canvas, int offset, Bitmap pipe,int wid, int hit, int orentation) {

        pipe= Bitmap.createScaledBitmap(pipe, Math.max(wid/5,hit/5),Math.min(wid, hit), false);
        if (orentation==wid)
            canvas.drawBitmap(pipe, offset*wid/5, margin/2, null);
        //we are in landscape
        else
            canvas.drawBitmap(pipe, margin/2, offset * hit / 5, null);

    }


    public void drawStraightPipe(Canvas canvas, int offset, Bitmap pipe,int wid, int hit, int orentation){

        pipe = Bitmap.createScaledBitmap(pipe, Math.max(wid/5,hit/5),Math.min(wid, hit), false);
        canvas.save();
        if (orentation == wid)
            pipe = Bitmap.createScaledBitmap(pipe,hit, wid/5, false);//<need to resize due to rotation

        canvas.rotate(90, pipe.getWidth() / 2, pipe.getHeight() / 2);

        //we are in portrait
        if (orentation == wid) {
            //subtract (pipeStraight.getHeight()-hit)/2 due to the rotation and the translation done
            canvas.drawBitmap(pipe, -(pipe.getHeight() - hit) / 2 + margin/2, -offset * wid / 5 - (pipe.getHeight()-hit)/2, null);
        }
        //we are in landscape
        else
            canvas.drawBitmap(pipe,  offset * hit / 5, -margin/2, null);
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

        float relX = (event.getX(0) )/ cWidth;
        float relY = (event.getY(0)) / cHeight;

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

        if(cWidth> cHeight)
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

        if(!params.discard) {
            touchedPipePos = -1;
            pipeSelect.setTouchedPipe(touchedPipe);
        }
        else
            pipeSelect.setTouchedPipe(null);
        return true;
    }



    public void setDiscard(boolean disc){params.discard=disc;}


    public void discardPipe(){
        Random rand = new Random();
        int randPipe = rand.nextInt(5);



        Bitmap newPipe=order.elementAt(touchedPipePos);

        //put in a while to ensure the pipe that's generated is different from the one that was there
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
        pipeSelect.newTurn();
    }


    private PipeSelectView pipeSelect;
    public void setPipeSelectView(PipeSelectView toSet){pipeSelect=toSet;}


    /**
     * The current parameters
     */
    private Parameters params = new Parameters();


    /**
     * Save the view state to a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to save to
     */
    public void putToBundle(String key, Bundle bundle ) {

        for (int i=0;i<order.size();i++) {
            int id=-1;
            if(order.elementAt(i).sameAs(pipe90))
                id=1;
            else if(order.elementAt(i).sameAs(pipeCap))
                id=2;
            else if(order.elementAt(i).sameAs(pipeStraight))
                id=3;
            else if(order.elementAt(i).sameAs(pipeTee))
                id=4;

            if(id!=-1)
                params.order.set(i,id) ;
        }

        bundle.putSerializable(key, params);

    }

    /**
     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from
     */
    public void getFromBundle(String key, Bundle bundle) {
        params = (Parameters) bundle.getSerializable(key);

        assert params != null;
        if (params.order != null) {
            for(int i=0; i<params.order.size();i++)
            {
                if(params.order.elementAt(i).equals(1))
                    order.set(i, pipe90);
                else if(params.order.elementAt(i).equals(2))
                    order.set(i, pipeCap);
                else if(params.order.elementAt(i).equals(3))
                    order.set(i, pipeStraight);
                else if(params.order.elementAt(i).equals(4))
                    order.set(i, pipeTee);

            }

        }

    }


/////////////////////////////////////////// NESTED CLASS parameters ///////////////////////

    /**
     * Parameters class for the Pipe's coordinates x, y and the rotation angle
     */
    private static class Parameters implements Serializable {

        public boolean discard=false;

        public Vector<Integer> order= new Vector<>();
    }


}