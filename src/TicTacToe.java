import java.util.*;

/**
 * @project
 *
 * Tic tac toe reqs:
 * 1. 2 players can start the game
 * 2. Player can only make alternate moves
 * 3. Maintain instances for each active game in the env
 * 8. Allows only valid moves
 * 9. maintain scores for player
 * 10. maintain number of games played by each player
 *
 *
 * Functionalities:
 *  move- validCheck, start, winningconditioncheck, endCondition
 *
 **/


/*
    intialize board/games in interface this givs freedom to intialize boards in diferent ways
        - can be extended to new features like level additions/different types of boards/diffent intialolizations
    Devesh is great!!
 */
interface IIntitializeGameService{
    Game intialize(List<Player> players);
}
class TicTacToeIntitializeGameService implements IIntitializeGameService{
    public Game intialize(List<Player> players){
        return new TicTacToeGame(3);
    }
}
interface IGameService{
    Game startGame(List<Player> players, List<String> args);
    void play(Player player, Game game, int[] move, Piece piece);
    //Game startGame(List<Player> players);
    boolean endCondition(Game game);
    Player getWinner(Game game);
}
class TicTacToeGameService implements IGameService{

    @Override
    public Game startGame(List<Player> players, List<String> args) {
        TicTacToeGame game =null;
        game = new TicTacToeGame();
        return game;
    }

    @Override
    public void play(Player player, Game game, int[] move, Piece piece) {
        Player currPlayer = game.getCurrPlayer();
        if(currPlayer==null || !player.equals(currPlayer)){
            if(!game.validMove(move)) throw new RuntimeException("Invalid Move");
            game.makeMove(player,move,piece);
            game.setCurrPlayer(player);
        }
        throw new RuntimeException("Player's chance has not yet arrived");
    }

    @Override
    public boolean endCondition(Game game) {
        return game.endCondition();
    }

    @Override
    public Player getWinner(Game game) {
        return game.getWinner();
    }
}

class GameManager {
    IGameService gameService;
    IIntitializeGameService intitializeGameService;
    HashMap<Player,Integer> scores;
    ArrayList<Player> players;
    ArrayList<Game> games;
    ScoreBoard scoreBoard;
     GameManager(IGameService tictactoeService,IIntitializeGameService intitializeGameService){
        this.gameService =tictactoeService;
        this.intitializeGameService = intitializeGameService;
        this.players = new ArrayList<>();
        games = new ArrayList<>();
        scoreBoard = new ScoreBoard();
    }

    void startGame(List<Player> players){
       Game game = intitializeGameService.intialize(players);
    }

    void play(Player player, int[] move,Game game, Piece piece){
        gameService.play(player,game,move,piece);
        if(gameService.endCondition(game)){
            Player winner = gameService.getWinner(game);
            if(winner!=null){
                scores.put(winner,scores.getOrDefault(winner,0)+1);
                games.remove(game);
            }
        }
    }
}
enum GameType{
    TICTACTOE,
    CHESS,
    LUDO
}
class ScoreBoard{
    HashMap<UUID, Integer>  scores;

    ScoreBoard(){
        scores = new HashMap<>();
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
        Collections.sort(ans,(a,b)->scores.get(b)-scores.get(a));
        return ans;
    }

}
class PersonInfo{
    Name name;
    int age;
    Gender gender;
    Country countryName;
}
enum Gender{
    FEMALE,
    MALE,
    OTHER,
}
enum Country{
    INDIA,
    USA,
}
class Name{
    String firstName;
    String middleName;
    String lastName;
    Name(String firstName, String middleName, String lastName){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
class Player{
    PersonInfo personInfo;
    int score;
    int gamesPlayed;
    UUID playerId;
}

abstract class Board{
    int[][] board;
    Board(int n, int m){
        board = new int[n][m];
    }
    boolean validMove(int[] move){
        return this.board[move[0]][move[1]]==0;
    }

}
class TicTacToeBoard extends Board{

    TicTacToeBoard(int n) {
        super(n,n);
    }
    void makeMove(int[] move, int piece){
        board[move[0]][move[1]] = piece;
    }
    boolean checkEndCondition(){
         throw new UnsupportedOperationException();
    }
    int getWinner(){
        throw new UnsupportedOperationException();
    }
}

abstract class Piece{
    String name;
}
/*
    I think Game should be abstract class:
    -- as it needs to extend for further games and still keep attributes same but different functionalities
    --!!! pieces

    mistake: try to keep objects as abstract as possible
*/
abstract class Game{
    Board board;
    List<Player> players;
    UUID gameId;
    abstract Player getCurrPlayer();
    abstract void setCurrPlayer(Player player);
    abstract void makeMove(Player player, int[] move, Piece piece);
    public abstract boolean validMove(int[] move);
    public abstract boolean endCondition() ;
    public abstract Player getWinner();
}
class TicTacToeGame extends Game{
    Player currPlayer;
    HashMap<Player,Integer> piecePlayerHashMap;
    public TicTacToeGame(){
        this.board = new TicTacToeBoard(3);
        players = new ArrayList<Player>();
        gameId = new UUID(100,10);
        currPlayer = null;
        piecePlayerHashMap = new HashMap<>();
    }
    public TicTacToeGame(int n){
        this.board = new TicTacToeBoard(n);
        players = new ArrayList<Player>();
        gameId = new UUID(100,10);
        currPlayer = null;
        piecePlayerHashMap = new HashMap<>();
    }
    public Player getCurrPlayer(){
        return this.currPlayer;
    }
    public void setCurrPlayer(Player player){
         this.currPlayer = player;
    }
    void makeMove(Player player, int[] move, Piece piece){
        if(!piecePlayerHashMap.containsKey(player)) throw new RuntimeException("Player not valid");
        int playerPiece = piecePlayerHashMap.get(player);
        TicTacToeBoard tboard =(TicTacToeBoard) this.board;
        tboard.makeMove(move,playerPiece);
    }
    public boolean validMove(int[] move){
        return this.board.validMove(move);
    }
    public boolean endCondition(){
        TicTacToeBoard tboard =(TicTacToeBoard) this.board;
        return tboard.checkEndCondition();
    }

    public Player getWinner(){
        if(!endCondition()) return null;
        TicTacToeBoard board = (TicTacToeBoard) this.board;
        int maxPiece = board.getWinner();
        for(Player player:players){
            if(piecePlayerHashMap.get(player)==maxPiece)
                return player;
        }
        throw new RuntimeException("Incorrect");
    }
}
public class TicTacToe {
    IIntitializeGameService intitilizeTicTokGame = new TicTacToeIntitializeGameService();
    IGameService ticTokGameSerice = new TicTacToeGameService();

    GameManager tickTokApp = new GameManager(ticTokGameSerice, intitilizeTicTokGame );
    /*

    IIntitializeGameService intitilizeChessGame = new ChessInitializeService();
    IGameService chessGameSerice = new ChessGameService();

    GameManager chessApp = new GameManager(chessGameSerice, intitilizeChessGame );
     */
}
