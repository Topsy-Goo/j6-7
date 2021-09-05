package ru.gb.antonov.j67.beans.errorhandlers;

public class UserCreatingException extends IllegalArgumentException
{
    public UserCreatingException (String messageText)
    {
        super (messageText);
    }
}
