package edu.msu.hutteng3.fawfulsteampunked;

import android.util.Log;
import android.util.Xml;
import android.view.View;

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
    private static final String USER = "hutteng3";
    private static final String PASSWORD = "azicf987";
    //private static final String CATALOG_URL = "https://facweb.cse.msu.edu/cbowen/cse476x/hatter-cat.php";
   // private static final String CATALOG_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/step6/hatter-cat.php";
    //private static final String SAVE_URL = "https://facweb.cse.msu.edu/cbowen/cse476x/hatter-save.php";
    private static final String SAVE_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/project2/476AddUser.php";
    //private static final String DELETE_URL = "https://facweb.cse.msu.edu/cbowen/cse476x/hatter-delete.php";
  //  private static final String DELETE_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/step6/hatter-delete.php";
    //private static final String LOAD_URL = "https://facweb.cse.msu.edu/cbowen/cse476x/hatter-load.php";
    //private static final String LOAD_URL = "http://webdev.cse.msu.edu/~hutteng3/cse476/step6/hatter-load.php";
    private static final String UTF8 = "UTF-8";


    /**
     * Save a hatting to the cloud.
     * This should be run in a thread.
     *
     * @param username name to save under
     * @param password given password
     * @param view     view we are getting the data from
     * @return true if successful
     */
   /* public boolean saveToCloud(String username, String password, View view) {
        username = username.trim();
        if (username.length() == 0) {
            return false;
        }

        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "pipe");
            xml.attribute(null, "user", USER);
            xml.attribute(null, "pw", PASSWORD);
            xml.attribute(null, "magic", MAGIC);

            xml.startTag(null, "pipes");

            xml.attribute(null, "username", username);
            xml.attribute(null, "password", password);

            xml.endTag(null, "pipes");

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
   /*     String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }

        /*
         * Send the data to the server
         */
     /*   byte[] postData = postDataStr.getBytes();


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
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }
/*
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                //Log.i("hatter", line);

            }
*/
      /*      stream = conn.getInputStream();


            /**
             * Create an XML parser for the result
             */
      /*      try {
               XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "pipe");

                String status = xmlR.getAttributeValue(null, "status");
                if (status.equals("no")) {
                    return false;
                }

                // We are done
            } catch (XmlPullParserException ex) {
                return false;
            } catch (IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }*/








    /**
     * Save a hatting to the cloud.
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

            xml.startTag(null, "hatter");
            xml.attribute(null, "user", USER);
            xml.attribute(null, "pw", PASSWORD);
            xml.attribute(null, "magic", MAGIC);


            xml.startTag(null, "hatting");
            xml.attribute(null, "username", username);
            xml.attribute(null, "password", password);
            xml.endTag(null, "hatting");

            xml.endTag(null, "hatter");

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
/*
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                //Log.i("hatter", line);

            }
*/
            stream = conn.getInputStream();


            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "hatter");

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






}


