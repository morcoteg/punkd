package edu.msu.hutteng3.fawfulsteampunked;

import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Tyler on 11/21/2015.
 */
public class Cloud {

    private static final String MAGIC = "NechAtHa6RuzeR8x";
    //private static final String USER = "hutteng3";
    //private static final String PASSWORD = "azicf987";

    private static final String SAVE_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/project2/476AddUser.php";
   // private static final String LOGIN_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/project2/476Login.php";
   private static final String LOGIN_URL = "http://cse.msu.edu/~hutteng3/476/476Login.php";
    //private static final String SEARCH_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/project2/476FindGame.php";
    private static final String SEARCH_URL = "http://cse.msu.edu/~hutteng3/476/476FindGame.php";
    private static final String SAVE_GAME_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/project2/476SaveGame.php";
    private static final String GAME_STATE_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/project2/476GetGameState.php";
    private static final String JOIN_URL = "http://cse.msu.edu/~hutteng3/476/476JoinGame.php";
    private static final String LOGOUT_URL = "http://cse.msu.edu/~hutteng3/476/476Logout.php";
    private static final String GCM_URL = "http://cse.msu.edu/~hutteng3/476/gcm.php";


    private static final String UTF8 = "UTF-8";





    /**
     * Save a user to the cloud.
     * This should be run in a thread.
     * @param username name to save under
     * @param view view we are getting the data from
     * @return true if successful
     */
    public boolean saveToCloud(String username, String password, View view) {
        username = username.trim();
        if(username.length() == 0) {
            return false;
        }

        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "pipe");
            xml.attribute(null, "magic", MAGIC);


            xml.startTag(null, "user");
            xml.attribute(null, "username", username);
            xml.attribute(null, "password", password);
            xml.endTag(null, "user");

            xml.endTag(null, "pipe");

            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        final String xmlStr = writer.toString(); //may need to be above return




        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }

        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();


        InputStream stream = null;
        try {
            URL url = new URL(SAVE_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();


            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "pipe");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }




    private volatile boolean success=false;



    /**
     * Log a user in from the cloud.
     * This should be run in a thread.
     * @param username name to save under
     * @param view view we are getting the data from
     * @return true if successful
     */
    public void login(final String username, final String password,final String token,final View view, final MainActivity main) {
                /*
         * Create a thread to load the user from the cloud
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML

                InputStream stream = null;

               String query = LOGIN_URL + "?user=" + username + "&magic=" + MAGIC +"&token=" + token + "&pw=" + password;

                try {
                    URL url = new URL(query);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();
                    if(responseCode != HttpURLConnection.HTTP_OK) {
                        stream = null;
                    }
                    else
                        stream = conn.getInputStream();

                }
                catch (MalformedURLException e) {
                    // Should never happen
                    stream = null;
                }
                catch (IOException ex) {
                    stream = null;
                }

                // Test for an error
                boolean fail = stream == null;
                if (!fail) {
                    try {

                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "pipe");
                        String status = xml.getAttributeValue(null, "status");
                        if (status.equals("yes")) {

                            while (xml.nextTag() == XmlPullParser.START_TAG) {
                                if (xml.getName().equals("user")) {

                                    //check if the username and password match a record in the database
                                    success =xml.getAttributeValue(null, "stat").equals("yes");
                                    main.setLogin(success, view);
                                    break;
                                }

                                Cloud.skipToEndTag(xml);
                            }
                        }
                        else {
                            fail = true;
                        }

                    }
                    catch (IOException ex) {
                        fail = true;
                    }
                    catch (XmlPullParserException ex) {
                        fail = true;
                    }
                    finally {
                        try {
                            stream.close();
                        }
                        catch (IOException ex) {
                        }
                    }
                }


                final boolean fail1 = fail;
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        if (fail1) {
                            Toast.makeText(view.getContext(),
                                    R.string.error,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

            }
        }).start();


    }






    /**
     * Log a user in from the cloud.
     * This should be run in a thread.
     * @param username name to save under
     * @param view view we are getting the data from
     * @return true if successful
     */
    public void findGame(final String username,final int gridSize, final View view, final GameSearch search) {
                /*
         * Create a thread to load the user from the cloud
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML

                InputStream stream = null;

                String query = SEARCH_URL + "?user=" + username + "&gridSize=" + gridSize +"&magic=" + MAGIC;

                try {
                    URL url = new URL(query);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();
                    if(responseCode != HttpURLConnection.HTTP_OK) {
                        stream = null;
                    }
                    else
                        stream = conn.getInputStream();

                }
                catch (MalformedURLException e) {
                    // Should never happen
                    stream = null;
                }
                catch (IOException ex) {
                    stream = null;
                }

                // Test for an error
                boolean fail = stream == null;
                if (!fail) {
                    try {

                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "pipe");
                        String status = xml.getAttributeValue(null, "status");
                        if (status.equals("yes")) {

                            while (xml.nextTag() == XmlPullParser.START_TAG) {
                                if (xml.getName().equals("user")) {

                                    //check if the username and password match a record in the database
                                    String user =xml.getAttributeValue(null, "username");
                                    String id=xml.getAttributeValue(null, "id");
                                    String token=xml.getAttributeValue(null, "token");
                                    if(!(user.equals("no")))
                                        search.startNewGame(user,id,token);

                                    break;
                                }

                                Cloud.skipToEndTag(xml);
                            }
                        }
                        else {
                            fail = true;
                        }

                    }
                    catch (IOException ex) {
                        fail = true;
                    }
                    catch (XmlPullParserException ex) {
                        fail = true;
                    }
                    finally {
                        try {
                            stream.close();
                        }
                        catch (IOException ex) {
                        }
                    }
                }


                final boolean fail1 = fail;
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        if (fail1) {
                            Toast.makeText(view.getContext(),
                                    R.string.error,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

            }
        }).start();


    }
















    /**
     * Save a hatting to the cloud.
     * This should be run in a thread.
     * @return true if successful
     */
    public boolean saveGameState(GameBoard activity) {


        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "board");

            xml.attribute(null, "magic", MAGIC);

            activity.saveXml(xml);

            xml.endTag(null, "board");

            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        final String xmlStr = writer.toString(); //may need to be above return




        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }

        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();


        InputStream stream = null;
        try {
            URL url = new URL(SAVE_GAME_URL);


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();


            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "board");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }







    /**
     * Log a user in from the cloud.
     * This should be run in a thread.
     * @param id the id of the game we are playing
     * @param view view we are getting the data from
     * @return true if successful
     */
    public void getTurn(final String id, final View view, final GameBoard activity) {
                /*
         * Create a thread to load the user from the cloud
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML

                InputStream stream = null;

                String query = GAME_STATE_URL + "?id=" + id + "&magic=" + MAGIC;

                try {
                    URL url = new URL(query);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();
                    if(responseCode != HttpURLConnection.HTTP_OK) {
                        stream = null;
                    }
                    else
                        stream = conn.getInputStream();

                }
                catch (MalformedURLException e) {
                    // Should never happen
                    stream = null;
                }
                catch (IOException ex) {
                    stream = null;
                }

                // Test for an error
                boolean fail = stream == null;
                if (!fail) {
                    try {

                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "board");
                        String status = xml.getAttributeValue(null, "status");
                        if (status.equals("yes")) {

                            while (xml.nextTag() == XmlPullParser.START_TAG) {
                                if (xml.getName().equals("state")) {

                                    //check if the username and password match a record in the database
                                    String grid =xml.getAttributeValue(null, "grid");
                                    String list=xml.getAttributeValue(null, "list");
                                    String opened=xml.getAttributeValue(null, "opened");
                                   // if(!(user.equals("no")))
                                    activity.populateGame(grid,list,opened);

                                    break;
                                }

                                Cloud.skipToEndTag(xml);
                            }
                        }
                        else {
                            fail = true;
                        }

                    }
                    catch (IOException ex) {
                        fail = true;
                    }
                    catch (XmlPullParserException ex) {
                        fail = true;
                    }
                    finally {
                        try {
                            stream.close();
                        }
                        catch (IOException ex) {
                        }
                    }
                }


                final boolean fail1 = fail;
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        if (fail1) {
                            Toast.makeText(view.getContext(),
                                    R.string.error,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

            }
        }).start();


    }











    /**
     * Log a user in from the cloud.
     * This should be run in a thread.
     * @param username name to save under
     * @param view view we are getting the data from
     * @return true if successful
     */
    public void joinGame(final String username, final View view, final GameSearch search) {
                /*
         * Create a thread to load the user from the cloud
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML

                InputStream stream = null;

                String query = JOIN_URL + "?user=" + username + "&magic=" + MAGIC;

                try {
                    URL url = new URL(query);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();
                    if(responseCode != HttpURLConnection.HTTP_OK) {
                        stream = null;
                    }
                    else
                        stream = conn.getInputStream();

                }
                catch (MalformedURLException e) {
                    // Should never happen
                    stream = null;
                }
                catch (IOException ex) {
                    stream = null;
                }

                // Test for an error
                boolean fail = stream == null;
                if (!fail) {
                    try {

                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "pipe");
                        String status = xml.getAttributeValue(null, "status");
                        if (status.equals("yes")) {

                            while (xml.nextTag() == XmlPullParser.START_TAG) {
                                if (xml.getName().equals("user")) {

                                    //check if the username and password match a record in the database
                                    String user =xml.getAttributeValue(null, "username");
                                    String id=xml.getAttributeValue(null, "id");
                                    String token=xml.getAttributeValue(null, "token");

                                    search.joinGame(user, id, token);
                                    break;
                                }

                                Cloud.skipToEndTag(xml);
                            }
                        }
                        else {
                            fail = true;
                        }

                    }
                    catch (IOException ex) {
                        fail = true;
                    }
                    catch (XmlPullParserException ex) {
                        fail = true;
                    }
                    finally {
                        try {
                            stream.close();
                        }
                        catch (IOException ex) {
                        }
                    }
                }


                final boolean fail1 = fail;
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        if (fail1) {
                            Toast.makeText(view.getContext(),
                                    R.string.error,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

            }
        }).start();


    }






    /**
     * Log a user in from the cloud.
     * This should be run in a thread.
     * @param view view we are getting the data from
     * @return true if successful
     */
    public void sendMessage(final String token, final String message, final View view) {
                /*
         * Create a thread to load the user from the cloud
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML

                InputStream stream = null;

                String query = GCM_URL + "?token=" + token + "&message=" + message;

                try {
                    URL url = new URL(query);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();
                    if(responseCode != HttpURLConnection.HTTP_OK) {
                        stream = null;
                    }
                    else
                        stream = conn.getInputStream();

                }
                catch (MalformedURLException e) {
                    // Should never happen
                    stream = null;
                }
                catch (IOException ex) {
                    stream = null;
                }

                // Test for an error
                boolean fail = stream == null;
                try {
                    stream.close();
                }
                catch (IOException ex) {
                }


                final boolean fail1 = fail;
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        if (fail1) {
                            Toast.makeText(view.getContext(),
                                    R.string.error,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

            }
        }).start();


    }



    /**
     * Log a user in from the cloud.
     * This should be run in a thread.
     * @param view view we are getting the data from
     * @return true if successful
     */
    public void logout(final String username, final View view) {
                /*
         * Create a thread to load the user from the cloud
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a cloud object and get the XML

                InputStream stream = null;

                String query = LOGOUT_URL + "?username=" + username + "&magic=" + MAGIC;

                try {
                    URL url = new URL(query);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();
                    if(responseCode != HttpURLConnection.HTTP_OK) {
                        stream = null;
                    }
                    else
                        stream = conn.getInputStream();

                }
                catch (MalformedURLException e) {
                    // Should never happen
                    stream = null;
                }
                catch (IOException ex) {
                    stream = null;
                }

                // Test for an error
                boolean fail = stream == null;
                try {
                    stream.close();
                }
                catch (IOException ex) {
                }


                final boolean fail1 = fail;
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        if (fail1) {
                            Toast.makeText(view.getContext(),
                                    R.string.error,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

            }
        }).start();


    }

    /**
     * Skip the XML parser to the end tag for whatever
     * tag we are currently within.
     * @param xml the parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml)
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG &&
                tag != XmlPullParser.END_DOCUMENT);
    }





}


