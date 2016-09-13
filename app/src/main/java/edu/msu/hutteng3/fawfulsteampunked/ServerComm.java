package edu.msu.hutteng3.fawfulsteampunked;

/**
 * Created by Tyler on 12/3/2015.
 */
public class ServerComm {


    // Singleton pattern object
    private static final ServerComm server = new ServerComm();
    // Private constructor prevents other classes from
    // creating a new object of this type
    private ServerComm() {
    }
    // Get a reference to the singleton server object
    public static ServerComm get() {
        return server;
    }
}
