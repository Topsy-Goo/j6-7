package ru.gb.antonov.j67.beans.errorhandlers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorMessage
{
    private List<String> messages;
    private Date         date;


    public ErrorMessage (List<String> strings)
    {
        messages = strings;
        date = new Date();
    }

    public ErrorMessage (String text)
    {
        this (List.of(text));
    }

    public ErrorMessage (String ... messages)
    {
        this (Arrays.asList(messages));
    }

    public List<String> getMessages() {   return Collections.unmodifiableList (messages);   }
}
