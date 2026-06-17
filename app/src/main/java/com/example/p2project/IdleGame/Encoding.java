package com.example.p2project.IdleGame;
import static android.content.Context.MODE_PRIVATE;

import android.os.Build;
import android.util.Log;

import com.example.p2project.HasTreats;
import com.example.p2project.InventoryScreen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
public class Encoding
{
    HasTreats main;
    public Encoding(HasTreats main)
    {
        this.main = main;
    }
    String encodeAnimalList(ArrayList<Animal> curList)
    {
        StringBuilder animals = new StringBuilder();
        for (Animal curAnimal : curList)
        {
            animals.append(encodeAnimal(curAnimal)).append("/");
        }
        return animals.toString();
    }
    String encodeAnimal(Animal curAnimal)
    {
        try {return curAnimal.name + ":" + curAnimal.production + ":" + curAnimal.passive + ":" + curAnimal.level + ":" + Arrays.toString(curAnimal.levels).replace("[", "").replace("]", "#").replace(" ", "");} catch (NullPointerException e) {return "null";}
    }

    ArrayList<Animal> decodeAnimalList(String curString)
    {
        ArrayList<Animal> animals = new ArrayList<Animal>();
        for (String curAnimal : curString.split("/"))
        {
            try{animals.add(deCodeAnimal(curAnimal));} catch (NullPointerException e) {}
        }
        return animals;
    }
    Animal deCodeAnimal(String curString)
    {
        Log.d("f", curString);
        String[] curAnimal = curString.split(":");
        Log.d("f", Arrays.toString(curAnimal));
        Integer[] levels = new Integer[]{};
        if (curAnimal[4].split(",").length > 0) levels = new Integer[curAnimal[4].split(",").length];
        for (int i = 0; i < curAnimal[4].split(",").length; i++)
        {
            try {levels[i] = Integer.parseInt(curAnimal[4].split(",")[i].replace("#",""));} catch (NumberFormatException e) {levels = new Integer[]{};}
        }

        try {return new Animal(curAnimal[0], Double.parseDouble(curAnimal[1]), Double.parseDouble(curAnimal[2]), Integer.parseInt(curAnimal[3]),levels);} catch (ArrayIndexOutOfBoundsException e){}
        return null;
    }
    public void saveInventory(ArrayList<Animal> curList) throws IOException
    {
        byte[] curBytes = encodeAnimalList(curList).getBytes();
        FileOutputStream out = main.getApplicationContext().openFileOutput("inventory.txt", MODE_PRIVATE);
        out.write(curBytes);
        Log.d("treats", "You have " + CurrencyTracker.treats + " treats");
        main.getSharedPreferences("data",MODE_PRIVATE).edit().putInt("treats", CurrencyTracker.treats.intValue()).apply();
        Log.d("treats", "It saved " + main.getSharedPreferences("data",MODE_PRIVATE).getInt("treats", 0) + " treats");
        saveActive();
    }
    public void loadInventory() throws IOException
    {
        FileInputStream in = main.getApplicationContext().openFileInput("inventory.txt");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            Inventory.inventory = decodeAnimalList(new String(in.readAllBytes()));
            Log.d("treats", "It loaded " + main.getSharedPreferences("data",MODE_PRIVATE).getInt("treats", 0) + " treats");
            CurrencyTracker.treats = (double) main.getSharedPreferences("data",MODE_PRIVATE).getInt("treats", 0);
            loadActive();
        }
    }
    void saveActive() throws IOException
    {
        ArrayList<Animal> curList = new ArrayList<Animal>();
        Collections.addAll(curList, Inventory.activeAnimals);
        FileOutputStream out = main.getApplicationContext().openFileOutput("active.txt", MODE_PRIVATE);
        out.write(encodeAnimalList(curList).getBytes());
    }
    void loadActive() throws IOException
    {
        FileInputStream in = main.getApplicationContext().openFileInput("active.txt");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            String[] animals = new String(in.readAllBytes()).split("/");
            for (int i = 0; i < Inventory.activeAnimals.length; i++)
            {
                Inventory.activeAnimals[i] = deCodeAnimal(animals[i]);
            }
        }
    }
}
