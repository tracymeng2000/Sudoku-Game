import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.Point;
import java.awt.Graphics2D;


/* Main Model */
public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    
    private Generator game;
    public static final int GRID_SIZE = 9; // Size of the board
    // Puzzle to be solved and the mask (which can be used to control the
   //  difficulty level).
   // Hardcoded here. Extra credit for automatic puzzle generation
   //  with various difficulty levels.
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

    private int[][] solution =
        {{0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0, 0}};

   // For testing, open only 2 cells.
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

    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList<Observer>();

        // generate puzzle
        game = new Generator();
        puzzle = game.newPuzzle();
        solution = deepCopy(puzzle);
        masks = game.newMasks();
    }
    
    public void refreshGame(){
        puzzle = game.newPuzzle();
        solution = deepCopy(puzzle);
        masks = game.newMasks();
        notifyObservers();
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

    public int[][] getPuzzle(){
        return puzzle;
    }

    public boolean[][] getMasks(){
        return masks;
    }

    public int[][] getSolution(){
        return solution;
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }


    public boolean isCorrect(JTextField[][] tfCells){
        for(int row = 0; row < GRID_SIZE; row++){
            for(int col = 0; col < GRID_SIZE; col++){
                if(Integer.parseInt(tfCells[row][col].getText()) != solution[row][col]){
                    return false;
                }
            }
        }

        return true;
    }



    /**
     * Notify observers that the model has changed.
     */
    public void notifyObservers() {

        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    
}
