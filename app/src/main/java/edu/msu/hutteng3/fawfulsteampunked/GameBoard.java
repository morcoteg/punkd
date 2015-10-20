package edu.msu.hutteng3.fawfulsteampunked;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GameBoard extends AppCompatActivity {



    /*
* The ID values for each of the hat types. The values must
* match the index into the array hats_spinner in arrays.xml.
*/
    public static final int GRID_5 = 0;
    public static final int GRID_10 = 1;
    public static final int GRID_20 = 2;

    private static final String PARAMETERS = "parameters";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);


        Bundle extras = getIntent().getExtras();

        String p1 = extras.getString("PLAYER_1_NAME");
        String p2 = extras.getString("PLAYER_2_NAME");
        int gridSize = extras.getInt("GRID_SIZE");


        player1name = p1;
        player2name = p2;

        //set these to initially be p1 for curerent and p2 for other to give the first player the first move
        currentPlayer = p1;
        otherPlayer = p2;

        getGameBoardView().setPlayer1name(p1);
        getGameBoardView().setPlayer2name(p2);
        getGameBoardView().setGridSize(gridSize);

        //give the pipe select view a path to the game board view
        getPipeSelectView().setGameBoardView(getGameBoardView());

        /*
        *   Save any state
        */
        if (savedInstanceState !=  null){
            getGameBoardView().getPlayingArea().getFromBundle(PARAMETERS, savedInstanceState);

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



    public void setP1Name(){
        getGameBoardView().setPlayer1name(Integer.toString(R.id.player1));
    }

    public void setP2Name(View view){
        getGameBoardView().setPlayer2name(Integer.toString(R.id.player2));
    }



    private String player1name;
    private String player2name;
    private String currentPlayer;
    private String otherPlayer;






    public void switchTurn(){
        getGameBoardView().invalidate();
        getPipeSelectView().invalidate();
        String temp=currentPlayer;
        currentPlayer=otherPlayer;
        otherPlayer=temp;

        getGameBoardView().setAddPipe(true);

        Toast toast = Toast.makeText(this, currentPlayer + "\'s turn", Toast.LENGTH_LONG);
        toast.show();
    }




    public void addPipe(View view){
        getGameBoardView().setAddPipe(true);
        getPipeSelectView().setDiscard(false);

        if(getGameBoardView().addToGrid(this)) {
            switchTurn();
        }
    }



    public void discardPipe(View view) {
        getPipeSelectView().setDiscard(true);
        getGameBoardView().setPipeToAdd(null);
    }



    public void open(View view){
        getGameBoardView().setAddPipe(false);
        getPipeSelectView().setDiscard(false);
        getGameBoardView().setOpened(true, currentPlayer);
        if(!getGameBoardView().checkForLeaks(currentPlayer))
           //getGameBoardView().getLeaks();

        getGameBoardView().invalidate();
    }



    public void surrender(View view) {
        Intent intent = new Intent(this, EndGame.class);

        intent.putExtra("WINNER", otherPlayer);
        intent.putExtra("LOSER", currentPlayer);

        startActivity(intent);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getGameBoardView().getPlayingArea().putToBundle(PARAMETERS, outState);
    }

}



