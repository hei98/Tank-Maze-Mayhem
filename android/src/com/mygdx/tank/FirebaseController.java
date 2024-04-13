package com.mygdx.tank;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseController implements FirebaseInterface {
    FirebaseController() {
        //adding test data to the database
        // add user
//        Map<String, Object> userEntry = new HashMap<>();
//        userEntry.put("username", "heidihra");
//        userEntry.put("email", "heidihra@stud.ntnu.no");
//        userEntry.put("usertype", "user");
//
//        Map<String, Object> userEntry2 = new HashMap<>();
//        userEntry.put("username", "vithusas");
//        userEntry.put("email", "vithusas@stud.ntnu.no");
//        userEntry.put("usertype", "user");
//
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
//        userRef.push().setValue(userEntry);
//        userRef.push().setValue(userEntry2);
//
//        // add data to leaderboard
//        Map<String, Object> leaderboardEntry = new HashMap<>();
//        leaderboardEntry.put("username", "heidihra");
//        leaderboardEntry.put("score", 5000);
//        Map<String, Object> leaderboardEntry2 = new HashMap<>();
//        leaderboardEntry.put("username", "vithusas");
//        leaderboardEntry.put("score", 8000);
//
//        DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard");
//        leaderboardRef.push().setValue(leaderboardEntry);
//        leaderboardRef.push().setValue(leaderboardEntry2);
    }
    @Override
    public void setValue(String reference, int value, String user) {
//        Map<String, Object> leaderboardEntry = new HashMap<>();
//        leaderboardEntry.put("username", user);
//        leaderboardEntry.put("score", value);
//
//        DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference(reference);
//        leaderboardRef.push().setValue(leaderboardEntry);
    }

    @Override
    public void getValue(String reference, final FirebaseDataListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(reference);
        // Read from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void getLeaderboardData(FirebaseDataListener listener) {
        DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard");
        leaderboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    LeaderboardEntry entry = childSnapshot.getValue(LeaderboardEntry.class);
                    entry.setKey(childSnapshot.getKey());
                    leaderboardEntries.add(entry);
                }
                listener.onDataReceived(leaderboardEntries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }
}
