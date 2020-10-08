public class Search {
	private String searchStr;
	private RecipeBook recipeBook;

	public Search(String searchStr, RecipeBook recipeBook) {
		this.searchStr = searchStr;
		this.recipeBook = recipeBook;
	}
	/**
	* This method compares the two strings and calculate the minimum edit distance 
	* with the dynamic programming algorithm
	*/
	public int compare(String inputStr, String recipeName) {
		int m = inputStr.length();
        int n = recipeName.length();
        
        int[][] cost = new int[m + 1][n + 1];
        for(int i = 0; i <= m; i++)
            cost[i][0] = i;
        for(int i = 1; i <= n; i++)
            cost[0][i] = i;
        
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(inputStr.charAt(i) == recipeName.charAt(j))
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
    * @param String This is the recipe name the user wants to search for
    * @param RecipeBook This is the array list of Recipe objects
    * @param int The top K Recipe names that need least edit distance from Recipe name to the search string
    * @return RecipeBook The top K array
    */
    public RecipeBook extractTop(int k) {

    }
}