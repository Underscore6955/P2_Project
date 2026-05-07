package com.example.p2project.IdleGame;

import java.util.ArrayList;
import java.util.Random;

public class EggRoll {
    ArrayList<Animal> lowTierTable = new ArrayList<Animal>();
    ArrayList<Animal> midTierTable = new ArrayList<Animal>();
    ArrayList<Animal> highTierTable = new ArrayList<Animal>();
    Animal tempAnimal;
    Random Randomise = new Random();
    int randomHolder;


    protected void onCreate(){
        lowTierTable.add(new Animal("Panda",1000f));
        lowTierTable.add(new Animal("dragon",1000f));
        midTierTable.add(new Animal("Panda",1000f));
        midTierTable.add(new Animal("dragon",1000f));
        highTierTable.add(new Animal("Panda",1000f));
        highTierTable.add(new Animal("dragon",1000f));
    }

    public Animal RollLowTier(){


        randomHolder = Randomise.nextInt();
        tempAnimal = lowTierTable.get(randomHolder);
        lowTierTable.remove(randomHolder);
        return tempAnimal;
    }
    public Animal RollMidTier(){
        randomHolder = Randomise.nextInt();
        tempAnimal = midTierTable.get(randomHolder);
        midTierTable.remove(randomHolder);
        return tempAnimal;
    }
    public Animal RollHighTier(){
        randomHolder = Randomise.nextInt();
        tempAnimal = highTierTable.get(randomHolder);
        highTierTable.remove(randomHolder);
        return tempAnimal;
    }
}
