//// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
//// then press Enter. You can now see whitespace characters in your code.
//
//
//
//
//
//import java.util.*;
//
//interface IMathchService{
//    Player match(Player player);
//    void unmatch(Player player);
//}
//
//class GameService{
//
//    ScoreBoard scoreBoard;
//    List<Game> games;
//}
//
//class ScoreBoard{
//    PriorityQueue<Player> topPlayers;
//    CountryName country;
//    HashMap<UUID, Integer>  scores;
//
//    ScoreBoard(CountryName country){
//        scores = new HashMap<>();
//        this.country = country;
//    }
//
//    void addWinner(Player player){
//        scores.put(player.playerId, scores.getOrDefault(player.playerId,0)+1);
//    }
//    int partition(List<UUID> players, int st, int end){
//        UUID last = players.get(end);
//        int i=st-1, j=st;
//        while(j<=end){
//            UUID player = players.get(j);
//            if(scores.get(player)> scores.get(last)){
//                UUID sub = players.get(st+1);
//                players.set(j,sub);
//                players.set(st+1,player);
//                st++;
//            }
//            j++;
//        }
//        UUID sub = players.get(st+1);
//        players.set(st+1, players.get(end));
//        players.set(end, sub);
//       return st+1;
//    }
//    void QuickSort(List<UUID> players, int st, int end, int k){
//        //int st=0, end = players.size()-1;
//        if(st>end) return;
//        int pos = this.partition(players,st,end);
//        if(pos==k)  return;
//        if(pos<k){
//            st = pos+1;
//        }
//        else end = pos-1;
//        QuickSort(players,st,end,k);
//    }
//    List<UUID> getTopKPlayers(int k){
//       //use quickselect salgo to get top 10 players but it will come in unsorted order
//        //to return sorted result, get the first top 10 then sort them and return
//        List<UUID> ans = new ArrayList<>();
//        List<UUID> players = new ArrayList<>();
//        players.addAll(scores.keySet());
//        QuickSort(players,0, players.size()-1, k);
//        for(int i=0;i<k;i++){
//            ans.add(players.get(i));
//        }
//        return ans;
//    }
//
//}
//
////class USAScoreBoard extends ScoreBoard{
////    USAScoreBoard(){
////        super(CountryName.USA);
////    }
////    //can add more region specific function to have different logic for
////}
//class Game{
//    Player player1;
//    Player player2;
//    UUID gameId;
//    boolean isTerminated;
//    int currPlayer;
//    Board board;
//    int[] LastMove;
//    int p1Id;
//    int p2id;
//    Game(Player p1, Player p2){
//        this.player1 = p1;
//        this.player2 = p2;
//        board = new Board();
//        isTerminated=false;
//        gameId = new UUID(100,100);
//        LastMove = new int[2];
//        p1Id=-1;
//        p2id=1;
//        this.currPlayer = p1Id;
//    }
//
//    int getId(Player player){
//        if(player.equals(this.player1)) return  p1Id;
//        if(player.equals(this.player2)) return  p2id;
//        return 0;
//    }
//    boolean makeMove(Player player, int[] move){
//        //if(player!=currPlayer) return false;//only alternate moves are allowed
//        int id = getId(player);
//        if(id==0) return false;
//        if(board.validMove(LastMove,move,id)){
//          if(board.makeMove(LastMove,move,id)) {
//              LastMove = move;
//              currPlayer = id*-1;
//              return true;
//          }
//        }
//        return false;
//    }
//    boolean checkWinningCondition(){
//        return board.checkWinningCondition();
//    }
//    Player getWinner(){
//        if(checkWinningCondition()){
//            int playerId = board.getWinner();
//            if(playerId==getId(this.player1)) return this.player1;
//            if(playerId==getId(this.player2)) return this.player2;
//        }
//        return null;
//    }
//}
//class Board{
//    int[][] board;
//
//     boolean validMove(int[] prev, int[] move, int player){
//        int x = prev[0];
//        int y = prev[1];
//        int xNew = move[0], yNew = move[1];
//        if(board[x][y]!=player) {
//            return false;
//        }
//        if(xNew>=0 && yNew>=0 && xNew< board.length && yNew< board.length && board[xNew][yNew]!=0) {
//            return false;
//        }
//        return true;
//    }
//    void resetBoard(){
//         for(int i=0;i< board.length;i++)
//             for(int j=0;i< board[0].length;j++){
//                 board[i][j]=0;
//             }
//    }
//    boolean makeMove(int[] prev, int[] move, int player){
//        if(!validMove(prev,move,player)) {
//            return false;
//        }
//        int xNew = move[0], yNew = move[1];
//        board[xNew][yNew]=player;
//        return true;
//    }
//    boolean checkWinningCondition(){
//         return true;
//    }
//
//    public int getWinner() {
//
//         return 1;
//    }
//}
//class Player{
//    UUID playerId;
//    PersonInfo personInfo;
//    Game game;
//
//    public UUID getPlayerId() {
//        return playerId;
//    }
//
//    public void setPlayerId(UUID playerId) {
//        this.playerId = playerId;
//    }
//
//    public Game getGame() {
//        return game;
//    }
//
//    public void setGame(Game game) {
//        this.game = game;
//    }
//
//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }
//
//    boolean isActive;
//}
//
//class PersonInfo{
//    Name name;
//    Country country;
//    City city;
//    int zipcode;
//}
//class Name{
//    String firstName;
//    String lastName;
//
//    public Name(String firstName, String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//}
//class Country{
//    CountryName name;
//    List<City> cities;
//    List<Integer> zipcodes;
//}
//enum CountryName{
//    USA,
//    INDIA,
//}
//enum City{
//    MUMBAI,
//    NYC,
//}
//
//public class Main {
//    public static void main(String[] args) {
//        // Press Alt+Enter with your caret at the highlighted text to see how
//        // IntelliJ IDEA suggests fixing it.
//        System.out.printf("Tic tac toe intial commit!");
//
//    }
//}