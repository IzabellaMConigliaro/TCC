package Main;

import java.nio.file.Paths;

public class Constant {
    public static final String HEADER_ATTRIBUTE = "@attribute %s {0,1}\n";
    public static final String SINGLE_OCCURRENCE_ATTRIBUTE = "singleOccurrence";
    public static final String DICTIONARY_VALUE_ATTRIBUTE = "dictionaryAttribute";

    public static final int MINIMUM_OCCURRENCE = 1;

    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_RATING = "rating";
    public static final String TABLE_COMMENT = "Comment";
    public static final int MAXIMUM_RATING_COMMENT = 5;
    public static final int MINIMUM_RATING_COMMENT = 1;
    public static final String ARFF_FILE_COMMENT = "comments.arff";

    public static final String COLUMN_COMMENT_FELIPE_MELO = "tweet";
    public static final String COLUMN_RATING_FELIPE_MELO = "polarity";
    public static final String TABLE_FELIPE_MELO = "felipe_melo";
    public static final int MAXIMUM_RATING_FELIPE_MELO = 1;
    public static final int MINIMUM_RATING_FELIPE_MELO = 0;
    public static final String ARFF_FILE_FELIPE_MELO = "felipe_melo.arff";

    public static final String COLUMN_COMMENT_PANG_LEE = "review";
    public static final String COLUMN_RATING_PANG_LEE = "polarity";
    public static final String TABLE_PANG_LEE = "pang_lee";
    public static final int MAXIMUM_RATING_PANG_LEE = 1;
    public static final int MINIMUM_RATING_PANG_LEE = 0;
    public static final String ARFF_FILE_PANG_LEE = "pang_lee.arff";

    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_POLARITY = "polarity";
    public static final String TABLE_DICTIONARY = "Dictionary";

    public static final String COLUMN_WORD_OPINION_LEXICON = "word";
    public static final String COLUMN_POLARITY_OPINION_LEXICON = "polarity";
    public static final String TABLE_OPINION_LEXICON = "opinion_lexicon";

    public static final String STOP_WORDS = Paths.get("").toAbsolutePath() + "/stopWords.json";
    public static final String STOP_WORDS_ENG = Paths.get("").toAbsolutePath() + "/stopWords_eng.json";

}
