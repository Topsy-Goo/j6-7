package ru.gb.antonov.j67.beans.errorhandlers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*   Сообщение, которое отправлем клиенту, если его запрос некорректен.   */
@Data
@NoArgsConstructor
public class ErrorMessage
{
    private String messageText;
    private Date   date;

    public ErrorMessage (String text)
    {
        messageText = text;
        date = new Date();
    }
}
