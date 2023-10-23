
// This file exists to create a static map of restriction IDs and names
// It is essentially a map but with a few helper features (e.g. isValid) to check if a given restriction ID is valid
import java.util.HashMap;
import java.util.Map;

public class Restrictions {
    static final Map<String, String> Restrictions;
    static {
        Restrictions = new HashMap<String, String>();
        Restrictions.put("1", "Vegetarian");
        Restrictions.put("3", "Seafood Watch");
        Restrictions.put("4", "Vegan");
        Restrictions.put("6", "Farm to Fork");
        Restrictions.put("7", "In Balance");
        Restrictions.put("8", "Organic");
        Restrictions.put("9", "Made without Gluten-Containing Ingredients");
        Restrictions.put("10", "Halal");
        Restrictions.put("11", "Kosher");
        Restrictions.put("18", "Humane");
    }

    public static String getRestrictionName(String id) {
        return Restrictions.get(id);
    }

    /*
     * @return A string representation of the restrictions
     */
    @Override
    public String toString() {
        String output = "";
        for (String id : Restrictions.keySet()) {
            output += id + ": " + Restrictions.get(id) + "\n";
        }
        return output;
    }

    // Check if a given string is a valid restriction ID
    // E.g. "1" is valid, "12" is not, since it is not in the map's keys
    public static boolean isValid(String id) {
        return Restrictions.containsKey(id);
    }

}