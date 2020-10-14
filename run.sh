cd src
javac -cp ".:../json-simple-1.1.jar:../fuzzywuzzy-1.3.0.jar" Recipe.java RecipeBook.java
java -cp ".:../json-simple-1.1.jar:../fuzzywuzzy-1.3.0.jar" RecipeBook
