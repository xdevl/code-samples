package com.xdevl.suggest.bean;

/**
 * A word is only defined by a string but could potentially contains
 * other attributes like a score indicating the probability for the user to choose it
 */
public class Word
{
    private final String mValue ;

    public Word(String value)
    {
        mValue=value ;
    }

    public String getValue()
    {
        return mValue ;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this==o) return true;
        if(!(o instanceof Word)) return false;

        Word word=(Word)o;

        return mValue.equals(word.mValue);

    }

    @Override
    public int hashCode()
    {
        return mValue.hashCode();
    }
}
