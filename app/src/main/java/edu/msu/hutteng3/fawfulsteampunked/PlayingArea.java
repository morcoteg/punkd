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






    public PlayingArea(Context context) {



        // Load the start pipes
        pipeStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        //pipeStartp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeEndp1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.gauge);
        pipeEndp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.gauge);
        handle=BitmapFactory.decodeResource(context.getResources(), R.drawable.handle);


        //Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
       // profileImage.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));





    }





    public void draw(Canvas canvas) {

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;
/*
        puzzleSize = (int) (minDim * SCALE_IN_VIEW);

        // Compute the margins so we center the puzzle
        marginX = (wid - puzzleSize) / 2;
        marginY = (hit - puzzleSize) / 2;

        //
        // Draw the outline of the puzzle
        //

        //draw the outline square first
        fillPaint.setColor(Color.rgb(51, 146, 51)); //rgb take int values for a red, green, and blue component to make the dark green
        canvas.drawRect(marginX - 5, marginY - 5, marginX + puzzleSize + 5, marginY + puzzleSize + 5, fillPaint);


        //change paint color to grey and create the puzzle area
        fillPaint.setColor(0xffcccccc);
        canvas.drawRect(marginX, marginY, marginX + puzzleSize, marginY + puzzleSize, fillPaint);


        scaleFactor = (float) puzzleSize / (float) puzzleComplete.getWidth();

        canvas.save();
        canvas.translate(marginX, marginY);
        canvas.scale(scaleFactor, scaleFactor);

        //If the puzzle is solved, display the final image and not the pieces
        if (isComplete)
            canvas.drawBitmap(puzzleComplete, 0, 0, null);


        canvas.restore();
        if(!isComplete) {
            for (PuzzlePiece piece : pieces) {
                piece.draw(canvas, marginX, marginY, puzzleSize, scaleFactor);
            }
        }

        */



        pipeStart=getScaledBitmap(pipeStart, canvas.getWidth()/10, canvas.getHeight()/10);



        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);


        paint.setTextSize(48f);

        canvas.drawText(player1name, 0, canvas.getHeight() / 6, paint);
        canvas.drawText(player2name, 0, canvas.getHeight() / 2, paint);



       // canvas.drawCircle(0,0,5,paint);
        //this works on the 7 in portrait, looks really bad in landscape
        //due to all the hardcoded ratios being there for testing to ake sure rotate was working
        canvas.save();
        canvas.translate(pipeStart.getWidth() / 2, pipeStart.getHeight() / 2);
        canvas.rotate(-90);
        canvas.drawBitmap(pipeStart, -pipeStart.getHeight() /4, -pipeStart.getWidth() / 2, paint);
       // canvas.drawBitmap(pipeStartp2, -9*hit / 16, -pipeStartp1.getHeight() / 2, paint);

        canvas.translate(-pipeStart.getWidth() / 2, -pipeStart.getHeight() / 2);
        canvas.restore();


        canvas.save();
        canvas.translate(pipeStart.getWidth() / 2, pipeStart.getHeight() / 2);
        canvas.rotate(-90);
        //canvas.drawBitmap(pipeStartp1,-pipeStartp1.getHeight() / 2, -pipeStartp1.getWidth() / 2, paint);
        // canvas.drawBitmap(pipeStartp2, -9*hit / 16, -pipeStartp1.getHeight() / 2, paint);

        canvas.translate(-pipeStart.getWidth() / 2, -pipeStart.getHeight() / 2);
        canvas.restore();



       // canvas.drawBitmap(pipeEndp2,- 11*wid/16, pipeStartp1.getHeight()*4f, paint);



       // canvas.drawCircle(0,0,5,paint);




       // canvas.drawBitmap(handle, 0, 3*hit / 16, paint);

      //  canvas.drawBitmap(handle, 0,hit / 2, paint);



    }


    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight)
    {
        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = reqWidth;
        int nHeight = reqHeight;

       // float parentRatio = (float) reqHeight / reqWidth;

      //  nHeight = bHeight;
        //nWidth = (int) (reqWidth * parentRatio);
       // nWidth = bWidth;


        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true);
    }






    /**
     * Pipe bitmaps
     */
    private Bitmap pipeStart; //<may be able to combine these into one of each
    //private Bitmap pipeStartp2;
    private Bitmap pipeEndp1;
    private Bitmap pipeEndp2;
    private Bitmap handle;


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

       //we'll neeed ths for move and place I think



        return false;
    }

*/





}
