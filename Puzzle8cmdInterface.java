package HW1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * The Main method to use the interface or send in commands in a txt file
 */
public class Puzzle8cmdInterface {
    /**
     * instance of the puzzle8
     */
    private static Puzzle8 puzzle = new Puzzle8();

    /**
     * This method reads a file line by line and calls the command.
     * If there is improperly formatted command, the method will print the line and stop the execution of the file.
     * @param puzzle the instance of the puzzle to send the command to
     * @param filename the name of the file to read from.
     */
    private static void cmdfile(String filename){
        FileReader fr;
        BufferedReader br;
        String command = "";
        boolean isError = false;

        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            while (br.ready() && !isError){
                //read file line by line
                command = br.readLine();
                //if not a comment or blank, print the command and call it
                if (!printComment(command)) {
                    System.out.println(command);
                    //call cmd(read the line)
                    if(!cmd(command)){
                        System.out.println("Error: invalid command: text of line that caused the issue: " + command);
                        isError = true;
                    }
                }
            }

            fr.close();
            br.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * This method checks the text in the file.
     * Checks if the input string is empty, if so prints a blank line and returns true
     * Checks if the input string starts with "//", if so prints the comment and returns true
     * Note: null = no value at all, empty = "", blank = " " or "\t" whitespace only
     * @param line the line of text to check
     * @return true if the line is a comment or is blank
     */
    private static boolean printComment(String line){
        if(line.isEmpty()){
            System.out.println();
            return true;
        } 
        else if(line.charAt(0) == '/' && line.charAt(1) == '/'){
            System.out.println(line);
            return true;
        }
        return false;
    }

    /**
     * This method calls commands based in the input. It parses it and calls the appropraite internal method. 
     * @param puzzle the instance of the puzzle to send the command to
     * @param command The string calling the command
     * @return Returns true if the command was called and false if the command was invalid. 
     * It does not check whether the command actually ran or not. 
     * For example, even if a move was invalid, it would still return true as long as the move method was called
     */
    public static boolean cmd(String command){
        String[] parseCmd = command.split(" ");
        String method = parseCmd[0];

        switch(method){
            case "setState":
                String[] a = Arrays.copyOfRange(parseCmd, 1, parseCmd.length);
                int[] b = new int[9];
                for (int i = 0; i < a.length; i++){
                    b[i] = Integer.parseInt(a[i]);
                }
                puzzle.setState(b);
                break;
            case "printState":
                puzzle.printState();
                break;
            case "move":
                puzzle.move(parseCmd[1]);
                break;
            case "scrambleState":
                puzzle.scrambleState(Integer.parseInt(parseCmd[1]));
                break;
            case "setSeed":
                puzzle.setSeed(Integer.parseInt(parseCmd[1]));
                break;
            case "solve":
                solve(parseCmd);
                return true;
            case "branchFactor":
                branchFactor(Integer.parseInt(parseCmd[1]), Integer.parseInt(parseCmd[2]));
                break;
            case "quit":
                break;
            default:
                System.out.println("Error: Invalid Command");
                return false;
        }

        return true;
    }

    /**
     * The solve method activates the desired method to solve the puzzle according to the input
     * @return retruns true if the solve method was called and false if the solve method is invalid
     */
    public static boolean solve(String[] parseCmd){
        String solveMethod = parseCmd[1];
        Puzzle8Solver solver;

        if (puzzle.isPuzzleEmpty){
            System.out.println("Empty Puzzle");
            return false;
        }

        switch (solveMethod){
            case "BFS":
                if(parseCmd.length == 3){
                    String maxNodes = parseCmd[2].replaceAll("[^0-9]", "");
                    solver = new Puzzle8Solver(puzzle, Integer.parseInt(maxNodes));
                    solver.bfs();
                } else {
                    solver = new Puzzle8Solver(puzzle, 1000);
                    solver.bfs();
                }
                break;
            case "DFS":
                if(parseCmd.length == 4){ //assumes that maxnodes always comes before depthlimit parameter
                    String maxNodes = parseCmd[2].replaceAll("[^0-9]", "");
                    String depthlimit = parseCmd[3].replaceAll("[^0-9]", "");
                    solver = new Puzzle8Solver(puzzle, Integer.parseInt(maxNodes), Integer.parseInt(depthlimit));
                    solver.dfs();

                } else if(parseCmd.length == 3){//only one parameter inputted
                    //if maxnodes is the only parameter
                    if(parseCmd[2].replaceAll("[^a-z]", "").equals("maxnodes")){ 
                        solver = new Puzzle8Solver(puzzle, Integer.parseInt(parseCmd[2].replaceAll("[^0-9]", "")));
                        solver.dfs();
                    }
                    //depthlimit is the only parameter
                    else {
                        solver = new Puzzle8Solver(puzzle, 1000, Integer.parseInt(parseCmd[2].replaceAll("[^0-9]", "")));
                        solver.dfs();
                    }

                } 
                else { 
                    solver = new Puzzle8Solver(puzzle, 1000, 31);
                    solver.dfs();
                }
            
                break;
            
            case "A*":
                if(parseCmd.length == 4){
                    String maxNodes = parseCmd[3].replaceAll("[^0-9]", "");
                    solver = new Puzzle8Solver(puzzle, Integer.parseInt(maxNodes));
                    solver.Astar(parseCmd[2]);
                } else {
                    solver = new Puzzle8Solver(puzzle, 1000);
                    solver.Astar(parseCmd[2]);
                }
                
                break;
                

            default:
                System.out.println("Invalid Solve Method");
                return false;

        }
        return true;
    }


    private static void branchFactor(int d, int nodes){
        Puzzle8Solver solver = new Puzzle8Solver(puzzle, nodes);
        System.out.println(d + " " + nodes + " " + solver.calculateBranchingFactor(d, nodes));
    }

    /**
     * The main method. If there is an args, it calls the cmdfile method
     * If there is no args, it opens the interactive prompt for the user to type commands in the terminal
     * @param args the filename 
     */
    public static void main(String[] args) {
        String input;
        Scanner keyb;

        System.out.println("Welcome to puzzle 8!");

        if (args.length > 0){
            cmdfile(args[0]);
        }
        else {
            input = "";
            keyb = new Scanner(System.in);
            while (!input.equals("quit")){ 
                input = keyb.nextLine();
                cmd(input);
            }
            keyb.close();
        }
    }
}//class
