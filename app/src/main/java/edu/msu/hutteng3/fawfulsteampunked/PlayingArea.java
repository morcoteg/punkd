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
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Tyler on 10/10/2015.
 * The area which stores the current pipes and the pipe we are adding (if there is one).
 */
@SuppressWarnings({"ALL", "SuspiciousNameCombination"})
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

    /**
     * The current parameters
     */
    private Parameters params = new Parameters();

    /*
     * If we are currently rotating the pipeToAdd or not
     */
    private boolean rotating=false;

    /**
     * First touch status
     */
    private Touch touch1 = new Touch();

    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();


    /**
     * the current players names
     */
    private String player1name;
    private String player2name;



    /**
     * the pipe which was selected from the queue
     */
    private Pipe pipeToAdd;


    /**
     * the indexes of the start pipes for each player, will be set based on gridSize
     */
    private int p1StartIndex=0;
    private int p2StartIndex=1;



    /**
     * Pipe bitmaps
     */
    private Bitmap pipeStraight;
    private Bitmap pipeEnd;
    private Bitmap handle;
    private Bitmap pipeCap;
    private Bitmap pipe90;
    private Bitmap pipeTee;
    private Bitmap leak;



    /**
     * starting and ending pipes
     */
    private Pipe startP1;
    private Pipe startP2;
    private Pipe endP1;
    private Pipe endP2;


    /**
     * the space which the playing area has been moved
     */
    public int xMargin=0;
    public int yMargin=0;

    /**
     * the chosen grid size
     */
    private int gridSize = 5; //default to 5 to avoid divide by 0 error

    /**
     * the locations of any found leaks
     */
    private Vector<Integer> leakAreas= new Vector<>();



    /* All of the getters and setters that have no extra purpose*/
    public Vector<Integer> getLeakArea(){return leakAreas;}
    public void setAddPipe(boolean on){params.toAdd = on;}
    public void setWon(boolean won){params.won=won;}
    public void setCurrentPlayer(String player){params.currentPlayer=player;}
    public void setOpened(boolean toOpen){params.opened=toOpen;}
    public void setPlayer1Name(String name){player1name=name;}
    public void setPlayer2Name(String name){player2name=name;}
    public int getGridSize(){return gridSize;}





    public Vector getPipeGrid(){

        Vector<Integer> pipeIds=new Vector<>();


        for(int i=0; i<gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(pipes[i][j] != null) {
                    pipeIds.add(pipes[i][j].getID()+((int)pipes[i][j].getAngle()));
                }
                else
                    pipeIds.add(9);
            }
        }

        return pipeIds;

    }





    /**
     * Set the pipe to adds bitmap,and with that set the connections and the placing player
     * @param pipe The pipe image we will be giving to pipeToAdd
     */
    public void setPipeToAdd(Bitmap pipe){
        pipeToAdd.setBitmap(pipe);
        pipeToAdd.setAngle(0);//<keep position but reset angle
        pipeToAdd.setStartingAngle(0);

        //do the pipe!=null error checks to avoid segfaults when its null
        if (pipe !=null) {
            //set the connects for the pipe we are adding
            if (pipe.sameAs(pipe90)) {
                pipeToAdd.setConnect(false, true, true, false);

                params.pipeToAddOGNorth = false;
                params.pipeToAddOGEast = true;
                params.pipeToAddOGSouth = true;
                params.pipeToAddOGWest = false;

                pipeToAdd.setID(1);

            } else if (pipe.sameAs(pipeCap)) {
                pipeToAdd.setConnect(false, false, true, false);

                params.pipeToAddOGNorth = false;
                params.pipeToAddOGEast = false;
                params.pipeToAddOGSouth = true;
                params.pipeToAddOGWest = false;

                pipeToAdd.setID(2);

            } else if (pipe.sameAs(pipeTee)) {
                pipeToAdd.setConnect(true, true, true, false);

                params.pipeToAddOGNorth = true;
                params.pipeToAddOGEast = true;
                params.pipeToAddOGSouth = true;
                params.pipeToAddOGWest = false;

                pipeToAdd.setID(4);

            }
            else{
                pipeToAdd.setConnect(false, true, false, true);

                params.pipeToAddOGNorth = false;
                params.pipeToAddOGEast = true;
                params.pipeToAddOGSouth = false;
                params.pipeToAddOGWest = true;

                pipeToAdd.setID(3);

            }

            pipeToAdd.setPlacingPlayer(params.currentPlayer);
        }

    }


    /**
     * Set the grid size and set the start and end pipes now that we
     * know where they should be on the grid
     * @param grid The chosen grid size
     */
    public void setGridSize(int grid){
        if (grid == 0) {
            gridSize = 5;
            p1StartIndex=0;
            p2StartIndex=2;

        }
        else if(grid == 1) {
            gridSize = 10;
            p1StartIndex=3;
            p2StartIndex=6;

        }
        else {
            gridSize = 20;
            p1StartIndex=7;
            p2StartIndex=14;

        }
        //need to set these pipes player names. Do it here because the player names have
        // already been set and we are just about to add the starts and ends to pipes
        startP1.setPlacingPlayer(player1name);
        startP2.setPlacingPlayer(player2name);
        endP1.setPlacingPlayer(player1name);
        endP2.setPlacingPlayer(player2name);


        startP1.set(this, 0, p1StartIndex);
        startP2.set(this, 0, p2StartIndex);
        endP1.set(this, gridSize - 1, p1StartIndex + 1);
        endP2.set(this, gridSize - 1, p2StartIndex + 1);

        pipes=new Pipe[gridSize][gridSize];
        pipes[0][p1StartIndex]=startP1;
        pipes[0][p2StartIndex]=startP2;
        pipes[gridSize-1][p1StartIndex+1]=endP1;
        pipes[gridSize-1][p2StartIndex+1]=endP2;
    }






    /**
     * Constructor, creates the start and end pipes and the pipe to add, and fills each with as much
     * as it can from this point
     * @param context The current activity
     */
    public PlayingArea(Context context) {

        // Load the start pipes
        //pipeStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeStraight = BitmapFactory.decodeResource(context.getResources(), R.drawable.straight);
        pipeEnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.gauge);
        handle = BitmapFactory.decodeResource(context.getResources(), R.drawable.handle);
        pipeCap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cap);
        pipe90 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe90);
        pipeTee = BitmapFactory.decodeResource(context.getResources(), R.drawable.tee);
        leak=BitmapFactory.decodeResource(context.getResources(), R.drawable.leak);


        startP1= new Pipe(3);
        startP1.setBitmap(pipeStraight);
        startP1.setAngle(0);
        startP1.set(this, 0, 0);
        startP1.setX(0);
        startP1.setY(0);
        startP1.setConnect(false, true, false, false);
        startP1.setPlacingPlayer(player1name);


        startP2= new Pipe(3);
        startP2.setBitmap(pipeStraight);
        startP2.setAngle(0);
        startP2.set(this, 0, 2);
        startP2.setX(0);
        startP2.setY(2 * gridSize / 5);
        startP2.setConnect(false, true, false, false);
        startP2.setPlacingPlayer(player2name);


        endP1= new Pipe(5);
        endP1.setBitmap(pipeEnd);
        endP1.setAngle(0);
        endP1.set(this, gridSize - 1, 1);
        endP1.setConnect(false, false, false, true);
        endP1.setPlacingPlayer(player1name);


        endP2= new Pipe(5);
        endP2.setBitmap(pipeEnd);
        endP2.setAngle(0);
        endP2.set(this, gridSize - 1, 3);
        endP2.setConnect(false, false, false, true);
        endP2.setPlacingPlayer(player2name);


        pipeToAdd=new Pipe(-1);
        pipeToAdd.setX(0.5f);
        pipeToAdd.setY(0.5f);

    }





    /**
     * Handles drawing of handles, pipe to add, and the gauges
     * @param canvas The current canvas we are drawing on
     */
    public void draw(Canvas canvas) {


        int wid = (canvas.getWidth());
        int hit = (canvas.getHeight());


        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;


        if(minDim == wid)
            hit = wid;
        else
            wid = hit;

        width = wid;
        height=hit;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(42f);


        canvas.save();
        canvas.scale(params.scale, params.scale);


        //pipeStart height to get below +42f for text size
        canvas.drawText(player1name, xMargin, p1StartIndex * hit / gridSize + pipeStraight.getHeight() + 42f + yMargin, paint);

        //2*hit/5 from where the start pipe is + pipeStart height to get below +42f for text size
        canvas.drawText(player2name, xMargin, p2StartIndex * hit / gridSize + pipeStraight.getHeight() + 42f + yMargin, paint);



        //the ratio is used to ensure alignment of the pipe parts, disregarding the gaudge
        float ratio = ((float)pipeEnd.getHeight()) / ((float)pipeStraight.getHeight());

        pipeStraight= Bitmap.createScaledBitmap(pipeStraight, wid / gridSize, hit / gridSize, false);

        int newHeight = (int)(ratio*pipeStraight.getHeight());
        pipeEnd = Bitmap.createScaledBitmap(pipeEnd, wid/gridSize, newHeight, false);
        pipes[gridSize-1][p1StartIndex + 1].setBitmap(pipeEnd);

        pipes[gridSize-1][p2StartIndex + 1].setBitmap(pipeEnd);




        int diff = pipeStraight.getHeight()-pipeEnd.getHeight(); //<the amount we need to adjust due to the gaudge



        //draw the leaks if there are any
        if(leakAreas.size()!=0)
            drawLeaks(canvas, wid, hit, paint);


        drawPipes(canvas, wid, hit, newHeight, diff, paint);


        //draws tha handles and their positions
        //handle = Bitmap.createScaledBitmap(handle, 50,50, false);
        handle = Bitmap.createScaledBitmap(handle, wid/ gridSize,hit/gridSize, false);
        if (params.opened) {
            //Draw a rotated handle for P1 and an unrotated for P2
            if(params.currentPlayer.contentEquals(player1name)) {
                canvas.save();
                canvas.rotate(-90, handle.getHeight() / 2, handle.getWidth() / 2+ p1StartIndex * hit / gridSize);
                canvas.drawBitmap(handle, 0-yMargin, p1StartIndex * (hit) / gridSize+xMargin, paint);
                canvas.restore();
                canvas.drawBitmap(handle, xMargin, p2StartIndex * hit / gridSize+yMargin, paint);
            }
            //Draw a rotated handle for P2 and an unrotated for P1
            else {
                canvas.save();
                canvas.rotate(-90, handle.getHeight() / 2, handle.getWidth() / 2 + p2StartIndex * hit / gridSize);
                canvas.drawBitmap(handle, -yMargin, p2StartIndex * (hit) / gridSize+xMargin, paint);
                canvas.restore();
                canvas.drawBitmap(handle, xMargin, p1StartIndex * hit / gridSize+yMargin, paint);

            }

        }
        else {
            //Draw the handles for the start pipes unrotated
            canvas.drawBitmap(handle, xMargin, p1StartIndex * hit / gridSize + yMargin, paint);
            canvas.drawBitmap(handle, xMargin, p2StartIndex * hit / gridSize + yMargin, paint);
        }





        //draw the gauge lines
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);


        if(params.won &&params.currentPlayer.contentEquals(player1name))
            canvas.drawLine((gridSize - 1) * wid / gridSize + pipeEnd.getWidth() / 2.1f+xMargin, (p1StartIndex+1)*hit / gridSize + diff / 5f+yMargin,
                    (gridSize - 1) * wid / gridSize + pipeEnd.getWidth() / 1.5f+xMargin,(p1StartIndex+1)* hit / gridSize - diff / 6.1f+yMargin, paint);

        else
            canvas.drawLine((gridSize - 1) * wid / gridSize + pipeEnd.getWidth() / 2.1f+xMargin, (p1StartIndex+1)*hit / gridSize + diff / 5f+yMargin,
                    (gridSize - 1) * wid / gridSize + pipeEnd.getWidth() / 3.6f+xMargin,(p1StartIndex+1)* hit / gridSize - diff / 6.1f+yMargin, paint);




        if(params.won &&params.currentPlayer.contentEquals(player2name))
            canvas.drawLine((gridSize - 1) * wid / gridSize + pipeEnd.getWidth() / 2.1f+xMargin, (p2StartIndex+1) * hit / gridSize + diff / 5f+yMargin,
                    (gridSize - 1) * wid / gridSize + pipeEnd.getWidth() / 1.5f+xMargin, (p2StartIndex+1) * hit / gridSize - diff / 6.1f+yMargin, paint);



        else
            canvas.drawLine((gridSize - 1) * wid / gridSize + pipeEnd.getWidth() / 2.1f+xMargin, (p2StartIndex+1)* hit / gridSize + diff / 5f+yMargin,
                (gridSize - 1) * wid / gridSize + pipeEnd.getWidth() / 3.6f+xMargin, (p2StartIndex+1) * hit / gridSize - diff / 6.1f+yMargin, paint);



        //resize and draw the pipe we are adding
        if (params.toAdd && pipeToAdd.getBitmap() != null) {
            float x = pipeToAdd.getX();
            float y = pipeToAdd.getY();

            canvas.save();

            float angle = pipeToAdd.getAngle();

            pipeToAdd.setBitmap(Bitmap.createScaledBitmap(pipeToAdd.getBitmap(), wid / gridSize, hit / gridSize, false));



            if(!rotating){
                canvas.rotate(angle, x * wid + pipeToAdd.getBitmap().getWidth() / 2, y * hit + pipeToAdd.getBitmap().getHeight() / 2);
                if (angle ==180.0f)
                    canvas.drawBitmap(pipeToAdd.getBitmap(), x * wid - xMargin, y * hit - yMargin, paint);
                else if (angle==90.0)
                    canvas.drawBitmap(pipeToAdd.getBitmap(), x * wid + yMargin, y * hit - xMargin, paint);
                else if (angle==270.0f)
                    canvas.drawBitmap(pipeToAdd.getBitmap(), x * wid - yMargin, y * hit + xMargin, paint);
                else if(angle==0.0f ||angle==360.0f)
                    canvas.drawBitmap(pipeToAdd.getBitmap(), x * wid + xMargin, y* hit + yMargin, paint);
                else
                    canvas.drawBitmap(pipeToAdd.getBitmap(), x * wid + xMargin, y * hit + yMargin, paint);


            }

            //special case for rotation, needed to maintain a centerpoint
            else{


                float start=pipeToAdd.getStartingAngle();


                if (start ==180.0f) {
                    canvas.translate(xMargin - pipeToAdd.getBitmap().getWidth(), yMargin - pipeToAdd.getBitmap().getHeight());
                    canvas.translate(x * wid, y * hit);
                    canvas.rotate(angle, pipeToAdd.getBitmap().getWidth(), pipeToAdd.getBitmap().getHeight());
                }
                else if (start==90.0) {
                    canvas.translate(xMargin, yMargin - pipeToAdd.getBitmap().getHeight());
                    canvas.translate(x * wid, y * hit);
                    canvas.rotate(angle, 0, pipeToAdd.getBitmap().getHeight());

                }
                else if (start==270.0f) {
                    canvas.translate(xMargin-pipeToAdd.getBitmap().getWidth(), yMargin);
                    canvas.translate(x * wid, y * hit);
                    canvas.rotate(angle, pipeToAdd.getBitmap().getWidth(), 0);
                }
                else {
                    canvas.translate(xMargin, yMargin);
                    canvas.translate(x * wid, y * hit);
                    canvas.rotate(angle,0, 0);
                }


                canvas.drawBitmap(pipeToAdd.getBitmap(), 0, 0, paint);

            }


            canvas.restore();


        }


        canvas.restore();
    }



    /**
     * Handles drawing of the pipes
     * @param canvas The current canvas we are drawing on
     * @param wid The width of the canvas
     * @param hit The height of the canvas
     * @param newHeight The height of a end pipe, needs to be larger than normal
     * @param diff The difference in heights between a normal pipe and an end pipe, used to line up
     *             other pipes and end pipes
     * @param paint The current paint (might not need)
     */
    public void drawPipes(Canvas canvas, int wid, int hit, int newHeight, float diff, Paint paint){
        //draw all the pipes in the grid
        for(int i=0; i<gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(pipes[i][j] != null) {
                    Pipe currPipe = pipes[i][j];

                    //special case for end pipe to add the diff
                    if (currPipe.getBitmap().sameAs(pipeEnd)) {

                        currPipe.setBitmap( Bitmap.createScaledBitmap(pipeEnd, wid/gridSize, newHeight, true));
                        canvas.drawBitmap(currPipe.getBitmap(), wid - pipeEnd.getWidth()+xMargin, j * hit / gridSize + diff+yMargin, paint);
                    }
                    else {
                        canvas.save();

                        float x = currPipe.getX();
                        float y = currPipe.getY();
                        canvas.rotate(currPipe.getAngle(), x * wid + currPipe.getBitmap().getWidth() / 2, y * hit + currPipe.getBitmap().getHeight() / 2);


                        currPipe.setBitmap(Bitmap.createScaledBitmap(currPipe.getBitmap(), wid / gridSize, hit / gridSize, false));

                        float angle=currPipe.getAngle();

                        if(angle==180.0f)
                            canvas.drawBitmap(currPipe.getBitmap(), i * wid / gridSize-xMargin, j * hit / gridSize-yMargin, paint);
                        else if(angle== 90.0f)
                            canvas.drawBitmap(currPipe.getBitmap(), i * wid / gridSize+yMargin, j * hit / gridSize-xMargin, paint);
                        else if(angle== 270.0f)
                            canvas.drawBitmap(currPipe.getBitmap(), i * wid / gridSize-yMargin, j * hit / gridSize+xMargin, paint);
                        else
                            canvas.drawBitmap(currPipe.getBitmap(), i * wid / gridSize+xMargin, j * hit / gridSize+yMargin, paint);

                        canvas.restore();
                    }
                }
            }
        }


    }



    public void what(){xMargin+=1;}


    /**
     * Handles drawing of the leaks
     * @param canvas The current canvas we are drawing on
     * @param wid The width of the canvas
     * @param hit The height of the canvas
     * @param paint The current paint (might not need)
     */
    public void drawLeaks(Canvas canvas, int wid, int hit, Paint paint){

        leak = Bitmap.createScaledBitmap(leak, wid / gridSize, hit / gridSize, false);

        for (int i = 0; i < leakAreas.size(); i+=2){
            int xLeakInd=leakAreas.elementAt(i);
            int yLeakInd=leakAreas.elementAt(i + 1);



            //leak off the top
            if(yLeakInd==-1)
                canvas.drawBitmap(leak, xLeakInd*wid/gridSize+xMargin, yLeakInd*hit/gridSize+yMargin, paint);

            //leak off the bottom
            if(yLeakInd==gridSize) {
                canvas.save();
                canvas.rotate(180,xLeakInd * wid / gridSize+leak.getWidth()/2, yLeakInd * hit / gridSize+leak.getHeight()/2);
                canvas.drawBitmap(leak, xLeakInd*wid/gridSize-xMargin, yLeakInd*hit/gridSize-yMargin, paint);
                canvas.restore();
            }

            //leak off the left side
            if(xLeakInd==-1) {
                canvas.save();
                canvas.rotate(-90, 0, 0);
                leak = Bitmap.createScaledBitmap(leak, wid / gridSize, hit / gridSize, false);
                canvas.drawBitmap(leak, -yLeakInd*hit/gridSize-pipeStraight.getHeight()-yMargin, xLeakInd*wid/gridSize+xMargin, paint);
                canvas.restore();

            }


            //leak off the right side
            if(xLeakInd==gridSize) {
                canvas.save();
                canvas.rotate(90, 0, 0);
                leak = Bitmap.createScaledBitmap(leak, wid / gridSize, hit / gridSize, false);
                canvas.drawBitmap(leak, yLeakInd*hit/gridSize+yMargin, -xLeakInd*wid/gridSize-pipeStraight.getWidth()-xMargin, paint);
                canvas.restore();
            }


            //error check if we are trying to draw stream off the board, those leaks are already drawn
            if(xLeakInd<0 || xLeakInd>=gridSize)
                continue;
            if(yLeakInd<0 || yLeakInd>=gridSize)
                continue;


            //the leak is coming from the bottom so draw normally
            //first error check is for out of index
            //second is to make sure the pipe we try to work with isnt null
            if(yLeakInd!=gridSize-1&&pipes[xLeakInd][yLeakInd+1]!=null &&pipes[xLeakInd][yLeakInd +1].getNorth() &&
                    pipes[xLeakInd][yLeakInd +1].getPlacingPlayer().contentEquals(params.currentPlayer))
                canvas.drawBitmap(leak, xLeakInd*wid/gridSize+xMargin, yLeakInd*hit/gridSize+yMargin, paint);

            //the leak is coming from above
            // first error check is for out of index
            // second is to make sure the pipe we try to work with isnt null
            if(yLeakInd!=0&&pipes[xLeakInd][yLeakInd-1]!=null &&pipes[xLeakInd][yLeakInd-1].getSouth()&&
                    pipes[xLeakInd][yLeakInd-1].getPlacingPlayer().contentEquals(params.currentPlayer)) {
                canvas.save();
                canvas.rotate(180,xLeakInd * wid / gridSize+leak.getWidth()/2, yLeakInd * hit / gridSize+leak.getHeight()/2);
                canvas.drawBitmap(leak, xLeakInd*wid/gridSize-xMargin, yLeakInd*hit/gridSize-yMargin, paint);
                canvas.restore();
            }

            //the leak is coming from the left
            //first error check is for out of index
            //second is to make sure the pipe we try to work with isnt null
            if(xLeakInd!=0&&pipes[xLeakInd-1][yLeakInd]!=null &&pipes[xLeakInd-1][yLeakInd].getEast()&&
                    pipes[xLeakInd-1][yLeakInd].getPlacingPlayer().contentEquals(params.currentPlayer)) {
                canvas.save();
                canvas.rotate(90, 0, 0);
                leak = Bitmap.createScaledBitmap(leak, wid / gridSize, hit / gridSize, false);
                canvas.drawBitmap(leak, yLeakInd*hit/gridSize+yMargin, -xLeakInd*wid/gridSize-pipeStraight.getWidth()-xMargin, paint);
                canvas.restore();
            }


            //the leak is coming from the right
            //first error check is for out of index
            //second is to make sure the pipe we try to work with isnt null
            if(xLeakInd!=gridSize-1&&pipes[xLeakInd+1][yLeakInd]!=null &&pipes[xLeakInd+1][yLeakInd].getWest()&&
                    pipes[xLeakInd+1][yLeakInd ].getPlacingPlayer().contentEquals(params.currentPlayer) &&
                            (xLeakInd!=gridSize-2 &&(yLeakInd!=p1StartIndex+1||yLeakInd!=p2StartIndex+1))) {
                canvas.save();
                canvas.rotate(-90, 0, 0);
                leak = Bitmap.createScaledBitmap(leak, wid / gridSize, hit / gridSize, false);
                canvas.drawBitmap(leak, -yLeakInd*hit/gridSize-pipeStraight.getHeight()-yMargin, xLeakInd*wid/gridSize+xMargin, paint);
                canvas.restore();
            }



        }
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
        return (start.search()&&(pipes[gridSize-1][p1StartIndex+1].beenVisited() ||pipes[gridSize-1][p2StartIndex+1].beenVisited()));
    }



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
             float y = event.getY(i);

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
                rotating=false;
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
                if(pipeToAdd !=null && pipeToAdd.getBitmap() !=null)
                    snapPipeAngle();
                view.invalidate();
                rotating=false;
                return true;

            case MotionEvent.ACTION_MOVE:
                if(pipeToAdd!=null &&pipeToAdd.getBitmap() != null &&pipeToAdd.hit(touch1.x, touch1.y,params.scale,width, height,xMargin,yMargin)) {
                    getPositions(event, view);
                    move();
                    view.invalidate();
                    return true;
                }
                else {
                    getPositions(event, view);
                    playingAreaMove();
                    view.invalidate();
                    return true;
                }
        }


        return false;
    }



    /**
     * Move the pipe to add by dx, dy if 1 touch, rotates if 2
     */
    public void move() {


        if (touch1.id < 0){
            return;
        }

        if (touch1.id >= 0){
            //At least one touch
            //We are moving
            touch1.computeDeltas();

            if(pipeToAdd.hit(touch1.x, touch1.y,params.scale,width, height,xMargin,yMargin)) {
                //multiply by 1/scale or wwe move too fast when we are zoomed in and too slow zoomed out
                pipeToAdd.setX(pipeToAdd.getX() + touch1.dX / width*1/params.scale);
                pipeToAdd.setY(pipeToAdd.getY() + touch1.dY / height*1/params.scale);
            }
        }

        if(touch2.id >= 0) {
            // Two touches

            /*
             * Rotation
             */
            float angle1 = angle((touch1.lastX), (touch1.lastY), (touch2.lastX), (touch2.lastY));
            float angle2 = angle((touch1.x), (touch1.y), (touch2.x), (touch2.y));
            float da = (angle2 - angle1);


            rotate(da, touch1.x, touch1.y);
            rotating=true;


        }
    }







    /**
     * Move the playing area by dx, dy if 1 touch, scale if 2 (so long as rotating is done)
     */
    public void playingAreaMove() {


        if (touch1.id < 0){
            return;
        }

        if (touch1.id >= 0){
            //At least one touch
            //We are moving
            touch1.computeDeltas();

            //panning


            //if we are rotating don't do anything, used to avoid messy rotation
            if(!rotating) {
                xMargin += (touch1.dX );
                yMargin += (touch1.dY );
            }



        }

        if(touch2.id >= 0) {


            /*
            * Scaling
            */
            float distance1 = distance(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float distance2 = distance(touch1.x, touch1.y, touch2.x, touch2.y);
            float scaleFloat = distance2 / distance1;


            //if we are rotating don't do anything, used to avoid messy rotation
            if(!rotating )
                scale(scaleFloat);
            else {
                float angle1 = angle((touch1.lastX), (touch1.lastY), (touch2.lastX), (touch2.lastY));
                float angle2 = angle((touch1.x), (touch1.y), (touch2.x), (touch2.y));
                float da = (angle2 - angle1);


                rotate(da, touch1.x, touch1.y);
            }

        }

    }



    /**
     * Scale the image based on distance between the point x1, y1
     * @param distance Distance to scale in... float?
     */
    public void scale(float distance) {
        params.scale *= distance;
    }



    /**
     * On action pointer up, set the angle to the nearest multiple of 90 and change the x and y of
     * the pipe to the new x and y
     */
    public void snapPipeAngle(){

        float currAngle=pipeToAdd.getAngle();

        if (currAngle<0)
            currAngle+=360;

        if(currAngle>=360)
            currAngle-=360;

        float normalizedAngle=currAngle%90;


        if (normalizedAngle >= 45)
            pipeToAdd.setAngle(currAngle + (90 - normalizedAngle));
        else if (normalizedAngle < 45)
            pipeToAdd.setAngle(currAngle - normalizedAngle);


        float newAngle=pipeToAdd.getAngle();
        if(newAngle==270)
            pipeToAdd.setConnect(params.pipeToAddOGEast, params.pipeToAddOGSouth,
                    params.pipeToAddOGWest, params.pipeToAddOGNorth);

        else if(pipeToAdd.getAngle()==180)
            pipeToAdd.setConnect(params.pipeToAddOGSouth, params.pipeToAddOGWest,
                    params.pipeToAddOGNorth, params.pipeToAddOGEast);

        else if(pipeToAdd.getAngle()==90)
            pipeToAdd.setConnect(params.pipeToAddOGWest,params.pipeToAddOGNorth,
                    params.pipeToAddOGEast,params.pipeToAddOGSouth);
        else if(pipeToAdd.getAngle()==0  || pipeToAdd.getAngle()==360 )
            pipeToAdd.setConnect(params.pipeToAddOGNorth,params.pipeToAddOGEast
                    ,params.pipeToAddOGSouth,params.pipeToAddOGWest);


        //find the new x and y based on where we started and ended up
        if(newAngle!=pipeToAdd.getStartingAngle()){

            float startAngle=pipeToAdd.getStartingAngle();

            if(newAngle==270&&(startAngle==0||startAngle==360) ||
                    (newAngle==180&&startAngle==270) ||
                    (newAngle==90&&startAngle==180) ||
                    (newAngle==0 || newAngle==360)&&startAngle==90) {
                pipeToAdd.setY(pipeToAdd.getY() - (float) pipeToAdd.getBitmap().getHeight() / height);

            }

            else if((newAngle==0 || newAngle==360)&&startAngle==270 ||
                    (newAngle==270 &&startAngle == 180) ||
                    (newAngle==180&&startAngle==90) ||
                    (newAngle==90&&(startAngle==0||startAngle==360))) {
                pipeToAdd.setX(pipeToAdd.getX()-(float)pipeToAdd.getBitmap().getWidth()/width);

            }


            else if(newAngle==180&&(startAngle==0||startAngle==360) ||
                    (newAngle==0 || newAngle==360)&&startAngle==180 ||
                    newAngle==270&&startAngle==90 ||
                    newAngle==90&&startAngle==270)  {
                pipeToAdd.setX(pipeToAdd.getX() - (float) pipeToAdd.getBitmap().getWidth() / width);
                pipeToAdd.setY(pipeToAdd.getY()-(float)pipeToAdd.getBitmap().getHeight()/height);
            }



        }

        //since we have just snapped, set the starting angle
        pipeToAdd.setStartingAngle(pipeToAdd.getAngle());
    }



    /**
     * Add a pipe to the grid if it passes the error checks
     * @param context The activity we are in, used to call the toasts
     * @param view The current view, used to call invalidate
     * @return true if the pipe was added to the grid
     */
    public boolean addToGrid(Context context,View view){
        if(pipeToAdd.getBitmap() !=null) {
            snap();
            view.invalidate();


            int xInd = (int)(pipeToAdd.getX() / (1 / (float)gridSize));
            int yInd = (int)(pipeToAdd.getY() / (1 / (float)gridSize));

            //placing on top of another pipe
            if (pipes[xInd][yInd] !=null){

                Toast toast = Toast.makeText(context, R.string.badMoveOnTop, Toast.LENGTH_LONG);
                toast.show();
                return false;
            }


            //placing in a space with a valve not positioned at another valve
            if(!checkNeighbors(xInd, yInd)){
                Toast toast = Toast.makeText(context, R.string.badMoveNoConnect, Toast.LENGTH_LONG);
                toast.show();
                return false;
            }


            //placing on your opponents path
            if(!checkPlaceOnOpponent(xInd, yInd)){
                Toast toast = Toast.makeText(context, R.string.badMoveOpponentsPath, Toast.LENGTH_LONG);
                toast.show();
                return false;
            }

            //placing directly to your end with no connection
            if(!checkDirectEnd(xInd, yInd)) {
                Toast toast = Toast.makeText(context, R.string.badMoveConnectToEnd, Toast.LENGTH_LONG);
                toast.show();
                return false;
            }


            pipes[xInd][yInd]=new Pipe(pipeToAdd.getID());
            pipes[xInd][yInd].setBitmap(pipeToAdd.getBitmap());
            pipes[xInd][yInd].setX(pipeToAdd.getX());
            pipes[xInd][yInd].setY(pipeToAdd.getY());
            pipes[xInd][yInd].setAngle(pipeToAdd.getAngle());
            pipes[xInd][yInd].set(this, xInd, yInd);

            pipes[xInd][yInd].setConnect(pipeToAdd.getNorth(), pipeToAdd.getEast(),
                    pipeToAdd.getSouth(), pipeToAdd.getWest());

            pipes[xInd][yInd].setPlacingPlayer(params.currentPlayer);

            pipeToAdd.setBitmap(null);
            pipeToAdd.setX(0.5f);
            pipeToAdd.setY(0.5f);
            pipeToAdd.setAngle(0);
            pipeToAdd.setStartingAngle(0);
            pipeToAdd.setID(-1);

            //it passed all the tests and is now set on the board
            return true;
        }
        return false;
    }



    /**
     * Check if there are any neighbors who are available for connection
     * @param xInd The current pipes x position
     * @param yInd The current pipes y position
     * @return true if there is a valid neighbor
     */
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


    /**
     * Check if we are placing on the opponent's path
     * @param xInd The current pipes x position
     * @param yInd The current pipes y position
     * @return true if we are not placing on the opponent's path
     */
    public boolean checkPlaceOnOpponent(int xInd, int yInd){


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



        if(northNeighbor!=null &&pipeToAdd.getNorth()&&northNeighbor.getSouth()
                && !pipeToAdd.getPlacingPlayer().contentEquals(northNeighbor.getPlacingPlayer()))
            return false;
        else if(eastNeighbor!=null &&pipeToAdd.getEast()&& eastNeighbor.getWest()
                && !pipeToAdd.getPlacingPlayer().contentEquals(eastNeighbor.getPlacingPlayer()))
            return false;
        else if (southNeighbor!=null &&pipeToAdd.getSouth()&&southNeighbor.getNorth()
                && !pipeToAdd.getPlacingPlayer().contentEquals(southNeighbor.getPlacingPlayer()))
            return false;
        else if(westNeighbor!=null &&pipeToAdd.getWest()&&westNeighbor.getEast()
                && !pipeToAdd.getPlacingPlayer().contentEquals(westNeighbor.getPlacingPlayer()))
            return false;


        return true;
    }




    /**
     * Check if we are trying to place directly onto the end pipe with on other connections
     * @param xInd The current pipes x position
     * @param yInd The current pipes y position
     * @return true if there is a pipe between the current pipe and the end
     */
    public boolean checkDirectEnd(int xInd, int yInd){

        //check for correct xPos
        if(xInd!=gridSize-2) {
            return true;
        }

        //check for correct yPos
        if(yInd!=p1StartIndex+1 && yInd!=p2StartIndex+1)
            return true;

        //no need for east neighbor since the first check makes sure the east neighbor is an end pipe
        Pipe northNeighbor=pipes[xInd][yInd - 1];
        Pipe southNeighbor=pipes[xInd][yInd+1];
        Pipe westNeighbor=pipes[xInd-1][yInd];


        boolean checkNorth=(northNeighbor!=null &&pipeToAdd.getNorth()&&northNeighbor.getSouth());
        boolean checkSouth=(southNeighbor!=null &&pipeToAdd.getSouth()&&southNeighbor.getNorth());
        boolean checkWest=(westNeighbor!=null &&pipeToAdd.getWest()&&westNeighbor.getEast());


        return (checkNorth || checkSouth || checkWest);


    }



    /**
     * Snaps the current pipe to the space closest on the grid, error checks if out of
     * range will add to the closest index
     */
    public void snap(){


        float x=pipeToAdd.getX();
        float y=pipeToAdd.getY();


        float relGridSize=1/((float)gridSize);

        float xTest=x%relGridSize;
        float yTest=y%relGridSize;


        if(xTest >=relGridSize/2)
            pipeToAdd.setX(x + (relGridSize - xTest));

        else
            pipeToAdd.setX(x - xTest);


        if(yTest >= relGridSize / 2)
            pipeToAdd.setY(y + (relGridSize - yTest));

        else
            pipeToAdd.setY(y - yTest);


        //from this point on are error checks for out of bounds
        if(pipeToAdd.getX()>=1.0f)
            pipeToAdd.setX(0.8f);

        if(pipeToAdd.getY()>=1.0f)
            pipeToAdd.setY(0.8f);

        if(pipeToAdd.getX()<=0.0f)
            pipeToAdd.setX(0.0f);


        if(pipeToAdd.getY()<=0.0f)
            pipeToAdd.setY(0.0f);

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
     * Determine the angle for two touches
     * @param x1 Touch 1 x
     * @param y1 Touch 1 y
     * @param x2 Touch 2 x
     * @param y2 Touch 2 y
     * @return computed distance in ... floats?
     */
    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;

        //Now that we have the change in x and y, the hypotenuse is the distance!
        return (float)Math.sqrt((dx * dx) + (dy * dy));
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
        pipeToAdd.setY(yp / height );

    }




    /**
     * Check if there are any leaks on the opening player's path
     * @param player The player who has opened their valve
     * @return true if there were no found leaks (can find more than 1)
     */
    public boolean checkForLeaks(String player){

        if(player.contentEquals(player1name)) {
            searchForAllLeaks(pipes[0][p1StartIndex]);
            return search(pipes[0][p1StartIndex]);
        }

        else {
            searchForAllLeaks(pipes[0][p2StartIndex]);
            return search(pipes[0][p2StartIndex]);
        }
    }



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

        return start.searchForAllLeaks();

    }







    public void updatePipes(Vector newPipes, String placingPlayer){


        int count=0;
        for(int i=0; i<gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(pipes[i][j] == null &&(int)newPipes.elementAt(count)!=9) {

                    int newId=(int)newPipes.elementAt(count)%10;

                    pipes[i][j]=new Pipe(newId);


                    pipes[i][j].setX(((float) i / gridSize));
                    pipes[i][j].setY((float) j / gridSize);


                    float newAngle=(float)((int)newPipes.elementAt(count)-newId);

                    pipes[i][j].setAngle(newAngle);
                    pipes[i][j].set(this, i, j);

                    setNewConnections(newId,newAngle,i,j);

                    pipes[i][j].setPlacingPlayer(placingPlayer);


                }

                count+=1;

            }
        }


        setBitmaps();

    }




    public void setNewConnections(int id,float angle,int xInd, int yInd){
        boolean north=false;
        boolean east=false;
        boolean south=false;
        boolean west=false;

        if(id==1) {
            if (angle==90.0f)
                west=south=true;
            else if(angle==180.0f)
                west=north=true;
            else if(angle==270.0f)
                east=north=true;
            else
                east=south=true;
        }
        else if(id==2)
            if (angle==90.0f)
                west=true;
            else if(angle==180.0f)
                north=true;
            else if(angle==270.0f)
                east=true;
            else
                south=true;
        else if(id==3)
            if (angle==90.0f ||angle==270.0f)
                north=south=true;
            else
                east=west=true;
        else if(id==4)
            if (angle==90.0f)
                east=west=south=true;
            else if(angle==180.0f)
                north=south=west=true;
            else if(angle==270.0f)
                east=west=north=true;
            else
                north=east=south=true;

        else if(id==5)
            west=true;


        pipes[xInd][yInd].setConnect(north,east,south,west);

    }




    /**
     * Save the view state to a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to save to
     */
    public void putToBundle(String key, Bundle bundle ) {

        params.leakAreas=leakAreas;


        params.pipeToAdd=pipeToAdd;
        params.pipeToAdd.setBitmap(null);


        params.pipes=pipes;


        //set all of the bitmaps and playing areas to null t avoid trying to
        // save them to the bundle (this is what caused crashing on hitting the home button)
        if(params.pipes !=null)
        for(int i=0; i<gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (params.pipes[i][j] != null) {
                    params.pipes[i][j].setBitmap(null);
                    params.pipes[i][j].setPlayingArea(null);

                }
            }
        }



        params.xMargin=xMargin;
        params.yMargin=yMargin;


        bundle.putSerializable(key, params);

    }


    /**
     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public void getFromBundle(String key, Bundle bundle) {
        params = (Parameters)bundle.getSerializable(key);

        assert params != null;
        if(params.pipeToAdd!=null)
            pipeToAdd = params.pipeToAdd;
        if(params.pipes!=null)
             pipes = params.pipes;
        if(params.leakAreas!=null)
            leakAreas =params.leakAreas;


        setBitmaps();

        //flip the margins since we are changing the layout
        //noinspection SuspiciousNameCombination,SuspiciousNameCombination,SuspiciousNameCombination
        xMargin=params.yMargin;
        //noinspection SuspiciousNameCombination
        yMargin=params.xMargin;
    }


    /**
     * Set the pipes and pipe to adds bitmap via it's id
     */
    public void setBitmaps(){

        if(pipeToAdd!=null) {

            int id=pipeToAdd.getID();

            if(id==1)
                pipeToAdd.setBitmap(pipe90);
            else if(id==2)
                pipeToAdd.setBitmap(pipeCap);
            else if(id==3)
                pipeToAdd.setBitmap(pipeStraight);
            else if(id==4)
                pipeToAdd.setBitmap(pipeTee);

        }


        if(pipes!=null)
            for(int i=0; i<gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if(pipes[i][j] != null) {
                        Pipe currPipe = pipes[i][j];

                        pipes[i][j].setPlayingArea(this);

                        int id=currPipe.getID();

                        if(id==1)
                            pipes[i][j].setBitmap(pipe90);
                        else if(id==2)
                            pipes[i][j].setBitmap(pipeCap);
                        else if(id==3)
                            pipes[i][j].setBitmap(pipeStraight);
                        else if(id==4)
                            pipes[i][j].setBitmap(pipeTee);
                        else if(id==5)
                            pipes[i][j].setBitmap(pipeEnd);

                    }
                }
        }

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


        /**
         * Storage for the pipeToAdd
         */
        public Pipe pipeToAdd;


        /**
         * Storage for the pipeToAdds connect (can be changed so it must be saved)
         */
        public boolean pipeToAddOGNorth=false;
        public boolean pipeToAddOGEast=false;
        public boolean pipeToAddOGSouth=false;
        public boolean pipeToAddOGWest=false;


        /**
         * Storage for the leak areas
         */
        public Vector<Integer> leakAreas;

        /**
         * Storage for if a player has opened their valve
         */
        public boolean opened=false;

        /**
         * Storage for the current player
         */
        public String currentPlayer;


        /**
         * Storage for if we are trying to add a pipe
         */
        public boolean toAdd = true;

        /**
         * Storage for if a player has a complete path with no leaks
         */
        public boolean won=false;



        /**
         *  Scale variable for when user scales the screen, zoom in/out
         */
        public float scale = 1.0f;


        /**
         * Storage for the xMargin
         */
        public int xMargin=0;


        /**
         * Storage for the yMargin
         */
        public int yMargin=0;

    }
}
