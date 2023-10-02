// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.



/*
 *
 *
 *
 * Tic tac toe reqs:
 * 1. Player forms a team another active player not currently involved in any game
 * 2. A team can start a new game (either of the player can start the game)
 * 3. Player can resume the game or terminate the game after login in
 * 4. If the game is terminate by any player, the game ends and other player is notified about it
 * 5. Player can only make alternate moves
 * 6. Notify the player that they can make move now as other player has played his chance
 * 7. Maintain instances for each active game in the env
 * 8. Allows only valid moves
 * 9. maintain scores for player
 * 10. maintain number of games played by each player
 * 11. get top 10 scores for any instance for each zonal consideration region,city, country.
 * 12. match each player with another active player with priority to proximity
 *
 *
 *
 *
 * object:
 * 1. player
 * 2. currgame
 * 3. board
 * 4. tictactoe
 * 5. scoreboard
 * 6.
 */


import java.util.List;
import java.util.UUID;

class TicTacToe{
}
class Game{
    Player player1;
    Player player2;
    UUID gameId;
    boolean isTerminated;
    int currPlayer;
    Board board;
    int[] LastMove;
    int p1Id;
    int p2id;
    Game(Player p1, Player p2){
        this.player1 = p1;
        this.player2 = p2;
        board = new Board();
        isTerminated=false;
        gameId = new UUID(100,100);
        LastMove = new int[2];
        p1Id=-1;
        p2id=1;
        this.currPlayer = p1Id;
    }

    int getId(Player player){
        if(player.equals(this.player1)) return  p1Id;
        if(player.equals(this.player2)) return  p2id;
        return 0;
    }
    boolean makeMove(Player player, int[] move){
        //if(player!=currPlayer) return false;//only alternate moves are allowed
        int id = getId(player);
        if(id==0) return false;
        if(board.validMove(LastMove,move,id)){
          if(board.makeMove(LastMove,move,id)) {
              LastMove = move;
              currPlayer = id*-1;
              return true;
          }
        }
        return false;
    }
    boolean checkWinningCondition(){
        return board.checkWinningCondition();
    }
    Player getWinner(){
        if(checkWinningCondition()){
            int playerId = board.getWinner();
            if(playerId==getId(this.player1)) return this.player1;
            if(playerId==getId(this.player2)) return this.player2;
        }
        return null;
    }
}
class Board{
    int[][] board;

     boolean validMove(int[] prev, int[] move, int player){
        int x = prev[0];
        int y = prev[1];
        int xNew = move[0], yNew = move[1];
        if(board[x][y]!=player) {
            return false;
        }
        if(xNew>=0 && yNew>=0 && xNew< board.length && yNew< board.length && board[xNew][yNew]!=0) {
            return false;
        }
        return true;
    }
    void resetBoard(){
         for(int i=0;i< board.length;i++)
             for(int j=0;i< board[0].length;j++){
                 board[i][j]=0;
             }
    }
    boolean makeMove(int[] prev, int[] move, int player){
        if(!validMove(prev,move,player)) {
            return false;
        }
        int xNew = move[0], yNew = move[1];
        board[xNew][yNew]=player;
        return true;
    }
    boolean checkWinningCondition(){
         return true;
    }

    public int getWinner() {

         return 1;
    }
}
class Player{
    UUID playerId;
    PersonInfo personInfo;
   // PersonaRecord record;
    Game game;

}

class PersonInfo{
    Name name;
    Country country;
    City city;
    int zipcode;
}
class Name{
    String firstName;
    String lastName;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
class Country{
    CountryName name;
    List<City> cities;
    List<Integer> zipcodes;
}
enum CountryName{
    USA,
    INDIA,
}
enum City{
    MUMBAI,
    NYC,
}

public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Tic tac toe intial commit!");

    }
}