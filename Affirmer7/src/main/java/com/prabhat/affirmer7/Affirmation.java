package com.prabhat.affirmer7;

/**
 * Created by prabhat on 25/1/14.
 */
public class Affirmation {
    public int deleteIcon;
    public String title;
    public int PlayPauseIcon;

    public Affirmation()  {   }
    public Affirmation(String title)
    {
        this.title=title;
    }

    public Affirmation(int deleteIcon,String title,int PlayPauseIcon)
    {
        this.deleteIcon=deleteIcon;
        this.title=title;
        this.PlayPauseIcon=PlayPauseIcon;
    }

    @Override
    public String toString()
    {
        return title;
    }
    @Override
    public boolean equals(Object object)
    {
        if (object == this) return true;
        if (object == null) return false;
        if (getClass() != object.getClass()) return false;

        Affirmation affirmation=(Affirmation)object;

        return (this.title==affirmation.title );
    }

}
