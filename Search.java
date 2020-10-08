// the code below may not work until the RecipeBook class is created and compiled 
// by Martin Wu on 10/08/2020

import java.util.*;
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
    * @param String The recipe name in the Recipe object
    * @return minimal edit distance from recipe name to search string
	*/
	public int compare(String recipeName) {

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
    * @return RecipeBook The top K Recipe objects whose name fuzzy match the search string
    */
    public RecipeBook extractTop(int k) {
    	if (k <= 0) {
    		return null;
    	}
    	ArrayList<Recipe> res = new ArrayList<Recipe>();
    	Map<Integer, Recipe> map = new HashMap<Integer, Recipe>();
    	int[] editDistanceArr = new int[recipeBook.size()];
    	for (int i = 0; i < recipeBook.size(); i++) {
    		editDistanceArr[i] = compare(recipeBook.get(i).getName());
    		map.put(editDistanceArr[i], recipeBook.get(i));
    	}
    	Arrays.sort(editDistanceArr);
    	for (int j = 0; j < k; j++) {
    		res.add(map.get(editDistanceArr[j]));
    	}

    	return res;
    }
}