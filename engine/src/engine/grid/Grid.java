package engine.grid;

public class Grid {
    private int rows;
    private int columns;
    private boolean[][] gridBoard;

    public Grid() {
        this.rows = 0;
        this.columns = 0;

    }
    public Grid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        gridBoard = new boolean[rows][columns];
    }

    //getters
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }

    //setters
    public void setRows(int rows) {
        this.rows = rows;
    }
    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void initGridBoard(){
        gridBoard = new boolean[rows][columns]; // default values are false for free cells
    }
}
