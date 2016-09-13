package edu.msu.hutteng3.fawfulsteampunked;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EndGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);


        Bundle extras = getIntent().getExtras();

        String winner = extras.getString("WINNER");
        String loser = extras.getString("LOSER");


        getEndGameView().setWinner(winner);
        getEndGameView().setLoser(loser);


    }


    private EndGameView getEndGameView() {

        return (EndGameView) findViewById(R.id.endGameView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_game, menu);
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

    /*
    *Handles the new game button press
     */
    public void onNewGame(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //<clears the stack


        startActivity(intent);


    }



    //disables the back button
    @Override
    public void onBackPressed() {
    }

}
