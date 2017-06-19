package Model;

public enum SelectionMethod {

    NO_DATA(0),
    SINGLE_OCCURRENCE(1),
    DICTIONARY_VALUE(2);

    private int value;

    private SelectionMethod(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static SelectionMethod getValue(int value) {
        for(SelectionMethod e: SelectionMethod.values()) {
            if(e.value == value) {
                return e;
            }
        }
        return null;
    }

}
