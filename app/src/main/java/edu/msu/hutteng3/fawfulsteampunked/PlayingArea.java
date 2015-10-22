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

import java.io.Serializable;
import java.util.Vector;

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
     * The current parameters
     */
    private Parameters params = new Parameters();


    // public Vector<Vector<Pipe>> pipes=new Vector<Vector<Pipe>>();


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




    //there was another way to change a pipes connect but it is simpler to just store the ooriginals and set on rotate
    //private boolean pipeToAddOGNorth=false;
    //private boolean pipeToAddOGEast=false;
    //private boolean pipeToAddOGSouth=false;
    //private boolean pipeToAddOGWest=false;

    public void setPipeToAdd(Bitmap pipe){
        pipeToAdd.setBitmap(pipe);
        pipeToAdd.setAngle(0);//<keep position but reset angle

        //do the pip!=null error checks to avoid segfualts when its null
        if (pipe !=null) {
            //set the connects for the pipe we are adding
            if (pipe.sameAs(pipe90)) {
                pipeToAdd.setConnect(false, true, true, false);

                params.pipeToAddOGNorth = false;
                params.pipeToAddOGEast = true;
                params.pipeToAddOGSouth = true;
                params.pipeToAddOGWest = false;

            } else if (pipe.sameAs(pipeCap)) {
                pipeToAdd.setConnect(false, false, true, false);

                params.pipeToAddOGNorth = false;
                params.pipeToAddOGEast = false;
                params.pipeToAddOGSouth = true;
                params.pipeToAddOGWest = false;

            } else if (pipe.sameAs(pipeTee)) {
                pipeToAdd.setConnect(true, true, true, false);

                params.pipeToAddOGNorth = true;
                params.pipeToAddOGEast = true;
                params.pipeToAddOGSouth = true;
                params.pipeToAddOGWest = false;

            } else if (pipe.sameAs(pipeStraight)) {
                pipeToAdd.setConnect(false, true, false, true);

                params.pipeToAddOGNorth = false;
                params.pipeToAddOGEast = true;
                params.pipeToAddOGSouth = false;
                params.pipeToAddOGWest = true;

            }
        }

    }




    private boolean toAdd = true;
    //private boolean toAdd = false;
    public void setAddPipe(boolean on){
        toAdd = on;
        //pipeToAdd.setX(0.5f);
        // pipeToAdd.setY(0.5f);
    }



    private int gridSize = 5; //default to 5 to avoid divide by 0 error/
    public void setGridSize(int grid){
        if (grid == 0)
            gridSize = 5;
        else if(grid == 1)
            gridSize = 10;
        else
            gridSize = 20;

        pipes=new Pipe[gridSize][gridSize];
        pipes[0][0]=startP1;
        pipes[0][2]=startP2;
        pipes[gridSize-1][1]=endP1;
        pipes[gridSize-1][3]=endP2;
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
    private Bitmap leak;




    private Pipe startP1;
    private Pipe startP2;
    private Pipe endP1;
    private Pipe endP2;

    /*
     * Percentage of the display width or height that
     * is occupied by the playing area.
     * since the button and the pipe select parts each take up 100dp a piece
     */
    final static float SCALE_IN_VIEW = 0.8f;



    public int xMargin=0;
    public int yMargin=0;




    public PlayingArea(Context context) {

        // Load the start pipes
        pipeStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeStraight = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeEnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.gauge);
        handle = BitmapFactory.decodeResource(context.getResources(), R.drawable.handle);
        pipeCap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cap);
        pipe90 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe90);
        pipeTee = BitmapFactory.decodeResource(context.getResources(), R.drawable.tee);
        leak=BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);




        startP1= new Pipe(context,1);
        startP1.setBitmap(pipeStraight);
        startP1.setAngle(0);
        startP1.set(this, 0, 0);
        startP1.setX(0);
        startP1.setY(0);
        startP1.setConnect(false, true, false, false);


        startP2= new Pipe(context, 2);
        startP2.setBitmap(pipeStraight);
        startP2.setAngle(0);
        startP2.set(this, 0, 2);
        startP2.setX(0);
        startP2.setY(2 * gridSize / 5);
        startP2.setConnect(false, true, false, false);



        endP1= new Pipe(context, 3);
        endP1.setBitmap(pipeEnd);
        endP1.setAngle(0);
        endP1.set(this, gridSize - 1, 1);
        endP1.setConnect(false, false, false, true);


        endP2= new Pipe(context, 4);
        endP2.setBitmap(pipeEnd);
        endP2.setAngle(0);
        endP2.set(this, gridSize - 1, 3);
        endP2.setConnect(false, false,false,true);



        pipeToAdd=new Pipe(context, -1);
        pipeToAdd.setX(0.5f);
        pipeToAdd.setY(0.5f);







    }



    private int currentId=4;








    private boolean opened=false;
    private String openingPlayer;
    public void setOpened(boolean toOpen, String currentPlayer){
        opened=toOpen;
        openingPlayer=currentPlayer;
    }



    public void draw(Canvas canvas) {

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();



        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;


        width=wid ;
        height=hit ;


        if(minDim==wid){
            // yMargin=Math.abs(wid-hit)/2;
            hit=minDim;
            height=width;
        }
        else{
            // xMargin=Math.abs(wid-hit)/2;
            wid=hit;
            width=height;
        }


        gridPix = (int) (minDim * SCALE_IN_VIEW); //that scale in view may only hold for the 7, need to check 4 and S

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(42f);


        //pipeStart height to get below +42f for text size
        canvas.drawText(player1name, 0+xMargin, pipeStart.getHeight() + 42f+yMargin, paint);

        //2*hit/5 from where the start pipe is + pipeStart height to get below +42f for text size
        canvas.drawText(player2name, 0+xMargin, 2 * hit / gridSize + pipeStart.getHeight() + 42f+yMargin, paint);





        //the ratio is used to ensure alignment of the pipe parts, disregarding the gaudge
        float ratio = ((float)pipeEnd.getHeight()) / ((float)pipeStart.getHeight());

        pipeStart = Bitmap.createScaledBitmap(pipeStart, wid / gridSize, hit / gridSize, false);

        int newHeight = (int)(ratio*pipeStart.getHeight());
        pipeEnd = Bitmap.createScaledBitmap(pipeEnd, wid/gridSize, newHeight, false);
        pipes[gridSize-1][1].setBitmap(pipeEnd);

        pipes[gridSize-1][3].setBitmap(pipeEnd);





        int diff = pipeStart.getHeight()-pipeEnd.getHeight(); //<the amount we need to adjust due to the gaudge




        //draw the leaks if there are any
        if(leakAreas.size()!=0)
            drawLeaks(canvas, wid, hit, paint);

        //draw all the pipes in the grid
        for(int i=0; i<gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(pipes[i][j] != null) {
                    Pipe currPipe = pipes[i][j];

                    //speacial case for end pipe to add the diff
                    if (currPipe.getBitmap().sameAs(pipeEnd)) {
                        currPipe.setBitmap( Bitmap.createScaledBitmap(pipeEnd, wid/gridSize, newHeight, false));
                        canvas.drawBitmap(currPipe.getBitmap(), wid - pipeEnd.getWidth()+xMargin, j * hit / gridSize + diff+yMargin, paint);
                    }
                    else {
                        canvas.save();

                        float x = currPipe.getX();
                        float y = currPipe.getY();
                        canvas.rotate(currPipe.getAngle(), x * wid + currPipe.getBitmap().getWidth() / 2, y * hit + currPipe.getBitmap().getHeight() / 2);


                        currPipe.setBitmap(Bitmap.createScaledBitmap(currPipe.getBitmap(), wid / gridSize, hit / gridSize, false));
                        canvas.drawBitmap(currPipe.getBitmap(), i * wid / gridSize+xMargin, j * hit / gridSize+yMargin, paint);
                        canvas.restore();
                    }
                }
            }
        }



        //draws tha handles and their positions
        handle = Bitmap.createScaledBitmap(handle, wid/ gridSize,hit/gridSize, false);
        if (opened) {
            //Draw a rotated hadle for P1 and an unrotated for P2
            if(openingPlayer==player1name) {
                canvas.save();
                canvas.rotate(-90, handle.getHeight() / 2+yMargin, handle.getWidth() / 2);
                canvas.drawBitmap(handle, 0, 0+xMargin, paint);
                canvas.restore();
                canvas.drawBitmap(handle, 0+xMargin, 2 * hit / gridSize+yMargin, paint);
            }
            //Draw a rotated hadle for P2 and an unrotated for P1
            else {
                canvas.save();
                canvas.rotate(-90, handle.getHeight() / 2 + yMargin, handle.getWidth() / 2 + 2 * hit / gridSize + xMargin);
                canvas.drawCircle(0, 2 *( hit +xMargin)/ gridSize+xMargin,10,paint);
                canvas.drawBitmap(handle, 0, 2 * (hit+xMargin) / gridSize+xMargin, paint);
                canvas.restore();
                canvas.drawBitmap(handle, 0+xMargin, 0+yMargin, paint);

            }

        }
        else {
            //Draw the handles for the start pipes unrotated
            canvas.drawBitmap(handle, 0+xMargin, 0+yMargin, paint);
            canvas.drawBitmap(handle, 0+xMargin, 2 * hit / gridSize+yMargin, paint);
        }







        //resize and draw the pipe we are adding
        if (toAdd == true && pipeToAdd.getBitmap() != null) {
            float x = pipeToAdd.getX();
            float y = pipeToAdd.getY();

            //update params PipeToAdd
            params.pipeToAdd = pipeToAdd;

            canvas.save();

            canvas.rotate(pipeToAdd.getAngle(), x * wid + pipeToAdd.getBitmap().getWidth() / 2, y * hit + pipeToAdd.getBitmap().getHeight() / 2);

            canvas.drawCircle(x * wid, y * hit, 10, paint);
            canvas.drawCircle( x * wid+pipeToAdd.getBitmap().getWidth()/2, y * hit +pipeToAdd.getBitmap().getHeight()/2,10,paint);

            pipeToAdd.setBitmap(Bitmap.createScaledBitmap(pipeToAdd.getBitmap(), wid / gridSize, hit / gridSize, false));
            canvas.drawBitmap(pipeToAdd.getBitmap(), pipeToAdd.getX() * wid+xMargin, pipeToAdd.getY() * hit+yMargin, paint);
            canvas.restore();
        }

    }




    public void drawLeaks(Canvas canvas, int wid, int hit, Paint paint){
        //draw the leaks after the valve i open if any are found

        leak = Bitmap.createScaledBitmap(leak, wid / gridSize, hit / gridSize, false);

        for (int i = 0; i < leakAreas.size(); i+=2){
            int xLeakInd=leakAreas.elementAt(i);
            int yLeakInd=leakAreas.elementAt(i + 1);

            //the leak is coming from the bottom so draw normally
            //first error check is for out of index
            //second is to make sure the pipe we try to work with isnt null
            if(yLeakInd!=4&&pipes[xLeakInd][yLeakInd+1]!=null &&pipes[xLeakInd][yLeakInd +1].getNorth())
                canvas.drawBitmap(leak, xLeakInd*wid/gridSize, yLeakInd*hit/gridSize, paint);

                //the leak is coming from above
                // first error check is for out of index
                // second is to make sure the pipe we try to work with isnt null
            else if(yLeakInd!=0&&pipes[xLeakInd][yLeakInd-1]!=null &&pipes[xLeakInd][yLeakInd-1].getSouth()) {
                canvas.save();

                canvas.rotate(180,xLeakInd * wid / gridSize+leak.getWidth()/2, yLeakInd * hit / gridSize+leak.getHeight()/2);

                canvas.drawBitmap(leak, xLeakInd*wid/gridSize, yLeakInd*hit/gridSize, paint);
                canvas.restore();

            }

            //the leak is coming from the left
            //first error check is for out of index
            //second is to make sure the pipe we try to work with isnt null
            else if(xLeakInd!=0&&pipes[xLeakInd-1][yLeakInd]!=null &&pipes[xLeakInd-1][yLeakInd].getEast()) {
                canvas.save();

                canvas.rotate(90, 0, 0);
                leak = Bitmap.createScaledBitmap(leak, wid / gridSize, hit / gridSize, false);
                canvas.drawBitmap(leak, yLeakInd*hit/gridSize, -xLeakInd*wid/gridSize-pipeStart.getWidth(), paint);
                canvas.restore();

            }



            //the leak is coming from the right
            //first error check is for out of index
            //second is to make sure the pipe we try to work with isnt null
            else if(xLeakInd!=5&&pipes[xLeakInd+1][yLeakInd]!=null &&pipes[xLeakInd+1][yLeakInd].getWest()) {
                canvas.save();

                canvas.rotate(-90, 0, 0);
                leak = Bitmap.createScaledBitmap(leak, wid / gridSize, hit / gridSize, false);
                canvas.drawBitmap(leak, -yLeakInd*hit/gridSize-pipeStart.getHeight(), xLeakInd*wid/gridSize, paint);
                canvas.restore();

            }


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
        if (x < 0 || x > gridSize || y < 0 || y > gridSize) {
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
                //snap();
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
                if(pipeToAdd.getBitmap() !=null)
                    snapPipeAngle();
                view.invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:


                if(pipeToAdd.getBitmap() != null ) {
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


    public void snapPipeAngle(){

        float currAngle=pipeToAdd.getAngle();

        if (currAngle<0)
            currAngle+=360;

        if(currAngle>360)
            currAngle-=360;

        float normalizedAngle=currAngle%90;




        if (normalizedAngle >= 45)
            pipeToAdd.setAngle(currAngle + (90 - normalizedAngle));
        else if (normalizedAngle < 45)
            pipeToAdd.setAngle(currAngle - normalizedAngle);



        if(pipeToAdd.getAngle()==270)
            pipeToAdd.setConnect(params.pipeToAddOGEast,params.pipeToAddOGSouth,
                    params.pipeToAddOGWest,params.pipeToAddOGNorth);

        else if(pipeToAdd.getAngle()==180)
            pipeToAdd.setConnect(params.pipeToAddOGSouth, params.pipeToAddOGWest,
                    params.pipeToAddOGNorth, params.pipeToAddOGEast);

        else if(pipeToAdd.getAngle()==90)
            pipeToAdd.setConnect(params.pipeToAddOGWest,params.pipeToAddOGNorth,
                    params.pipeToAddOGEast,params.pipeToAddOGSouth);
        else if(pipeToAdd.getAngle()==0)
            pipeToAdd.setConnect(params.pipeToAddOGNorth,params.pipeToAddOGEast
                    ,params.pipeToAddOGSouth,params.pipeToAddOGWest);

    }




    public boolean addToGrid(Context context,View view){
        if(pipeToAdd.getBitmap() !=null) {
            snap();
            view.invalidate();


            int xInd = (int)(pipeToAdd.getX() / (1 / (float)gridSize));
            int yInd = (int)(pipeToAdd.getY() / (1 / (float)gridSize));

            if (pipes[xInd][yInd] !=null){

                Toast toast = Toast.makeText(context, R.string.badMove, Toast.LENGTH_LONG);
                toast.show();
                return false;
            }


            if(!checkNeighbors(xInd, yInd)){
                Toast toast = Toast.makeText(context, R.string.badMove, Toast.LENGTH_LONG);
                toast.show();
                return false;


            }

            currentId+=1;
            pipes[xInd][yInd]=new Pipe(context,currentId);
            pipes[xInd][yInd].setBitmap(pipeToAdd.getBitmap());
            pipes[xInd][yInd].setX(pipeToAdd.getX());
            pipes[xInd][yInd].setY(pipeToAdd.getY());
            pipes[xInd][yInd].setAngle(pipeToAdd.getAngle());
            pipes[xInd][yInd].set(this, xInd, yInd);


            //saving the added pipe to params.pipes for Bundles          //////////////////////////////////////DONT FORGET THIS SHIT TODO
            //params.pipes[xInd][yInd]=new Pipe(context,currentId);
            //params.pipes[xInd][yInd].setBitmap(pipeToAdd.getBitmap());
            //params.pipes[xInd][yInd].setX(pipeToAdd.getX());
            //params.pipes[xInd][yInd].setY(pipeToAdd.getY());
            //params.pipes[xInd][yInd].setAngle(pipeToAdd.getAngle());
            //params.pipes[xInd][yInd].set(this, xInd, yInd);              ///////////////////////////////////////////////////////////

            pipes[xInd][yInd].setConnect(pipeToAdd.getNorth(), pipeToAdd.getEast(),
                    pipeToAdd.getSouth(),pipeToAdd.getWest());

            params.pipes=pipes;

            pipeToAdd.setBitmap(null);
            pipeToAdd.setX(0.5f);
            pipeToAdd.setY(0.5f);
            pipeToAdd.setAngle(0);




            return true;
        }
        return false;
    }





    public boolean checkNeighbors(int xInd, int yInd){


        Pipe northNeighbor=null;
        Pipe eastNeighbor=null;
        Pipe southNeighbor=null;
        Pipe westNeighbor=null;


        //get the four neighbors if they exist
        if(yInd !=0)
            northNeighbor = pipes[xInd][yInd - 1];
        if(xInd !=gridSize-1)
            eastNeighbor=pipes[xInd+1][yInd];
        if(yInd !=gridSize-1)
            southNeighbor=pipes[xInd][yInd+1];
        if(xInd !=0)
            westNeighbor=pipes[xInd-1][yInd];



        if(northNeighbor!=null &&pipeToAdd.getNorth()&&northNeighbor.getSouth())
            return true;
        else if(eastNeighbor!=null &&pipeToAdd.getEast()&& eastNeighbor.getWest())
            return true;
        else if (southNeighbor!=null &&pipeToAdd.getSouth()&&southNeighbor.getNorth())
            return true;
        else if(westNeighbor!=null &&pipeToAdd.getWest()&&westNeighbor.getEast())
            return true;


        return false;
    }






    public void snap(){

        float x=pipeToAdd.getX();
        float y=pipeToAdd.getY();

        float relGridSize=1/((float)gridSize);

        float xTest=x%relGridSize;
        float yTest=y%relGridSize;


        float xOffset=((float)xMargin)/((float)width);
        float yOffset=((float)yMargin)/((float)height);

        if(xTest >=relGridSize/2)
            pipeToAdd.setX(x+(relGridSize-xTest)-xOffset);
        else
            pipeToAdd.setX(x-xTest-xOffset);

        if(yTest >= relGridSize / 2)
            pipeToAdd.setY(y + (relGridSize - yTest) - yOffset);
        else
            pipeToAdd.setY(y - yTest - yOffset);


        //from this point on are error checks for out of bounds
        if(pipeToAdd.getX()>=1.0f)
            pipeToAdd.setX(0.8f);

        if(pipeToAdd.getY()>=1.0f)
            pipeToAdd.setY(0.8f);
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

            if(pipeToAdd.hit(touch1.x, touch1.y,gridPix,1,width, height)) {
                pipeToAdd.setX(pipeToAdd.getX() + touch1.dX / width);
                pipeToAdd.setY(pipeToAdd.getY() + touch1.dY / height);
            }
        }

        if(touch2.id >= 0) {
            // Two touches

            /*
             * Rotation
             */
            float angle1 = angle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float angle2 = angle(touch1.x, touch1.y, touch2.x, touch2.y);
            float da = (angle2 - angle1);

            // if(pipeToAdd.hit(touch1.x, touch1.y,gridPix,1,width,height))
            rotate(da, touch1.x, touch1.y);

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






    public boolean checkForLeaks(String player){

        if(player==player1name) {
            searchForAllLeaks(pipes[0][0]);
            return search(pipes[0][0]);
        }

        else {
            searchForAllLeaks(pipes[0][2]);
            return search(pipes[0][2]);
        }
    }









    private Vector<Integer> leakAreas=new Vector<Integer>();
    public Vector<Integer> getLeakArea(){return leakAreas;}



    /**
     * Search to determine if this pipe has no leaks
     * @param start Starting pipe to search from
     * @return true if no leaks
     */
    public boolean searchForAllLeaks(Pipe start) {
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

        boolean found= start.searchForAllLeaks();
        //params.leakAreas=leakAreas;
        return found;

    }













    /**
     * Save the view state to a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to save to
     */
    public void putToBundle(String key, Bundle bundle ) {

        bundle.putSerializable(key, params);

    }

    /**
     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from
     */
    public void getFromBundle(String key, Bundle bundle) {
        params = (Parameters)bundle.getSerializable(key);

        if(params.pipeToAdd!=null)
             pipeToAdd = params.pipeToAdd;
        if(params.pipes!=null)
             pipes = params.pipes;
        //leakAreas =params.leakAreas;
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


/////////////////////////////////////////// NESTED CLASS parameters ///////////////////////

    /**
     * Parameters class for the Pipe's coordinates x, y and the rotation angle
     */
    private static class Parameters implements Serializable {



        /**
         * Storage for the pipes
         * First level: X, second level Y
         */
        public Pipe [][] pipes;

        public Pipe pipeToAdd;



        public boolean pipeToAddOGNorth=false;
        public boolean pipeToAddOGEast=false;
        public boolean pipeToAddOGSouth=false;
        public boolean pipeToAddOGWest=false;


        //Vector<Integer> leakAreas;


    }
}
