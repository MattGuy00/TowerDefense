package WizardTD.Game.Entities.Monsters;

import java.util.LinkedList;
import java.util.Random;

import WizardTD.App;
import processing.core.PGraphics;

public final class Moag extends Gremlin {
    private final int numberOfMonstersToSpawn;

    int initialHP;
    float speed;
    float armour; 
    int manaOnDeath;
    
    public Moag(int numberOfMonsters, int initialHP, float speed, float armour, int manaOnDeath) {
        super(initialHP, speed, armour, manaOnDeath);

        numberOfMonstersToSpawn = numberOfMonsters;
        this.initialHP = initialHP;
        this.speed = speed;
        this.armour = armour;
        this.manaOnDeath = manaOnDeath;
    }

    public LinkedList<Monster> getMonstersInMoag()  {
        Random rnd = new Random();

        LinkedList<Monster> monstersToSpawnOnDeath = new LinkedList<>();
        for (int i = 0; i < numberOfMonstersToSpawn; i++) {
            Gremlin g = new Gremlin(initialHP, speed, armour, manaOnDeath);
            g.setPath(this.getPath());
            g.setStartingTile(this.getStartingTile());
            g.setMoveIter(this.getMoveIter());

            g.setCenterPos(this.getCenterPos().x + rnd.nextInt(App.CELLSIZE), this.getCenterPos().y + rnd.nextInt(App.CELLSIZE));
            monstersToSpawnOnDeath.add(g);
        }

        return monstersToSpawnOnDeath;
    }

    @Override
    public void drawToLayer(PGraphics layer) {
        layer.tint(200, 0, 100);
        layer.image(this.getCurrentSprite(), this.getPos().x, this.getPos().y);
        layer.tint(255);
    }
}
