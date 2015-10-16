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
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Tyler on 10/10/2015.
 */
public class PlayingArea {

    private static final String PARAMETERS = "parameters";

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

    /**
     * First touch status
     */
    private Touch touch1 = new Touch();

    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();



    private String player1name;
    private String player2name;



    public void setPlayer1Name(String name){
        player1name=name;
    }


    public void setPlayer2Name(String name){
        player2name=name;
    }


    private Pipe pipeToAdd;
    public void setPipeToAdd(Bitmap pipe){pipeToAdd.setBitmap(pipe);}



    private boolean toAdd = false;
    public void setAddPipe(boolean on){
        toAdd = on;
        pipeToAdd.setX(0.5f);
        pipeToAdd.setY(0.5f);
    }



    private int gridSize = 5; //default to 5 to avoid divide by 0 error/
    public void setGridSize(int grid){
        if (grid == 0)
            gridSize = 5;
        else if(grid == 1)
            gridSize = 10;
        else
            gridSize = 20;
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



    /**
     * Percentage of the display width or height that
     * is occupied by the playing area.
     * since the button and the pipe select parts each take up 100dp a piece
     */
    final static float SCALE_IN_VIEW = 0.8f;


    public PlayingArea(Context context) {

        // Load the start pipes
        pipeStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeStraight = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeEnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.gauge);
        handle = BitmapFactory.decodeResource(context.getResources(), R.drawable.handle);
        pipeCap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cap);
        pipe90 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe90);
        pipeTee = BitmapFactory.decodeResource(context.getResources(), R.drawable.tee);




        pipeToAdd=new Pipe(context,1);
    }





    public void draw(Canvas canvas) {

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        width=wid;
        height=hit;

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? hit : wid;

        gridPix = (int) (minDim * SCALE_IN_VIEW); //that scale in view may only hold for the 7, need to check 4 and S



        //the ratio is used to ensure alignment of the pipe parts, disregarding the gaudge
        float ratio = ((float)pipeEnd.getHeight()) / ((float)pipeStart.getHeight());

        pipeStart = Bitmap.createScaledBitmap(pipeStart, wid / gridSize, hit / gridSize, false);

        int newHeight = (int)(ratio*pipeStart.getHeight());
        pipeEnd = Bitmap.createScaledBitmap(pipeEnd, wid/gridSize, newHeight, false);

        handle = Bitmap.createScaledBitmap(handle, wid/ gridSize,hit/gridSize, false);


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(42f);

        //pipeStart height to get below +42f for text size
        canvas.drawText(player1name, 0, pipeStart.getHeight() + 42f, paint);

        //2*hit/5 from where the start pipe is + pipeStart height to get below +42f for text size
        canvas.drawText(player2name, 0, 2 * hit / gridSize + pipeStart.getHeight() + 42f, paint);


        //Draw the start pipes
        canvas.drawBitmap(pipeStart, 0, 0, paint); //player 1
        canvas.drawBitmap(pipeStart, 0,2 * hit / gridSize, paint); //player 2




        //Draw the end pipes
        int diff = pipeStart.getHeight()-pipeEnd.getHeight(); //<the amount we need to adjust due to the gaudge

        canvas.drawBitmap(pipeEnd, wid- pipeEnd.getWidth(),  hit/gridSize+diff, paint);
        canvas.drawBitmap(pipeEnd, wid - pipeEnd.getWidth(),3*hit/gridSize+diff,  paint);


        //Draw the handles for the start pipes unrotated
        canvas.drawBitmap(handle, 0, 0, paint);
        canvas.drawBitmap(handle, 0,2*hit/gridSize, paint);





        //resize and draw the pipe we are adding
        if (toAdd == true && pipeToAdd.getBitmap() != null) {
            float x = pipeToAdd.getX();
            float y = pipeToAdd.getY();

            canvas.save();

            canvas.rotate(pipeToAdd.getAngle(), x * wid, y * hit );

            canvas.drawCircle(x*wid , y*hit, 10,paint);
            pipeToAdd.setBitmap(Bitmap.createScaledBitmap(pipeToAdd.getBitmap(), wid / gridSize, hit / gridSize, false));
            canvas.drawBitmap(pipeToAdd.getBitmap(), pipeToAdd.getX() * wid, pipeToAdd.getY() * hit, paint);
            canvas.restore();
        }

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
        if (x < 0 || x >= width || y < 0 || y >= height) {
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
     * The size of the grid in pixels
     */
    private int gridPix;


    /**
     * Get the positions for the two touches and put them
     * into the appropriate touch objects.
     * @param event the motion event
     */
    private void getPositions(MotionEvent event, View view) {
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            // Get coordinates
            float x = event.getX(i);
            float y = event.getY(i)-200;

            if(id == touch1.id) {
                touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
                touch2.copyToLast();
                touch2.x = x;
                touch2.y = y;
            }
        }

        view.invalidate();
    }

    /**
     * Handle a touch event from the view.
     *
     * @param view  The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        int id = event.getPointerId(event.getActionIndex());
        //
        // Convert an x,y location to a relative location in the
        // puzzle.
        //

       // float relX = (event.getX(0)) / width;
       // float relY = (event.getY(0)) / height;

        switch(event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(event, view);
                touch1.copyToLast();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:

                if(touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(event, view);
                    touch2.copyToLast();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touch1.id = -1;
                touch2.id = -1;
                view.invalidate();
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                if(id == touch2.id) {
                    touch2.id = -1;
                } else if(id == touch1.id) {
                    // Make what was touch2 now be touch1 by
                    // swapping the objects.
                    Touch t = touch1;
                    touch1 = touch2;
                    touch2 = t;
                    touch2.id = -1;
                }
                view.invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:


                if(pipeToAdd.getBitmap() != null) {
                    getPositions(event, view);
                    move();
                    //lastRelX = relX;
                    //lastRelY = relY;
                    view.invalidate();
                    return true;
                }

               // break;

        }


        return false;
    }







    /**
     * Handle a release of a touch message.
     * @param x x location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {
        if(pipeToAdd != null) {
           /* if(dragging.maybeSnap()) {
                // We have snapped into place
                view.invalidate();

                //Delete the piece being dragged and reinsert at the start to fix overlapping
                pieces.remove(pieces.indexOf(dragging));
                pieces.add(0,dragging);

                if(isDone()) {

                    pView=(PuzzleView)view; //<probably a better way to do this since casting isn't a good idea
                    isComplete=true; //<set true so we can draw the final image

                    // The puzzle is done
                    // Instantiate a dialog box builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(view.getContext());

                    ShuffleListener listener = new ShuffleListener();

                    // Parameterize the builder
                    builder.setTitle(R.string.hurrah);
                    builder.setMessage(R.string.completed_puzzle);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setNegativeButton(R.string.shuffle, listener);

                    // Create the dialog box and show it
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }*/
            //pipeToAdd = null;
            return true;
        }

        return false;
    }


    /**
     * Determine the angle for two touches
     * @param x1 Touch 1 x
     * @param y1 Touch 1 y
     * @param x2 Touch 2 x
     * @param y2 Touch 2 y
     * @return computed angle in degrees
     */
    private float angle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }


    /**
     * Move the puzzle piece by dx, dy

     */
    public void move() {


        if (touch1.id < 0){
            return;
        }

        if (touch1.id >= 0){
            //At least one touch
            //We are moving
            touch1.computeDeltas();

            pipeToAdd.setX(pipeToAdd.getX() + touch1.dX/width);
            pipeToAdd.setY(pipeToAdd.getY() + touch1.dY/height);
        }

        if(touch2.id >= 0) {
            // Two touches

            /*
             * Rotation
             */
            float angle1 = angle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float angle2 = angle(touch1.x, touch1.y, touch2.x, touch2.y);
            float da = (angle2 - angle1);

            rotate(da, touch1.x, touch1.y);
           // touch1.lastX = touch1.x;
           // touch1.lastY = touch1.y;
        }
    }


    /**
     * Rotate the image around the point x1, y1
     * @param dAngle Angle to rotate in degrees
     * @param x1 rotation point x
     * @param y1 rotation point y
     */
    public void rotate(float dAngle, float x1, float y1) {
        pipeToAdd.setAngle(dAngle + pipeToAdd.getAngle());

        // Compute the radians angle
        double rAngle = Math.toRadians(dAngle);
        float ca = (float) Math.cos(rAngle);
        float sa = (float) Math.sin(rAngle);
        float xp = (pipeToAdd.getX()*width - x1) * ca - (pipeToAdd.getY()*height - y1) * sa + x1;
        float yp = (pipeToAdd.getX()*width - x1) * sa + (pipeToAdd.getY()*height - y1) * ca + y1;

        pipeToAdd.setX(xp / width);
        pipeToAdd.setY(yp / height);
    }


    /**
     * Save the view state to a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to save to
     */
    public void putToBundle(String key, Bundle bundle ) {

        //iterate through the existing pipes urine
        for (Pipe[] pipeArray : pipes){
            for (Pipe pipe  : pipeArray){
                //pipe.putToBundle(PARAMETERS, outState);
            }
        }

    }

    /**
     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from
     */
    public void getFromBundle(String key, Bundle bundle) {


    }

    //////////////////////////////////////////////// NESTED CLASS touch ///////////////////////////

    /**
     * Local class to handle the touch status for one touch.
     * We will have one object of this type for each of the
     * two possible touches.
     */
    private class Touch {
        /**
         * Touch id
         */
        public int id = -1;

        /**
         * Current x location
         */
        public float x = 0;

        /**
         * Current y location
         */
        public float y = 0;

        /**
         * Previous x location
         */
        public float lastX = 0;

        /**
         * Previous y location
         */
        public float lastY = 0;

        /**
         * Change in x value from previous
         */
        public float dX = 0;

        /**
         * Change in y value from previous
         */
        public float dY = 0;

        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }

        /**
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }
    }



}
