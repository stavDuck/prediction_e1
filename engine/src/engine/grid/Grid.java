package engine.grid;

import engine.Position;
import engine.entity.EntityInstance;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid {
    private int rows;
    private int columns;
    private int freeSpots;
    private EntityInstance[][] gridBoard;

    public Grid() {
        this.rows = 0;
        this.columns = 0;
        this.freeSpots = rows*columns;
    }
    public Grid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        gridBoard = new EntityInstance[rows][columns];
    }

    //getters
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }

    public EntityInstance getPositionInGridBoard(int x, int y){
        return gridBoard[x][y];
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    //setters
    public void setRows(int rows) {
        this.rows = rows;
    }
    public void setColumns(int columns) {
        this.columns = columns;
    }
    public void setPositionInGridBoard(EntityInstance val, int x, int y){
        gridBoard[x][y] = val;
    }
    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public void initGridBoard(){
        gridBoard = new EntityInstance[rows][columns]; // default values are false for free cells
    }


    // function gather all the free spots in the grid and return the grid position
    public Position getRandomFreePoseInGrid(){
        // save all the free position in arr
        List<Position> arrOfFreePos = IntStream.range(0, rows)
                .boxed()
                .flatMap(row -> IntStream.range(0, columns)
                        .filter(col -> gridBoard[row][col] == null)
                        .mapToObj(col -> new Position(row, col)))
                .collect(Collectors.toList());

        // chose random position
        Random random = new Random();
        return arrOfFreePos.get(random.nextInt(arrOfFreePos.size()));
    }
}
