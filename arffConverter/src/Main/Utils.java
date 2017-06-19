package Main;

import Model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Main.ArffConv.getStopWordsPath;
import static Main.ArffConv.isRemoveStopWords;
import static Main.Constant.HEADER_ATTRIBUTE;

public class Utils {
    private static List<String> stopWords;
    static FileWriter arffFile = null;


    public static List<String> converterArray(String comment) {
        String[] array = comment.split(" \\b");
        List<String> temp = new ArrayList<>(Arrays.asList(array));

        temp.forEach(e -> { temp.set(temp.indexOf(e), e.trim()); });


        return temp;
    }

    public static String patternizedString(String text) {

        if (stopWords == null || stopWords.isEmpty()) {
            getStopWords();
        }

        text = text.replaceAll("[ãâàáä]", "a")
                .replaceAll("[êèéë]", "e")
                .replaceAll("[îìíï]", "i")
                .replaceAll("[õôòóö]", "o")
                .replaceAll("[ûúùü]", "u")
                .replaceAll("[ÃÂÀÁÄ]", "A")
                .replaceAll("[ÊÈÉË]", "E")
                .replaceAll("[ÎÌÍÏ]", "I")
                .replaceAll("[ÕÔÒÓÖ]", "O")
                .replaceAll("[ÛÙÚÜ]", "U")
                .replace('ç', 'c')
                .replace('Ç', 'C')
                .replace('ñ', 'n')
                .replace('Ñ', 'N')
                .replaceAll(" +", " ")
                .toLowerCase()
                .replaceAll("[^a-z ]", "");

        if (isRemoveStopWords()) {
            for (String stopWord : stopWords) {
                text = text.replaceAll("\\b " + stopWord + "\\b", "");
            }
        }

        text = text.replaceAll("(\\p{javaLetter})\\1", "$1");

        return text;
    }

    public static int booleanToInt(boolean b) {
        return (b) ? 1 : 0;
    }

    public static int getBinaryValue(Comment comment, Attribute attribute) {
        return booleanToInt (converterArray(comment.getText()).contains(attribute.getWord()));
    }

    public static String getBinarySingleOccurrenceValue(boolean hasSingleOccurrence, String singleOccurrenceValue) {
        int value;
        try {
            value = Integer.valueOf(singleOccurrenceValue);
        } catch (NumberFormatException e) {
            return singleOccurrenceValue;
        }
        return hasSingleOccurrence ? singleOccurrenceValue : String.valueOf((value + 1) % 2);
    }

    public static void getStopWords() {
        stopWords = new ArrayList<>();
        Gson gson = new Gson();

        try {
            Type collectionType = new TypeToken<List<String>>(){}.getType();
            stopWords = gson.fromJson(new FileReader(getStopWordsPath()), collectionType);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String headerAttribute(String string) {
        return HEADER_ATTRIBUTE.replace("%s", string);
    }

    static void writeArff(String string) throws IOException {
        arffFile.write(string);
        arffFile.flush();
    }

    static void closeFileWriter(FileWriter fileWriter) {
        if(fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static FileWriter getNewFileWriter(String fileName) throws IOException {
        return new FileWriter(new File(fileName));
    }

    static String getValuesRatio(DBTable mainTable) {

        String classValues = "";
        for (int i = mainTable.getMinimumRating(); i <= mainTable.getMaximumRating(); i++) {
            classValues = classValues.concat(String.valueOf(i));

            if (i < mainTable.getMaximumRating()) {
                classValues = classValues.concat(",");
            }
        }
        return classValues;
    }

    static String getSingleOccurrenceValue(List<String> singleOccurrence, Dictionary dictionary) {
        int singleOccurrenceValue = 0;

        for (String attribute : singleOccurrence) {
            DictionaryWord dictionaryWord = dictionary.getDictionaryWord(attribute);

            if (dictionaryWord != null) {
                singleOccurrenceValue = singleOccurrenceValue + dictionaryWord.getPolarity();
            }
        }

        return singleOccurrenceValue < 0 ? "0" : singleOccurrenceValue > 0 ? "1" : "?";
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }
}