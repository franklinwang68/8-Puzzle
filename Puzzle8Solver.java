package HW1;

import java.util.*;

/**
 * Puzzle Solver that uses various algorithms to solve the Puzzle8
 */
public class Puzzle8Solver {
    private int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
    private Puzzle8 puzzle; //The puzzle to solve
    private int maxNodes;
    private int nodesCreated; //increments each time "new Node()" is written in the code
    private Set<String> visited;
    private int depthlimit;

    /**
     * The constructor for the Puzzle8Solver
     * @param puzzle the puzzle to solve
     * @param maxNodes the maximum nodes limit
     */
    public Puzzle8Solver(Puzzle8 puzzle, int maxNodes) {
        this.puzzle = puzzle;
        this.maxNodes = maxNodes;
        this.nodesCreated = 0;
        this.visited = new HashSet<>();
    }

    /**
     * Another constructor for dfs
     * @param puzzle
     * @param maxNodes
     * @param depthlimit
     */
    public Puzzle8Solver(Puzzle8 puzzle, int maxNodes, int depthlimit) {
        this.puzzle = puzzle;
        this.maxNodes = maxNodes;
        this.depthlimit = depthlimit;
        this.nodesCreated = 0;
        this.visited = new HashSet<>();
    }

    /**
     * Returns true whether the given state matches the goal state or not
     * @param state the state to check in the form if int[][]
     * @return true if the state is the goal
     */
    private boolean isGoalState(int[][] state) {
        return Arrays.deepEquals(state, goalState);
    }

    /**
     * Returns the possible states of the children of the parent state
     * For BFS
     * @param state The parent state
     * @return A list of possible children states as Nodes
     */
    private List<Node> getSuccessors(int[][] currentState) {    
        List<Node> successors = new ArrayList<>(); //list of successors to return
        Puzzle8 currentPuzzle = new Puzzle8();
        int[] s = new int[9];
        int count = 0;
        int[] zero = currentPuzzle.getZero();
        int[][] newState1 = new int[3][3]; //a new state is declared for each possible move because a deep copy is needed.
        int[][] newState2 = new int[3][3];
        int[][] newState3 = new int[3][3];
        int[][] newState4 = new int[3][3];

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                s[count] = currentState[i][j];
                count++;
                newState1[i][j] = currentState[i][j]; //creating deep copies.
                newState2[i][j] = currentState[i][j];
                newState3[i][j] = currentState[i][j];
                newState4[i][j] = currentState[i][j];
            }
        }

        currentPuzzle.setState(s);

        // Define all moves
        String[] movesDirections = {"left", "right", "up", "down"};
        
        // Generate new states for each valid move
        for (String direction : movesDirections) {
            // Add new state to succssors if move is valid
            if (currentPuzzle.checkValidMove(direction)) {
                
                switch(direction){
                    case "up": 
                        newState1[zero[0]][zero[1]] = newState1[zero[0]-1][zero[1]];
                        newState1[zero[0]-1][zero[1]] = 0;
                        successors.add(new Node(newState1, null, "move up"));
                        nodesCreated++;
                        break;
                    case "down":
                        newState2[zero[0]][zero[1]] = newState2[zero[0]+1][zero[1]];
                        newState2[zero[0]+1][zero[1]] = 0;
                        successors.add(new Node(newState2, null, "move down"));
                        nodesCreated++;
                        break;
                    case "left":
                        newState3[zero[0]][zero[1]] = newState3[zero[0]][zero[1]-1];
                        newState3[zero[0]][zero[1]-1] = 0;
                        successors.add(new Node(newState3, null, "move left"));
                        nodesCreated++;
                        break;
                    case "right":
                        newState4[zero[0]][zero[1]] = newState4[zero[0]][zero[1]+1];
                        newState4[zero[0]][zero[1]+1] = 0;
                        successors.add(new Node(newState4, null, "move right"));
                        nodesCreated++;
                        break;
                    default:
                        //System.out.println("Error: Invalid Move");
                        break;
                }
            }
        }
        return successors;
    }

    /**
     * Converts the state to a string to be stored in visited.
     * Strings are easily hashable
     * @param state the state 
     * @return the String equivalent of the state
     */
    private String stateToString(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : state) {
            for (int cell : row) {
                sb.append(cell).append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Breadth First Search to solve Puzzle8
     * The implementation the inner Node class that has a the attribute path
     * Thus, reconstructing the path from the goal state is not necessary. However, this takes up larger memory.
     */
    public void bfs() {
        //queues the starting state
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(puzzle.getPuzzleState(), new ArrayList<>(), null));
        visited.add(stateToString(puzzle.getPuzzleState()));
        nodesCreated++; //tracks the number of states. This is the initial state

        if(isGoalState(queue.peek().state)){
            System.out.println("Puzzle already in Goal State");
            return;
        }

        while (!queue.isEmpty()) {
            //max limit reached
            if (nodesCreated >= maxNodes) {
                System.out.printf("Error: maxnodes limit (%d) reached\n", maxNodes);
                return;
            }

            //pops node and stores the state and path
            Node currentNode = queue.poll();
            int[][] currentState = currentNode.state;
            List<String> path = currentNode.path;

            //Checks if children are goal state and adds to queue if not goal
            for (Node successor : getSuccessors(currentState)) {
                String successorString = stateToString(successor.state);

                //repeated state checking
                if (!visited.contains(successorString)) {
                    visited.add(successorString);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(successor.move); 

                    if (isGoalState(successor.state)) {
                        printSolution(newPath);
                        return;
                    }

                    queue.add(new Node(successor.state, newPath, successor.move));
                    nodesCreated++;
                }
            }
        }
        System.out.println("No solution found within the node limit.");
    }

    /**
     * Depth First Search for puzzle 8
     * Added a depth attribute to Node to track the depth of a node for depthlimit parameter
     */
    public void dfs(){
        //pushes the starting state
        Stack<Node> stack = new Stack<>();
        stack.push(new Node(puzzle.getPuzzleState(), new ArrayList<>(), null));
        visited.add(stateToString(puzzle.getPuzzleState()));
        nodesCreated++; //tracks the number of states. This is the initial state

        if(isGoalState(stack.peek().state)){
            System.out.println("Puzzle already in Goal State");
            return;
        }

        while (!stack.isEmpty()) {
            //max limit reached
            if (nodesCreated >= maxNodes) {
                System.out.printf("Error: maxnodes limit (%d) reached\n", maxNodes);
                return;
            }

            //pops node and stores the state and path
            Node currentNode = stack.pop();
            int[][] currentState = currentNode.state;
            List<String> path = currentNode.path;
            //int depth = currentNode.depth;

            if (isGoalState(currentNode.state)) {
                printSolution(path);
                return;
            }

            for(int depth = currentNode.depth; depth < depthlimit; depth++){
                for (Node successor : getSuccessors(currentState)) {
                    String successorString = stateToString(successor.state);

                    //repeated state checking
                    if (!visited.contains(successorString)) {
                        visited.add(successorString);
                        List<String> newPath = new ArrayList<>(path);
                        newPath.add(successor.move); 
                        stack.push(new Node(successor.state, newPath, successor.move, depth + 1));
                        nodesCreated++;
                    }
                }
            }
        }
        System.out.println("No solution found within the node limit or depth limit.");

    }

    /**
     * Runs A* search on the puzzle to find a solution
     * @param h the heuristic to use
     */
    public void Astar(String h){
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        pq.add(new Node(puzzle.getPuzzleState(), new ArrayList<>(), null, heuristic(puzzle.getPuzzleState(), h))); //first node
        visited.add(stateToString(puzzle.getPuzzleState()));

        if(isGoalState(pq.peek().state)){
            System.out.println("Puzzle already in Goal State");
            return;
        }

        while (!pq.isEmpty()) {
            if (nodesCreated >= maxNodes) {
                System.out.printf("Error: maxnodes limit (%d) reached\n", maxNodes);
                return;
            }

            Node currentNode = pq.poll();
            int[][] currentState = currentNode.state;
            List<String> path = currentNode.path;
            int gCost = currentNode.gCost; //cost of current node from start state

            if (isGoalState(currentState)) {
                printSolution(path);
                return;
            }

            for (Node successor : getSuccessors(currentState)) {
                String successorString = stateToString(successor.state);
                if (!visited.contains(successorString)) {
                    visited.add(successorString);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(successor.move);

                    int newGCost = gCost + 1; //computes the new cost 
                    int newFCost = newGCost + heuristic(successor.state, h); //calculates f(n) = g(n) + h(n)
                    pq.add(new Node(successor.state, newPath, successor.move, newGCost, newFCost));
                    nodesCreated++;
                }
            }
        }
        System.out.println("No solution found within the node limit.");
    }


    /**
     * Gives the heuristic value given a state and the heurstic to use
     * @param state the state of the puzzle
     * @param heuristicType the heuristic to use
     * @return the value of the heuristic function on the given state
     */
    private int heuristic(int[][] state, String heuristicType) {
        switch (heuristicType) {
            case "h1":
                return misplacedTiles(state);
            case "h2":
                return manhattanDistance(state);
            default:
                System.err.println("Invalid Heurisitc");
                System.exit(1);
                return 0;
        }
    }

    /**
     * The heuristic function to calculate the number of misplaced tiles
     * @param state the state of the puzzle
     * @return the number of misplaced tiles
     */
    private int misplacedTiles(int[][] state) {
        int misplaced = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //doesn't count the blank tile as misplaced
                if (state[i][j] != 0 && state[i][j] != goalState[i][j]) {
                    misplaced++;
                }
            }
        }
        return misplaced;
    }

    /**
     * The heuristic function to calculate the sum of distances tiles from the goal
     * @param state the state of the puzzle
     * @return the sum of the distances of tiles from the goal
     */
    private int manhattanDistance(int[][] state) {
        int distance = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = state[i][j];
                if (value != 0) {
                    int goalX = value / 3; 
                    int goalY = value % 3;
                    distance += Math.abs(i - goalX) + Math.abs(j - goalY);
                }
            }
        }
        return distance;
    }


    /**
     * Calculates the effective branching factor b, using N + 1 = 1 + b + b^2 + ... b^d
     * @param d the solution depth
     * @param nodesGenerated the nodes generated
     * @return the effective branching factor
     */
    public double calculateBranchingFactor(int d, int nodesGenerated){
        double low = 1.0;
        double high = nodesGenerated;
        double mid = 0.0;
        double epsilon = 1e-15; //the acceptable error of the branching factor sum
        
        //Uses binary search to determine branching factor
        while (high - low > epsilon) {
            mid = (low + high) / 2.0;
            double sum = 0.0;
            double power = 1.0;
            
            //compute the sum
            for (int i = 0; i <= d; i++) {
                sum += power;
                power *= mid;
            }
            
            //move the bounds appropriately based on the sum result
            if (sum < nodesGenerated + 1) {
                low = mid;
            } else {
                high = mid;
            }
        }
        
        return (low + high) / 2.0;
    }  

    /**
     * Prints the solution
     * @param path the path of the solution
     */
    private void printSolution(List<String> path){
        System.out.println("Nodes created during search: " + nodesCreated);
        System.out.println("Solution length: " + path.size());
        System.out.println("Effective Branching Factor: " + calculateBranchingFactor(path.size(), nodesCreated));
        System.out.println("Move sequence:");
        for (String move : path) {
            System.out.println(move);
        }
    }

    /**
     * A static class Node that represents each state of the puzzle as a node
     */
    private static class Node {
        int[][] state;
        List<String> path;
        String move; //Direction to move to get to children from parent in the format "move up"
        int depth;
        int gCost;
        int cost;

        /**
         * Constructor usesd for BFS
         * @param state
         * @param path
         * @param move
         */
        Node(int[][] state, List<String> path, String move) {
            this.state = state;
            this.path = path;
            this.move = move;
        }

        /**
         * Constructor used for DFS
         * @param state
         * @param path
         * @param move
         * @param depth
         */
        Node(int[][] state, List<String> path, String move, int depth) {
            this.state = state;
            this.path = path;
            this.move = move;
            this.depth = depth;
        }


        /**
         * Constructor for Astar
         * @param state
         * @param path
         * @param move
         * @param gCost
         * @param cost
         */
        Node(int[][] state, List<String> path, String move, int gCost, int cost){
            this.state = state;
            this.path = path;
            this.move = move;
            this.gCost = gCost;
            this.cost = cost;
        }
    }
}

