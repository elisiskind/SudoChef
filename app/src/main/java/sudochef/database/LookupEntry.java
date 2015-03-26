package sudochef.database;

import sudochef.inventory.Units;

public class LookupEntry {
    public String searchWord;
    public String generalWord;
    public Units type;
    public int TimeTilExpire;

    public LookupEntry(String sw, String gw, Units t, int tte)
    {
        searchWord =sw;
        generalWord = gw;
        type = t;
        TimeTilExpire = tte;
    }

    public boolean equals(LookupEntry L) { return this.generalWord.equals(L.generalWord); }

}
