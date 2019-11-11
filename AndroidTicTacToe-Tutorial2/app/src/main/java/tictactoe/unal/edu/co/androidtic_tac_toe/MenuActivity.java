package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.btnSinglePlayer:
                intent = new Intent(this, AndroidTicTacToeActivity.class);
                intent.putExtra("multiplayer", false);
                break;
            case R.id.btnMultiPlayer:
                intent = new Intent(this, MultiplayerActivity.class);
                break;
        }
        if (intent != null) startActivity(intent);
    }
}
