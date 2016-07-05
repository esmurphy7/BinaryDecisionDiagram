Assignment 3

Compilation
javac Main.java

Execution
java Main < bddKnuth8.txt
java Main < bddKnuthFig24.txt

Notes
The main function in Main.java has been edited to make the w array the correct size (ie: the 0 has been removed).
This is because the maxBDD algorithm (algorithm B in the book) states that there is exactly one w coefficient for each binary variable x_i.