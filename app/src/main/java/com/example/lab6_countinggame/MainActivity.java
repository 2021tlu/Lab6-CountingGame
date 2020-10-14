package com.example.lab6_countinggame;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    // Views
    // TextView title = findViewById(R.id.title);
    TextView numText;
    TextView turnText;
    Game game;

    Gson gson;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numText = findViewById(R.id.numText);
        turnText = findViewById(R.id.turnText);

        game = new Game();
        gson = new Gson();

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void resetGame(View view){
        game.reset();
        numText.setText("" + game.getCount());
        turnText.setText(getTurnText(game.getTurn(), game.getState()));
    }

    public void saveGame(View view){
        String json = gson.toJson(game);
        editor.putString("jsonGameObj",json);
        editor.apply();
    }

    public void loadGame(View view){
        game = gson.fromJson(sharedPreferences.getString("jsonGameObj",""),Game.class);
        numText.setText("" + game.getCount());
        turnText.setText(getTurnText(game.getTurn(), game.getState()));
    }

    private class Game{
        private int count;
        private int turn;
        private int state;

        public Game(){
            count=0;
            turn=0;
            state=0;
        }
        public Game(int c, int t, int f){
            count=c;
            turn=t;
            state=f;
        }

        public void reset() {
            count = 0;
            turn = 0;
            state = 0;
        }

        public void addCount(int addition){
            count += addition;
        }

        public void incrementTurn(){
            turn += 1;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTurn() {
            return turn;
        }

        public void setTurn(int turn) {
            this.turn = turn;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    public void changeNumText(View view) {
        Log.i("buttonResponse:", (String) view.getTag());
        game.addCount(Integer.parseInt((String) view.getTag()));

        if (game.getCount() >= 21) {
            if (game.getState() == 0) {
                game.setState(1);
                turnText.setText(getTurnText(game.getTurn(),game.getState()));
            }
            else{
                game.setCount(game.getCount() - Integer.parseInt((String) view.getTag())); // keep constant
            }
        }
        else {
            game.incrementTurn();
            turnText.setText(getTurnText(game.getTurn(),game.getState()));
        }

        numText.setText("" + game.getCount());
    }
    public String getTurnText(int turn, int state){
        if(state==1)
            return "Player " + (((turn+1)%2)+1) + " Wins!";
        return "Player " + ((turn%2)+1) + "'s Turn";
    }
}