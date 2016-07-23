package com.kgbapps.bsgenerator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class History {
    private ArrayDeque<Phrase> mHistoryDeque;

    public History() {
        mHistoryDeque = new ArrayDeque<>();
    }

    public ArrayDeque<Phrase> getHistory() {
        return mHistoryDeque;
    }

    public List<Phrase> getHistoryAsList() {
        List<Phrase> phrasesList = new ArrayList<>(30);
        ArrayDeque<Phrase> phrasesDeque = mHistoryDeque.clone();
        for (Phrase phrase : phrasesDeque) {
            phrasesList.add(phrasesDeque.pollFirst());
        }
        return phrasesList;
    }

    public void addPhrase(Phrase phrase) {
        mHistoryDeque.addFirst(phrase);
//        if (mHistoryDeque.size() > 30) {
//            mHistoryDeque.pollLast();
//        }
    }

    public void removePhrase(Phrase phrase) {
        mHistoryDeque.remove(phrase);
    }
}
