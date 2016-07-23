package com.kgbapps.bsgenerator;

public class Generator {
    public static final String TAG = Generator.class.getSimpleName();

    private WordBook mWordBook;

    public Generator (WordBook wordBook) {
        mWordBook = wordBook;
    }

    public String generatePhrase() {
        String phrase = String.format("%s %s %s %s.",
                mWordBook.getRandomAdverb(),
                mWordBook.getRandomVerb(),
                mWordBook.getRandomAdjective(),
                mWordBook.getRandomNoun()
        );
        return phrase;
    }
}
