package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    // Represents the internal state of the game.
    private ITicTacToeGame mGame;

    //Various text displayed.
    private TextView mInfoTextView;

    // Tells whether the game is over or not.
    private boolean mGameOver;

    // Someone has the first turn.
    private boolean humanMovesFirst;

    private TextView mHumanWinsLabel, mTiesLabel, mAndroidWinsLabel;

    private int mHumanWins, mTies, mAndroidWins;

    private BoardView mBoardView;

    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPlayer;

    private char mPlayerInTurn;

    private boolean mMultiplayer;

    private boolean mPlayer1;

    private String mGameId;
    private String mPlayer1Id;
    private String mPlayer1Name;
    private String mPlayer2Id;
    private String mPlayer2Name;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // Determine which cell was touched.
            int col = (int) motionEvent.getX() / mBoardView.getBoardCellWidth();
            int row = (int) motionEvent.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if(!mGameOver && mPlayerInTurn == TicTacToeGame.HUMAN_PLAYER){
                char player = mPlayer1 || !mMultiplayer? TicTacToeGame.HUMAN_PLAYER : TicTacToeGame.COMPUTER_PLAYER;
                if(!setMove(player, pos))
                {
                    return false;
                }

                int winner = mGame.checkForWinner();

                //If no winner yet, let the computer make a move.
                if (winner == 0){
                    Handler handler = new Handler();
                    mPlayerInTurn = TicTacToeGame.COMPUTER_PLAYER;
                    mInfoTextView.setText(getString(R.string.turn_computer));
                    if(mMultiplayer){
                        //Send board to db
                        sendBoardToDB();
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int winner = getComputerMove();
                                updateWinnerStatus(winner);
                                mPlayerInTurn = TicTacToeGame.HUMAN_PLAYER;
                            }
                        }, 1000);
                    }
                } else {
                    updateWinnerStatus(winner);
                }
            }

            //So we aren't notified of continued events when finger is moved.
            return false;
        }
    };

    private void sendBoardToDB() {
        if(!mMultiplayer) return;
        String board = mGame.getBoard();
        HashMap<String, String> gameUpdated = new HashMap<>();
        gameUpdated.put("board", board);
        gameUpdated.put("player_in_turn", getTurn());
        gameUpdated.put("winner_status", String.valueOf(mGame.checkForWinner()));
        db.collection("games").document(mGameId).set(gameUpdated, SetOptions.merge()).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mBoardView.invalidate();
                    }
                }
        );//Handle
    }

    private void updateWinnerStatus(int winner) {
        mGameOver = winner > 0;

        if(mGameOver){
            sendBoardToDB();
        }

        if(mMultiplayer && !mPlayer1)
        {
            if(winner == 2){
                winner = 3;
            } else if (winner == 3) {
                winner = 2;
            }
        }

        switch (winner){
            case 0:
                mInfoTextView.setText(R.string.turn_human);
                break;
            case 1:
                mInfoTextView.setText(R.string.result_tie);
                mTies++;
                mTiesLabel.setText(String.valueOf(mTies));
                break;
            case 2:
                mInfoTextView.setText(R.string.result_human_wins);
                mHumanWins++;
                mHumanWinsLabel.setText(String.valueOf(mHumanWins));
                break;
            case 3:
                mInfoTextView.setText(R.string.result_computer_wins);
                mAndroidWins++;
                mAndroidWinsLabel.setText(String.valueOf(mAndroidWins));
                break;
        }
    }

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mMultiplayer = intent.getBooleanExtra("multiplayer", false);
        mPlayer1 = intent.getBooleanExtra("player_1", true);
        mGameId = intent.getStringExtra("game_id");

        if(mMultiplayer)
        {
            mGame = new TicTacToeMultiplayer(mGameId, mPlayer1);
        } else {
            mGame = new TicTacToeGame();
        }

        mBoardView = findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        mInfoTextView = findViewById(R.id.txtInformation);
        mHumanWinsLabel = findViewById(R.id.txtHumanWins);
        mTiesLabel = findViewById(R.id.txtTies);
        mAndroidWinsLabel = findViewById(R.id.txtAndroidWins);
        mHumanWins = mTies = mAndroidWins = 0;
        humanMovesFirst = false;
        mPlayerInTurn = TicTacToeGame.HUMAN_PLAYER;

        if(!mMultiplayer)
            mPlayer2Name = "Android";
        else
            mPlayer2Name = "Opponent";

        if(mMultiplayer) {
            db.collection("games").document(mGameId).addSnapshotListener(
                    new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null || documentSnapshot == null) {
                                Log.w("Fail", "Listen failed.", e);
                                return;
                            }

                            String board = documentSnapshot.get("board").toString();
                            String winner = documentSnapshot.get("winner_status").toString();

                            mGame.setBoard(board);
                            readTurn();
                            updateWinnerStatus(Integer.parseInt(winner));
                            mBoardView.invalidate();
                        }
                    }
            );
        }

        startNewGame();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mario_1up);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wrong_answer);
    }

    @Override
    protected void onPause(){
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    private void startNewGame()
    {
        mGame.clearBoard();

        if(mMultiplayer)
        {
            sendBoardToDB();
        } else
            mBoardView.invalidate();

        mGameOver = false;
        if(mPlayer1)
            humanMovesFirst = !humanMovesFirst;

        if(mMultiplayer)
        {
            this.readTurn();
        } else
        {
            if (!humanMovesFirst) {
                getComputerMove();
                mInfoTextView.setText(R.string.turn_human);
            } else {
                mInfoTextView.setText(R.string.first_human);
            }
        }
    }

    private void readTurn() {
        mPlayerInTurn = TicTacToeGame.FREE_SPOT;
        db.collection("games").document(mGameId).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map data = documentSnapshot.getData();
                        String turn = null;
                        if(data != null) {
                            turn = (String) data.get("player_in_turn");
                        }

                        if(turn == null){
                            Log.w("Warning","Player turn not received from db");
                        } else if(turn.equals("1")) {
                            if(mPlayer1) mPlayerInTurn = TicTacToeGame.HUMAN_PLAYER;
                            else mPlayerInTurn = TicTacToeGame.COMPUTER_PLAYER;
                        } else if (turn.equals("2")) {
                            if(mPlayer1) mPlayerInTurn = TicTacToeGame.COMPUTER_PLAYER;
                            else mPlayerInTurn = TicTacToeGame.HUMAN_PLAYER;
                        }

                        if(!mGameOver) {
                            if (mPlayerInTurn == TicTacToeGame.HUMAN_PLAYER) {
                                mInfoTextView.setText(R.string.turn_human);
                            } else if (mPlayerInTurn == TicTacToeGame.COMPUTER_PLAYER) {
                                mInfoTextView.setText(R.string.turn_computer);
                            } else {
                                mInfoTextView.setText("...");
                            }
                        }
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error", "Cannot retrieve turn from db");
                    }
                }
        );
    }

    private String getTurn() {

        String player = "";

        //HashMap game = new HashMap<String, Object>();
        if(mPlayer1) {
            if (mPlayerInTurn == TicTacToeGame.HUMAN_PLAYER)
                player = "1";
            else
                player = "2";
        } else {
            if (mPlayerInTurn == TicTacToeGame.COMPUTER_PLAYER)
                player = "1";
            else
                player = "2";
        }

        return player;
    }

    private boolean setMove(char player, int location){
        if(mGame.setMove(player, location) || mPlayerInTurn == TicTacToeGame.COMPUTER_PLAYER){
            mBoardView.invalidate();

            if(player == TicTacToeGame.HUMAN_PLAYER) mHumanMediaPlayer.start();
            else if(player == TicTacToeGame.COMPUTER_PLAYER) mComputerMediaPlayer.start();

            return true;
        }

        return false;
    }

    private int getComputerMove() {
        int winner;
        int move = mGame.getComputerMove();

        if(move >= 0)
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);

        winner = mGame.checkForWinner();
        return winner;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT_ID);
                return true;
            case R.id.returnToMain:
                Intent intent = new Intent(this, Menu.class);
                startActivity(intent);
        }

        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // Selected is the radio button that should be selected.
                int selected = 2;

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                if(mGame.getClass() == TicTacToeGame.class) {
                                    switch (item) {
                                        case 0:
                                            ((TicTacToeGame)mGame).setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                            break;
                                        case 1:
                                            ((TicTacToeGame)mGame).setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                            break;
                                        case 2:
                                            ((TicTacToeGame)mGame).setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                            break;
                                    }
                                    // Display the selected difficulty level
                                    Toast.makeText(getApplicationContext(), levels[item],
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                dialog = builder.create();

                break;
            case DIALOG_QUIT_ID:
                //Create the quit confirmation dialog.

                builder.setMessage(R.string.quit_question).
                        setCancelable(false).
                        setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
            case DIALOG_ABOUT_ID:
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;
        }

        return dialog;
    }
}
