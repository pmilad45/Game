package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import game.engine.monsters.*;
import game.engine.Constants;
=======
import game.engine.monsters.Monster;
>>>>>>> Stashed changes
=======
import game.engine.monsters.Monster;
>>>>>>> Stashed changes
=======
import game.engine.monsters.Monster;
>>>>>>> Stashed changes

public class ContaminationSock extends TransportCell implements CanisterModifier{
    
    public ContaminationSock(String name, int effect){
        super(name, effect);
    }

<<<<<<< Updated upstream
    @Override
    public void modifyEnergy(Monster monster){
        monster.setEnergy(monster.getEnergy()-Constants.SLIP_PENALTY);
    }
=======
	public ContaminationSock(String name, int effect) {
		super(name, effect);
	}


	@Override
	public void transport(Monster monster) {
		super.transport(monster);
		modifyCanisterEnergy(monster, -Constants.SLIP_PENALTY);
	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.alterEnergy(canisterValue);
	}
	
>>>>>>> Stashed changes

}
