package game.engine.cells;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.*;
import game.engine.Constants;

public class ContaminationSock extends TransportCell implements CanisterModifier{
    
    public ContaminationSock(String name, int effect){
        super(name, effect);
    }

    @Override
    public void modifyEnergy(Monster monster){
        monster.setEnergy(monster.getEnergy()-Constants.SLIP_PENALTY);
    }

}
