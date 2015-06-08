package com.example.sonic.gowdy.ui;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sonic.gowdy.GowdyConstants;
import com.example.sonic.gowdy.Kneipe;
import com.example.sonic.gowdy.R;
import com.example.sonic.gowdy.adapters.Kneipenadapter;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.StatusLine;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private String mImageUrl = "http://webuser.hs-furtwangen.de/~taubew/android/image/schwarzwaldnebel1.jpg";
    private String mCouchUrl = "https://gowdy.iriscouch.com/gowdy/_design/gowdy/_view/alleKneipen";
    private String[] mTags = {"schwarzwaldnebel1.jpg", "schwarzwaldnebel2.jpg", "sonnenuntergang.jpg"};

    private ArrayList<Kneipe> mKneipen = null;
    private ArrayList<Kneipe> mKneipenFiltered = null;

    private RecyclerView.LayoutManager mLayoutManager;

    static Uri mMediaUri;

    @InjectView(R.id.spinnerView) Spinner mSpinner;
    @InjectView(R.id.spinnerView2) Spinner mSpinner2;
    @InjectView(R.id.viewMapButton) Button mViewMapButton;
    @InjectView(R.id.takePhotoButton) Button mTakePhotoButton;
    @InjectView(R.id.imageRequest) ImageView mImageRequest;
    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // Couchdb

        // Get Image from request
        Picasso.with(this).load(mImageUrl).into(mImageRequest);

        setKneipenData();

        // Set on click listeners
        mViewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mMediaUri = getOutputMediaFileUri();

                if (mMediaUri == null) {
                    // display an error
                    Toast.makeText(MainActivity.this, "There was a problem accessing your device's external storage.", Toast.LENGTH_LONG).show();
                }
                else {
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takePhotoIntent, 222); // a request code is the second parameter
                }
            }
        });
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, String.format("value=%d", position));

                if (position != 0) {
                    loadItemsInSpinner2(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Filter
                if (position != 0) {
                    updateKneipenData("spinner2", position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


// Post data
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Create json doc
                try {
                    JSONObject parent = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put("lv1");
                    jsonArray.put("lv2");

                    jsonObject.put("mk1", "mv1");
                    jsonObject.put("mk2", jsonArray);
                    parent.put("k2", jsonObject);
                    Log.d("output", parent.toString(2));

                    // "https://gowdy.iriscouch.com/_utils/database.html?", "gowdy", parent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        // Post doc
        JSONObject jsonDoc = new JSONObject();
        try {
            jsonDoc.put("name", "irgendwas"); // erstellt Name=Value‐Paar jsonDoc.put("latitude", 58.0);
            jsonDoc.put("longitude", 7.88);
        } catch (JSONException e) {
            e.printStackTrace();
        } // end try

        String body = jsonDoc.toString();
        try {
            StringEntity entity = new StringEntity(body);
            HttpPost httpPost = new HttpPost("/home/ubuntu/hosting/servers/gowdy/db");
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //JSONperHTTP jsonHTTP = new JSONperHTTP(); // eigene Hilfsklasse
        //String returnPost = jsonHTTP.makeHttpPost(URL_KNEIPEN_DB, "POST", null, body);


        // Post image
        /*if (requestCode == GowdyConstants.IMAGE_CODE) {

            File file = new File(mMediaUri.getPath());

            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] byte_arr = stream.toByteArray();

            String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            //params.add(new BasicNameValuePair("image", image_str));

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut("https://gowdy.iriscouch.com/_utils/database.html?gowdy");
            ByteArrayEntity entity = new ByteArrayEntity(byte_arr);
            entity.setContentType("image/jpeg");
            httpPut.setEntity(entity);

            String rev = getAlbumRev(url+album_id, httpClient);
            String attachURL = url+album_id+"/"+imgUuid;
            if (rev != null && !rev.trim().isEmpty()) {
                attachURL += "?rev=" + rev + "&_rev=" + rev;
                Log.v("CouchDB", attachURL);
                try {
                    httpPut.setURI(new URI(attachURL));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                HttpResponse httpResponse = null;
                try {
                    httpResponse = httpClient.execute(httpPut);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                org.apache.http.StatusLine stat = httpResponse.getStatusLine();
                String result = stat.toString();
            }

            // Get the file
            *//*File file = new File(mMediaUri.getPath());

            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
            Log.v(TAG + " bitMap", bitmap.toString());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);*//*


        }*/

        // Upload with Okhttp?

    }

    private Uri getOutputMediaFileUri() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (isExternalStorageAvailable()) {
            // Get the URI

            // 1. Get external storage directory
            String appName = getString(R.string.app_name);
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);
            // 2. Create our subdirectory
            if (! mediaStorageDir.exists()) {
                if (! mediaStorageDir.mkdirs()) {
                    Log.e("CameraOptionsDialog", "Failed to create directory"); // define tag
                    return null;
                }
            }
            // 3. Create a file name

            // 4. Create the file
            File mediaFile;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now); // Compare to approach of Stormy

            String path = mediaStorageDir.getPath() + File.separator;
            mediaFile = new File(path + "IMG_" + timestamp + ".jpg");

            Log.d("CameraOptionsDialog", "File: " + Uri.fromFile(mediaFile));

            // 5. Return the file's URI
            return Uri.fromFile(mediaFile);
        } else {
            return null;
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) { // equals is the string comparator
            return true;
        }
        else {
            return false;
        }
    }

    private void updateKneipenData(String caller, int position) {
        mKneipenFiltered = new ArrayList<Kneipe>();

        switch (position) {
            case 0:
                mKneipenFiltered = mKneipen;
                break;
            case 1:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("5")) {
                        Log.i(TAG, "This is 5 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 2:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("4")) {
                        Log.i(TAG, "This is 4 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 3:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("3")) {
                        Log.i(TAG, "This is 3 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 4:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("2")) {
                        Log.i(TAG, "This is 2 stars");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
            case 5:
                for (int i = 0; i < mKneipen.size(); i++) {
                    if (mKneipen.get(i).getBewertung().equals("1")) {
                        Log.i(TAG, "This is 1 star");
                        mKneipenFiltered.add(mKneipen.get(i));
                    }
                }
                break;
        }

        updateDisplay(mKneipenFiltered);
    }

    public void loadItemsInSpinner2(int position) {
        ArrayList<String> list = new ArrayList<String>();

        switch (position) {
            case 0:
                Log.i(TAG, "Auswählen");
                list.add("Zuerst Kategorie wählen");
                break;
            case 1:
                Log.i(TAG, "Sterne ausgewählt");
                list.add("Filter auswählen");
                list.add("5");
                list.add("4");
                list.add("3");
                list.add("2");
                list.add("1");
                break;
            case 2:
                Log.i(TAG, "Entfernung ausgewählt");
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner2.setAdapter(adapter);
    }

    private void setKneipenData() {
        final ArrayList<Kneipe> kneipen = new ArrayList<Kneipe>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(mCouchUrl).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // Error
                Log.e(TAG, e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // Success
                String jsonData = response.body().string();
                try {
                    JSONObject jsonMain = new JSONObject(jsonData);
                    Log.v(TAG, jsonMain.toString());

                    JSONArray jsonRows = jsonMain.getJSONArray("rows");
                    Log.v(TAG, jsonRows.toString());

                    for (int i = 0; i < jsonRows.length(); i++) {
                        Log.v(TAG + " KneipenArray", jsonRows.get(i).toString());

                        JSONObject jsonKneipe = jsonRows.getJSONObject(i);

                        String value = jsonKneipe.getString("value");

                        JSONObject jsonValue = new JSONObject(value);
                        Log.v(TAG, jsonValue.toString());

                        String name = jsonValue.getString("name");
                        String adresse = jsonValue.getString("adresse");
                        String typ = jsonValue.getString("kneipen_typ");
                        String bewertung = jsonValue.getString("bewertung");

                        Kneipe kneipe = makeKneipe(name, adresse, typ, bewertung);
                        Log.v(TAG, kneipe.toString());

                        kneipen.add(kneipe);
                        Log.v(TAG, kneipen.toString());

                        if (kneipen != null) {
                            updateDisplay(kneipen);
                        }

                        mKneipen = kneipen;
                        Log.v(TAG + " mKneipen", mKneipen.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateDisplay(final ArrayList<Kneipe> kneipen) {
        Log.i(TAG, kneipen.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setHasFixedSize(true); // Not always recommended, but in this case enhances performance

                mLayoutManager = new LinearLayoutManager(MainActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                final Kneipenadapter mAdapter = new Kneipenadapter(kneipen, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    private Kneipe makeKneipe(String name, String adresse, String typ, String bewertung) {
        Kneipe kneipe = new Kneipe();
        kneipe.setName(name);
        kneipe.setAdresse(adresse);
        kneipe.setTyp(typ);
        kneipe.setBewertung(bewertung);

        return kneipe;
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
}
