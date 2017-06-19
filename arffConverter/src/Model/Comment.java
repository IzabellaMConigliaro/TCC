package Model;

import java.util.List;

import static Main.Utils.converterArray;
import static Main.Utils.patternizedString;

public class Comment {

    private String text;
    private List<String> words;
    private int value;

    public Comment(String text, int value) {
        this.text = patternizedString(text);
        this.words = converterArray(this.text);
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public List<String> getWords() {
        return words;
    }

    public int getValue() {
        return value;
    }
}