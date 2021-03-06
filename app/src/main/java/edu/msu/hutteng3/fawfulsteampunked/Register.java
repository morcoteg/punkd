package edu.msu.hutteng3.fawfulsteampunked;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
    public void onCreateAccount(final View view) {

        EditText txtUsername = (EditText) findViewById(R.id.regUsername);
        final String username = txtUsername.getText().toString();

        EditText txtPassword = (EditText) findViewById(R.id.regPassword);
        final String password = txtPassword.getText().toString();

        EditText txtPasswordCheck = (EditText) findViewById(R.id.regPasswordCheck);
        String passwordCheck = txtPasswordCheck.getText().toString();


        //check if the passwords are the same
        if(!password.equals(passwordCheck))
            Toast.makeText(view.getContext(), R.string.passMissMatchError, Toast.LENGTH_SHORT).show();


        //check if the password is the correct size
        else if(password.length()<4 || password.length()>10)
            Toast.makeText(view.getContext(), R.string.passLengthError, Toast.LENGTH_SHORT).show();


        //check if the username is the correct size
        else if(username.length()<4 || username.length()>10)
            Toast.makeText(view.getContext(), R.string.userLengthError, Toast.LENGTH_SHORT).show();


        else{
            //ADD CHECK FOR USERNAME ALREADY IN DATABASE, IF IT PASSES ADD IT AND FINISH()
            //final HatterActivity activity = (HatterActivity) getActivity();
            //final HatterView view = (HatterView) activity.findViewById(R.id.hatterView);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // Create a cloud object and get the XML

                    Cloud cloud = new Cloud();
                    final boolean ok = cloud.saveToCloud(username,password, view);





                   // RegistrationIntentService thing = new RegistrationIntentService();

                   // Intent intent = new Intent();


                  //  thing.onHandleIntent(intent);





                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            //dlg.dismiss();
                            if(!ok) {
                             /*
                             * If we fail to save, display a toast
                             */
                                Toast.makeText(view.getContext(),
                                        R.string.error,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }



                    });

                }




            }).start();


            //if(ok)
                finish();
        }

    }








}
