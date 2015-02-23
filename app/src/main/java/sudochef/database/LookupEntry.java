package sudochef.database;

public class LookupEntry {
    public String searchWord;
    public String generalWord;
    public String type;
    public int TimeTilExpire;

    public LookupEntry(String sw, String gw, String t, int tte)
    {
        searchWord =sw;
        generalWord = gw;
        type = t;
        TimeTilExpire = tte;
    }

    public boolean equals(LookupEntry L) { return this.generalWord.equals(L.generalWord); }

}
