package Model;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {

    private Map<String, DictionaryWord> dictionary;

    public void insertDictionaryWord(DictionaryWord dictionaryWord) {
        if (dictionary == null) {
            dictionary = new HashMap<>();
        }

        dictionary.put(dictionaryWord.getWord(), dictionaryWord);
    }

    public DictionaryWord getDictionaryWord(String attribute) {
        return dictionary.get(attribute);
    }
}
