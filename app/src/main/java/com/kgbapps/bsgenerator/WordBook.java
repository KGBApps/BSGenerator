package com.kgbapps.bsgenerator;

import android.content.Context;
import android.util.Log;

import com.kgbapps.bsgenerator.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordBook {

    public static final String TAG = WordBook.class.getSimpleName();

    private List<String> mAdverbs;
    private List<String> mVerbs;
    private List<String> mAdjectives;
    private List<String> mNouns;


    public WordBook(Context context) {
        // Import word lists
        mAdverbs = new ArrayList<>(importAdverbs(context));
        mVerbs = new ArrayList<>(importVerbs(context));
        mAdjectives = new ArrayList<>(importAdjectives(context));
        mNouns = new ArrayList<>(importNouns(context));
    }

    private List<String> importAdverbs(Context context) {
        List<String> result = new ArrayList<>();
        try (
                InputStream is = context.getResources().openRawResource(R.raw.adverbs);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String word;
            while ((word = reader.readLine()) != null) {
                result.add(word);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Error reading adverbs.txt");
            ioe.printStackTrace();
        }
        return result;
    }

    private List<String> importVerbs(Context context) {
        List<String> result = new ArrayList<>();
        try (
                InputStream is = context.getResources().openRawResource(R.raw.verbs);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String word;
            while ((word = reader.readLine()) != null) {
                result.add(word);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Error reading verbs.txt");
            ioe.printStackTrace();
        }
        return result;
    }

    private List<String> importAdjectives(Context context) {
        List<String> result = new ArrayList<>();
        try (
                InputStream is = context.getResources().openRawResource(R.raw.adjectives);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String word;
            while ((word = reader.readLine()) != null) {
                result.add(word);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Error reading adjectives.txt");
            ioe.printStackTrace();
        }
        return result;
    }

    private List<String> importNouns(Context context) {
        List<String> result = new ArrayList<>();
        try (
                InputStream is = context.getResources().openRawResource(R.raw.nouns);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String word;
            while ((word = reader.readLine()) != null) {
                result.add(word);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Error reading nouns.txt");
            ioe.printStackTrace();
        }
        return result;
    }

    public String getRandomAdverb() {
        return mAdverbs.get(getRandom(mAdverbs.size()));
    }

    public String getRandomVerb() {
        return mVerbs.get(getRandom(mVerbs.size()));
    }

    public String getRandomAdjective() {
        return mAdjectives.get(getRandom(mAdjectives.size()));
    }

    public String getRandomNoun() {
        return mNouns.get(getRandom(mNouns.size()));
    }

    private int getRandom(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }
}
