package com.kgbapps.bsgenerator;

public class Phrase {
    public static final String TAG = Phrase.class.getSimpleName();

    private String mPhrase;
    private String mStarter;

    public Phrase (String phrase) {
        mPhrase = phrase;
    }

    public Phrase (String starter, String phrase) {
        mStarter = starter;
        mPhrase = phrase;
    }

    @Override
    public String toString() {
        return mPhrase;
    }

    public String getPhrase() {
        return mPhrase;
    }

    public String getLongPhrase() {
        return String.format("%s %s", mStarter, mPhrase);
    }
}
