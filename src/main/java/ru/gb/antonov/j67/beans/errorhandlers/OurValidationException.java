package ru.gb.antonov.j67.beans.errorhandlers;

import java.util.Collections;
import java.util.List;

public class OurValidationException extends RuntimeException
{
    private final List<String> messages;

    public OurValidationException (List<String> strings)
    {
        messages = strings;
    }

    public List<String> getMessages() {   return Collections.unmodifiableList (messages);   }
}
