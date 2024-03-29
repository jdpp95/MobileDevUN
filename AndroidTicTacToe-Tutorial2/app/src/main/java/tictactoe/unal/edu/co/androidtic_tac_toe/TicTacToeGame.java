/* TicTacToeConsole.java
 * By Frank McCown (Harding University)
 *
 * This is a tic-tac-toe game that runs in the console window.  The human
 * is X and the computer is O.
 */
package tictactoe.unal.edu.co.androidtic_tac_toe;

import java.util.Random;

public class TicTacToeGame implements ITicTacToeGame {

    private char mBoard[] = {'1','2','3','4','5','6','7','8','9'};
    public static final int BOARD_SIZE = 9;

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char FREE_SPOT = ' ';

    private Random mRand;

    public static String getEmptyBoard() {
        String board = "";
        for(int i=0; i < BOARD_SIZE; i++)
        {
            board += FREE_SPOT;
        }

        return board;
    }

    //The computer's difficulty levels
    public enum DifficultyLevel {
        Easy,
        Harder,
        Expert
    }

    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    public DifficultyLevel getmDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setmDifficultyLevel(DifficultyLevel difficultyLevel){
        mDifficultyLevel = difficultyLevel;
    }

    public TicTacToeGame() {

        // Seed the random number generator
        mRand = new Random();
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
        return null;
    }

    @Override
    public void setBoard(String board) {
        throw new UnsupportedOperationException();
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

        if (mDifficultyLevel == DifficultyLevel.Easy) move = getRandomMove();
        else if(mDifficultyLevel == DifficultyLevel.Harder) {
            move = getWinningMove();
            if (move == -1)
            {
                move = getRandomMove();
            }
        } else if(mDifficultyLevel == DifficultyLevel.Expert){
            move = getWinningMove();
            if (move == -1)
                move = getBlockingMove();
            if (move == -1)
                move = getRandomMove();
        }

        return move;
    }

    private int getBlockingMove() {
        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];   // Save the current number
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER;
                    setMove(COMPUTER_PLAYER, i);
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }

        return -1;
    }

    private int getWinningMove() {
        // First see if there's a move O can make to win
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    setMove(COMPUTER_PLAYER, i);
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }

        return -1;
    }

    private int getRandomMove() {
        int move = -1;

        // Generate random move
        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);

        setMove(COMPUTER_PLAYER, move);
        mBoard[move] = COMPUTER_PLAYER;
        return move;
    }
}