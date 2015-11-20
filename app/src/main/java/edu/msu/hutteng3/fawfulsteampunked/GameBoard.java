package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

public class GameBoard extends AppCompatActivity {



    /*
* The ID values for each of the hat types. The values must
* match the index into the array hats_spinner in arrays.xml.
*/
    public static final int GRID_5 = 0;
    public static final int GRID_10 = 1;
    public static final int GRID_20 = 2;

    private static final String PARAMETERS_PLAYINGAREA = "parametersPA";
    private static final String PARAMETERS_GAMEBOARD = "parametersGB";
    private static final String PARAMETERS_PIPESELECT = "parametersPS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        Bundle extras = getIntent().getExtras();

        String p1 = extras.getString("PLAYER_1_NAME");
        String p2 = extras.getString("PLAYER_2_NAME");
        int gridSize = extras.getInt("GRID_SIZE");

        getGameBoardView().setPlayer1name(p1);
        getGameBoardView().setPlayer2name(p2);
        getGameBoardView().setGridSize(gridSize);

        //give the pipe select view a path to the game board view
        getPipeSelectView().setGameBoardView(getGameBoardView());

        /*
        *   Save any state
        */
        if (savedInstanceState !=  null){
            getGameBoardView().getPlayingArea().getFromBundle(PARAMETERS_PLAYINGAREA, savedInstanceState);
            getPipeSelectView().getPipeArea().getFromBundle(PARAMETERS_PIPESELECT,savedInstanceState);
            this.getFromBundle(PARAMETERS_GAMEBOARD , savedInstanceState);

            getGameBoardView().invalidate();
        }



        if(params.firstTurn) {
            //set these to initially be p1 for current and p2 for other to give the first player the first move
            params.currentPlayer = p1;
            params.otherPlayer = p2;
            this.setTitle(params.currentPlayer);
            params.firstTurn=false;
            getGameBoardView().setCurrentPlayer(params.currentPlayer);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_board, menu);
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
     * The gameBoard view object
     */
    private GameBoardView getGameBoardView() {
        return (GameBoardView) findViewById(R.id.gameBoardView);
    }


    /**
     * The pipeSelectView view object
     */
    private PipeSelectView getPipeSelectView() {
        return (PipeSelectView) findViewById(R.id.pipeSelectView);
    }


    /**
     * Handles loading from the app selection screen
     */
    public void onResume(){
        super.onResume();
        getGameBoardView().getPlayingArea().setBitmaps();

    }


    /**
     * Handles switching player's turns
     */
    public void switchTurn(){
        getGameBoardView().invalidate();
        getPipeSelectView().invalidate();
        String temp=params.currentPlayer;
        params.currentPlayer=params.otherPlayer;
        params.otherPlayer=temp;

        params.setAddPipe=true;
        getGameBoardView().setAddPipe(true);
        getPipeSelectView().setTouchedPipePos(-1);

        Toast toast = Toast.makeText(this, params.currentPlayer +getResources().getString(R.string.titleSet), Toast.LENGTH_LONG);
        toast.show();

        this.setTitle(params.currentPlayer);
        getGameBoardView().setCurrentPlayer(params.currentPlayer);
    }



    /**
     * Handles the add button press
     */
    public void addPipe(@SuppressWarnings("UnusedParameters") View view){
        params.setAddPipe=true;
        getGameBoardView().setAddPipe(true);
        getPipeSelectView().setDiscard(false);

        if(getGameBoardView().addToGrid(this)) {
            getPipeSelectView().discardPipe();
        }
    }



    /**
     * Handles the discard button press
     */
    public void discardPipe(View view) {
        getPipeSelectView().setDiscard(true);
        params.setAddPipe=false;
        getGameBoardView().setAddPipe(false);
        getGameBoardView().setPipeToAdd(null);
    }



    /**
     * The color select button
     */
    private Button getAddButton() {
        return (Button)findViewById(R.id.buttonAdd);
    }

    /**
     * The color select button
     */
    private Button getDiscardButton() {
        return (Button)findViewById(R.id.buttonDis);
    }


    /**
     * The color select button
     */
    private Button getOpenButton() {
        return (Button)findViewById(R.id.buttonOpen);
    }


    /**
     * The color select button
     */
    private Button getSurrenderButton() {
        return (Button)findViewById(R.id.buttonSurrender);
    }


    /**
     * Handles the open valve button
     */
    public void open(@SuppressWarnings("UnusedParameters") View view){
        params.setAddPipe=false;
        getGameBoardView().setAddPipe(false);

        getPipeSelectView().setDiscard(false);
        getGameBoardView().setOpened(true);
        if(getGameBoardView().checkForLeaks(params.currentPlayer)) {
            params.won = true;
            getGameBoardView().setWon(true);
        }
        params.addEnabled=false;
        params.discardEnabled=false;
        params.openEnabled=false;

        getAddButton().setEnabled(false);
        getDiscardButton().setEnabled(params.discardEnabled);
        getOpenButton().setEnabled(params.openEnabled);


        params.surrenderString=R.string.openedValve;
        getSurrenderButton().setText(params.surrenderString);
        getGameBoardView().invalidate();
    }


    /**
     * Handles the surrender button
     */
    public void surrender(@SuppressWarnings("UnusedParameters") View view) {

        getGameBoardView().clear();
        getPipeSelectView().clear();

        Intent intent = new Intent(this, EndGame.class);

        //the pipe was fully connected and there were no leaks
        if(params.won){
            intent.putExtra("WINNER", params.currentPlayer);
            intent.putExtra("LOSER", params.otherPlayer);
        }
        //either the current player surrendered or he had leaks in his path
        else {
            intent.putExtra("WINNER", params.otherPlayer);
            intent.putExtra("LOSER", params.currentPlayer);
        }



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(getGameBoardView().getPlayingArea()!=null)
            getGameBoardView().getPlayingArea().putToBundle(PARAMETERS_PLAYINGAREA, outState);

        if(getPipeSelectView().getPipeArea() !=null)
            getPipeSelectView().getPipeArea().putToBundle(PARAMETERS_PIPESELECT, outState);

        this.putToBundle(PARAMETERS_GAMEBOARD, outState);
    }



    //disables the back button
    @Override
    public void onBackPressed() {
    }





    /**
     * The current parameters
     */
    private Parameters params = new Parameters();




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
        getAddButton().setEnabled(params.addEnabled);
        getDiscardButton().setEnabled(params.discardEnabled);
        getOpenButton().setEnabled(params.openEnabled);
        this.setTitle(params.currentPlayer);

        getSurrenderButton().setText(params.surrenderString);


        getGameBoardView().setAddPipe(params.setAddPipe);
    }




        /////////////////////////////////////////// NESTED CLASS parameters ///////////////////////

    /**
     * Parameters class for the Pipe's coordinates x, y and the rotation angle
     */
    private static class Parameters implements Serializable {

        /**
         * If the add pipes button is enabled
         */
        public boolean addEnabled=true;

        /**
         * PIf the discard button is enabled
         */
        public boolean discardEnabled=true;

        /**
         * If the open valve button is enabled
         */
        public boolean openEnabled=true;


        /**
         * Storage for the current player
         */
        public String currentPlayer;

        /**
         * Storage for the not current player
         */
        public String otherPlayer;

        /**
         * Storage for if we are on the first turn or not
         */
        public boolean firstTurn=true;

        /**
         * Storage for if we have a pipe we are trying to add
         */
        public boolean setAddPipe=true;

        /**
         * Storage for the text for the surrender button
         */
        public int surrenderString=R.string.buttonSurrender;

        /**
         * Storage for if a player has won or not
         */
        public boolean won=false;
    }


}













