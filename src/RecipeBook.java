import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
// fuzzy search package
import me.xdrop.fuzzywuzzy.*;
import me.xdrop.fuzzywuzzy.model.*;
 

public class RecipeBook  { 
	public static ArrayList<Recipe> recipe_book = new ArrayList<Recipe>();
    public static void main(String[] args) throws Exception  {
    	//String filename = "./recipebook.json";
        read_json("./recipebook.json"); //reads recipebook.json and builds recipebook 
        int recipeIndex = 1000; //used to indicate which recipe is currently being read
        int currStep = 0; //used to indicate which step is currently being read
        
       // Recipe rp = new Recipe();
        //rp.setId(id);
        //rp.setDescription("test_description1");
       // rp.setIngredients(new String [] {"1", "2", "3"});
      //  rp.setInstructions(new String [] {"4", "5", "6"});
      //  rp.printAll();
        //addRecipe(rp, "./recipebook.json");
        
        // i/o
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Chocolate Java Cake's Recipe Book! Type 'h' or 'help' for a list of commands");
        
        while(true) {
        	String s = in.nextLine();
        	
        	//help
        	if(s.equals("h") || s.equals("help")) {
        		System.out.println("'b' or 'browse' to browse all recipes");
        		System.out.println("'s' or 'search' to search for a recipe");
        	}
        	
        	//browse all recipes
            if(s.equals("b")||s.equals("browse") || s.equals("B")) {
                System.out.println("Choose a recipe from the list below by entering the corresponding number");
                for(int i = 0; i <recipe_book.size(); i++) {
                    System.out.println((i+1) + ". " + recipe_book.get(i).getName());
                }
                recipeIndex = Integer.parseInt(in.nextLine()) - 1;
                currStep = 0;
                //print entire recipe
                recipe_book.get(recipeIndex).printAll();
                System.out.println("Enjoy! Type 'i' to view instructions individually");
            }

            //initiate the step-by-step printout process
            if(s.equals("i") || s.equals("I")) {
                System.out.println("Step by step view. Type 'n' or 'next' to view the next instruction.");
                System.out.println(recipe_book.get(recipeIndex).getInstructions()[currStep]);
            }

            //print the next step
            if((s.equals("n") || s.equals("N")) && currStep < recipe_book.get(recipeIndex).getIngredients().length) {
                currStep++;
                if(currStep == recipe_book.get(recipeIndex).getInstructions().length) {
                    System.out.println("That was the last step, enjoy! Type 'h' or 'help' for a list of commands");
                } else {
                    System.out.println(recipe_book.get(recipeIndex).getInstructions()[currStep]);
                }
            }

            //search
            if (s.equals("s") || s.equals("search")) {
                System.out.println("Enter the recipe you would like to search for:");
                String searchStr = in.nextLine();
                // apply fuzzy search here
                ArrayList<String> recipeNames = new ArrayList<String>();
                for (Recipe r : recipe_book) {
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


    public static void addRecipe(Recipe r, String filename) throws FileNotFoundException, IOException, ParseException
    {
    	
    	//recipe_book.add(r); // store it locally, so the user can see the new recipe.
    	
    	FileWriter file = null;
    	Object obj = new JSONParser().parse(new FileReader(filename)); 
        JSONArray book = (JSONArray) obj; 
        r.setId(book.size()); // Need to set the id for our new entry.
        
        
        // New entry
        JSONObject entry = new JSONObject();
        entry.put("id", r.getId());
        entry.put("name", r.getName());
        entry.put("description", r.getDescription());
        entry.put("favorite", r.getFavorite());
        
        // Taking care of the arrays
        JSONArray ingredients = new JSONArray();
        
        for (int i = 0; i < r.getIngredients().length; i++)
        {
        	ingredients.add(r.getIngredients()[i]);
        }
        
        JSONArray instructions = new JSONArray();
        
        for (int i = 0; i < r.getInstructions().length; i++)
        {
        	instructions.add(r.getInstructions()[i]);
        }
        
        // Putting the last key-value pairs together
        entry.put("ingredients", ingredients);
        entry.put("instructions", instructions);
       
        book.add(entry);
        //System.out.println(book.toJSONString());
        try 
        {
        	 
            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter(filename);
            file.write(book.toJSONString());
            
        } 
        
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        
        finally 
        {
                file.flush();
                file.close();
        }
    }
    

}