package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    // Represents the internal state of the game.
    private TicTacToeGame mGame;

    // Buttons making up the board.
    private Button mBoardButtons[];

    //Various text displayed.
    private TextView mInfoTextView;

    // Tells whether the game is over or not.
    private boolean mGameOver;

    // Someone has the first turn.
    private boolean humanMovesFirst;

    private TextView mHumanWinsLabel, mTiesLabel, mAndroidWinsLabel;

    private int mHumanWins, mTies, mAndroidWins;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new TicTacToeGame();
        mInfoTextView = findViewById(R.id.txtInformation);
        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = findViewById(R.id.one);
        mBoardButtons[1] = findViewById(R.id.two);
        mBoardButtons[2] = findViewById(R.id.three);
        mBoardButtons[3] = findViewById(R.id.four);
        mBoardButtons[4] = findViewById(R.id.five);
        mBoardButtons[5] = findViewById(R.id.six);
        mBoardButtons[6] = findViewById(R.id.seven);
        mBoardButtons[7] = findViewById(R.id.eight);
        mBoardButtons[8] = findViewById(R.id.nine);
        mHumanWinsLabel = findViewById(R.id.txtHumanWins);
        mTiesLabel = findViewById(R.id.txtTies);
        mAndroidWinsLabel = findViewById(R.id.txtAndroidWins);
        mHumanWins = mTies = mAndroidWins = 0;
        humanMovesFirst = false;

        startNewGame();

//        mInfoTextView = findViewById(R.id.);
    }

    private void startNewGame()
    {
        mGame.clearBoard();
        mGameOver = false;

        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++)
        {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        humanMovesFirst = !humanMovesFirst;
        if(!humanMovesFirst)
            getComputerMove();
        else {
            mInfoTextView.setText(R.string.first_human);
        }
    }

    private void setMove(char player, int location){
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if(player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

    private class ButtonClickListener implements View.OnClickListener {

        private int location;

        private ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View view) {
                if(mBoardButtons[location].isEnabled() && !mGameOver) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                //If no winner yet, let the computer make a move.
                int winner = mGame.checkForWinner();
                if (winner == 0){
                    winner = getComputerMove();
                }

                if(winner > 0)
                {
                    mGameOver = true;
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
        }
    }

    private int getComputerMove() {
        int winner;
        mInfoTextView.setText(R.string.turn_computer);
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
