package tictactoe.unal.edu.co.androidtic_tac_toe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class BoardView extends View {

    //Width of the board grid lines.
    public static final int GRID_WIDTH = 6;

    private Bitmap mHumanBitmap;
    private Bitmap mComputerBitMap;
    private Paint mPaint;
    private TicTacToeGame mGame;

    public void initialize(){
        mComputerBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.x);
        mHumanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.o);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public BoardView(Context context){
        super(context);
        initialize();
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initialize();
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        // Determine the width and height of the View.
        int boardWidth = getWidth();
        int boardHeight = getHeight();

        //Make thick, light gray lines.
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(GRID_WIDTH);

        //Draw two vertical board lines.
        int cellWidth = boardWidth / 3;
        canvas.drawLine(cellWidth, 0, cellWidth, boardHeight, mPaint);
        canvas.drawLine(cellWidth * 2, 0, cellWidth * 2, boardHeight, mPaint);

        //Draw two horizontal board lines.
        int cellHeight = boardHeight / 3;
        canvas.drawLine(0, cellHeight, boardWidth, cellHeight, mPaint);
        canvas.drawLine(0, cellHeight * 2, boardWidth, cellHeight * 2, mPaint);

        //Draw all the X and O images
        for (int i = 0; i < TicTacToeGame.BOARD_SIZE; i++){
            int col = i % 3;
            int row = i / 3;

            //Define the boundaries of a destination rectangle for the image.
            int left = col * cellWidth;
            int top = row * cellHeight;
            int right = left + cellWidth;
            int bottom = top + cellHeight;

            Bitmap bitmap = null;

            if(mGame != null) {
                if(mGame.getBoardOccupant(i) == TicTacToeGame.COMPUTER_PLAYER) {
                    bitmap = mComputerBitMap;
                } else if (mGame.getBoardOccupant(i) == TicTacToeGame.HUMAN_PLAYER){
                    bitmap = mHumanBitmap;
                }
                if(bitmap != null)
                    canvas.drawBitmap(bitmap,
                        null, //src
                        new Rect(left, top, right, bottom), //dest
                        null);
            }
        }
    }

    public void setGame(TicTacToeGame game){
        mGame = game;
    }

    public int getBoardCellWidth(){
        return getWidth() / 3;
    }

    public int getBoardCellHeight(){
        return getHeight() / 3;
    }
}
