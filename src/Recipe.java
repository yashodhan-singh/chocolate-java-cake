public class Recipe implements Comparable<Recipe> {
    private int id;
    private String name;
    private String description;
    private String[] ingredients;
    private String[] instructions;
    private int editDistance;
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
    public int getEditDistance() {
        return editDistance;
    }
    public int setEditDistance(int editDistance) {
        this.editDistance = editDistance;
    }
    @Override
    public int compareTo(Recipe recipe) {
        return Integer.compare(editDistance, recipe.getEditDistance());
    }
}