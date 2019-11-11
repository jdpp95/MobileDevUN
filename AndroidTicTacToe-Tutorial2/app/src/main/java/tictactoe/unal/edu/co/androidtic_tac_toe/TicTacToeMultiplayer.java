/* TicTacToeConsole.java
 * By Frank McCown (Harding University)
 *
 * This is a tic-tac-toe game that runs in the console window.  The human
 * is X and the computer is O.
 */
package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class TicTacToeMultiplayer implements ITicTacToeGame {

    private char mBoard[] = {'1','2','3','4','5','6','7','8','9'};
    public static final int BOARD_SIZE = 9;

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char FREE_SPOT = ' ';

    private String gameId;
    private boolean isPlayer1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TicTacToeMultiplayer(String gameId, boolean isPlayer1) {
        this.gameId = gameId;
        this.isPlayer1 = isPlayer1;
    }

    // Check for a winner.  Return
    //  0 if no winner or tie yet
    //  1 if it's a tie
    //  2 if X won
    //  3 if O won
    public int checkForWinner() {

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6]== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    @Override
    public char getBoardOccupant(int i) {
        return mBoard[i];
    }

    @Override
    public String getBoard() {
        String board = "";

        for(int i = 0; i < mBoard.length; i++)
        {
            board += mBoard[i];
        }

        return board;
    }

    @Override
    public void setBoard(String board) {
        for(int i=0; i < board.length(); i++)
        {
            mBoard[i] = board.charAt(i);
        }
    }

    @Override
    public void clearBoard() {
        for(int i = 0; i < BOARD_SIZE; i++)
        {
            mBoard[i] = FREE_SPOT;
        }
    }

    @Override
    public boolean setMove(char player, int location) {
        if(player == HUMAN_PLAYER || player == COMPUTER_PLAYER){
            if(mBoard[location] == FREE_SPOT)
            {
                mBoard[location] = player;
                return true;
            } return false; // TODO: else => Location not available

        } return false; // TODO: else => invalid player
    }

    public int getComputerMove()
    {
        int move = -1;

        //TODO: Implement this.

        return move;
    }

    public HashMap<String, String> getMapBoard()
    {
        HashMap<String, String> map = new HashMap<>();
        for(int i=0; i < BOARD_SIZE; i++)
        {
            map.put(String.valueOf(i+1), String.valueOf(mBoard[i]));
        }

        return map;
    }
}