public class Recipe {
    private int id;
    private String name;
    private String description;
    private String[] ingredients;
    private String[] instructions;
    
    public Recipe ()
    {
    	
    }
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

    public void printAll() {
        System.out.println(this.name);
        System.out.println(this.description);
        System.out.println();
        System.out.println("INGREDIENTS");
        for (String element: this.ingredients) {
            System.out.println(element);
        }
        System.out.println();
        System.out.println("INSTRUCITONS");
        for (String element: this.instructions) {
            System.out.println(element);
        }
        System.out.println();
    }
}