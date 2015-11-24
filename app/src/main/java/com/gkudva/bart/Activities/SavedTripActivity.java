package com.gkudva.bart.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gkudva.bart.Adapters.BartSavedTripAdapter;
import com.gkudva.bart.Models.BartSugarCRMModel;
import com.gkudva.bart.R;

import java.util.ArrayList;

public class SavedTripActivity extends AppCompatActivity {

    private ListView lvSavedTrip;
    private BartSavedTripAdapter adapter;
    private ArrayList<BartSugarCRMModel> bartSugarCRMModellist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bartSugarCRMModellist = (ArrayList)BartSugarCRMModel.listAll(BartSugarCRMModel.class);
        adapter = new BartSavedTripAdapter(this, bartSugarCRMModellist);
        lvSavedTrip = (ListView) findViewById(R.id.lvSavedTrip);
        lvSavedTrip.setAdapter(adapter);

        setupListView();
    }

    public void setupListView()
    {
        lvSavedTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final BartSugarCRMModel selectedModel = (BartSugarCRMModel) adapter.getItem(position);
                new AlertDialog.Builder(SavedTripActivity.this)
                        .setTitle("TRIP")
                        .setMessage("Origin: " + selectedModel.getOrigin() + "\nDestination: " + selectedModel.getDestination())
                        .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("Origin", selectedModel.getOrigin());
                                returnIntent.putExtra("Destination", selectedModel.getDestination());
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {

                                    BartSugarCRMModel.deleteAll(BartSugarCRMModel.class, "identifier = ?", selectedModel.getIdentifier());
                                    bartSugarCRMModellist.remove(selectedModel);
                                    adapter.remove(pos);

                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    dialog.cancel();

                                } catch (SQLiteException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
