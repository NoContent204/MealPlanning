package com.example.mealplanner;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner mondaymeal;
    private Spinner tuesdaymeal;
    private Spinner wednesdaymeal;
    private Spinner thursdaymeal;
    private Spinner fridaymeal;
    private Spinner saturdaymeal;
    private Spinner sundaymeal;
    public static ArrayList<String> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mondaymeal = findViewById(R.id.mondaymeal);
        tuesdaymeal = findViewById(R.id.tuesdaymeal);
        wednesdaymeal = findViewById(R.id.wednesdaymeal);
        thursdaymeal = findViewById(R.id.thursdaymeal);
        fridaymeal = findViewById(R.id.fridaymeal);
        saturdaymeal = findViewById(R.id.saturdaymeal);
        sundaymeal = findViewById(R.id.sundaymeal);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseStore.Auth();
        String data = FirebaseStore.getMeals();
        populateComboboxes(data);
    }

    private void populateComboboxes(String data){
        ArrayList<String> names = new ArrayList<>();
        data = data.substring(data.indexOf("documents")-5);
        JSONObject json;
        try {
            int i = 0;
            json = new JSONObject(data);
            JSONArray meals = new JSONArray(json.get("documents").toString());
            int lengthofarray = meals.length();
            JSONObject meal;
            do {
                meal = meals.getJSONObject(i);
                String name = meal.get("name").toString();
                names.add(name.replace("projects/FIREBASEPROJECTNAME/databases/(default)/documents/Meals/",""));
                i++;
            }while(i!=lengthofarray);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
            mondaymeal.setAdapter(arrayAdapter);
            tuesdaymeal.setAdapter(arrayAdapter);
            wednesdaymeal.setAdapter(arrayAdapter);
            thursdaymeal.setAdapter(arrayAdapter);
            fridaymeal.setAdapter(arrayAdapter);
            saturdaymeal.setAdapter(arrayAdapter);
            sundaymeal.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void generateIngredientList(View view) throws JSONException {
        ingredients.addAll(FirebaseStore.getIngredients((String) mondaymeal.getSelectedItem()));
        ingredients.addAll(FirebaseStore.getIngredients((String) tuesdaymeal.getSelectedItem()));
        ingredients.addAll(FirebaseStore.getIngredients((String) wednesdaymeal.getSelectedItem()));
        ingredients.addAll(FirebaseStore.getIngredients((String) thursdaymeal.getSelectedItem()));
        ingredients.addAll(FirebaseStore.getIngredients((String) fridaymeal.getSelectedItem()));
        ingredients.addAll(FirebaseStore.getIngredients((String) saturdaymeal.getSelectedItem()));
        ingredients.addAll(FirebaseStore.getIngredients((String) sundaymeal.getSelectedItem()));

        for (String ingredient:ingredients){
            System.out.println(ingredient);
        }

        Intent intent = new Intent(this, Ingredients.class);
        startActivity(intent);

    }
}
