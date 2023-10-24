/**
 * @project
 **/

import java.util.*;

/*


Objects:
building, elevator, panel, button, floor

[done]R1: There exist multiple elevator cars and floors in the building.

[done]R2: The building can have a maximum of 15 floors and three elevators.

[done]R3: The elevator car can move up or down or be in an idle state.
[done]R4: The elevator door can only be opened when it is in an idle state.

R5: Every elevator car passes through each floor.

R6: The panel outside the elevator should have buttons to call an elevator car and to specify whether the passenger wants
to go up or down.

R7: The panel inside the elevator should have buttons to go to every floor. There should be buttons to open or close the
lift doors.
R8: There should be a display inside and outside the elevator car to show the current floor number and direction of the
elevator car.

R9: The display inside the elevator should also show the capacity of the elevator car.

R10: Each floor has a separate panel and a display for each elevator car.
R11: Multiple passengers can go to the same or different floors in the same or opposite direction.

R12: The elevator system should be able to control the elevator car movement and the door functioning and monitor the
elevator car.

R13: The elevator control system should be able to send the most appropriate elevator to the passenger when the passenger calls the elevator car.

R14: The elevator car can carry a maximum of eight persons or 680 kilograms at once.
 */

abstract class Button{
    int buttonId;
    ButtonPressed status;
    public abstract boolean keyPressed();

    public void pressButton() {
        this.status = ButtonPressed.PRESSED;
    }
    public void unpressButton() {
        this.status = ButtonPressed.UNPRESSED;
    }
}
enum Direction{
    UP,
    DOWN,

}
enum ButtonPressed{
    PRESSED,
    UNPRESSED
}
enum MISButton{
    FIRE,
    STOP,
}
enum DoorStatus{
    OPEN,
    CLOSED
}
class EButton extends Button{

    int floor;
    Elevator elevator;
    public EButton(int floor) {
        this.floor = floor;
    }

    public Elevator getElevator() {
        return elevator;
    }
    public int getFloor() {
        return floor;
    }

    @Override
    public boolean keyPressed() {
        return this.status==ButtonPressed.PRESSED;
    }

}

class FButton extends Button{
    public Direction getDir() {
        return dir;
    }

    Direction dir;
    FButton(Direction dir){
        this.dir = dir;
    }

    @Override
    public boolean keyPressed() {
        return this.status==ButtonPressed.PRESSED;
    }
}

abstract  class Panel{
List<Button> buttons;
int panelId;
}
class EPanel extends Panel{
    Elevator elevator;
    List<Button> floorButtons;
    int floors;
    EPanel(Elevator elevator, int floors){
        this.elevator = elevator;
        this.floors = floors;
        createButtons();
    }

    private void createButtons() {
        floorButtons = new ArrayList<>();
        for(int i=0;i<floors;i++){
            floorButtons.add(new EButton(i));
        }
    }

}

class FPanel extends Panel{
    FButton up;
    FButton down;
    public FPanel(){
        up = new FButton(Direction.UP);
        down = new FButton(Direction.DOWN);
    }
}

class Floor{
    FPanel panel;
    Floor prevFloor;
    Floor nextFloor;
    public Floor(){

    }
    int floor;
    int floorId;
}

enum EleStatus{
    UP,
    DOWN,
    IDLE,
}
interface IStatus{
    void moveUp();
    void moveDown();
    void stop();
    void open();
    void close();
}

class idleState implements IStatus{
    Elevator elevator;
    public idleState(Elevator elevator){
        this.elevator = elevator;

    }
    @Override
    public void moveUp() {
        elevator.setstate(elevator.getMoveupStatus());
    }

    @Override
    public void moveDown() {
        elevator.setstate(elevator.getMovedownStatus());
    }

    @Override
    public void stop() {

    }

    @Override
    public void open() {
        this.elevator.setDoorStatus(DoorStatus.OPEN);
    }

    @Override
    public void close() {
        this.elevator.setDoorStatus(DoorStatus.CLOSED);
    }
}

class movingUpState implements IStatus{
    Elevator elevator;
    public movingUpState(Elevator elevator){
        this.elevator = elevator;
    }
    @Override
    public void moveUp() {

    }

    @Override
    public void moveDown() {

    }

    @Override
    public void stop() {
        elevator.setstate(elevator.getIdleStatus());
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

}

class movingDownState implements IStatus{
    Elevator elevator;
    public movingDownState(Elevator elevator){
        this.elevator = elevator;
    }
    @Override
    public void moveUp() {

    }
    @Override
    public void moveDown() {

    }

    @Override
    public void stop() {
        elevator.setstate(elevator.getIdleStatus());
    }
    @Override
    public void open() {

    }

    @Override
    public void close() {

    }
}
class Building{
    List<Elevator> elevators;
    List<Floor> floors;
}

interface IElevator{
    public void moveUp();
    public void moveDown();
    public void open();
    public void close();
}
class Elevator implements IElevator{
    Panel panel;
    int elevatorId;
    int currFloor;
    DoorStatus doorStatus;
    IStatus idleStatus;
    IStatus moveupStatus;
    IStatus movedownStatus;
    int people;
    int weight;
    IStatus currStatus;

    Elevator( Panel panel){
        idleStatus = new idleState(this);
        moveupStatus = new movingUpState(this);
        movedownStatus = new movingDownState(this);
        currStatus = this.idleStatus;
       // currFloor = floor;
        this.panel = panel;
    }

    public DoorStatus getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(DoorStatus doorStatus) {
        this.doorStatus = doorStatus;
    }
    public IStatus getIdleStatus() {
        return idleStatus;
    }

    public IStatus getMoveupStatus() {
        return moveupStatus;
    }

    public IStatus getMovedownStatus() {
        return movedownStatus;
    }

    public void setstate(IStatus status){
        this.currStatus = status;
    }

    public void moveUp(){
        currStatus.moveUp();
        currFloor++;
    }
    public void moveDown(){
        currStatus.moveDown();
        currFloor--;
        //currStatus.
    }
    public void open(){
        currStatus.open();
    }
    public void close(){
        currStatus.close();
    }
    public void move(int floor){
        if(currFloor ==floor) return;
        if(currFloor<floor)
            moveUp();
        else moveDown();
    }
}

interface AdditionStartegy{
    public void addFloorReq(int floor, List<Floor> floors);
}

class DefaultStrategy implements AdditionStartegy{
    public void addFloorReq(int floor,List<Floor> floors){
        //add in insetion sort order
    }
}
interface ElevatorAssignementStartegy{
    Elevator findElevator(List<Elevator> elevators, Direction direction);
}

class nearestElevatorAssignment implements ElevatorAssignementStartegy{

    @Override
    public Elevator findElevator(List<Elevator> elevators,Direction direction) {
       return elevators.get(0);
    }
}

class ElevatorMgnmt{

    HashMap<Elevator, List<Floor>> rides;
    List<Elevator> elevators;
    AdditionStartegy strategy;
    ElevatorAssignementStartegy assignementStartegy;
    public ElevatorMgnmt(List<Elevator> elevators){
        for(Elevator elevator: elevators) {
            rides.put(elevator, new ArrayList<>());
        }
        this.elevators = elevators;
    }


    void elevatorButtonPressed(Button button){
        EButton ebutton = (EButton) button;
        if(ebutton.keyPressed()) return;
        int floor = ebutton.floor;
        Elevator elevator = ebutton.getElevator();
        ebutton.pressButton();
        strategy.addFloorReq(floor, rides.get(elevator));
        ebutton.unpressButton();
    }

    void hallButtonPressed(Button button){
        FButton fButton = (FButton) button;
        Direction dir = fButton.getDir();
        Elevator elevator = assignementStartegy.findElevator(elevators, dir);
    }
}
interface Command{
    public void execute();
}
class EButtonPressedCommand implements Command{
    ElevatorMgnmt elevatorMgnmt;
    @Override
    public void execute() {

    }
}