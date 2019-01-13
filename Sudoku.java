import java.awt.*; // Uses AWT's Layout Managers
import java.awt.event.*; // Uses AWT's Event Handlers
import javax.swing.*; // Uses Swing's Container/Components 
/**
 * Sudoku
 */

public class Sudoku extends JFrame implements Observer {
    // Name-constants for the game properties
    public static final int GRID_SIZE = 9; // Size of the board
    public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

    // Name-constants for UI control (sizes, colors and fonts)
    public static final int CELL_SIZE = 60; // Cell width/height in pixels
    public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
    public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
    // Board width/height in pixels
    public static final Color OPEN_COLOR = new Color(135, 206, 250);
    public static final Color CLOSED_COLOR = new Color(240, 240, 240); // RGB
    public static final Color CLOSED_FONT = Color.BLACK;
    public static final Font FONT_SIZES = new Font("Monospaced", Font.BOLD, 20);

    // The game board composes of 9x9 JTextFields,
    // each containing String "1" to "9", or empty String
    private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];
    private int[][] puzzle;
    private boolean[][] masks;


    private Model model;
    
    //constructor
    public Sudoku(Model m, String title) {
        super(title);
        model = m;
        model.addObserver(this);

        puzzle = model.getPuzzle();
        masks = model.getMasks();
        
        


        Container cp = getContentPane();
        cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE)); // 9x9 GridLayout

        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                tfCells[row][col] = new JTextField(); // Allocate element of array
                cp.add(tfCells[row][col]); // ContentPane adds JTextField
                if (masks[row][col]) {
                    tfCells[row][col].setText(""); // set to empty string
                    tfCells[row][col].setEditable(true);
                    tfCells[row][col].setBackground(OPEN_COLOR);
                    int r = row;
                    int c = col;
                    tfCells[row][col].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            //System.out.println(Integer.toString(r) + " " + Integer.toString(c) + " " + tfCells[r][c].getText());
                            String inp = tfCells[r][c].getText();
                            System.out.println(inp);
                            if(! (inp.equals("1") || inp.equals("2") || inp.equals("3") 
                                || inp.equals("4") || inp.equals("5") || inp.equals("6") 
                                || inp.equals("7") || inp.equals("8") || inp.equals("9"))){

                                tfCells[r][c].setText("");
                            }else if(isFull()){
                                if(model.isCorrect(tfCells)){
                                    Object[] options = {"Play Again", "Quit!"};
                                    int input = JOptionPane.showOptionDialog(cp,
                                        "Congrats, you got it correct!",
                                        "Would you like to play again?",
                                        JOptionPane.YES_OPTION,
                                        JOptionPane.NO_OPTION,
                                        null,     //custom icon
                                        options, 
                                        options[0]); //default play again
                                    if(input == JOptionPane.YES_OPTION){
                                        model.refreshGame();
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(cp, "Keep trying!");
                                }
                            }
                        }
                    });

                } else {
                    tfCells[row][col].setText(Integer.toString(puzzle[row][col]));
                    tfCells[row][col].setEditable(false);
                    tfCells[row][col].setBackground(CLOSED_COLOR);
                    tfCells[row][col].setForeground(CLOSED_FONT);
                }
                // Beautify all the cells
                tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
                tfCells[row][col].setFont(FONT_SIZES);
            }
        }

        // Set the size of the content-pane and pack all the components
        //  under this container.
        cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window closing
        setTitle("Sudoku");
        setVisible(true);

        //printing solution for testing
        int[][] solution = model.getSolution();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(solution[i][j]);
           }
            System.out.println();
        }
    }

    //checks if all cells have numbers filled 
    private boolean isFull(){
      for(int col = 0; col < GRID_SIZE; col++){
        for(int row = 0; row < GRID_SIZE; row++){
          if(tfCells[row][col].getText().equals("")) return false;
        }
      }
      return true;
    }


    public void update(Object observable) {
        this.setVisible(true);
        puzzle = model.getPuzzle();
        masks = model.getMasks();
        refreshFrame(puzzle, masks);
    }

    public void refreshFrame(int[][] puzzle, boolean[][] masks){
        for(int row = 0; row < GRID_SIZE; row++){
            for(int col = 0; col < GRID_SIZE; col++){
                if (masks[row][col]) {
                    tfCells[row][col].setText(""); // set to empty string
                    tfCells[row][col].setEditable(true);
                    tfCells[row][col].setBackground(OPEN_COLOR);

                } else {
                    tfCells[row][col].setText(Integer.toString(puzzle[row][col]));
                    tfCells[row][col].setEditable(false);
                    tfCells[row][col].setBackground(CLOSED_COLOR);
                    tfCells[row][col].setForeground(CLOSED_FONT);
                }
            }
        }
    }
}
