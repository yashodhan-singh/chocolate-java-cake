import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
// import java.util.ArrayList;
// import java.util.Iterator; 
// import java.util.Scanner;
// import java.util.Map;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class RecipeBook  { 
	public static ArrayList<Recipe> recipe_book = new ArrayList<Recipe>();
    public static void main(String[] args) throws Exception  { 
        read_json("../recipebook.json"); //reads recipebook.json and builds recipebook 
        int recipeIndex = 1000; //used to indicate which recipe is currently being read
        int currStep = 0; //used to indicate which step is currently being read
        
        //i/o
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
            if(s.equals("s")||s.equals("search")) {
                System.out.println("Enter the recipe you would like to search for:");
                String searchStr = in.nextLine(); //user inputs the query

                ArrayList<Recipe> results = extractTop(3, searchStr); //extractTop is used to return top 3 in order, but always returns same 3 no matter what. Try 'Red' and 'Lava'
                System.out.println("Choose a recipe from the list below by entering the corresponding number");
                for(int i = 0; i <results.size(); i++) {
                    System.out.println((i+1) + ". " + results.get(i).getName());
                }

                recipeIndex = Integer.parseInt(in.nextLine()) - 1;
                currStep = 0;

                //print entire recipe
                results.get(recipeIndex).printAll();
                System.out.println("Enjoy! Type 'i' to view instructions individually");
            }
        	
        }
        
        
        
    }
    
    //reads json file
    public static void read_json(String filename) throws FileNotFoundException, IOException, ParseException {
    	Object obj = new JSONParser().parse(new FileReader(filename)); 
        JSONArray book = (JSONArray) obj; 
        int length = book.size();
        for (int i =0; i< length; i++) 
            parseRecipe((JSONObject) book.get(i));
    }
    
    //builds recipe object
    public static void parseRecipe(JSONObject recipe) {
    	JSONArray ingre = (JSONArray)recipe.get("ingredients");
    	String[] ingredients = new String[ingre.size()];
    	JSONArray instr = (JSONArray)recipe.get("instructions");
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
    
    /**
    * This method compares the two strings and calculate the minimum edit distance 
    * with the dynamic programming algorithm
    * @param String The recipe name in the Recipe object
    * @param String the search string user enters
    * @return minimal edit distance from recipe name to search string
    */
    public static int compare(String recipeName, String searchStr) {

        int m = searchStr.length();
        int n = recipeName.length();
        
        int[][] cost = new int[m + 1][n + 1];
        for(int i = 0; i <= m; i++)
            cost[i][0] = i;
        for(int i = 1; i <= n; i++)
            cost[0][i] = i;
        
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(searchStr.toLowerCase().charAt(i) == recipeName.toLowerCase().charAt(j))
                    cost[i + 1][j + 1] = cost[i][j];
                else {
                    int a = cost[i][j];
                    int b = cost[i][j + 1];
                    int c = cost[i + 1][j];
                    cost[i + 1][j + 1] = a < b ? (a < c ? a : c) : (b < c ? b : c);
                    cost[i + 1][j + 1]++;
                }
            }
        }
        return cost[m][n];
    }

    /**
    * This method searches throught the RecipeBook (Array of Recipe). 
    * It calculates the Levenshetin distance
    * show the usage of various javadoc Tags.
    * @param int The top K Recipe names that need least edit distance from Recipe name to the search string
    * @return ArrayList<Recipe> The top K Recipe objects whose name fuzzy match the search string
    */
    public static ArrayList<Recipe> extractTop(int k, String searchStr) {
        if (k <= 0) {
            return null;
        }
        ArrayList<Recipe> res = new ArrayList<Recipe>();
        Map<Integer, Recipe> map = new HashMap<Integer, Recipe>();
        int[] editDistanceArr = new int[recipe_book.size()];
        for (int i = 0; i < recipe_book.size(); i++) {
            editDistanceArr[i] = compare(recipe_book.get(i).getName(), searchStr);
            map.put(editDistanceArr[i], recipe_book.get(i));
        }
        Arrays.sort(editDistanceArr);
        for (int j = 0; j < k; j++) {
            res.add(map.get(editDistanceArr[j]));
        }

        return res;
    }

}