package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RoomActivity extends AppCompatActivity {

    private ListView mGameListView;
    private ArrayAdapter mListAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> mGameNameList;
    private List<String> mGameIds;
    private String mGameId;
    private String mPlayer2Id;
    private String mGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        //mGameId = getIntent().getStringExtra("player1Name");
        mPlayer2Id = getIntent().getStringExtra("player2Id");

        mGameNameList = new ArrayList<>();
        mGameIds = new ArrayList<>();
        mListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mGameNameList);

        mGameListView = findViewById(R.id.gameList);
        mGameListView.setAdapter(mListAdapter);
        mGameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                mGameId = mGameIds.get(pos);
                mGameName = mGameNameList.get(pos);
                setPlayer2();
                Log.d("ItemClicked", mGameName + "id: " + l);

            }
        });
    }

    private void setPlayer2() {
        HashMap game = new HashMap<String, Object>();
        game.put("player_2", mPlayer2Id);
        db.collection("games").document(mGameId).set(game, SetOptions.merge()).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(RoomActivity.this, AndroidTicTacToeActivity.class);
                        intent.putExtra("multiplayer",true);
                        intent.putExtra("game_id",mGameId);
                        intent.putExtra("player_1",false);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onStart(){
        super.onStart();
        getGameList();
    }

    private void getGameList(){
        db.collection("games").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    mGameNameList.clear();
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        try{
                            Object gameId = document.getId();
                            Object gameName = document.getData().get("name");
                            if(gameName != null){
                                mGameNameList.add(gameName.toString());
                                mGameIds.add(gameId.toString());
                                Log.v("Game added to list", gameName.toString());
                            }
                        } catch (NullPointerException e) {
                        }
                    }

                    mListAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Error","Cannot retrieve game list from database");
                }
            }
        });
    }

    public void refresh(View v){
        getGameList();
    }
}
