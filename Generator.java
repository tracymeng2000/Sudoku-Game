import java.util.*;

public class Generator{
	private final int GRID_SIZE = 9; 

	private int[][] puzzle = 
		{{0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0}};

    private boolean[][] masks = 
      	{{false, false, false, false, false, false, false, false, false},
       	{false, false, false, false, false, false, false, false, false},
       	{false, false, false, false, false, false, false, false, false},
       	{false, false, false, false, false, false, false, false, false},
       	{false, false, false, false, false, false, false, false, false},
       	{false, false, false, false, false, false, false, false, false},
       	{false, false, false, false, false, false, false, false, false},
       	{false, false, false, false, false, false, false, false, false},
       	{false, false, false, false, false, false, false, false, false}};


    private final int UNASSIGNED = 0; //represents an empty cell in puzzle
    private final int UNDEFINED = -1; //represents an undefined location in position

    //generates a new puzzle and returns the puzzle
    public int[][] newPuzzle(){
    	generateNewPuzzle();
    	return puzzle;
    }

    //generates a new Sudoku puzzle and assign it to puzzle
    private void generateNewPuzzle(){
    	if(newCompleteBoard()){
    		System.out.println("A new board is generated");
    	}else{
    		System.out.println("A new board can not be generated");
    	}
    }

    public boolean[][] newMasks(){
    	generateNewMasks();
    	return masks;
    }




    private List<Integer> shuffledNums(int startInt, int endInt){
    	//generates a list of integers (1 to 9)
    	List<Integer> result = new ArrayList<>();
		for (int i = startInt; i <= endInt; i++) {
    		result.add(i);
		}
		//shuffle the order of the integers
		Collections.shuffle(result);
		return result;
    }

    //returns a position on the grid that has no number assigned to it
    //in the form of {row, column}
    private int[] unassignedPosition(int[][] grid){
    	int[] position = {UNDEFINED, UNDEFINED};
    	for(int r = 0; r < GRID_SIZE; r++){
    		for(int c = 0; c < GRID_SIZE; c++){
    			if(grid[r][c] == UNASSIGNED){
    				position[0] = r;
    				position[1] = c;
    				return position;
    			}
    		}
    	}
    	return position;
    }

    //returns whether num has already appeared in row
    private boolean usedInRow(int row, int num){
    	for(int c = 0; c < GRID_SIZE; c++){
    		if(puzzle[row][c] == num) return true;
    	}
    	return false;
    }

    //returns whether num has already appeared in col
    private boolean usedInCol(int col, int num){
    	for (int r = 0; r < GRID_SIZE; r++){
    		if(puzzle[r][col] == num) return true;
    	}
    	return false;
    }


    //returns whether num has already appeared in the 3 x 3 subgrid it belongs to
    private boolean usedInBox(int startRow, int startCol, int num){
    	for(int r = startRow; r < startRow + 3; r++){
    		for(int c = startCol; c < startCol + 3; c++){
    			if(puzzle[r][c] == num) return true;
    		}
    	}
    	return false;
    }

    //returns if it's safe to place num at position (row, col) on the grid
    //it is safe if it hasn't appeared in the current row, col and subgrid
    private boolean isSafe(int row, int col, int num){
    	return !usedInRow(row, num) 
    		&& !usedInCol(col, num)
    		&& !usedInBox(row - row % 3, col - col % 3, num);
    }

    //returns true when all cells on the grid are placed with number
    //otherwise places safe random numbers at unassigned positions 
    //until all cells are filled with numbers
    private boolean newCompleteBoard(){
    	int[] emptyPosition = unassignedPosition(puzzle);
    	if(emptyPosition[0] == UNDEFINED){
    		return true;
    	}    	
    	int row = emptyPosition[0];
    	int col = emptyPosition[1];

    	//generates a list of integers (1 to 9)
    	List<Integer> order = shuffledNums(1, GRID_SIZE);

		//try placing order[0] to order[8] at the unassigned position
    	for(int i = 0; i < 9; i++){
    		//place if it's safe
    		if(isSafe(row, col, order.get(i))){
    			puzzle[row][col] = order.get(i);
    			//find the next unassigned position and continue the procudure
    			if(newCompleteBoard()){
    				return true;
    			}
    			puzzle[row][col] = UNASSIGNED;
    		}
    	}
    	//returns false if none of the values in order work
    	return false;
    }

    private static int[][] deepCopy(int[][] original) {
    if (original == null) {
        return null;
    }

    final int[][] result = new int[original.length][];
    for (int i = 0; i < original.length; i++) {
        result[i] = Arrays.copyOf(original[i], original[i].length);
        // For Java versions prior to Java 6 use the next:
        // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
    }
    return result;
}

    private boolean findSolution(int[][] grid){
    	int[] emptyPosition = unassignedPosition(grid);

    	if(emptyPosition[0] == UNDEFINED){
    		return true;
    	}

    	int row = emptyPosition[0];
    	int col = emptyPosition[1];
    	for(int i = 1; i <= 9; i++){
    		if(isSafe(row, col, i)){
    			grid[row][col] = i;
    			if(findSolution(grid)) {
    				return true;
    			}
    			grid[row][col] = UNASSIGNED;
    		}
    	}
    	return false;
    }

    private boolean isUnique(int removed, int row, int col){
    	for(int i = 1; i <= 9; i++){
    		if(i != removed && isSafe(row, col, i)){
    			int[][] copyOfPuzzle = deepCopy(puzzle);
    			copyOfPuzzle[row][col] = i;
    			if(findSolution(copyOfPuzzle)) return false;
    		}
    	}
    	return true;
    }

    private void generateNewMasks(){
    	List<Integer> order = shuffledNums(0, GRID_SIZE * GRID_SIZE - 1);
    	for(int i = 0; i < GRID_SIZE * GRID_SIZE; i++){
    		int row = order.get(i) / GRID_SIZE;
    		int col = order.get(i) % GRID_SIZE;
    		int removedNum = puzzle[row][col];
    		masks[row][col] = true;
    		puzzle[row][col] = UNASSIGNED;
    		if(!isUnique(removedNum, row, col)){
    			puzzle[row][col] = removedNum;
    			masks[row][col] = false;
    		}
    	}
    }






}
