package tictactoe.unal.edu.co.androidtic_tac_toe;

public interface ITicTacToeGame {

    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    void clearBoard();

    /** Set the given player at the given location on the game board.
     *  The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    boolean setMove(char player, int location);

    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    int getComputerMove();

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    int checkForWinner();

    /**
     * Gets the occupant of a given cell in the board.
     * @param i - The position of the cell.
     * @return - Character representing the occupant.
     */
    char getBoardOccupant(int i);
}
