package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    public void btnNewGame_Clicked(View view) {
        startNewGame();
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
}
