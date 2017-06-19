package Model;

import static Main.Utils.patternizedString;

public class DictionaryWord {
    String word;
    int polarity;

    public DictionaryWord(String word, int polarity) {
        this.word = patternizedString(word);
        this.polarity = polarity;
    }

    public String getWord() {
        return word;
    }

    public int getPolarity() {
        return polarity;
    }
}
