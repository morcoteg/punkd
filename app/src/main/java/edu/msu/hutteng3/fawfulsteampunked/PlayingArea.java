package edu.msu.hutteng3.fawfulsteampunked;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Tyler on 10/10/2015.
 */
public class PlayingArea {
    /**
     * Width of the playing area (integer number of cells)
     */
    private int width;

    /**
     * Height of the playing area (integer number of cells)
     */
    private int height;

    /**
     * Storage for the pipes
     * First level: X, second level Y
     */
    private Pipe [][] pipes;




    private String player1name;
    private String player2name;



    public void setPlayer1Name(String name){
        player1name=name;
    }


    public void setPlayer2Name(String name){
        player2name=name;
    }


    /**
     * Image drawing scale
     */
    private float imageScale = 1;



    public void setScale(int scale){
        imageScale=scale;
    }



    private int gridSize=5; //default to 5 to avoid divide by 0 error/
    public void setGridSize(int grid){
        if (grid==0)
            gridSize=5;
        else if(grid==1)
            gridSize=10;
        else
            gridSize=20;
    }

    /**
     * Pipe bitmaps
     */
    private Bitmap pipeStart; //<may be able to combine these into one
    private Bitmap pipeStraight;
    private Bitmap pipeEnd;
    private Bitmap handle;
    private Bitmap pipeCap;
    private Bitmap pipe90;
    private Bitmap pipeTee;




    public PlayingArea(Context context) {



        // Load the start pipes
        pipeStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeStraight = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeEnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.gauge);
        handle=BitmapFactory.decodeResource(context.getResources(), R.drawable.handle);
        pipeCap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cap);
        pipe90 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe90);
        pipeTee = BitmapFactory.decodeResource(context.getResources(), R.drawable.tee);


    }





    public void draw(Canvas canvas) {

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();


        //the ratio is used to ensure alignment of the pipe parts, disregarding the gaudge
        float ratio=((float)pipeEnd.getHeight())/((float)pipeStart.getHeight());


        pipeStart=Bitmap.createScaledBitmap(pipeStart, wid / gridSize, hit / gridSize, false);

        int newHeight=(int)(ratio*pipeStart.getHeight());



        pipeEnd=Bitmap.createScaledBitmap(pipeEnd, wid/gridSize, newHeight, false);


        handle=Bitmap.createScaledBitmap(handle, wid/ gridSize,hit/gridSize, false);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);


        paint.setTextSize(42f);

        //pipeStart height to get below +42f for text size
        canvas.drawText(player1name, 0, pipeStart.getHeight() + 42f, paint);

        //2*hit/5 from where the start pipe is + pipeStart height to get below +42f for text size
        canvas.drawText(player2name, 0, 2 * hit / gridSize + pipeStart.getHeight() + 42f, paint);

        canvas.drawText("touch screen to simulate turn,"
                , wid/4, hit/2, paint); //NEED TO TAKE OUT FOR FINAL

        canvas.drawText("then choose 'surrender' to go"
                , wid/4, hit/2+42f, paint); //NEED TO TAKE OUT FOR FINAL

        canvas.drawText("to end of game view."
                , wid/4, hit/2+84f, paint); //NEED TO TAKE OUT FOR FINAL


        //Draw the start pipes
        canvas.save();

       // canvas.translate(pipeStart.getHeight() / 2, pipeStart.getWidth() / 2);
       // canvas.rotate(90);
        //canvas.translate(-pipeStart.getHeight()/2, -pipeStart.getWidth()/2);
        //canvas.drawBitmap(pipeStart, 0, 0, paint);


        canvas.drawBitmap(pipeStart, 0, 0, paint);
        //canvas.drawBitmap(pipeStart, 0,hit / gridSize,  paint);//test
        canvas.drawBitmap(pipeStart, 0,2 * hit / gridSize, paint);
        //canvas.drawBitmap(pipeStart, 0,3 * hit / gridSize, paint); //test
        //canvas.drawBitmap(pipeStart, 0,4 * hit / gridSize,  paint); //test


        /* Tests to make sure starts and ends are alligned
        canvas.drawBitmap(pipeStart, pipeStart.getWidth(), 0, paint);
        canvas.drawBitmap(pipeStart, 2*pipeStart.getWidth(), 0, paint);
        canvas.drawBitmap(pipeStart,  3 * pipeStart.getWidth(),0, paint);
        canvas.drawBitmap(pipeStart,  4 * pipeStart.getWidth(),0, paint);
        canvas.drawBitmap(pipeStart,  5 * pipeStart.getWidth(),0, paint);
        canvas.drawBitmap(pipeStart,  6 * pipeStart.getWidth(),0, paint);
        canvas.drawBitmap(pipeStart,  7 * pipeStart.getWidth(),0, paint);
        canvas.drawBitmap(pipeStart,  8*pipeStart.getWidth(),0, paint);
*/
        canvas.restore();


        //Draw the end pipes
        canvas.save();
        int diff=pipeStart.getHeight()-pipeEnd.getHeight(); //<the amount we need to adjust due to the guadge
        //canvas.rotate(-90, pipeStart.getWidth() / 2, pipeStart.getHeight() / 2);

        //canvas.drawBitmap(pipeEnd, wid- pipeEnd.getWidth(), diff, paint); //test
        canvas.drawBitmap(pipeEnd, wid- pipeEnd.getWidth(),  hit/gridSize+diff, paint);
        //canvas.drawBitmap(pipeEnd,wid - pipeEnd.getWidth(), 2*hit/gridSize+diff, paint); //test
        canvas.drawBitmap(pipeEnd, wid - pipeEnd.getWidth(),3*hit/gridSize+diff,  paint);
        //canvas.drawBitmap(pipeEnd,wid - pipeEnd.getWidth(), 4*hit/gridSize+diff, paint); //test

        canvas.restore();


        //Draw the handles for the start pipes unrotated
        canvas.drawBitmap(handle, 0, 0, paint);
        //canvas.drawBitmap(handle, 0,hit/5, paint); //test
        canvas.drawBitmap(handle, 0,2*hit/gridSize, paint);
        //canvas.drawBitmap(handle, 0,3*hit/5, paint); //test
        //canvas.drawBitmap(handle, 0,4*hit/5, paint); //test


    }




    /**
     * Construct a playing area
     * @param width Width (integer number of cells)
     * @param height Height (integer number of cells)
     */
    public PlayingArea(int width, int height) {
        this.width = width;
        this.height = height;

        // Allocate the playing area
        // Java automatically initializes all of the locations to null
        pipes = new Pipe[width][height];



    }

    /**
     * Get the playing area height
     * @return Height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the playing area width
     * @return Width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the pipe at a given location.
     * This will return null if outside the playing area.
     * @param x X location
     * @param y Y location
     * @return Reference to Pipe object or null if none exists
     */
    public Pipe getPipe(int x, int y) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }

        return pipes[x][y];
    }

    /**
     * Add a pipe to the playing area
     * @param pipe Pipe to add
     * @param x X location
     * @param y Y location
     */
    public void add(Pipe pipe, int x, int y) {
        pipes[x][y] = pipe;
        pipe.set(this, x, y);
    }

    /**
     * Search to determine if this pipe has no leaks
     * @param start Starting pipe to search from
     * @return true if no leaks
     */
    public boolean search(Pipe start) {
        /*
         * Set the visited flags to false
         */
        for(Pipe[] row: pipes) {
            for(Pipe pipe : row) {
                if (pipe != null) {
                    pipe.setVisited(false);
                }
            }
        }

        /*
         * The pipe itself does the actual search
         */
        return start.search();
    }





    /**
     * Handle a touch event from the view.
     *
     * @param view  The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
 /*   public boolean onTouchEvent(View view, MotionEvent event) {

       //we'll need ths for move and place I think


        return false;
    }

*/




}
