package com.java.com.algo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by uday on 09/08/16.
 */
public class MainListActivity extends Activity {
    ListView listView ;
    private static final String TAG = "ExampleQuizApp";
    private static final String QUIZ_JSON_FILE = "Quiz.json";
    private int mQuestionIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, readQuizFromFile());


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }
    /**
     * Create a quiz, as defined in Quiz.json, when the user clicks on "Read quiz from file."
     *
     * @throws IOException
     */
    public String[] readQuizFromFile() {
        // clearQuizStatus();
        String[] questions = null;
        try {
            JSONObject jsonObject = JsonUtils.loadJsonFile(this, QUIZ_JSON_FILE);
            JSONArray jsonArray = jsonObject.getJSONArray(JsonUtils.JSON_FIELD_QUESTIONS);
            questions = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject questionObject = jsonArray.getJSONObject(i);
                Question question = Question.fromJson(questionObject, mQuestionIndex++);

                questions[i]=question.question;
            }
        }
        catch (IOException  | JSONException jsonException)
        {

        }

   return  questions;
    }



    /**
     * Used to ensure questions with smaller indexes come before questions with larger
     * indexes. For example, question0 should come before question1.
     */
    private static class Question implements Comparable<Question> {

        private String question;
        private int questionIndex;
        private String[] answers;
        private int correctAnswerIndex;

        public Question(String question, int questionIndex, String[] answers,
                        int correctAnswerIndex) {
            this.question = question;
            this.questionIndex = questionIndex;
            this.answers = answers;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public static Question fromJson(JSONObject questionObject, int questionIndex)
                throws JSONException {
            String question = questionObject.getString(JsonUtils.JSON_FIELD_QUESTION);
            JSONArray answersJsonArray = questionObject.getJSONArray(JsonUtils.JSON_FIELD_ANSWERS);
            String[] answers = new String[JsonUtils.NUM_ANSWER_CHOICES];
            for (int j = 0; j < answersJsonArray.length(); j++) {
                answers[j] = answersJsonArray.getString(j);
            }
            int correctIndex = questionObject.getInt(JsonUtils.JSON_FIELD_CORRECT_INDEX);
            return new Question(question, questionIndex, answers, correctIndex);
        }

        @Override
        public int compareTo(Question that) {
            return this.questionIndex - that.questionIndex;
        }


    }
}
