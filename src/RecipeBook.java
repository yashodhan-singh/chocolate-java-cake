import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
// fuzzy search package
import me.xdrop.fuzzywuzzy.*;
import me.xdrop.fuzzywuzzy.model.*;
public class RecipeBook {
    public static ArrayList < Recipe > recipe_book = new ArrayList < Recipe > ();
    public static void main(String[] args) throws Exception {
        read_json("recipebook.json"); //reads recipebook.json and builds recipebook 

        /*//prints first element of ith recipe's ingredients
        for (int i = 0; i < recipe_book.size(); i++) {
            System.out.println(recipe_book.get(i).getIngredients()[0]);
        }*/

        //basic i/o
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Chocolate Java Cake's Recipe Book! Type 'h' or 'help' for a list of commands");

        while (true) {

            String s = in .nextLine();

            //help
            if (s.equals("h") || s.equals("help")) {
                System.out.println("'b' or 'browse' to browse all recipes");
                System.out.println("'s' or 'search' to search for a recipe");
            }

            //browse
            if (s.equals("b") || s.equals("browse")) {
                System.out.println("Choose a recipe from the list below by entering the corresponding number");
                for (int i = 0; i < recipe_book.size(); i++) {
                    System.out.println((i + 1) + ". " + recipe_book.get(i).getName());
                }

                int recipeIndex = Integer.parseInt( in .nextLine()) - 1;

                //print entire recipe
                System.out.println(s);
            }
            //search
            if (s.equals("s") || s.equals("search")) {
                System.out.println("Enter the recipe you would like to search for:");
                String searchStr = in.nextLine();
                // apply fuzzy search here
                ArrayList<String> recipeNames = new ArrayList<String>();
                for (Recipe r: recipe_book) {
                    recipeNames.add(r.getName());
                }
                // return top 3 most similar results 
                List<ExtractedResult> resList = FuzzySearch.extractTop(searchStr, recipeNames, 3); 
                // print them out 
                System.out.println("Here's what we got.");
                for (int i = 0; i < resList.size(); i++) {
                    String index = Integer.toString(i + 1);
                    System.out.println(index + ". " + resList.get(i).getString());
                }
            }

        }

        


    }

    //reads json file
    public static void read_json(String filename) throws FileNotFoundException, IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONArray book = (JSONArray) obj;
        int length = book.size();
        for (int i = 0; i < length; i++)
            parseRecipe((JSONObject) book.get(i));
    }

    //builds recipe object
    public static void parseRecipe(JSONObject recipe) {
        JSONArray ingre = (JSONArray) recipe.get("ingredients");
        String[] ingredients = new String[ingre.size()];
        JSONArray instr = (JSONArray) recipe.get("instructions");
        String[] instructions = new String[instr.size()];
        for (int i = 0; i < ingredients.length; i++)
            ingredients[i] = (String) ingre.get(i);
        for (int i = 0; i < instructions.length; i++)
            instructions[i] = (String) instr.get(i);
        Recipe new_recipe = new Recipe(
            Integer.valueOf(recipe.get("id").toString()),
            (String) recipe.get("name"),
            (String) recipe.get("description"),
            ingredients, instructions);
        recipe_book.add(new_recipe);
    }



    

}