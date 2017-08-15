package Main;

/**
 * TO DO
 *
 * @author		Claudio Giuliano
 * @version 	%I%, %G%
 * @since			1.0
 */
public interface Stemmer
{
    /**
     * reset() resets the stemmer so it can stem another word.  If you invoke
     * the stemmer by calling add(char) and then stem(), you must call reset()
     * before starting another word.
     */
    public abstract void reset();

    /**
     * Add a character to the word being stemmed.  When you are finished
     * adding characters, you can call stem(void) to process the word.
     */
    public abstract void add(char ch);

    /**
     * Add a character to the word being stemmed.  When you are finished
     * adding characters, you can call stem(void) to process the word.
     */
    public abstract void add(char[] ch);

    /**
     * Returns the length of the word resulting from the stemming process.
     */
    public abstract int getResultLength();

    /**
     * Returns a reference to a character buffer containing the results of
     * the stemming process.  You also need to consult getResultLength()
     * to determine the length of the result.
     */
    public abstract char[] getResultBuffer();


    /**
     * Stem a word provided as a String.  Returns the result as a String.
     */
    public abstract String stem(String s);

    /** Stem a word contained in a char[].  Returns true if the stemming process
     * resulted in a word different from the input.  You can retrieve the
     * result with getResultLength()/getResultBuffer() or toString().
     */
    public abstract boolean stem(char[] word);

    /** Stem a word contained in a portion of a char[] array.  Returns
     * true if the stemming process resulted in a word different from
     * the input.  You can retrieve the result with
     * getResultLength()/getResultBuffer() or toString().
     */
    public abstract boolean stem(char[] wordBuffer, int offset, int wordLen);

    /** Stem a word contained in a leading portion of a char[] array.
     * Returns true if the stemming process resulted in a word different
     * from the input.  You can retrieve the result with
     * getResultLength()/getResultBuffer() or toString().
     */
    public abstract boolean stem(char[] word, int wordLen);

    /** Stem the word placed into the Stemmer buffer through calls to add().
     * Returns true if the stemming process resulted in a word different
     * from the input.  You can retrieve the result with
     * getResultLength()/getResultBuffer() or toString().
     */
    public abstract boolean stem();

    public abstract boolean stem(int i0);

} // end interface Stemmer