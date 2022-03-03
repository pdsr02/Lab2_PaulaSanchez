package es.ulpgc.eite.da.basicquizlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class QuestionActivity extends AppCompatActivity {

  public static final String TAG = "Quiz.QuestionActivity";

  public static final String STATE_INDEX = "Quiz.Question.STATE_INDEX";
  public static final String STATE_NEXT_BTN = "Quiz.Question.STATE_NEXT_BTN";
  public static final String STATE_CLICK_BTN = "Quiz.Question.STATE_CLICK_BTN";


  public static final int CHEAT_REQUEST = 1;

  private Button falseButton, trueButton,cheatButton, nextButton;
  private TextView questionText, replyText;

  private String[] questionArray;
  private int questionIndex=0;
  private int[] replyArray;
  private boolean nextButtonEnabled, trueButtonClicked;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.activity_question);

    getSupportActionBar().setTitle(R.string.question_title);

    // si ocurre esto es q la activity se esta re-creando (no creando)
    if(savedInstanceState != null){
      Log.d(TAG, "onCreate(): recuperando estado...");

      questionIndex=savedInstanceState.getInt(STATE_INDEX);
      nextButtonEnabled=savedInstanceState.getBoolean(STATE_NEXT_BTN);
      trueButtonClicked=savedInstanceState.getBoolean(STATE_CLICK_BTN);
    }

    initLayoutData();
    linkLayoutComponents();
    updateLayoutContent();
    enableLayoutButtons();

    if(savedInstanceState != null){

      // si has contestado debemos actualizar mensaje de pantalla
      if(nextButtonEnabled) {

        if(trueButtonClicked) { // has pulsado el btn true

          if(replyArray[questionIndex] == 1) {
            replyText.setText(R.string.correct_text);
          } else {
            replyText.setText(R.string.incorrect_text);
          }

        } else { // has pulsado el btn false

          if(replyArray[questionIndex] == 0) {
            replyText.setText(R.string.correct_text);
          } else {
            replyText.setText(R.string.incorrect_text);
          }
        }
      }
    }
  }


  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume()");

  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(TAG, "onSaveInstanceState()");

    outState.putInt(STATE_INDEX, questionIndex);
    outState.putBoolean(STATE_NEXT_BTN, nextButtonEnabled);
    outState.putBoolean(STATE_CLICK_BTN, trueButtonClicked);
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause()");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy()");
  }

  private void enableLayoutButtons() {

    trueButton.setOnClickListener(v -> onTrueButtonClicked());
    falseButton.setOnClickListener(v -> onFalseButtonClicked());
    nextButton.setOnClickListener(v -> onNextButtonClicked());
    cheatButton.setOnClickListener(v -> onCheatButtonClicked());
  }

  private void initLayoutData() {
    questionArray=getResources().getStringArray(R.array.question_array);
    replyArray=getResources().getIntArray(R.array.reply_array);
  }


  private void linkLayoutComponents() {
    falseButton = findViewById(R.id.falseButton);
    trueButton = findViewById(R.id.trueButton);
    cheatButton = findViewById(R.id.cheatButton);
    nextButton = findViewById(R.id.nextButton);

    questionText = findViewById(R.id.questionText);
    replyText = findViewById(R.id.replyText);
  }


  private void updateLayoutContent() {
    questionText.setText(questionArray[questionIndex]);

    if(!nextButtonEnabled) {
      replyText.setText(R.string.empty_text);
    }

    nextButton.setEnabled(nextButtonEnabled);
    cheatButton.setEnabled(!nextButtonEnabled);
    falseButton.setEnabled(!nextButtonEnabled);
    trueButton.setEnabled(!nextButtonEnabled);
  }


  private void onTrueButtonClicked() {

    /*
    if(nextButtonEnabled) {
      return;
    }
    */

    if(replyArray[questionIndex] == 1) {
      replyText.setText(R.string.correct_text);
    } else {
      replyText.setText(R.string.incorrect_text);
    }

    nextButtonEnabled = true;
    trueButtonClicked = true;
    updateLayoutContent();
  }

  private void onFalseButtonClicked() {

    /*
    if(nextButtonEnabled) {
      return;
    }
    */

    if(replyArray[questionIndex] == 0) {
      replyText.setText(R.string.correct_text);
    } else {
      replyText.setText(R.string.incorrect_text);
    }

    trueButtonClicked = false;
    nextButtonEnabled = true;
    updateLayoutContent();

  }

  private void onCheatButtonClicked() {

    /*
    if(nextButtonEnabled) {
      return;
    }
    */

    Intent intent = new Intent(this, CheatActivity.class);
    intent.putExtra(CheatActivity.EXTRA_ANSWER, replyArray[questionIndex]);
    startActivityForResult(intent, CHEAT_REQUEST);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Log.d(TAG, "onActivityResult()");

    if (requestCode == CHEAT_REQUEST) {
      if (resultCode == RESULT_OK) {

        boolean answerCheated =
            data.getBooleanExtra(CheatActivity.EXTRA_CHEATED, false);

        Log.d(TAG, "answerCheated: " + answerCheated);

        if(answerCheated) {
          nextButtonEnabled = true;
          onNextButtonClicked();
        }
      }
    }
  }

  private void onNextButtonClicked() {
    Log.d(TAG, "onNextButtonClicked()");

    /*
    if(!nextButtonEnabled) {
      return;
    }
    */

    nextButtonEnabled = false;
    questionIndex++;

    // si queremos que el quiz acabe al llegar
    // a la ultima pregunta debemos comentar esta linea
    checkIndexData();

    if(questionIndex < questionArray.length) {
      updateLayoutContent();
    }

  }

  private void checkIndexData() {

    // hacemos que si llegamos al final del quiz
    // volvamos a empezarlo nuevamente
    if(questionIndex == questionArray.length) {
      questionIndex=0;
    }

  }

}
