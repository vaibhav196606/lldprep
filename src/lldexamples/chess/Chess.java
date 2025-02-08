package lldexamples.chess;


import java.util.Scanner;

enum Color {
    WHITE, BLACK
}

enum GameState {
    STARTED, IDLE, OVER
}

enum PieceState {
    ALIVE, DEAD
}

class Player{
    private Color color;
    private String name;
    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
    }
    public Color getColor() {
        return color;
    }
    public String getName() {
        return name;
    }
    public boolean isWhite() {
        return color == Color.WHITE;
    }
}

interface MoveStrategy {
    public boolean canMove(Cell start, Cell end);
}

class PawnMove implements MoveStrategy {
    public boolean canMove(Cell start, Cell end) {
        return true;
    }
}
class KingMove implements MoveStrategy {
    public boolean canMove(Cell start, Cell end) {
        return true;
    }
}
class QueenMove implements MoveStrategy {
    public boolean canMove(Cell start, Cell end) {
        return true;
    }
}
class KnightMove implements MoveStrategy {
    public boolean canMove(Cell start, Cell end) {
        return true;
    }
}
class BishopMove implements MoveStrategy {
    public boolean canMove(Cell start, Cell end) {
        return true;
    }
}

class RookMove implements MoveStrategy {
    public boolean canMove(Cell start, Cell end) {
        return true;
    }
}

class MoveContext {
    private MoveStrategy strategy;
    MoveContext(MoveStrategy strategy) {
        this.strategy = strategy;
    }

    boolean canMove(Cell start, Cell end) {
        return strategy.canMove(start, end);
    }
}

abstract class Piece{
    private Color color;
    private PieceState state;
    protected MoveContext moveContext;
    public Piece(Color color, PieceState state) {
        this.color = color;
        this.state = state;
    }

    public boolean canMove(Cell start, Cell end){
        return moveContext.canMove(start, end);
    };

    public boolean isWhite(){
        return color == Color.WHITE;
    }

}

class Pawn extends Piece{
    Pawn(Color color, PieceState state) {
        super(color, state);
        moveContext = new MoveContext(new PawnMove());
    }
}
class King extends Piece{
    King(Color color, PieceState state) {
        super(color, state);
        moveContext = new MoveContext(new KingMove());
    }
}
class Bishop extends Piece{
    Bishop(Color color, PieceState state) {
        super(color, state);
        moveContext = new MoveContext(new BishopMove());
    }
}
class Knight extends Piece{
    Knight(Color color, PieceState state) {
        super(color, state);
        moveContext = new MoveContext(new KnightMove());
    }
}
class Rook extends Piece{
    Rook(Color color, PieceState state) {
        super(color, state);
        moveContext = new MoveContext(new RookMove());
    }
}
class Queen extends Piece{
    Queen(Color color, PieceState state) {
        super(color, state);
        moveContext = new MoveContext(new QueenMove());
    }
}

class Cell{
    private int row;
    private int col;
    private Piece piece;
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        piece = null;
    }

    public Cell(int row, int col, Piece piece) {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }
    boolean isEmpty() {
        return piece == null;
    }
    void setPiece(Piece piece) {
        this.piece = piece;
    }
    Piece getPiece() {
        return this.piece;
    }
}

class Board {
    Cell[][] cells;
    Board() {
        cells = new Cell[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        cells[0][0] = new Cell(0,0,new Rook(Color.BLACK,PieceState.ALIVE));
        cells[0][1] = new Cell(0,1,new Knight(Color.BLACK,PieceState.ALIVE));
        cells[0][2] = new Cell(0,2,new Bishop(Color.BLACK,PieceState.ALIVE));
        cells[0][3] = new Cell(0,3,new Queen(Color.BLACK,PieceState.ALIVE));
        cells[0][4] = new Cell(0,4,new King(Color.BLACK,PieceState.ALIVE));
        cells[0][5] = new Cell(0,5,new Bishop(Color.BLACK,PieceState.ALIVE));
        cells[0][6] = new Cell(0,6,new Knight(Color.BLACK,PieceState.ALIVE));
        cells[0][7] = new Cell(0,7,new Rook(Color.BLACK,PieceState.ALIVE));
        for(int col = 0; col < 8; col++) {
            cells[1][col] = new Cell(1,col,new Pawn(Color.BLACK,PieceState.ALIVE));
        }
        for(int col = 0; col < 8; col++) {
            cells[6][col] = new Cell(6,col,new Pawn(Color.WHITE,PieceState.ALIVE));
        }
        cells[7][0] = new Cell(7,0,new Rook(Color.WHITE,PieceState.ALIVE));
        cells[7][1] = new Cell(7,1,new Knight(Color.WHITE,PieceState.ALIVE));
        cells[7][2] = new Cell(7,2,new Bishop(Color.WHITE,PieceState.ALIVE));
        cells[7][3] = new Cell(7,3,new Queen(Color.WHITE,PieceState.ALIVE));
        cells[7][4] = new Cell(7,4,new King(Color.WHITE,PieceState.ALIVE));
        cells[7][5] = new Cell(7,5,new Bishop(Color.WHITE,PieceState.ALIVE));
        cells[7][6] = new Cell(7,6,new Knight(Color.WHITE,PieceState.ALIVE));
        cells[7][7] = new Cell(7,7,new Rook(Color.WHITE,PieceState.ALIVE));

        for(int row = 2; row < 6; row++) {
            for(int col = 0; col < 8; col++) {
                cells[row][col] = new Cell(row,col);
            }
        }

    }
}

class Move{
    public static boolean initiateMove(Board board, Player player) {
        boolean successMove = false;
        while(!successMove) {
            System.out.println("Its player : " + player.getName() + " turn");
            System.out.println("Enter starting x,y from where you would like to move");
            int startX;
            int startY;
            int endX;
            int endY;
            Scanner scanner = new Scanner(System.in);
            startX = scanner.nextInt();
            startY = scanner.nextInt();
            System.out.println("Enter ending x,y to where you would like to move");
            endX = scanner.nextInt();
            endY = scanner.nextInt();
            if(validateInput(startX, startY, endX, endY) && isMoveValid(board.cells[startX][startY],board.cells[endX][endY], player)){
                System.out.println("Move successful from "+startX+","+startY+" to "+endX+","+endY);
                if(checkForWin(board.cells[endX][endY].getPiece()))return true;
                board.cells[endX][endY].setPiece(board.cells[startX][startY].getPiece());
                board.cells[startX][startY].setPiece(null);
                successMove = true;
            }
            else {
                System.out.println("Invalid move, try again");
            }

        }
        return false;
    }

    private static boolean checkForWin(Piece piece) {
        if(piece instanceof King) {return true;}
        return false;
    }

    private static boolean isMoveValid(Cell cell1, Cell cell2, Player player) {
        if(cell1.isEmpty() ||  cell1.getPiece().isWhite() != player.isWhite() || (!cell2.isEmpty() && cell2.getPiece().isWhite()==cell1.getPiece().isWhite())) {
            return false;
        }
        return cell1.getPiece().canMove(cell2, cell1);
    }

    private static boolean validateInput(int startX, int startY, int endX, int endY) {
        if(startX >=8 || startX <0 || startY >=8 || startY <0 || endX >=8 || endX <0 || endY >=8 || endY <0) {return false;}
        return true;
    }
}

class Game{
    private Board board;
    private Player player1;
    private Player player2;
    private GameState gameState;
    private boolean isWhiteTurn;
    public Game() {
        board = new Board();
        player1 = new Player(Color.BLACK, "Vaibhav");
        player2 = new Player(Color.WHITE, "Computer");
        gameState = GameState.IDLE;
        isWhiteTurn = false;
    }

    void play() {
        gameState = GameState.STARTED;
        while(gameState != GameState.OVER) {
            if(isWhiteTurn){
                if(Move.initiateMove(board, player1)){
                    System.out.println(player1.getName() + " won the game");
                    gameState = GameState.OVER;
                    break;
                };
            }
            else {
                if(Move.initiateMove(board,player2)){
                    System.out.println(player2.getName() + " won the game");
                    gameState = GameState.OVER;
                    break;
                }
            }
            isWhiteTurn = !isWhiteTurn;
        }
    }
}

public class Chess {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}

// UML available in the same folder
