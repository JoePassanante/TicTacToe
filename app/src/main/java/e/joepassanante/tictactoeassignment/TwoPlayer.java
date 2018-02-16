package e.joepassanante.tictactoeassignment;
/*
Author: Joe Passanante
Date: 2/16/18

*/
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TwoPlayer extends AppCompatActivity {
    private final String USERNAME1_DATA = "username1";
    private final String USERNAME2_DATA = "username2";
    String player1,player2;
    private TicTacToeLogic game;
    private boolean playerTurn = true;
    private ImageButton[] buttons;
    private boolean _gameOver = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        Intent intent = this.getIntent();
        player1 = intent.getStringExtra(USERNAME1_DATA);
        player2 = intent.getStringExtra(USERNAME2_DATA);

        //list of all the buttons, makes accessing the interface easier.
        buttons = new ImageButton[]{
                (ImageButton)findViewById(R.id.R1C1),
                (ImageButton)findViewById(R.id.R1C2),
                (ImageButton)findViewById(R.id.R1C3),
                (ImageButton)findViewById(R.id.R2C1),
                (ImageButton)findViewById(R.id.R2C2),
                (ImageButton)findViewById(R.id.R2C3),
                (ImageButton)findViewById(R.id.R3C1),
                (ImageButton)findViewById(R.id.R3C2),
                (ImageButton)findViewById(R.id.R3C3)
        };

        //create/load board instance
        if(savedInstanceState!=null){
            int[][] oldBoard = new int[3][3];
            oldBoard[0] = savedInstanceState.getIntArray("CurrentBoardRow0");
            oldBoard[1] = savedInstanceState.getIntArray("CurrentBoardRow1");
            oldBoard[2] = savedInstanceState.getIntArray("CurrentBoardRow2");
            this.playerTurn = savedInstanceState.getBoolean("Turn");
            this._gameOver = savedInstanceState.getBoolean("Game");
            game = new TicTacToeLogic(oldBoard);
            updateIcons(); //display old board.
        }else{
            game = new TicTacToeLogic();
            updateIcons();
        }

        //assign actions to the buttons
        for(int i = 0; i<buttons.length;i++){
            buttons[i].setOnClickListener(new listen(i));
            //fix error with images not scaling
            buttons[i].setScaleType(ImageButton.ScaleType.FIT_XY);
        }
        if(this._gameOver){
            Log.i("_gameOver Tag Value",String.valueOf(_gameOver));
            gameOver();
        }


    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putIntArray("CurrentBoardRow0",game.getBoard()[0]); //save the different rows
        savedInstanceState.putIntArray("CurrentBoardRow1",game.getBoard()[1]);
        savedInstanceState.putIntArray("CurrentBoardRow2",game.getBoard()[2]);
        savedInstanceState.putBoolean("Turn",this.playerTurn); //saved the current turn.
        savedInstanceState.putBoolean("Game",this._gameOver); //saved the current game state.
    }
    //Tic Tac Toe Button Box Listener
    class listen implements View.OnClickListener{
        private int buttonNum;
        public listen(int buttonNumber){
            buttonNum = buttonNumber;
        }
        @Override
        public void onClick(View view){
            view.setContentDescription("None");
            if(game==null) //can't operate button when game is not initiated.
                return;
            Log.println(Log.INFO,"Button Log",String.valueOf(buttonNum));
            //check if it is player 1's turn.
            if(game.checkOpenSpot(buttonNum) && playerTurn){
                game.setMove(0, buttonNum);
                turnOver();
                return; //we have to return or else it will also trigger player 2's turn.
            }
            //check of action was for player 2
            if(game.checkOpenSpot(buttonNum)&&!playerTurn){
                game.setMove(1, buttonNum);
                turnOver();
                return;
            }
        }
    }
    //Interact with the TicTacToeLogic so we can edit the interface.
    private void turnOver(){
        this.playerTurn = !this.playerTurn;
        updateIcons();
        //check to see if the game is over.
        if(game.checkForWinner()!=ITicTacToe.PLAYING) {
            gameOver();
            _gameOver = true;
            return;
        }
    }
    //updates all the icons on the gameboard.
    private void updateIcons(){
        TextView view = (TextView)findViewById(R.id.TacTitle);
        view.setText("It is your turn "+ ((playerTurn)? player1 : player2) + "!");
        for(int i = 0; i<buttons.length;i++){
            switch(game.getLocationValue(i)){
                case(ITicTacToe.CROSS):
                    buttons[i].setImageResource(R.drawable.cross);
                    break;
                case(ITicTacToe.NOUGHT):
                    buttons[i].setImageResource(R.drawable.naught);
                    break;
                default: buttons[i].setImageResource(R.drawable.empty);
            }
        }
    }
    private void gameOver() {
        this.playerTurn = false; //disable player moves
        TextView end = (TextView) findViewById(R.id.endofgame);
        switch (game.checkForWinner()) {
            case (ITicTacToe.CROSS_WON):
                gameOverAlert("Game Over X Won!");
                break;
            case (ITicTacToe.NOUGHT_WON):
                gameOverAlert("Game Over O Won!");
                break;
            default:
                gameOverAlert("Game Over! It was  Tie!");
                break;
        }
    }
    //end of game alert with option to restart.
    private void gameOverAlert(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s + "\n\nClick the restart button to play another game!")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.println(Log.INFO,"Game Restart","Restarting game");
                        //clear old properties
                        game.clearBoard();
                        _gameOver = false;
                        playerTurn = true;
                        //update the board
                        updateIcons();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
