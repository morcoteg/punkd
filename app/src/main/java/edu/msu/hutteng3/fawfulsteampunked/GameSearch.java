package edu.msu.hutteng3.fawfulsteampunked;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

public class GameSearch extends AppCompatActivity {


    private static final String PARAMETERS = "parameters";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        Bundle extras = getIntent().getExtras();

        params.currentPlayer = extras.getString("PLAYER_NAME");

        this.setTitle(params.currentPlayer);

                /*
        *   Save any state
        */
        if (savedInstanceState !=  null){
            this.getFromBundle(PARAMETERS, savedInstanceState);

        }
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


    }





}
