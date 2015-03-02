package sudochef.inventory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ProductTime {
    private Calendar cal;
    private static final String FORMAT = "MM dd yyyy";

    public ProductTime(GregorianCalendar c)
    {
        setCal(c);
    }

    public ProductTime(String s)
    {
        String time[] = SeparateDate(s);
        setCal(new GregorianCalendar(Integer.parseInt(time[2]),
                Integer.parseInt(time[0]) - 1,
                Integer.parseInt(time[1])));
    }

    public String FormatToString()
    {
        SimpleDateFormat fmt = new SimpleDateFormat(FORMAT, Locale.US);
        fmt.setCalendar(getCal());
        return fmt.format(getCal().getTime());
    }

    public String[] SeparateDate(String s)
    {
        String output[] = new String[3];
        output[0] = "" + s.charAt(0) + s.charAt(1);
        output[1] = "" + s.charAt(3) + s.charAt(4);
        output[2] = "" + s.charAt(6) + s.charAt(7)+ s.charAt(8) + s.charAt(9);
        return output;

    }

    public int Subtract(ProductTime s)
    {
        String[] caller = SeparateDate(this.FormatToString());
        String[] subtractor = SeparateDate(s.FormatToString());
        int callerDays = calcMonth(Integer.parseInt(caller[0])) + Integer.parseInt(caller[1]) + Integer.parseInt(caller[2]) * 365;
        int subtractorDays = calcMonth(Integer.parseInt(subtractor[0])) + Integer.parseInt(subtractor[1]) + Integer.parseInt(subtractor[2]) * 365;

        return  callerDays - subtractorDays;
    }

    public Calendar getCal()
    {
        return cal;
    }

    public void setCal(Calendar cal)
    {
        this.cal = cal;
    }

    public int calcMonth(int m)
    {
        int out = 0;
        switch (m) {
            case 12:
                out = out + 30;
            case 11:
                out = out + 31;
            case 10:
                out = out + 30;
            case 9:
                out = out + 31;
            case 8:
                out = out + 31;
            case 7:
                out = out + 31;
            case 6:
                out = out + 31;
            case 5:
                out = out + 30;
            case 4:
                out = out + 31;
            case 3:
                out = out + 28;
            case 2:
                out = out + 31;
            case 1:
                out = out + 0;
        }
        return out;
    }

}
