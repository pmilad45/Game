package game.engine.cells;

//all comments are written for clarification and organization


//imports 
import game.engine.monsters.Monster;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;

//class and constructor
public class DoorCell extends Cell implements CanisterModifier {
    private final Role role;
    private final int energy;    
    private boolean activated;
    
    public DoorCell(String name, Role role, int energy){
        super(name);
        this.role = role;
        this.energy = energy;
        this.activated = false;
    }


    //getter/setter methods
    public Role getRole(){
        return role;
    }

    public int getEnergy(){
        return energy;
    }

    public boolean isActivated(){
        return activated;
    }

    public void setActivated(boolean activated){

    }


    //must overridde canister modifier:
    @Override
    public void modifyEnergy (Monster monster){
        if(activated != true){
            if(this.getRole() == role){
                monster.setEnergy(getEnergy()+energy); 
            }
            else{
                monster.setEnergy(getEnergy()-energy);
            }

        }
        activated = true;
    }
    

}
