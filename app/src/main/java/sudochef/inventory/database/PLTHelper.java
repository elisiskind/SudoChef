package sudochef.inventory.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PLTHelper {
    private static final String TAG = "PLTHealper";
    @SuppressWarnings("serial")
    private static final ArrayList<String> stopwords = new ArrayList<String>(){{
        add("I");
        add("a");
        add("about");
        add("an");
        add("are");
        add("as");
        add("at");
        add("be");
        add("by");
        add("for");
        add("from");
        add("how");
        add("in");
        add("is");
        add("it");
        add("of");
        add("on");
        add("or");
        add("that");
        add("the");
        add("this");
        add("to");
        add("was");
        add("what");
        add("when");
        add("where");
        add("who");
        add("will");
        add("with");
        add("the");
    }};


    private static boolean exists(String s)
    {
        return stopwords.contains(s.toLowerCase());
    }

    public static List<LookupEntry> Search(ProductLookupTable db, String productName)
    {
        String productNameDelim[] = productName.split("[ ]+");
        List<String> output = new ArrayList<String>(Arrays.asList(productNameDelim));
        for (Iterator<String> it = output.iterator(); it.hasNext();)
        {
            String word = it.next();
            if(exists(word))
            {
                it.remove();
                Log.d("Eval", "removing" + word);
            }
        }
        return pltQuery(db, output, productName);
    }

    private static List<LookupEntry> pltQuery(ProductLookupTable db, List<String> in, String fullName)
    {
        List<LookupEntry> output = new ArrayList<LookupEntry>();
//        LookupEntry fullNameSearch = db.search(fullName);
//        if(fullNameSearch != null)
//        {
//            output.add(fullNameSearch);
//        }
//        else
//        {
            boolean dupFlag;
            for (String inputword : in) {
                dupFlag = false;
                LookupEntry temp = db.search(inputword.toLowerCase());
                for (LookupEntry outputword : output) {
                    if (temp != null && outputword.equals(temp)) {
                        dupFlag = true;
                    }
                }
                if (!dupFlag && temp != null) {
                    output.add(temp);
                }
            }
//        }

        return output;
    }

}
