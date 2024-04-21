package com.mygdx.tank;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mygdx.tank.model.LeaderboardEntry;

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

    @Override
    public void updateLeaderboard(String userName, int score) {
        DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard").child(userName);
        LeaderboardEntry newEntry = new LeaderboardEntry(userName, score);
        leaderboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LeaderboardEntry existingEntry = snapshot.getValue(LeaderboardEntry.class);
                    Gdx.app.log("InfoTag", "existing score:" + existingEntry.getScore());
                    if (score > existingEntry.getScore()) {
                        leaderboardRef.setValue(newEntry);
                    }
                } else {
                    Gdx.app.log("InfoTag", "new highscore:" + userName + score);
                    leaderboardRef.setValue(newEntry);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Gdx.app.log("InfoTag", "databaseError" + error.getMessage());
            }
        });
    }


}
