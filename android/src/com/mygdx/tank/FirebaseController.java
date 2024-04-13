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

public class FirebaseController implements FirebaseInterface {
    FirebaseController() {

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
