package tictactoe.unal.edu.co.androidtic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class NamePromptActivity extends AppCompatActivity {

    EditText mTxtName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_prompt);
        mTxtName = findViewById(R.id.txtNewUsername2);
    }

    public void onClick(View view) {
        mUsername = mTxtName.getText().toString();
        db.collection("users").whereEqualTo("username", mUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().size() > 0)
                        Toast.makeText(NamePromptActivity.this, "This username already exists", Toast.LENGTH_SHORT).show();
                    else {
                        addUser();
                    }
                } else {
                    Toast.makeText(NamePromptActivity.this, "Cannot validate names :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", mUsername);
        db.collection("users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    String userId = task.getResult().getId();
                    Intent intent = new Intent(NamePromptActivity.this, RoomActivity.class);
                    intent.putExtra("player2Name", mUsername);
                    intent.putExtra("player2Id", userId);
                    startActivity(intent);
                }
            }
        });
    }
}
