import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;  

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
  
public class RecipeBook  { 
	public static ArrayList<Recipe> recipe_book = new ArrayList<Recipe>();
    public static void main(String[] args) throws Exception  { 
        read_json("recipebook.json");
        for (int i = 0; i < recipe_book.size(); i++) {
        	System.out.println(recipe_book.get(i).getIngredients()[0]);
        }
    }
    public static void read_json(String filename) throws FileNotFoundException, IOException, ParseException {
    	Object obj = new JSONParser().parse(new FileReader(filename)); 
        JSONArray book = (JSONArray) obj; 
        int length = book.size();
        for (int i =0; i< length; i++) 
            parseRecipe((JSONObject) book.get(i));
    }
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

} 
class Recipe {
    private int id;
    private String name;
    private String description;
    private String[] ingredients;
    private String[] instructions;
    
    public Recipe(int id, String name, String description, String[] ingredients, String[] instructions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }
}