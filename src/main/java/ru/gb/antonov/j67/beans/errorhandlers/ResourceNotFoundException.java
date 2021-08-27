package ru.gb.antonov.j67.beans.errorhandlers;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException (String messageText)
    {
        super (messageText);
    }//1
}
