package e.joepassanante.tictactoeassignment;
/*
Author: Joe Passanante
Date: 2/16/18

*/
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class HomeScreen extends AppCompatActivity {
    private Switch myswitch;
    private boolean twoplayer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
         myswitch = (Switch)findViewById(R.id.switch1);

         //update settings when player wants to do multilayer vs single player
        myswitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(myswitch.isChecked()){
                    twoplayer = true;
                    //show player2 username box
                    findViewById(R.id.usernametexttwo).setVisibility(View.VISIBLE);
                    findViewById(R.id.usernametwo).setVisibility(View.VISIBLE);
                }else{
                    //remove player2 username box
                    findViewById(R.id.usernametexttwo).setVisibility(View.INVISIBLE);
                    findViewById(R.id.usernametwo).setVisibility(View.INVISIBLE);
                    twoplayer=false;
                }
                Log.i("Switch",String.valueOf(twoplayer));
            }
        });
    }
    public void onClickPlay(View view){
        //both usernames of the players are found.
        EditText userInput1 = (EditText)findViewById(R.id.usernameone);
        EditText userInput2 = (EditText)findViewById(R.id.usernametwo);

        //if user wants to play multiplayer
        if(twoplayer){
            //check if both usernames have been assigned
            if(this.checkUsername(userInput1.getText().toString())&&this.checkUsername(userInput2.getText().toString())){
                //start the two player version of the game.
                Intent TicTacToe = new Intent(this, e.joepassanante.tictactoeassignment.TwoPlayer.class);
                TicTacToe.putExtra("username1",userInput1.getText().toString());
                TicTacToe.putExtra("username2",userInput2.getText().toString());
                startActivity(TicTacToe);
            }
        }else{
            if(this.checkUsername(userInput1.getText().toString())){
                //player is playing single player
                Intent TicTacToe = new Intent(this, e.joepassanante.tictactoeassignment.TicTacToe.class);
                TicTacToe.putExtra("username",userInput1.getText().toString());
                startActivity(TicTacToe);
            }
        }
    }
    //used for checking if usernames are empty, and if so creates an alert
    private boolean checkUsername(String u){
        if(u.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sorry! You cannot play without entering a username!")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.println(Log.INFO, "UserConfirm", "Going to enter username");
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return !u.isEmpty();
    }
}
