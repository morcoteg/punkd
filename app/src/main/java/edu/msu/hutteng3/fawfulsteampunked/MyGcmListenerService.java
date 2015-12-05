package edu.msu.hutteng3.fawfulsteampunked;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {



        String message = data.getString("message");
        Log.d("gcm", "From: " + from);
        Log.d("gcm", "Message: " + message);


        //ServerComm serverComm = ServerComm.get();

        if(message.equals("join") && gameSearch!=null)
            gameSearch.joinMessage();

        else if(message.equals("turn") && gameBoard!=null)
            gameBoard.loadGameState();

        else if(message.equals("open") && gameBoard!=null)
            gameBoard.openMessage();

        else if(message.equals("surrender") && gameBoard!=null)
            gameBoard.surrenderMessage();
    }


    private static GameSearch gameSearch=null;
    public void setGameSearch(GameSearch newGameSearch){gameSearch=newGameSearch;}

    private static GameBoard gameBoard=null;
    public void setGameBoard(GameBoard newGameBoard){gameBoard=newGameBoard;}
}