package Model;

import static Main.Constant.*;

public class DBTable {

    private int maximumRating;
    private int minimumRating;

    private String columnText;
    private String columnValue;
    private String tableName;

    private String arffFile;

    private String stopWords;

    private DBTable dictionaryTable;

    public enum Dataset {

        NO_DATA(0),
        GOOGLE_PLAY_COMMENTS(1),
        FELIPE_MELO_TWEETS(2),
        PANG_LEE_MOVIES(3);

        private int value;

        Dataset(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Dataset getValue(int value) {
            for(Dataset e: Dataset.values()) {
                if(e.value == value) {
                    return e;
                }
            }
            return null;
        }
    }

    public DBTable(String columnText, String columnValue, String tableName) {
        this.columnText = columnText;
        this.columnValue = columnValue;
        this.tableName = tableName;
    }

    public DBTable(int maximumRating, int minimumRating, String columnText, String columnValue,
                   String tableName, String arffFile, String stopWords, DBTable dictionaryTable) {
        this.maximumRating = maximumRating;
        this.minimumRating = minimumRating;
        this.columnText = columnText;
        this.columnValue = columnValue;
        this.tableName = tableName;
        this.arffFile = arffFile;
        this.stopWords = stopWords;
        this.dictionaryTable = dictionaryTable;
    }

    public int getMaximumRating() {
        return maximumRating;
    }

    public int getMinimumRating() {
        return minimumRating;
    }

    public String getColumnText() {
        return columnText;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public String getTableName() {
        return tableName;
    }

    public String getArffFile() {
        return arffFile;
    }

    public String getStopWords() {
        return stopWords;
    }

    public DBTable getDictionaryTable() {
        return dictionaryTable;
    }

    public String getQuery(int limitQuerySelect) {
        String limit = "";

        if (limitQuerySelect > 0) {
            limit += " LIMIT " + limitQuerySelect;
        }

        return getQuery() + limit;
    }

    public String getQuery() {
        return "SELECT " + getColumnText() + ", " + getColumnValue() + " FROM " + getTableName();
    }

    public String getDictionaryTableQuery() {
        return dictionaryTable.getQuery();
    }

    public static DBTable initDictionaryTable() {
        return new DBTable(COLUMN_WORD, COLUMN_POLARITY, TABLE_DICTIONARY);
    }

    public static DBTable initOpinionLexiconTable() {
        return new DBTable(COLUMN_WORD_OPINION_LEXICON, COLUMN_POLARITY_OPINION_LEXICON, TABLE_OPINION_LEXICON);
    }

    public static DBTable initCommentsTable() {
        return new DBTable(MAXIMUM_RATING_COMMENT, MINIMUM_RATING_COMMENT,
                COLUMN_COMMENT, COLUMN_RATING, TABLE_COMMENT, ARFF_FILE_COMMENT, STOP_WORDS,
                initDictionaryTable());
    }

    public static DBTable initFelipeMeloTable() {
        return new DBTable(MAXIMUM_RATING_FELIPE_MELO, MINIMUM_RATING_FELIPE_MELO,
                COLUMN_COMMENT_FELIPE_MELO, COLUMN_RATING_FELIPE_MELO, TABLE_FELIPE_MELO, ARFF_FILE_FELIPE_MELO, STOP_WORDS,
                initDictionaryTable());
    }

    public static DBTable initPangLeeTable() {
        return new DBTable(MAXIMUM_RATING_PANG_LEE, MINIMUM_RATING_PANG_LEE,
                COLUMN_COMMENT_PANG_LEE, COLUMN_RATING_PANG_LEE, TABLE_PANG_LEE, ARFF_FILE_PANG_LEE, STOP_WORDS_ENG,
                initOpinionLexiconTable());
    }

    public static DBTable initTable(Dataset dataset) {
        switch (dataset) {
            case GOOGLE_PLAY_COMMENTS:
                return initCommentsTable();
            case FELIPE_MELO_TWEETS:
                return initFelipeMeloTable();
            case PANG_LEE_MOVIES:
                return initPangLeeTable();
            default:
                return null;
        }
    }
}
