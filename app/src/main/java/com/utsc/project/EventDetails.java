package com.utsc.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EventDetails extends AppCompatActivity {

    void addEventButton(Event e) {
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.eventlistlayout);
        Context c = this;
        Button b = new Button(this);
        b.setText(e.name);
        //e.loadAttendeesFromDB();
        buttonContainer.addView(b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent intent = getIntent();
        String message = intent.getStringExtra("com.utsc.project.VENUENAME");
        TextView textView = findViewById(R.id.eventName);
        textView.setText("Events for " + message);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Event e = child.getValue(Event.class);
                    for (DataSnapshot currentAttendee : child.child("attendees").getChildren()) {
                        User u = currentAttendee.getValue(User.class);
                        if (u == null) {
                            e.addAttendee(u.id);
                        }
                    }
                    Intent intent = getIntent();
                    Integer id = intent.getIntExtra("com.utsc.project.VENUEID", 0);
                    if (e.venueid == id) {
                        addEventButton(e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.listEvents(listener);
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, VenueDisplay.class);
        startActivity(intent);
    }
}