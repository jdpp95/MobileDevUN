package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MultiplayerActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.findAvailableGames:
                intent = new Intent(this, NamePromptActivity.class);
                break;
            case R.id.newMultiGame:
                intent = new Intent(this, NewMultiGameActivity.class);
                break;
        }
        if(intent != null) startActivity(intent);
    }
}
