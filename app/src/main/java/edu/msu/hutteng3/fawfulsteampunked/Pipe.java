package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by Tyler on 10/10/2015.
 */
public class Pipe implements Serializable{


    /**
     * Playing area this pipe is a member of
     */
    private PlayingArea playingArea = null;

    public void setPlayingArea(PlayingArea toSet){playingArea=toSet;}



    /**
     * The current parameters
     */
    //private Parameters params = new Parameters();



    /**
     * Array that indicates which sides of this pipe
     * has flanges. The order is north, east, south, west.
     *
     * As an example, a T that has a horizontal pipe
     * with the T open to the bottom would be:
     *
     * false, true, true, true
     */
    private boolean[] connect = {false, false, false, false};

    /**
     * X location in the playing area
     */
    private float x = 0;

    private float mAngle = 0;

    /**
     * Y location in the playing area
     */
    private float y = 0;

    public float getX(){ return x; }
    public void setX(float newX){ x = newX; }

    public float getY(){ return y; }
    public void setY(float newY){ y = newY; }

    public float getAngle(){return mAngle;}
    public void setAngle(float angle){mAngle = angle;}





    private String placingPlayer;
    public String getPlacingPlayer(){return placingPlayer;}
    public void setPlacingPlayer(String player){placingPlayer=player;}


    /**
     * X location in the playing area (index into array)
     */
    private int xIndex = 0;
    public int getXIndex(){return xIndex;}

    /**
     * Y location in the playing area (index into array)
     */
    private int yIndex = 0;
    public int getYIndex(){return yIndex;}




    /**
     * Depth-first visited visited
     */
    private boolean visited = false;


    /**
     * Constructor
     * @param north True if connected north
     * @param east True if connected east
     * @param south True if connected south
     * @param west True if connected west
     */
    public Pipe(boolean north, boolean east, boolean south, boolean west) {
        connect[0] = north;
        connect[1] = east;
        connect[2] = south;
        connect[3] = west;
    }


    /**
     * The pipe ID
     */
    private int id;
    public void setID(int newID){id=newID;}
    public int getID(){return id;}


    public void setConnect(boolean north, boolean east, boolean south, boolean west) {
        connect[0] = north;
        connect[1] = east;
        connect[2] = south;
        connect[3] = west;
    }



    public boolean getNorth() {
       return connect[0];
    }

    public boolean getEast() {
        return connect[1];
    }

    public boolean getSouth() {
        return connect[2];
    }

    public boolean getWest() {
        return connect[3];
    }







    /**
     * The image for the actual pipe.
     */
   // private Bitmap pipe;


    public Pipe(Context context, int id) {
        this.id = id;

       // pipe = BitmapFactory.decodeResource(context.getResources(), id);
    }






    /**
     * Search to see if there are any downstream of this pipe
     *
     * This does a simple depth-first search to find any connections
     * that are not, in turn, connected to another pipe. It also
     * set the visited flag in all pipes it does visit, so you can
     * tell if a pipe is reachable from this pipe by checking that flag.
     * @return True if no leaks in the pipe
     */
    public boolean search() {
        visited = true;

        for(int d=0; d<4; d++) {
            /*
             * If no connection this direction, ignore
             */
            if (!connect[d]) {
                continue;
            }

            Pipe n = neighbor(d);
            if (n == null) {
                // We leak
                // We have a connection with nothing on the other side
                return false;
            }

            // What is the matching location on
            // the other pipe. For example, if
            // we are looking in direction 1 (east),
            // the other pipe must have a connection
            // in direction 3 (west)
            int dp = (d + 2) % 4;
            if (!n.connect[dp]) {
                // We have a bad connection, the other side is not
                // a flange to connect to
                return false;
            }

            if (n.visited) {
                // Already visited this one, so no leaks this way
                continue;
            } else {
                // Is there a lead in that direction
                if (!n.search()) {
                    // We found a leak downstream of this pipe
                    return false;
                }
            }
        }

        // Yah, no leaks
        return true;
    }








    /**
     * Search to see if there are any downstream of this pipe
     *
     * This does a simple depth-first search to find any connections
     * that are not, in turn, connected to another pipe. It also
     * set the visited flag in all pipes it does visit, so you can
     * tell if a pipe is reachable from this pipe by checking that flag.
     * @return True if no leaks in the pipe
     */
    public boolean searchForAllLeaks() {
        visited = true;

        for(int d=0; d<4; d++) {
            /*
             * If no connection this direction, ignore
             */
            if (!connect[d]) {
                continue;
            }

            Pipe n = neighbor(d);
            if (n == null) {
                // We leak
                // We have a connection with nothing on the other side
                //north is empty
                if(d==0) {
                    playingArea.getLeakArea().add(xIndex);
                    playingArea.getLeakArea().add(yIndex-1);
                }
                //east is empty
                else if(d==1){
                    playingArea.getLeakArea().add(xIndex+1);
                    playingArea.getLeakArea().add(yIndex);

                }

                //south is empty
                else if(d==2){
                    playingArea.getLeakArea().add(xIndex);
                    playingArea.getLeakArea().add(yIndex+1);

                }

                //west is empty
                else if(d==3){
                    playingArea.getLeakArea().add(xIndex-1);
                    playingArea.getLeakArea().add(yIndex);

                }
                //return false;
                continue;
            }

            // What is the matching location on
            // the other pipe. For example, if
            // we are looking in direction 1 (east),
            // the other pipe must have a connection
            // in direction 3 (west)
            int dp = (d + 2) % 4;
            if (!n.connect[dp]) {
                // We have a bad connection, the other side is not
                // a flange to connect to
               // playingArea.getLeakArea().add(n.getXIndex());
               // playingArea.getLeakArea().add(n.getYIndex());
                //return false;
                continue;
            }

            if (n.visited) {
                // Already visited this one, so no leaks this way
                continue;
            } else {
                // Is there a lead in that direction
                if (!n.searchForAllLeaks()) {
                    // We found a leak downstream of this pipe
                   // playingArea.getLeakArea().add(n.getXIndex());
                    //playingArea.getLeakArea().add(n.getYIndex());
                    return false;
                   // continue;
                }
            }
        }

        // Yah, no leaks
        return true;
    }






    /**
     * Find the neighbor of this pipe
     * @param d Index (north=0, east=1, south=2, west=3)
     * @return Pipe object or null if no neighbor
     */
    private Pipe neighbor(int d) {
        switch(d) {
            case 0:
                if(yIndex!=0)
                    return playingArea.getPipe(xIndex, yIndex-1);

            case 1:
                if(this.getPlayingArea().getGridSize()!=xIndex+1)
                    return playingArea.getPipe(xIndex+1, yIndex);

            case 2:
                if(this.getPlayingArea().getGridSize()!=yIndex+1)
                    return playingArea.getPipe(xIndex, yIndex+1);

            case 3:
                if(xIndex!=0)
                    return playingArea.getPipe(xIndex-1, yIndex);
        }

        return null;
    }

    /**
     * Get the playing area
     * @return Playing area object
     */
    public PlayingArea getPlayingArea() {
        return playingArea;
    }

    /**
     * Set the playing area and location for this pipe
     * @param playingArea Playing area we are a member of
     * @param x X index
     * @param y Y index
     */
    public void set(PlayingArea playingArea, int x, int y) {
        this.playingArea = playingArea;
        this.xIndex = x;
        this.yIndex = y;
    }

    /**
     * Has this pipe been visited by a search?
     * @return True if yes
     */
    public boolean beenVisited() {
        return visited;
    }

    /**
     * Set the visited flag for this pipe
     * @param visited Value to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }



    private Bitmap bitmap;
    public void setBitmap(Bitmap newMap){bitmap=newMap;}
    public Bitmap getBitmap() {return bitmap;}





    /**
     * Test to see if we have touched a pipe
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param puzzleSize the size of the puzzle in pixels
     * @param scaleFactor the amount to scale a piece by
     * @return true if we hit the piece
     */
    public boolean hit(float testX, float testY, int puzzleSize, float scaleFactor,int wid, int hit) {

        // Make relative to the location and size to the piece size

        //MAY NEED TO CHANGE WHEN BOARD IS UNIFORM SIZE







        int pX = (int) ((testX - x * wid) * .8f) + bitmap.getWidth() / 2; //I have no idea why /2 failed but /8 works
        int pY = (int) ((testY - y * hit) * .8f) + bitmap.getHeight() / 2;




        if(pX < 0 || pX >= bitmap.getWidth() || pY < 0 || pY >= bitmap.getHeight())
            return false;
        else
        // We are within the rectangle of the piece.
        // Are we touching actual picture?
            // return (bitmap.getPixel(pX, pY) & 0xff000000) != 0;
        return true;


    }




/*


     * The current parameters

    private Parameters params = new Parameters();





     * Save the view state to a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to save to

    public void putToBundle(String key, Bundle bundle ) {

        bundle.putSerializable(key, params);

    }


     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from

    public void getFromBundle(String key, Bundle bundle) {
        params = (Parameters) bundle.getSerializable(key);


    }




    /////////////////////////////////////////// NESTED CLASS parameters ///////////////////////


     * Parameters class for the Pipe's coordinates x, y and the rotation angle

    private static class Parameters implements Serializable {


        public boolean visied;





    }

*/


}