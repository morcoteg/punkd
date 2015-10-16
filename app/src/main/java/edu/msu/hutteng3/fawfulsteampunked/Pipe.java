package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by Tyler on 10/10/2015.
 */
public class Pipe {


    /**
     * Playing area this pipe is a member of
     */
    private PlayingArea playingArea = null;

    /**
     * The current parameters
     */
    private Parameters params = new Parameters();



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

    private float mAngle = (1/3);

    /**
     * Y location in the playing area
     */
    private float y = 0;

    public float getX(){ return x; }
    public void setX(float newX){ x = newX; }

    public float getY(){ return y; }
    public void setY(float newY){ y = newY; }

    public float getAngle(){return mAngle;}


    /**
     * X location in the playing area (index into array)
     */
    private int xIndex = 0;


    /**
     * Y location in the playing area (index into array)
     */
    private int yIndex = 0;





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


    /**
     * THe image for the actual pipe.
     */
    private Bitmap pipe;


    public Pipe(Context context, int id) {
        this.id = id;

        pipe = BitmapFactory.decodeResource(context.getResources(), id);
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
     * Find the neighbor of this pipe
     * @param d Index (north=0, east=1, south=2, west=3)
     * @return Pipe object or null if no neighbor
     */
    private Pipe neighbor(int d) {
        switch(d) {
            case 0:
                return playingArea.getPipe(xIndex, yIndex-1);

            case 1:
                return playingArea.getPipe(xIndex+1, yIndex);

            case 2:
                return playingArea.getPipe(xIndex, yIndex+1);

            case 3:
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
     * Move the puzzle piece by dx, dy
     * @param dx x amount to move
     * @param dy y amount to move
     */
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    /**
     * Rotate the image around the point x1, y1
     * @param dAngle Angle to rotate in degrees
     * @param x1 rotation point x
     * @param y1 rotation point y
     */
    public void rotate(float dAngle, float x1, float y1) {
        mAngle += dAngle;
        //params.hatAngle += dAngle;

        // Compute the radians angle
        double rAngle = Math.toRadians(dAngle);
        float ca = (float) Math.cos(rAngle);
        float sa = (float) Math.sin(rAngle);
        float xp = (x - x1) * ca - (y - y1) * sa + x1;
        float yp = (x - x1) * sa + (y - y1) * ca + y1;

        //x += xp;
        //y += yp;
    }








    /**
     * Test to see if we have touched a pipe
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param puzzleSize the size of the puzzle in pixels
     * @param scaleFactor the amount to scale a piece by
     * @return true if we hit the piece
     */
    public boolean hit(float testX, float testY, int puzzleSize, float scaleFactor) {

        // Make relative to the location and size to the piece size

        //MAY NEED TO CHANGE WHEN BOARD IS UNIFORM SIZE

        int pX = (int)((testX -x) * puzzleSize) + bitmap.getWidth()/8; //I have no idea why /2 failed but /8 works
        int pY = (int)((testY-y ) * puzzleSize) + bitmap.getHeight()/8;


        if(pX < 0 || pX >= bitmap.getWidth() || pY < 0 || pY >= bitmap.getHeight())
            return false;
        else
        // We are within the rectangle of the piece.
        // Are we touching actual picture?
             return (bitmap.getPixel(pX, pY) & 0xff000000) != 0;
    }


    ///////////////////////////////////////////////// NESTED CLASSES parameters ///////////////////////////

    /**
     * Parameters class for the Pipe's coordinates x, y and the rotation angle
     */
    private static class Parameters implements Serializable {


        /**
         * X location of Pipe relative to the image
         */
        public float pipeX = 0;

        /**
         * Y location of Pipe relative to the image
         */
        public float pipeY = 0;


        /**
         * Pipe rotation angle
         */
        public float hatAngle = 0;

    }

}