package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.io.Serializable;

public class GameSearch extends AppCompatActivity {


    private static final String PARAMETERS = "parameters";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        Bundle extras = getIntent().getExtras();

        params.currentPlayer = extras.getString("PLAYER_NAME");
        params.gridSize=extras.getInt("GRID_SIZE");

        this.setTitle(params.currentPlayer);

                /*
        *   Save any state
        */
        if (savedInstanceState != null) {
            this.getFromBundle(PARAMETERS, savedInstanceState);

        }


        MyGcmListenerService listener = new MyGcmListenerService();
        listener.setGameSearch(this);


    }












    @Override
    protected void onStart() {
        super.onStart();

        Cloud cloud=new Cloud();

        View view=findViewById(android.R.id.content);


        cloud.findGame(params.currentPlayer, params.gridSize, view, this);

        //cloud.joinGame(params.currentPlayer,view, this);

        TextView waiting = (TextView) findViewById(R.id.waiting);

        String display="5";
        if(params.gridSize==1)
            display="10";
        else if(params.gridSize==2)
            display="20";

        waiting.setText(getString(R.string.waiting) + " " + display + "X" + display + "... \n" + getString(R.string.loggedInUser));



    }



    public void joinMessage(){

        Cloud cloud=new Cloud();

        View view=findViewById(android.R.id.content);

        cloud.joinGame(params.currentPlayer, view, this);
    }




    public void startNewGame(String otherPlayer, String id,String token){


        Cloud cloud=new Cloud();
        View view=findViewById(android.R.id.content);
        cloud.sendMessage(token,"join",view);

        Intent intent = new Intent(this, GameBoard.class);
        intent.putExtra("PLAYER_1_NAME", params.currentPlayer);
        intent.putExtra("PLAYER_2_NAME", otherPlayer);
        intent.putExtra("PLAYER_DEVICE", params.currentPlayer); //<may not need
        intent.putExtra("PLAYER_CLOUD", otherPlayer);
        intent.putExtra("GAME_ID", id);
        intent.putExtra("GRID_SIZE", params.gridSize);
        intent.putExtra("OPPONENT_TOKEN", token);
        startActivity(intent);



    }



    public void joinGame(String otherPlayer, String id,String token){

        Intent intent = new Intent(this, GameBoard.class);
        intent.putExtra("PLAYER_1_NAME", otherPlayer);
        intent.putExtra("PLAYER_2_NAME", params.currentPlayer);
        intent.putExtra("PLAYER_DEVICE", params.currentPlayer); //<may not need
        intent.putExtra("PLAYER_CLOUD", otherPlayer);
        intent.putExtra("GAME_ID", id);
        intent.putExtra("GRID_SIZE", params.gridSize);
        intent.putExtra("OPPONENT_TOKEN", token);
        startActivity(intent);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    @Override
    public void onBackPressed() {

        Cloud cloud=new Cloud();

        View view=findViewById(android.R.id.content);

        cloud.logout(params.currentPlayer, view);

        super.onBackPressed();

    }







    /**
     * The current parameters
     */
    private Parameters params = new Parameters();




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);



        this.putToBundle(PARAMETERS, outState);
    }



    /**
     * Save the view state to a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to save to
     */
    @SuppressWarnings("WeakerAccess")
    public void putToBundle(@SuppressWarnings("SameParameterValue") String key, Bundle bundle ) {

        bundle.putSerializable(key, params);

    }

    /**
     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from
     */
    @SuppressWarnings("WeakerAccess")
    public void getFromBundle(@SuppressWarnings("SameParameterValue") String key, Bundle bundle) {
        params = (Parameters) bundle.getSerializable(key);

        assert params != null;
        this.setTitle(params.currentPlayer);

    }




    /////////////////////////////////////////// NESTED CLASS parameters ///////////////////////

    /**
     * Parameters class for the Pipe's coordinates x, y and the rotation angle
     */
    private static class Parameters implements Serializable {

        /**
         * If the add pipes button is enabled
         */
        public String currentPlayer="";


        /**
         * the selected grid size
         */
        public int gridSize=0;

    }





}
