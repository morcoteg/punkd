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
        if (toAdd == true && pipeToAdd != null) {
            float x=pipeToAdd.getX();
            float y=pipeToAdd.getY();

            pipeToAdd.setBitmap(Bitmap.createScaledBitmap(pipeToAdd.getBitmap(), wid / gridSize, hit / gridSize, false));
            canvas.drawBitmap(pipeToAdd.getBitmap(), pipeToAdd.getX()*wid, pipeToAdd.getY()*hit, paint);
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
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    /**
     * The size of the grid in pixels
     */
    private int gridPix;



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

        float relX = (event.getX(0)) / width;
        float relY = (event.getY(0)) / height;

        switch(event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return onReleased(view, relX, relY);

            case MotionEvent.ACTION_MOVE:

                if(pipeToAdd != null) {

                    pipeToAdd.move(relX - lastRelX, relY - lastRelY);
                    lastRelX = relX;
                    lastRelY = relY;
                    view.invalidate();
                    return true;
                }
                break;
        }


        return false;
    }




    /**
     * Handle a touch message. This is when we get an initial touch
     *
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {

        float scaleFactor=(float)Math.pow(gridSize,2);
        if (pipeToAdd.getBitmap() !=null){
            if(pipeToAdd.hit(x, y, gridPix, 1)) {
                // We hit a pipe
                lastRelX = x;
                lastRelY = y;
                return true;
           }

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




}
