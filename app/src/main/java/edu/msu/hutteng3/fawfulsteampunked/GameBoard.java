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

public class GameBoard extends AppCompatActivity {



    /*
* The ID values for each of the hat types. The values must
* match the index into the array hats_spinner in arrays.xml.
*/
    public static final int GRID_5 = 0;
    public static final int GRID_10 = 1;
    public static final int GRID_20 = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);




        Bundle extras = getIntent().getExtras();

        String p1= extras.getString("PLAYER_1_NAME");
        String p2= extras.getString("PLAYER_2_NAME");
        int gridSize=extras.getInt("GRID_SIZE");


        player1name=p1;
        player2name=p2;

        //set these to initially be p1 for curerent and p2 for other to give the first player the first move
        currentPlayer=p1;
        otherPlayer=p2;

        getGameBoardView().setPlayer1name(p1);
        getGameBoardView().setPlayer2name(p2);
        getGameBoardView().setScale(gridSize);
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
     * The hatter view object
     */
    private GameBoardView getGameBoardView() {
        return (GameBoardView) findViewById(R.id.gameBoardView);
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


    public void newTurn(){
        //Needs to be moved somewhere else to not have the player name come up as NULL
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        // Parameterize the builder
        builder.setTitle(currentPlayer + "\'s turn");



        String thing=player1name;
        builder.setItems(new CharSequence[]
                        {"Add a pipe", "Delete a pipe", "Surrender", "Open valve"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                //add
                                // Toast.makeText(hope, "clicked 1",0).show();
                                int x=0;
                                break;
                            case 1:
                                //delete
                                //Toast.makeText(hope, "clicked 2", 0).show();
                                break;
                            case 2:
                                //surrender
                                surrender(otherPlayer,currentPlayer);
                                break;
                            case 3:
                                //open valve
                                //Toast.makeText(hope, "clicked 4", 0).show();
                                break;
                        }
                    }
                });
        builder.create().show();
    }





    public void surrender(String winner, String loser) {
        Intent intent = new Intent(this, EndGame.class);


        intent.putExtra("WINNER", winner);

        intent.putExtra("LOSER", loser);


        startActivity(intent);

    }



}



