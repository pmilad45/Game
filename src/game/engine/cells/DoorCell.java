package game.engine.cells;

<<<<<<< Updated upstream
//all comments are written for clarification and organization


//imports 
import game.engine.monsters.Monster;
=======
import game.engine.Board;
>>>>>>> Stashed changes
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;
import java.util.ArrayList;

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
        this.activated = activated;
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
    

	@Override

		public void modifyCanisterEnergy(Monster monster, int canisterValue) {
			if (monster.getRole() == this.role) {
				monster.alterEnergy(canisterValue);
			} else {
				monster.alterEnergy(-canisterValue);
			}
		}

	@Override

		public void onLand(Monster landingMonster, Monster opponentMonster) {
			super.onLand(landingMonster, opponentMonster);
			if (activated) {
				return;
			}
			boolean anyEnergyChanged = false;
			if (landingMonster.getRole() == role) {
				int beforeL = landingMonster.getEnergy();
				modifyCanisterEnergy(landingMonster, energy);
				anyEnergyChanged = landingMonster.getEnergy() != beforeL;
				ArrayList<Monster> st = Board.getStationedMonsters();
				if (st != null) {
					for (Monster m : st) {
						if (m.getRole() == landingMonster.getRole()) {
							int b = m.getEnergy();
							modifyCanisterEnergy(m, energy);
							if (m.getEnergy() != b) {
								anyEnergyChanged = true;
							}
						}
					}
				}
		} else {
			if (landingMonster.isShielded()) {
				int b0 = landingMonster.getEnergy();
				modifyCanisterEnergy(landingMonster, energy);
				anyEnergyChanged = landingMonster.getEnergy() != b0;
			} else {
				int beforeL = landingMonster.getEnergy();
				modifyCanisterEnergy(landingMonster, energy);
				if (landingMonster.getEnergy() != beforeL) {
					anyEnergyChanged = true;
				}
				ArrayList<Monster> st2 = Board.getStationedMonsters();
				if (st2 != null) {
					for (Monster m : st2) {
						if (m.getRole() == landingMonster.getRole()) {
							int b = m.getEnergy();
							modifyCanisterEnergy(m, energy);
							if (m.getEnergy() != b) {
								anyEnergyChanged = true;
							}
						}
					}
				}
			}
		}
		activated = anyEnergyChanged;
	}

}
