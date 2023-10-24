/**
 * @project
 **/


/*
Requirements:

R1: Our board will be in the shape of a rectangle.

R2: All pieces will have four sides that can either have an indentation, an extrusion, or a flat edge.

R3: There are four corner pieces, some edge pieces, and the remaining ones are the middle pieces.
A corner piece has two flat sides, an edge piece only has one flat side, and a middle piece doesn’t have any flat edge.
R4: All pieces will be unique, so only one piece will fit with one other piece.

R5: Two pieces fit together by the curvature of the indentation on one piece matching up to the curvature of the extrusion on another.


Objects:
1. piece
2. edges can have  indentation, extrusion, flat sides,
    each indentation can have only fit 1 type extrusion by matching up their curvatures,
3. board  will have list of pieces
4.
 */
import javafx.scene.image.Image;

import java.util.*;
enum Curvature{
    STRAIGHT,
    TILTED,
    CURVED
}
interface Protusion{
   public Curvature getCurvature();
}
class Indentation implements Protusion{
    Curvature curvature;
    public Indentation(Curvature curvature){
        this.curvature = curvature;
    }
    public Curvature getCurvature(){
        return this.curvature;
    }
}

class Extrusion implements Protusion{
    Curvature curvature;
    public Extrusion(Curvature curvature){
        this.curvature = curvature;
    }
    public Curvature getCurvature(){
        return this.curvature;
    }
}
class Edge{
    List<Protusion> protusions;

    public Edge(List<Protusion> protusions){
        this.protusions = protusions;
    }
    public List<Protusion> getProtusions() {
        return protusions;
    }

    boolean matchProtusions(Edge edge){
        int index=0;
        for(Protusion p: edge.protusions){
            if(p.getCurvature()!=this.protusions.get(index++).getCurvature()) return false;
        }
        return true;
    }
}
enum Dir{
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

abstract class Orientation{
    Dir dir;
    int[] method;
    public Orientation(int[] method){
        this.method = method;
    }
    public int[] getOrientation(){
        return method;
    }
    public abstract Dir getMatchingDir();
}
class UpOrientation extends Orientation{

    public UpOrientation(int[] method) {
        super(method);
        dir = Dir.UP;
    }

    @Override
    public Dir getMatchingDir() {
        return Dir.DOWN;
    }
}
class DownOrientation extends Orientation{

    public DownOrientation(int[] method) {
        super(method);
        dir = Dir.DOWN;
    }
    @Override
    public Dir getMatchingDir() {
        return Dir.DOWN;
    }
}
class LeftOrientation extends Orientation{

    public LeftOrientation(int[] method) {
        super(method);
        dir =Dir.LEFT;
    }

    @Override
    public Dir getMatchingDir() {
        return Dir.RIGHT;
    }
}
class RightOrientation extends Orientation{

    public RightOrientation(int[] method) {
        super(method);
        dir = Dir.RIGHT;
    }
    @Override
    public Dir getMatchingDir() {
        return Dir.LEFT;
    }
}
abstract class JPiece {
    List<Edge> edges;
    List<Orientation> orientations;
    public abstract void Rotate();
    public abstract Edge edgeForGivenorientation(Dir dir);

}

class DefaultJPiece extends JPiece{

    public DefaultJPiece(List<Edge> edges, List<Orientation> orientations){
        this.edges = edges;
        this.orientations = orientations;
    }
    public  void Rotate(){
        //perform cyclic shift of orientations
    }
    public Edge edgeForGivenorientation(Dir dir){
        for(int i=0;i< orientations.size();i++) {
            if (orientations.get(i).dir==dir) return edges.get(i);
        }
        return null;
    }
}
interface Solver{
   public boolean canAddPieceAtPosition(JBoard defaultJBoard, JPiece piece, int[] position);
}
class DefaultSolver implements Solver{
    public boolean canAddPieceAtPosition(JBoard jboard,JPiece piece, int[] position) {
        JPiece[][] board = jboard.getBoard();
        for(Orientation orientation: piece.orientations){
            int[] methos = orientation.getOrientation();
            int x = position[0]+methos[0];
            int y = position[1]+methos[1];
            if(x<0 ||y<0||x>=board.length|| y>=board[0].length ) continue;
            JPiece neighbour = board[x][y];
            if(neighbour!=null ){
                Edge edge = neighbour.edgeForGivenorientation(orientation.getMatchingDir());
                if(!edge.matchProtusions(piece.edgeForGivenorientation(orientation.dir))) return false;
            }
        }
        return true;
    }
}
abstract class JBoard{
    public JPiece[][] getBoard() {
        return board;
    }

    public JPiece[][] board;
    Solver solver;
    public abstract boolean addPiece(JPiece piece, int[] position);
    public abstract boolean canAddPieceAtPosition(JPiece piece,int[] position);
    public abstract JPiece removePiece(int[] position);
    public abstract boolean canRemovePiece(int[] position);

}
class DefaultJBoard extends JBoard {


    public DefaultJBoard(int m, int n){
        this.board = new JPiece[m][n];
    }
    @Override
    public boolean addPiece(JPiece piece, int[] position) {
        if(canAddPieceAtPosition(piece,position)) {
            board[position[0]][position[1]] = piece;
            return true;
        }
        return false;
    }

    @Override
    public boolean canAddPieceAtPosition(JPiece piece, int[] position) {
        solver.canAddPieceAtPosition(this, piece, position);
    }

    @Override
    public JPiece removePiece(int[] position) {
        JPiece piece = board[position[0]][position[1]];
        board[position[0]][position[1]]=null;
        return piece;
    }

    @Override
    public boolean canRemovePiece(int[] position) {
        return board[position[0]][position[1]]!=null;
    }
}



class JigsawGame{
    List<JPiece> pieces;
    List<JPiece> placedPieced;
    JBoard board;
    List<Player> players;
    Image image;

    boolean addPiece(JPiece piece, int[] position){
        if(board.addPiece(piece,position)){
            placedPieced.add(piece);
            pieces.remove(piece);
            return true;
        }
        return false;
    }
    void rotate(JPiece piece){
        piece.Rotate();
    }

    boolean isComplete(){
        return pieces.size()==0;
    }

    boolean removePiece(int[] position){
        if(board.canRemovePiece(position)){
            JPiece piece = board.removePiece(position);
            pieces.add(piece);
            placedPieced.remove(piece);
            return true;
        }
        return false;
    }

}