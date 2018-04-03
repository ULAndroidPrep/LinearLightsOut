package edu.rosehulman.fisher.linearlightsout;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  LightsOutGame mGame;
  private TextView mGameStateTextView;
  private List<Button> mGameButtons;
  private static final String GAME_BUTTONS_KEY = "GAME_BUTTONS";
  private static final String NUM_TURNS_KEY = "NUM_TURNS";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mGame = new LightsOutGame();
    mGameStateTextView = findViewById(R.id.gameStateTextView);
    mGameButtons = new ArrayList<>();
    mGameButtons.add((Button) findViewById(R.id.button0));
    mGameButtons.add((Button) findViewById(R.id.button1));
    mGameButtons.add((Button) findViewById(R.id.button2));
    mGameButtons.add((Button) findViewById(R.id.button3));
    mGameButtons.add((Button) findViewById(R.id.button4));
    mGameButtons.add((Button) findViewById(R.id.button5));
    mGameButtons.add((Button) findViewById(R.id.button6));

    restoreState(savedInstanceState);
    updateView();
  }

  private void restoreState(Bundle savedInstanceState) {
    if (savedInstanceState != null && savedInstanceState.getString(GAME_BUTTONS_KEY) != null) {
      // Note, getIntArray wasn't working so using a String (which is a pain)
      String gameString = savedInstanceState.getString(GAME_BUTTONS_KEY);
      Log.d(Constants.TAG, "Game String = " + gameString);
      int[] gameArray = new int[7];
      for (int i = 0; i < gameString.length(); i++) {
        gameArray[i] = gameString.charAt(i) == '1' ? 1 : 0;
      }
      mGame.setAllValues(gameArray);

      int numPresses = savedInstanceState.getInt(NUM_TURNS_KEY);
      mGame.setNumPresses(numPresses);
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(Constants.TAG, "Saving " + mGame.toString());
    outState.putString(GAME_BUTTONS_KEY, mGame.toString());
    outState.putInt(NUM_TURNS_KEY, mGame.getNumPresses());
  }
  
  public void pressedButton(View view) {
    int tag = Integer.parseInt(view.getTag().toString());
    mGame.pressedButtonAtIndex(tag);
    Log.d(Constants.TAG, "You pressed " + tag);
    updateView();
  }

  private void updateView() {
    boolean didWin = mGame.checkForWin();
    for (int i = 0; i < mGameButtons.size(); ++i) {
      Button gameButton = mGameButtons.get(i);
      gameButton.setText(Integer.toString(mGame.getValueAtIndex(i)));
      gameButton.setEnabled(!didWin);
    }
    int turns = mGame.getNumPresses();
    String gameStr = getResources().getQuantityString(R.plurals.game_state_turns, turns, turns);
    if (didWin) {
      gameStr = getString(R.string.win);
    } else if (turns == 0) {
      gameStr = getString(R.string.label_start);
    }
    mGameStateTextView.setText(gameStr);
  }

  public void pressedNewGame(View view) {
    mGame = new LightsOutGame();
    updateView();
  }
}
