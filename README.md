# Chocolate Java Cake
A repository for NYU's Fall 2020 Collaborating Remotely: Building Software From Anywhere class Project #1: Recipe Book

### Trello
Our trello board, which contains our current progress on implementing user stories and tasks can be found [here](https://trello.com/b/LQBT9YPQ/chocolate-java-cake)

### Confluence
Our confluence board, which contains the instructions of how to use our application can be found [here](https://recipe-book.atlassian.net/wiki/home)

### Running the .java file on command line: 
3 files are involved which have to be within the same dir:
  - json-simple-1.1.jar (the jar file includes json-simple library)
  - RecipeBook.java (which includes another Recipe object class)
  - recipebook.json
Compilation: 
-javac -cp .:json-simple-1.1.jar RecipeBook.java
Running: 
-java -cp .:json-simple-1.1.jar RecipeBook
