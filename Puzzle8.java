package HW1;

import java.util.*;
import java.util.Random;

/**
 * A class that represents the puzzle 8
 * It has methods to operate the puzzle and scramble the puzzle
 * Commands can be sent using a text file or through the interactive terminal
 */
public class Puzzle8 {

    /**
     * Represents the current state of the puzzle
     */
    private int[][] puzzleState; 
    /**
     * Set to true of the puzzleState is empty and false
     */
    public boolean isPuzzleEmpty; 
    /**
     * Tracks the index of the empty space in the puzzle
     * The 0 index tracks the row coordinate and the 1 index tracks the column coordinate
     */
    private int[] zero;
    /**
     * The seed for the random generator
     */
    private int seedRand;

    /**
     * A constructor for a puzzle
     */
    public Puzzle8(){
        puzzleState = new int[3][3]; //[rows][columns]
        isPuzzleEmpty = true; 
        zero = new int[2];
        seedRand = 1;
    }

    /**
     * gets the puzzleState
     * @param the puzzleState
     */
    public int[][] getPuzzleState() {
        return this.puzzleState;
    }

    /**
     * Getter to get the coordinates of the blank tile
     * @return the coordinates of the blank tile in the puzzleState
     */
    public int[] getZero(){
        return this.zero;
    }

    /**
     * Set the current state of the puzzle. The argument specifies the desired puzzle tile positions in left-right, top-down order with the blank tile represented by “0”. 
     * For example, the goal state would be specified as “0 1 2 3 4 5 6 7 8”. 
     * @param puzzlestate The desired tile locations
     */
    public void setState(int[] puzzlestate) {
        if(checkState(puzzlestate)){
            int count = 0;
            for(int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    puzzleState[i][j] = puzzlestate[count];
                    count++;
                }
            }
            isPuzzleEmpty = false;
            updateZero();
        }
        else System.out.println("Error: Invalid Puzzle State");
    }

    /**
     * The method checks if the puzzle state is valid by ensuring the numbers are correct
     * It does not check if the puzzle is solvable
     * @param state The state to check in the format of an array
     * @return true if the state is valid and false if not
     */
    private boolean checkState(int[] state){
        int[] key = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        int[] stateCopy = state.clone(); //Creates a copy of input state, so that the sort does not alter then original variable
        Arrays.sort(stateCopy);
        return Arrays.equals(stateCopy, key);
    }

    /**
     * Prints the current state of the puzzle based on puzzleState
     */
    public void printState(){
        if (isPuzzleEmpty){
            System.out.println("Empty Puzzle");
        }
        else {
            for(int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    if (puzzleState[i][j] == 0) System.out.print("  ");
                    else System.out.print(puzzleState[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    /**
     * Moves the blank tile in the desired direction.
     * Valid directions are "up", "down", "left", "right"
     * @return false if the move is invalid or if the puzzle is empty
     */
    public boolean move(String direction){
        if (checkValidMove(direction)){
            switch(direction){
                case "up": 
                    puzzleState[zero[0]][zero[1]] = puzzleState[zero[0]-1][zero[1]];
                    puzzleState[zero[0]-1][zero[1]] = 0;
                    break;
                case "down":
                    puzzleState[zero[0]][zero[1]] = puzzleState[zero[0]+1][zero[1]];
                    puzzleState[zero[0]+1][zero[1]] = 0;
                    break;
                case "left":
                    puzzleState[zero[0]][zero[1]] = puzzleState[zero[0]][zero[1]-1];
                    puzzleState[zero[0]][zero[1]-1] = 0;
                    break;
                case "right":
                    puzzleState[zero[0]][zero[1]] = puzzleState[zero[0]][zero[1]+1];
                    puzzleState[zero[0]][zero[1]+1] = 0;
                    break;
                default:
                    return false;

            }
            updateZero();
            return true;
        }
        return false;
    }
    
    /**
     * Checks if a move is valid or not but does not change the puzzle state
     * @param direction the direction of the move to check
     * @return true if the move direction is valid
     */
    public boolean checkValidMove(String direction){
        updateZero();
        switch(direction){
            case "up": 
                if (zero[0] > 0){
                    return true;
                } else {/*System.out.println("Error: Invalid Move");*/ return false;}
            case "down":
                if (zero[0] < 2){
                    return true;
                } else {/*System.out.println("Error: Invalid Move");*/ return false;}
            case "left":
                if (zero[1] > 0){
                    return true;
                } else {/*System.out.println("Error: Invalid Move");*/ return false;}
            case "right":
                if (zero[1] < 2){
                    return true;
                } else {/*System.out.println("Error: Invalid Move");*/ return false;}      
            default:
                System.out.println("Error: Invalid Move");
                return false;
        }
    }

    /**
     * Updates the the index of the emptyspace in the puzzle
     */
    private void updateZero(){
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if(puzzleState[i][j] == 0){
                    zero[0] = i;
                    zero[1] = j;
                }
            }
        }
    }

    /**
     * Scramble the state of the puzzle by making n random valid moves starting from the goal state. 
     * Each move in the sequence should be chosen randomly from the available valid moves for the state reached after the previous move.
     * @param n The number of random moves 
     */
    public void scrambleState(int n){
        int[] goal = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        Random rnd = new Random(seedRand);
        int moveNum;
        boolean attempt;

        setState(goal);

        for(int i = 0; i < n; i++){
            attempt = true;
            while(attempt){
                moveNum = rnd.nextInt(4);
                switch(moveNum){
                    case 0:
                        if (move("up")) attempt = false;
                        break;
                    case 1:
                        if (move("down")) attempt = false;
                        break;
                    case 2:
                        if (move("left")) attempt = false;
                        break;
                    case 3:
                        if (move("right")) attempt = false;
                        break;
                    default:
                        attempt = true;
                        break;
                }//matches random int to move direction
            }// while. tries another move if previous move invalid
        }//for. attempt n moves
        isPuzzleEmpty = false;
        updateZero();
    }

    /*
     * This method sets the seed for the random move generator in scrambleState()
     */
    public void setSeed(int seed){
        seedRand = seed;
    }
}//class