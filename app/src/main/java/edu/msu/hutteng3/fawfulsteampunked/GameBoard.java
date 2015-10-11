package edu.msu.hutteng3.fawfulsteampunked;

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










}



