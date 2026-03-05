package game.engine.cells;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.*;

public class ContaminationSock extends TransportCell implements CanisterModifier{
    public ContaminationSock(String name, int effect){
        super(name, effect);
    }

    @Override
    public void modifyEnergy()

}
