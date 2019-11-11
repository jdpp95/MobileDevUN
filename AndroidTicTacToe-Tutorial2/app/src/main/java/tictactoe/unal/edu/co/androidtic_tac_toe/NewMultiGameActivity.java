package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class NewMultiGameActivity extends AppCompatActivity implements View.OnClickListener {

    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText txtNewGame, txtUsername;
    private String mUsername, mGameName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        txtNewGame = findViewById(R.id.txtMultiname);
        txtUsername = findViewById(R.id.txtNewUsername);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.createMulti:
                mUsername = txtUsername.getText().toString();

                db.collection("users").whereEqualTo("username", mUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().size() > 0)
                                Toast.makeText(NewMultiGameActivity.this, "This username already exists", Toast.LENGTH_SHORT).show();
                            else {
                                addUser();
                            }
                        } else {
                            Toast.makeText(NewMultiGameActivity.this, "Cannot validate names :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
        }
        /*
        if(intent != null){
            startActivity(intent);
        }*/
    }

    private void addUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", mUsername);
        db.collection("users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    addGame(task.getResult().getId());
                }
            }
        });
    }

    private void addGame(String player1Id) {
        //Add game
        Map<String, String> game = new HashMap<>();
        mGameName = txtNewGame.getText().toString();
        game.put("name", mGameName);
        game.put("player_1", player1Id);
        game.put("player_2","");
        game.put("player_in_turn","1");
        game.put("board", TicTacToeGame.getEmptyBoard());
        game.put("winner_status", "0");

        db.collection("games").add(game).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intent = new Intent(NewMultiGameActivity.this, WaitingSecondPlayer.class);

                String gameId = documentReference.getId();
                intent.putExtra("game_id", gameId);

                Log.d("Sucess!!", "DocumentSnapshot added with ID: " + gameId);
                startActivity(intent);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failure!!", "Error adding document", e);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
