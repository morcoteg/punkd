package edu.msu.hutteng3.fawfulsteampunked;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Tyler on 10/10/2015.
 * The actual pipes themselves
 */
public class Pipe implements Serializable{


    /**
     * Playing area this pipe is a member of
     */
    private PlayingArea playingArea = null;

    /**
     * Array that indicates which sides of this pipe
     * has flanges. The order is north, east, south, west.
     *
     * As an example, a T that has a horizontal pipe
     * with the T open to the bottom would be:
     *
     * false, true, true, true
     */
    @SuppressWarnings("CanBeFinal")
    private boolean[] connect = {false, false, false, false};

    /**
     * X location in the playing area
     */
    private float x = 0;

    /**
     * the pipes angle
     */
    private float mAngle = 0;

    /**
     * Y location in the playing area
     */
    private float y = 0;

    /**
     * The player who placed this pipe
     */
    private String placingPlayer;

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
     * The pipe ID
     */
    private int id;

    /**
     * The pipe bitmap
     */
    private Bitmap bitmap;

    /**
     * The starting angle of a rotation, used in rotating the pipe
     */
    private float startingAngle=0;


    /* General getters and setters with on extra functionality*/
    public float getStartingAngle(){return startingAngle;}
    public void setStartingAngle(float angle){startingAngle=angle;}

    public void setBitmap(Bitmap newMap){bitmap=newMap;}
    public Bitmap getBitmap() {return bitmap;}

    public void setPlayingArea(PlayingArea toSet){playingArea=toSet;}
    @SuppressWarnings("WeakerAccess")
    public PlayingArea getPlayingArea() {
        return playingArea;
    }

    public float getX(){ return x; }
    public void setX(float newX){ x = newX; }

    public float getY(){ return y; }
    public void setY(float newY){ y = newY; }

    public float getAngle(){return mAngle;}
    public void setAngle(float angle){mAngle = angle;}

    public void setID(int newID){id=newID;}
    public int getID(){return id;}

    public void setConnect(boolean north, boolean east, boolean south, boolean west) {
        connect[0] = north;
        connect[1] = east;
        connect[2] = south;
        connect[3] = west;
    }
    public boolean getNorth() {return connect[0];}
    public boolean getEast() {return connect[1];}
    public boolean getSouth() {return connect[2];}
    public boolean getWest() {return connect[3];}

    public String getPlacingPlayer(){return placingPlayer;}
    public void setPlacingPlayer(String player){placingPlayer=player;}





    /**
     * Constructor
     * @param id The id for what type of pipe this is
     */
    public Pipe(int id) {
        this.id = id;
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
                //noinspection UnnecessaryContinue
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
     * Basically the same as the given DFS only we keep going after we find a
     * leak so we can find them all
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
                if(dp==2) {
                    playingArea.getLeakArea().add(xIndex);
                    playingArea.getLeakArea().add(yIndex-1);
                }
                //east is empty
                else if(dp==3){
                    playingArea.getLeakArea().add(xIndex+1);
                    playingArea.getLeakArea().add(yIndex);

                }

                //south is empty
                else if(dp==0){
                    playingArea.getLeakArea().add(xIndex);
                    playingArea.getLeakArea().add(yIndex+1);

                }

                //west is empty
                else if(dp==1){
                    playingArea.getLeakArea().add(xIndex-1);
                    playingArea.getLeakArea().add(yIndex);

                }
                //return false;
                continue;
            }

            if (n.visited) {
                // Already visited this one, so no leaks this way
                //noinspection UnnecessaryContinue
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
                break;

            case 1:
                if(this.getPlayingArea().getGridSize()!=xIndex+1)
                    return playingArea.getPipe(xIndex+1, yIndex);
                break;

            case 2:
                if(this.getPlayingArea().getGridSize()!=yIndex+1)
                    return playingArea.getPipe(xIndex, yIndex+1);
                break;

            case 3:
                if(xIndex!=0)
                    return playingArea.getPipe(xIndex-1, yIndex);
                break;
        }

        return null;
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
    public void setVisited(@SuppressWarnings("SameParameterValue") boolean visited) {
        this.visited = visited;
    }



    /**
     * Test to see if we have touched a pipe
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param scaleFactor the amount to scale a piece by
     * @return true if we hit the piece
     */
    public boolean hit(float testX, float testY, float scaleFactor,int wid, int hit, int xMargin, int yMargin) {

        // Make relative to the location and size to the piece size

        float newWid=wid*scaleFactor;
        float newHit=hit*scaleFactor;

        float minDim = newWid < newHit ? newWid : newHit;


        return !(testX < x * minDim + xMargin * scaleFactor || testX > x * minDim + bitmap.getWidth() * scaleFactor + xMargin * scaleFactor ||
                testY < y * minDim + yMargin * scaleFactor || testY > y * minDim + bitmap.getHeight() * scaleFactor + yMargin * scaleFactor);

    }




}