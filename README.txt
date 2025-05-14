This is the README for CSDS391 HW1-5 for Franklin Wang fmw13

Puzzle8.java is the class that represents and has actions for the puzzle 8. 

Puzzle8cmdInterface.java is the class that has the command interface set up to send in the commands.
Run this as it has the main function.

Puzzle8Solver.java is the class that implements the BFS algorithm to solve the puzzle. 
A Node is considered to be created whenever the code creates one, and not necessarily when a node is added to the search tree.
All Successors are created as Nodes before they are checked against the goal state.
It converts visited states into Strings and hashes the String representaion.
The complete node path is stored in each node so reconstructing the path is not needed.

testFile2.txt are the tests used to check the implemention of BFS. 
testOuput2.txt are pasted results in the terminal after running testFile2.txt

testFile3.txt are the tests used to check the implemention of DFS. 
testOuput3.txt are pasted results in the terminal after running testFile3.txt

testFile4.txt are the tests used to check the implemention of A*. 
testOuput4.txt are pasted results in the terminal after running testFile4.txt
//////*****A* is in Puzzle8Solver line 244 and down.*****\\\\\\

testFile5.txt are the tests used to for calculating the branch factor
testOuput5.txt are pasted results in the terminal after running testFile5.txt
//////*****Branch Factor calculation is in Puzzle8Solver line 350 and down.*****\\\\\\

To run the code using the testFile# first compile the code
Then enter "java HW1.Puzzle8cmdInterface testFile#.txt", where # is the testFile number
I used VS code and there appears to be some rule that the package name must be there for it to run.