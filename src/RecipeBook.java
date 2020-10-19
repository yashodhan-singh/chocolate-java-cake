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
import java.util.concurrent.ThreadLocalRandom;

public class RecipeBook  { 
	public static ArrayList<Recipe> recipe_book = new ArrayList<Recipe>();
    public static void main(String[] args) throws Exception  {
    	//String filename = "./recipebook.json";
        
        // !!!!!!!!! It seems that the path is different if you are running this on Eclipse or just with command line
        // so be careful. use ../recipebook.json if you run the program with run.sh
        read_json("./recipebook.json"); //reads recipebook.json and builds recipebook 
        int recipeIndex = 1000; //used to indicate which recipe is currently being read
        int currStep = 0; //used to indicate which step is currently being read
    
       
        // i/o
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Chocolate Java Cake's Recipe Book! Type 'h' or 'help' for a list of commands");
        
        while(true) {
        	String s = in.nextLine();
        	
        	//help
        	if(s.equals("h") || s.equals("help")) {
        		System.out.println("'b' or 'browse' to browse all recipes");
        		System.out.println("'s' or 'search' to search for a recipe");
                System.out.println("'a' or 'add' to add a new recipe");
                System.out.println("'r' or 'random' to get a recipe");
                System.out.println("'f' or 'favorite' to get your favorite recipe list");
                System.out.println("'s' or 'search' to search for a recipe");
        	}
        	// add new recipe
            if (s.equals("a") || s.equals("add")) {
                System.out.println("What is the recipe name?");
                String name = in.nextLine();
                System.out.println("What is the recipe description?");
                String description = in.nextLine();
                System.out.println("What are the ingredients? Hit enter after one ingredient and type 'done' when finished.");
                String ingredients = "";
                String next = "";
                while(!next.equals("done")) {

                    next = in.nextLine();
                     if (next.equals("done")) {
                        break;
                    }
                    ingredients += next + "\n";
                }
            
                String[] ingredientsArr = ingredients.split("\n");
                System.out.println("What are the instructions? Hit enter after one instruction and type 'done' when finished.");
                String instructions = "";
                int i = 1;
                next = "";
                while(!next.equals("done")) {
                    next = in.nextLine();
                    
                    if (next.equals("done")) {
                        break;
                    }
                    instructions += String.format("%d.%s\n",i,next); // format the instruction with index
                    i++;
                }
                String[] instructionsArr = instructions.split("\n");
                System.out.println("Success!");
              
                addRecipe(name, description, ingredientsArr, instructionsArr, "./recipebook.json");

                recipe_book = new ArrayList<Recipe>(); // clear recipe_book for new updates
                read_json("./recipebook.json"); //read again because of the new updates 
            }
        	//browse all recipes
            if(s.equals("b")||s.equals("browse") || s.equals("B")) {
                System.out.println("Choose a recipe from the list below by entering the corresponding number");
                for(int i = 0; i <recipe_book.size(); i++) {
                    System.out.println((i+1) + ". " + recipe_book.get(i).getName());
                }
                while(true) {
                    try {
                        recipeIndex = Integer.parseInt(in.nextLine()) - 1;   
                        currStep = 0;
                        //print entire recipe
                        recipe_book.get(recipeIndex).printAll();
                        if (!recipe_book.get(recipeIndex).getFavorite()) {
                            System.out.println("Do you want to favorite this recipe? Type 'yes' or 'no' ");
                            if (in.nextLine().equals("yes")) {
                                recipe_book.get(recipeIndex).setFavorite(true);
                            }
                        }
                       
                        System.out.println("Enjoy! Type 'i' to view instructions individually."); 
                        break;
                    }
                    catch (NumberFormatException ex){
                        System.out.println("Oops. You should enter a number.");
                    }
                }
                
               
            }
         

            //initiate the step-by-step printout process
            if(s.equals("i") || s.equals("I")) {
                if (recipeIndex == 1000) {
                    System.out.println("Oops. There are no instructions for you because you haven't chosen a recipe yet.");
                    continue;
                }

                System.out.println("Step by step view. Hit enter to view the next instruction.");
                //print the next step
                while (in.nextLine().isEmpty() && currStep < recipe_book.get(recipeIndex).getInstructions().length){
                    System.out.println(recipe_book.get(recipeIndex).getInstructions()[currStep]);
                    currStep++;
                 
                }
                System.out.println("That was the last step, enjoy! Type 'h' or 'help' for a list of commands");
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
                System.out.println("Which one would you like to choose?");
                while (true) {
                    try
                    {
                        recipeIndex = Integer.parseInt(in.nextLine()) - 1;
                        recipe_book.get(recipeIndex).printAll();
                        System.out.println("Do you want to favorite this recipe? Type 'yes' or 'no' ");
                        if (in.nextLine().equals("yes")) {
                            recipe_book.get(recipeIndex).setFavorite(true);
                        }
                        System.out.println("Enjoy! Type 'i' to view instructions individually");
                        break;
                    } catch (NumberFormatException ex)
                    {
                        System.out.println("Oops. You should enter a number.");
                    }
                }
              
                

            }
            // get random recipe
            if (s.equals("r") || s.equals("random")) {
                System.out.println("The random recipe for you today is " + getRandomRecipe().getName());
            }
        	// get all favorite recipes
            if (s.equals("f") || s.equals("favorite")) {
                System.out.println("Here is your favorite list: \n");
                for (Recipe r: recipe_book) {
                    
                    if (r.getFavorite()) {
                        System.out.println(r.getName());
                    }
                    
                }
                
            }
        } 
        


    }

    //reads json file
    public static void read_json(String filename) throws FileNotFoundException, IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(filename));
        JSONArray book = (JSONArray) obj;
        int length = book.size();
        for (int i = 0; i < length; i++){
            parseRecipe((JSONObject) book.get(i));
        }
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
            ingredients, instructions, (boolean) recipe.get("favorite"));
        recipe_book.add(new_recipe);
    }


    /**
    *   
    *    This method gets a random recipe in the recipe_book array list
    *   for user.
    *   @author Martin Wu
    *   @return Recipe : a recipe object
    */
    public static Recipe getRandomRecipe() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, recipe_book.size());
        
        return recipe_book.get(randomNum);
    }

    public static void addRecipe(String name, String description, String[] ingredientsArr, String[] instructionsArr, String filename) throws FileNotFoundException, IOException, ParseException
    {
    	
    	//recipe_book.add(r); // store it locally, so the user can see the new recipe.
    	
    	FileWriter file = null;
    	Object obj = new JSONParser().parse(new FileReader(filename)); 
        JSONArray book = (JSONArray) obj; 
        
        // New entry
        JSONObject entry = new JSONObject();
        entry.put("id", book.size());// Need to set the id for our new entry.
        
        entry.put("name", name);
        entry.put("description", description);

        entry.put("favorite", false); // by default
        
        // Taking care of the arrays
        JSONArray ingredients = new JSONArray();
        
        for (int i = 0; i < ingredientsArr.length; i++)
        {
        	ingredients.add(ingredientsArr[i]);
        }
        
        JSONArray instructions = new JSONArray();
        
        for (int i = 0; i < instructionsArr.length; i++)
        {
        	instructions.add(instructionsArr[i]);
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
