package sudochef.yummly;

/**
 * @author Eli Siskind
 *
 * This class holds a parsed JSON response to the SearchRecpies Yummly API call.
 */
public class Response {
    private String attribution;
    private int count;
    private RecipeResult[] matches;

    public Response(){
        attribution = null;
        count = -1;
        matches = null;
    }

    /**
     * @return the attribution
     */
    public String getAttribution() {
        return attribution;
    }

    /**
     * @param attribution the attribution to set
     */
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the matches
     */
    public RecipeResult[] getMatches() {
        return matches;
    }

    /**
     * @param matches the matches to set
     */
    public void setMatches(RecipeResult[] matches) {
        this.matches = matches;
    }

    public void print() {
        if(matches != null){
            for(int i = 0; i < matches.length; i++)
                matches[i].print();
        }
    }
}
