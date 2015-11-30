package edu.msu.hutteng3.fawfulsteampunked;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";

    private boolean rememberMeChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load in the username/password if REMEMBER ME
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);

        EditText usernameTxt = (EditText) findViewById(R.id.username);
        usernameTxt.setText(username);
        EditText passwordTxt = (EditText) findViewById(R.id.password);
        passwordTxt.setText(password);

        if (username == null || password == null) {
            //Prompt for username and password
        }

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

    @SuppressWarnings("WeakerAccess")
    public void setGrid(int size) {
        gridSize = size;
    }


    public void onStartGame(@SuppressWarnings("UnusedParameters") View view) {

        EditText txtDescriptionp1 = (EditText) findViewById(R.id.username);
        String username = txtDescriptionp1.getText().toString();

        EditText txtDescriptionp2 = (EditText) findViewById(R.id.password);
        String password = txtDescriptionp2.getText().toString();

        Cloud cloud = new Cloud();
        cloud.login(username, password, view, this);

    }




    public void setLogin(boolean stat,final View view){

        EditText txtDescriptionp1 = (EditText) findViewById(R.id.username);
        String username = txtDescriptionp1.getText().toString();

        EditText txtDescriptionp2 = (EditText) findViewById(R.id.password);
        String password = txtDescriptionp2.getText().toString();

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxRemember);
        if (checkBox.isChecked()) {
            //checkBox.setChecked(false);
            this.rememberMeChecked = true;

            getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(PREF_USERNAME, username)
                    .putString(PREF_PASSWORD, password)
                    .commit();
        }


        if(stat) {

            Intent intent = new Intent(this, GameSearch.class);
            intent.putExtra("PLAYER_NAME", username);
            intent.putExtra("GRID_SIZE", gridSize);
            startActivity(intent);
        }
        else{

            view.post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(view.getContext(), R.string.invalidUserPass, Toast.LENGTH_SHORT).show();
                }

            });


        }



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









    public void onRegister(@SuppressWarnings("UnusedParameters") View view) {

        Intent intent = new Intent(this, Register.class);

        startActivity(intent);

    }

}
