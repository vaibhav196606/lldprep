package lldexamples;

import java.util.ArrayList;
import java.util.HashMap;

enum GameState{
    STARTED, IDLE, OVER
}

class Player{
    private String name;
    private int position;
    public Player(String name){
        this.name = name;
        this.position = 0;
    }

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }
}

class Board{
    private HashMap<Integer,Integer> ladders;
    private HashMap<Integer,Integer> snakes;
    public Board(){
        ladders = new HashMap<>();
        snakes = new HashMap<>();
        assignValues();
    }

    private void assignValues() {
        int snakeCount = 0;
        while(snakeCount<8){
            int start = (int)(Math.random()*100)+1;
            int end = (int)(Math.random()*100)+1;
            if(start>end && !snakes.containsKey(start) && !ladders.containsKey(start)){
                snakes.put(start,end);
                snakeCount++;
            }
        }
        int ladderCount = 0;
        while(ladderCount<8){
            int start = (int)(Math.random()*100)+1;
            int end = (int)(Math.random()*100)+1;
            if(start<end && !ladders.containsKey(start) && !snakes.containsKey(start)){
                ladders.put(start,end);
                ladderCount++;
            }
        }
    }

    public HashMap<Integer, Integer> getLadders() {
        return ladders;
    }

    public HashMap<Integer, Integer> getSnakes() {
        return snakes;
    }

}

class Dice{
    public static int rollDice(){
        return (int)(Math.random()*6)+1;
    }
}

class Move{
    public static void execute(Board board, Player player, Game game){
        System.out.println("Its turn for : " + player.getName());
        System.out.println("Rolling the dice");
        int dice = Dice.rollDice();
        System.out.println("Its : " + dice);
        int position = player.getPosition() + dice;
        if(position>100){
            System.out.println("Its out of bounds! Sorry");
            return;
        }
        if(board.getLadders().containsKey(position)){
            System.out.println("Ladder at : " + position);
            position = board.getLadders().get(position);
        }
        if(board.getSnakes().containsKey(position)){
            System.out.println("Snake at : " + position);
            position = board.getSnakes().get(position);
        }
        System.out.println("New Position : " + position);
        player.setPosition(position);
    }
}

class Game{
    private static Game instance;
    private Board board;
    private ArrayList<Player> players;
    private GameState state;
    private int noOfPlayers;
    public Game(int noOfPlayers){
        board = new Board();
        players = new ArrayList<>();
        this.noOfPlayers = noOfPlayers;
        for(int i = 1; i <=noOfPlayers; i++){
            players.add(new Player(String.valueOf(i)));
        }
    }
    void play(){
        state = GameState.STARTED;
        while(state!=GameState.OVER){
            for(int i = 0; i < noOfPlayers; i++){
                Move.execute(board, players.get(i), this);
                if(players.get(i).getPosition()==100){
                    state = GameState.OVER;
                    System.out.println("Player " + players.get(i).getName() +" wins!");
                    break;
                }
            }
        }
    }
    public void setState(GameState state){
        this.state = state;
    }

}

public class SnakeAndLadder {
    public static void main(String[] args) {
        Game game = new Game(3);
        game.play();
    }
}
