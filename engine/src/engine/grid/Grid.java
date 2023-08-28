package engine.grid;

public class Grid {
    private int rows;
    private int columns;

    public Grid() {
        this.rows = 0;
        this.columns = 0;

    }
    public Grid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
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
}
