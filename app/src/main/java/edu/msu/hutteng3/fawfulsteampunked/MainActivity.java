package edu.msu.hutteng3.fawfulsteampunked;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*
         * Set up the spinner
         */

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grid_spinner, android.R.layout.simple_spinner_item);

        // Apply the adapter to the spinner
        getSpinner().setAdapter(adapter);


        getSpinner().setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int pos, long id) {
                setGrid(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

    }


    /**
     * The grid choice spinner
     */
    private Spinner getSpinner() {
        return (Spinner) findViewById(R.id.spinnerGrid);
    }


    /**
     * Request code when selecting a gid size
     */
    private int gridSize = 0;

    public void setGrid(int size) {
        gridSize = size;
    }


    public void onStartGame(View view) {

        Intent intent = new Intent(this, GameBoard.class);

        EditText txtDescriptionp1 = (EditText) findViewById(R.id.player1);
        String p1 = txtDescriptionp1.getText().toString();
        intent.putExtra("PLAYER_1_NAME", p1);

        EditText txtDescriptionp2 = (EditText) findViewById(R.id.player2);
        String p2 = txtDescriptionp2.getText().toString();
        intent.putExtra("PLAYER_2_NAME", p2);

        intent.putExtra("GRID_SIZE",gridSize);

        startActivity(intent);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
     * Handle a How To button press
     * @param view The current view
     */
    public void onHowTo(View view) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(view.getContext());



        // Parameterize the builder
        builder.setTitle(R.string.howToTitle);
        builder.setMessage(R.string.rules);
        builder.setPositiveButton(android.R.string.ok, null);

        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
