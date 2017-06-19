package Model;

import java.util.ArrayList;
import java.util.List;

public class Attribute {

    private String word;
    private List<Integer> occurrence;
    private int totalOccurrence;
    private Boolean polarity;

    public Attribute(String word) {
        this.word = word;
        this.totalOccurrence = 0;

        this.occurrence = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
            add(0);
        }};
    }

    public String getWord() {
        return word;
    }

    public void increaseTotalOccurrence() {
        totalOccurrence++;
    }

    public void increaseRatingOccurrence(int rating) {
        increaseTotalOccurrence();
        this.occurrence.set(rating, this.occurrence.get(rating) + 1);
    }

    public Integer getTotalOccurrence() {
        return totalOccurrence;
    }

    public String getPolarity() {
        return polarity != null ?
                polarity ? "1" : "0"
                : "?";
    }

    public int getPolarityValue() {
        return polarity != null ?
                polarity ? 1 : -1
                : 0;
    }

    public void setPolarity(Boolean polarity) {
        this.polarity = polarity;
    }

    public void setPolarity(int polarity) {
        if (polarity == 0) {
            this.polarity = null;
        } else this.polarity = polarity != -1;
    }
}