package com.example.mealplanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FirebaseStore {

    private static String authtoken;
    private static final StringBuilder datastr = new StringBuilder();
    private static String data;


    public static void Auth(){
        Thread Network = new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                datastr.setLength(0);
                URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=APIKEY");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                InputStream stream;
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
                String info = "{\"email\":\"FIREBASEUSEREMAIL\",\"password\":\"FIREBASEUSERPASSWORD\",\"returnSecureToken\":true}";
                osw.write(info);
                osw.flush();
                osw.close();
                os.close();
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    stream = urlConnection.getInputStream();
                    BufferedReader bin = new BufferedReader(new InputStreamReader(stream));
                    //temp string to hold each line
                    String inputLine = "";

                    while (inputLine != null) {
                        inputLine = bin.readLine();
                        if (inputLine != null) {
                            if (inputLine.contains("idToken")) {
                                authtoken = inputLine;
                                authtoken = authtoken.replace("\"idToken\": ", "");
                                authtoken = authtoken.replace("\"", "");
                                authtoken = authtoken.replace(",", "");
                                authtoken = authtoken.trim();
                            }
                            datastr.append(inputLine).append("\n");
                        }

                    }

                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });
        Network.start();
        try {
            Network.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static String getMeals(){
        data = "";
        Thread Network = new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                datastr.setLength(0);

                URL url = new URL("https://firestore.googleapis.com/v1/projects/FIREBASEPROJECTNAME/databases/(default)/documents/Meals/?key=APIKEY");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");

                urlConnection.setRequestProperty("Authorization", "Bearer " + authtoken);

                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.connect();

                urlConnection.getResponseMessage();
                System.out.println(urlConnection.getResponseMessage());
                InputStream stream = urlConnection.getInputStream();
                BufferedReader bin = new BufferedReader(new InputStreamReader(stream));
                String inputline = "";
                while (inputline != null) {
                    inputline = bin.readLine();
                    if (inputline != null) {
                        datastr.append(inputline).append("\n");
                    }
                }
                data = datastr.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });
        Network.start();
        try {
            Network.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;


    }

    public static ArrayList<String> getIngredients(String meal) throws JSONException {
        data = "";
        ArrayList<String> ingredients = new ArrayList<>();
        Thread Network2 = new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                datastr.setLength(0);
                URL url = new URL("https://firestore.googleapis.com/v1/projects/FIREBASEPROJECTNAME/databases/(default)/documents/Meals/"+meal+"/?key=APIKEY");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");

                urlConnection.setRequestProperty("Authorization", "Bearer " + authtoken);

                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.connect();

                urlConnection.getResponseMessage();
                System.out.println(urlConnection.getResponseMessage());
                InputStream stream = urlConnection.getInputStream();
                BufferedReader bin = new BufferedReader(new InputStreamReader(stream));
                String inputline = "";
                while (inputline != null) {
                    inputline = bin.readLine();
                    if (inputline != null) {
                        datastr.append(inputline).append("\n");
                    }
                }
                data = datastr.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        });
        Network2.start();
        try {
            Network2.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jsondata = new JSONObject(data);
        JSONArray ingredientsjson = jsondata.getJSONObject("fields").getJSONObject("Ingredients").getJSONObject("arrayValue").getJSONArray("values");
        int lengthofarray = ingredientsjson.length();
        JSONObject ingredientjson;
        int i = 0;
        do {
            ingredientjson = ingredientsjson.getJSONObject(i);
            ingredients.add(ingredientjson.get("stringValue").toString());
            i++;
        }while(i!=lengthofarray);
        return ingredients;




    }




}
