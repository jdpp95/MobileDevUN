package tictactoe.unal.edu.co.androidtic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class WaitingSecondPlayer extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView lblGameName;
    private String mGameName;
    private String mGameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_second_player);
        lblGameName = findViewById(R.id.lblGameName);
        Intent intent = getIntent();
        mGameId = intent.getStringExtra("game_id");
        mGameName = getIntent().getStringExtra("game_name");
        lblGameName.setText(mGameName);
        db.collection("games").document(mGameId).addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Fail", "Listen failed.", e);
                            return;
                        }

                        String board = documentSnapshot.get("board").toString();

                        if(!documentSnapshot.get("player_2").equals("") && board.equals(TicTacToeGame.getEmptyBoard())) {
                            //String id = documentSnapshot.getId();
                            Intent intent = new Intent(WaitingSecondPlayer.this, AndroidTicTacToeActivity.class);
                            intent.putExtra("game_id",mGameId);
                            intent.putExtra("multiplayer",true);
                            intent.putExtra("player_1",true);
                            startActivity(intent);
                        }
            }
        });
    }
}
