package com.example.p2project.IdleGame;

public class Animal
{
    String name;
    Double production;
    Double passive;
    Integer level = 1;
    Double xp = 0D;
    public Animal (String name, Double production, Double passive)
    {
        this.name = name;
        this.production = production;
        this.passive = passive;
    }
    public void giveXp(Double xp)
    {
        this.xp += xp;
        while (this.xp >= 100D)
        {
            this.xp -= 100D;
            this.level += 1;
            this.production *= 1.1;
            this.passive *= 1.1;
        }
    }
}
