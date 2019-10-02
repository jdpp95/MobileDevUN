package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    // Represents the internal state of the game.
    private TicTacToeGame mGame;

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

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // Determine which cell was touched.
            int col = (int) motionEvent.getX() / mBoardView.getBoardCellWidth();
            int row = (int) motionEvent.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if(!mGameOver && mPlayerInTurn == TicTacToeGame.HUMAN_PLAYER){
                if(!setMove(TicTacToeGame.HUMAN_PLAYER, pos))
                {
                    return false;
                }

                int winner = mGame.checkForWinner();

                //If no winner yet, let the computer make a move.
                if (winner == 0){
                    Handler handler = new Handler();
                    mPlayerInTurn = TicTacToeGame.COMPUTER_PLAYER;
                    mInfoTextView.setText(R.string.turn_computer);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int winner = getComputerMove();
                            updateWinnerStatus(winner);
                            mPlayerInTurn = TicTacToeGame.HUMAN_PLAYER;
                        }
                    }, 1000);
                } else {
                    updateWinnerStatus(winner);
                }
            }

            //So we aren't notified of continued events when finger is moved.
            return false;
        }
    };

    private void updateWinnerStatus(int winner) {
        mGameOver = winner > 0;

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
        mGame = new TicTacToeGame();
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

        startNewGame();

//        mInfoTextView = findViewById(R.id.);
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
        mBoardView.invalidate();

        mGameOver = false;
        humanMovesFirst = !humanMovesFirst;

        if(!humanMovesFirst){
            getComputerMove();
            mInfoTextView.setText(R.string.turn_human);
        } else {
            mInfoTextView.setText(R.string.first_human);
        }
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

                                switch (item){
                                    case 0:
                                        mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                        break;
                                    case 1:
                                        mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                        break;
                                    case 2:
                                        mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                        break;
                                }
                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
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
