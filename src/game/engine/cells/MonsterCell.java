package game.engine.cells;

import game.engine.monsters.Monster;

public class MonsterCell extends Cell{
    private final Monster cellMonster;

    public MonsterCell(String name, Monster cellMonster){
        super(name);
        this.cellMonster = cellMonster;
    }


    //getters and setters

    public Monster getMonster(){
        return cellMonster;
    }
}