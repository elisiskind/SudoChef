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

    public Calendar getCal()
    {
        return cal;
    }

    public void setCal(Calendar cal)
    {
        this.cal = cal;
    }

}
