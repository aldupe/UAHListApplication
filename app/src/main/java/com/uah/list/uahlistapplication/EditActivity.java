package com.uah.list.uahlistapplication;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMObjectResult;

/**
 * Created by ADPM on 30/07/2015.
 */
public class EditActivity extends Activity {
    String originalItem;
    int location;
    UAHListApplication blApplication;
    List<Item> itemList;
    public static final String CLASS_NAME = "EditActivity";

    @Override
    /**
     * onCreate called when edit activity is created.
     *
     * Sets up the application, sets listeners, and gets intent info from calling activity.
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/* Get application context, item list. */
        blApplication = (UAHListApplication) getApplicationContext();
        itemList = blApplication.getItemList();
        setContentView(R.layout.activity_edit);

		/* Information required to edit item. */
        Intent intent = getIntent();
        originalItem = intent.getStringExtra("ItemText");
        location = intent.getIntExtra("ItemLocation", 0);
        EditText itemToEdit = (EditText) findViewById(R.id.itemToEdit);
        itemToEdit.setText(originalItem);

		/* Set key listener for edittext (done key to accept item to list). */
        itemToEdit.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    finishedEdit(v);
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * On completion of edit, edit itemList, return to main activity with edit return code.
     */
    public void finishedEdit(View v) {
        Item item = itemList.get(location);
        EditText itemToEdit = (EditText) findViewById(R.id.itemToEdit);
        String text = itemToEdit.getText().toString();
        item.setName(text);
        /**
         * IBMObjectResult is used to handle the response from the server after
         * either creating or saving an object.
         *
         * onResult is called if the object was successfully saved.
         * onError is called if an error occurred saving the object.
         */
        item.save().continueWith(new Continuation<IBMDataObject, Void>() {

            @Override
            public Void then(Task<IBMDataObject> task) throws Exception {
                if(task.isCancelled()) {
                    Log.e(CLASS_NAME, "Exception : " + task.toString() + " was cancelled.");
                }

                else if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                }

                else {
                    Intent returnIntent = new Intent();
                    setResult(UAHListApplication.EDIT_ACTIVITY_RC, returnIntent);
                    finish();
                }
                return null;
            }

        },Task.UI_THREAD_EXECUTOR);

    }
}

