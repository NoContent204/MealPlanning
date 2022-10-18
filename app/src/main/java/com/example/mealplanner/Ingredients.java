package com.example.mealplanner;

import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Ingredients extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        TextView ingredientsView = findViewById(R.id.ingredients);
        ingredientsView.setMovementMethod(new ScrollingMovementMethod());
        StringBuilder stringBuilder = new StringBuilder();


        for (String ingredient : MainActivity.ingredients){
            stringBuilder.append(ingredient).append("\n");
        }

        ingredientsView.setText(stringBuilder.toString());
    }
}