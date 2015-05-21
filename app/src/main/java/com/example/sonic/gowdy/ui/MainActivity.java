package com.example.sonic.gowdy.ui;

import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.sonic.gowdy.Kneipe;
import com.example.sonic.gowdy.R;
import com.example.sonic.gowdy.adapters.Kneipenadapter;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private String mImageUrl = "http://webuser.hs-furtwangen.de/~taubew/android/image/schwarzwaldnebel1.jpg";
    private String mCouchUrl = "https://gowdy.iriscouch.com/gowdy/_design/gowdy/_view/kneipen_all";
    private String[] mTags = {"schwarzwaldnebel1.jpg", "schwarzwaldnebel2.jpg", "sonnenuntergang.jpg"};

    private ArrayList<Kneipe> mKneipen;

    private RecyclerView.LayoutManager mLayoutManager;
//Mongo
    @InjectView(R.id.spinnerView) Spinner mSpinner;
    @InjectView(R.id.spinnerView2) Spinner mSpinner2;
    @InjectView(R.id.recyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.imageRequest) ImageView mImageRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // Couchdb


        // Get Image from request
        Picasso.with(this).load(mImageUrl).into(mImageRequest);

        mKneipen = getKneipenData();

        mRecyclerView.setHasFixedSize(true); // Not always recommended, but in this case enhances performance

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final Kneipenadapter mAdapter = new Kneipenadapter(mKneipen, this);
        mRecyclerView.setAdapter(mAdapter);

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
                ArrayList<Kneipe> kneipen = getKneipenData();
                // New kneipen array
                ArrayList<Kneipe> kneipenFiltered = new ArrayList<Kneipe>();

                switch (position) {
                    // The filter code could be outsourced in a separate method
                    case 0:
                        kneipenFiltered = kneipen;
                        break;
                    case 1:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "5") {
                                Log.i(TAG, "This is 5 stars");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                    case 2:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "4") {
                                Log.i(TAG, "This is 4 stars");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                    case 3:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "3") {
                                Log.i(TAG, "This is 3 stars");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                    case 4:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "2") {
                                Log.i(TAG, "This is 2 stars");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                    case 5:
                        for (int i = 0; i < kneipen.size(); i++) {
                            if (kneipen.get(i).getBewertung() == "1") {
                                Log.i(TAG, "This is 1 star");
                                kneipenFiltered.add(kneipen.get(i));
                            }
                        }
                        break;
                }

                mAdapter.setKneipen(kneipenFiltered);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private ArrayList<Kneipe> getKneipenData() {
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
                    JSONObject object1 = new JSONObject(jsonData);
                    Log.v(TAG, object1.toString());

                    JSONArray array1 = object1.getJSONArray("rows");
                    Log.v(TAG, array1.toString());

                    JSONObject object2 = array1.getJSONObject(0);
                    Log.v(TAG, object2.toString());

                    String string1 = object2.getString("value");
                    Log.v(TAG, string1);

                    JSONObject object3 = new JSONObject(string1);
                    Log.v(TAG, object3.toString());

                    JSONObject object4 = object3.getJSONObject("kneipen");
                    Log.v(TAG, object4.toString());


                    Iterator<String> iter = object4.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            Object value = object4.get(key);
                            Log.v(TAG, value.toString());
                            JSONObject neee = new JSONObject(value.toString());

                            String name = neee.getString("name");
                            String adresse = neee.getString("adresse");
                            String typ = neee.getString("typ");
                            String bewertung = neee.getString("bewertung");

                            Kneipe kneipe = makeKneipe(name, adresse, typ, bewertung);
                            Log.v(TAG, kneipe.toString());
                            kneipen.add(kneipe);
                            Log.v(TAG, kneipen.toString());
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.v(TAG, kneipen.toString());
        return kneipen;
    }

    private Kneipe makeKneipe(String name, String adresse, String typ, String bewertung) {
        Kneipe kneipe = new Kneipe();
        kneipe.setName("Zum Bergbrau");
        kneipe.setAdresse("Kahnum 87, Stuttgart");
        kneipe.setTyp("Gute Weine");
        kneipe.setBewertung("4");


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
