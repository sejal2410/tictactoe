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
 * 5. [done] Player can only make alternate moves
 * 6. Notify the player that they can make move now as other player has played his chance
 * 7. Maintain instances for each active game in the env
 * 8. [done]Allows only valid moves
 * 9. [done]maintain scores for player
 * 10. maintain number of games played by each player
 * 11. [done]get top 10 scores for any instance for each zonal consideration region,city, country.
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
 *
 * Functionalities:
 *
 *
 *
 */


import javax.management.Notification;
import java.util.*;
import java.util.stream.Collectors;

class Player{
    UUID playerId;
    PersonInfo personInfo;
    // PersonaRecord record;
    Game game;

}
class MatchService{
    Location globe;
    Player findMatch(Player player1){
        Optional<Player> match = globe.match(player1);
        if(match!=null && match.isPresent()){
            Player player2 =  match.get();
            return player2;
        }
        throw new RuntimeException("Player not found for given player");
    }
}
class LocationManagerService{
    HashMap<String, smallestRegion> locationHashMap;
    smallestRegion getLocation(String loc){
        //processing logic to get proper key for new location mapping in the form country-state-city;
        return locationHashMap.get(loc);
    }

    public void setNewLocation(Player player, String newLoc) {
        smallestRegion region = getLocation(newLoc);
        region.addPlayer(player);
    }
}
class PlayerService{
    LocationManagerService locMgr;
    HashMap<UUID, Player> players;
    void changeLocation(Player player, String newLoc){
        locMgr.setNewLocation(player,newLoc);
        players.put(player.playerId,player);
    }
    void registerPlayer(PersonInfo personInfo, String location){
        Player player = new Player();
        player.personInfo = personInfo;
        player.playerId = new UUID(100,10);
        locMgr.setNewLocation(player,location);
    }
    boolean registerNewGame(Player player, Player player2, Game game){
        return false; //new UnsupportedOperationException();
    }
    boolean terminateGame(Player player){
        if(player==null || player.game==null) throw new RuntimeException("termination for game called when either player or the game is not being played");
        player.game = null;
        return true;
    }

    public List<Player> getPlayersFromUUID(List<UUID> playerUUIDS) {
        return playerUUIDS.stream().map(uuid -> players.get(uuid)).collect(Collectors.toList());
    }
}
class NotificationService{
    void notifi(){

    }
}
class GameService{
    HashMap<UUID,Game> activeGames;
    ScoreBoard scoreBoard;
    MatchService matchService;
    PlayerService playerService;
    NotificationService notify;

    void startGame(Player player1){
        Player player2 = matchService.findMatch(player1);
        Game game = new Game(player1,player2);
        playerService.registerNewGame(player1,player2,game);
        activeGames.put(game.gameId,game);
    }
    void terminate(Game game){
        Player player1 = game.player1;
        Player player2 = game.player2;;
        playerService.terminateGame(player1);
        playerService.terminateGame(player2);
        activeGames.remove(game.gameId);
    }
    void play(Player player, int[] move){
        Game game = player.game;
        if(!activeGames.containsKey(game.gameId)) throw new RuntimeException("Incorrect game");
        game.makeMove(player,move);
        if(game.checkWinningCondition()){
            notify.notifi();
            scoreBoard.addWinner(player);
        }
    }

    List<Player> getTopPlayers(){
        List<UUID> playerUUIDS = scoreBoard.getTopKPlayers(10);
        return playerService.getPlayersFromUUID(playerUUIDS);
    }

}
class ScoreBoard{
    PriorityQueue<Player> topPlayers;
    CountryName country;
    HashMap<UUID, Integer>  scores;

    ScoreBoard(CountryName country){
        scores = new HashMap<>();
        this.country = country;
    }

    void addWinner(Player player){
        scores.put(player.playerId, scores.getOrDefault(player.playerId,0)+1);
    }
    int partition(List<UUID> players, int st, int end){
        UUID last = players.get(end);
        int i=st-1, j=st;
        while(j<=end){
            UUID player = players.get(j);
            if(scores.get(player)> scores.get(last)){
                UUID sub = players.get(st+1);
                players.set(j,sub);
                players.set(st+1,player);
                st++;
            }
            j++;
        }
        UUID sub = players.get(st+1);
        players.set(st+1, players.get(end));
        players.set(end, sub);
       return st+1;
    }
    void QuickSort(List<UUID> players, int st, int end, int k){
        //int st=0, end = players.size()-1;
        if(st>end) return;
        int pos = this.partition(players,st,end);
        if(pos==k)  return;
        if(pos<k){
            st = pos+1;
        }
        else end = pos-1;
        QuickSort(players,st,end,k);
    }
    List<UUID> getTopKPlayers(int k){
       //use quickselect salgo to get top 10 players but it will come in unsorted order
        //to return sorted result, get the first top 10 then sort them and return
        List<UUID> ans = new ArrayList<>();
        List<UUID> players = new ArrayList<>();
        players.addAll(scores.keySet());
        QuickSort(players,0, players.size()-1, k);
        for(int i=0;i<k;i++){
            ans.add(players.get(i));
        }
        return ans;
    }

}

//class USAScoreBoard extends ScoreBoard{
//    USAScoreBoard(){
//        super(CountryName.USA);
//    }
//    //can add more region specific function to have different logic for
//}
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


class PersonInfo{
    Name name;
    Location loc;
}

abstract class Location{
    void add(Location loc){
        throw new UnsupportedOperationException();
    }
    void remove(Location loc){
        throw new UnsupportedOperationException();
    }
    String getDescription(){
        throw new UnsupportedOperationException();
    }
    Optional<Player> match(Player player) {
        throw new UnsupportedOperationException();
    }
    void unmatch(Player player){
        throw new UnsupportedOperationException();
    }
    Location getParent(){
        throw new UnsupportedOperationException();
    }
    void setParent(Location location){
        throw new UnsupportedOperationException();
    }
    void startedPlaying(){
        throw new UnsupportedOperationException();
    }
}
/*
    this makes sense as not many players are prone to change the location a lot
    loading from db wpuld not be a big issue in composite struture.

*/

class smallestRegion extends Location{
    String description;
    ArrayList<Player> players;
    Location parent;
    void addPlayer(Player player){
        players.add(player);
        player.personInfo.loc=this;
    }
    Location getParent(){
        return this.parent;
    }
    void setParent(Location location){
        this.parent = location;
    }
    void removePlayer(Player player){
        players.remove(player);
        player.personInfo.loc=null;
    }
    Optional<Player> match(Player player){
        Optional<Player> matched;
        matched = players.stream().filter(player1 -> player1.game==null).findFirst();
        return matched;
    }
    void unmatch(Player player){
        players.add(player);
    }
    String getDescription(){
        return this.description;
    }
}

class Region extends Location{
    ArrayList<Location> nestedRegions;
    String description;
    Location parent;
    Location getParent(){
        return this.parent;
    }
    void setParent(Location location){
        this.parent = location;
    }

    void add(Location loc){
        nestedRegions.add(loc);
    }
    void remove(Location loc){
        nestedRegions.remove(loc);
    }
    String getDescription(){
        return this.description;
    }
    Optional<Player> match(Player player){
        Optional<Player> matched=null;
        for(Location location: this.nestedRegions)
            matched = location.match(player);
        return matched;
    }
}

class Name{
    String firstName;
    String lastName;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
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