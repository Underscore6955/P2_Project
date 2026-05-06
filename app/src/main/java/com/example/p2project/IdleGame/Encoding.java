package com.example.p2project.IdleGame;
import static android.content.Context.MODE_PRIVATE;

import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p2project.MainActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
public class Encoding
{
    MainActivity main;
    public Encoding(MainActivity main)
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
        return deEncodeString(animals.toString(), 1);
    }
    String encodeAnimal(Animal curAnimal)
    {
        try {return curAnimal.name + ":" + curAnimal.production + ":" + curAnimal.passive;} catch (NullPointerException e) {return "null";}
    }
    String deEncodeString(String curString, int deOrEncode)
    {
        StringBuilder encodedString = new StringBuilder();
        for (char curChar : curString.toCharArray())
        {
            if(curChar != ':' && curChar != '/')
            {
                encodedString.append((char) (curChar + 5 * deOrEncode));
            }
            else
            {
                encodedString.append(curChar);
            }
        }
        return encodedString.toString();
    }
    ArrayList<Animal> decodeAnimalList(String curString)
    {
        ArrayList<Animal> animals = new ArrayList<Animal>();
        for (String curAnimal : deEncodeString(curString, -1).split("/"))
        {
            Log.d("decoding", "just decoded " + curAnimal);
            try{animals.add(deCodeAnimal(curAnimal));} catch (NullPointerException e) {}
        }
        for (Animal animal : animals){try{Log.d("added", "added "+ animal.name + " to list" );} catch (NullPointerException e) {Log.d("null", "added null to list");}}
        return animals;
    }
    Animal deCodeAnimal(String curString)
    {
        String[] curAnimal = curString.split(":");
        try {return new Animal(curAnimal[0], Double.parseDouble(curAnimal[1]), Double.parseDouble(curAnimal[2]));} catch (ArrayIndexOutOfBoundsException e){}
        Log.d("failed", "failed to decode " + curString + " into animal");
        return null;
    }
    public void saveInventory(ArrayList<Animal> curList) throws IOException
    {
        byte[] curBytes = encodeAnimalList(curList).getBytes();
        Log.d("save", "saved " + encodeAnimalList(curList));
        FileOutputStream out = main.getApplicationContext().openFileOutput("inventory.txt", MODE_PRIVATE);
        out.write(curBytes);
        saveActive();
    }
    public void loadInventory() throws IOException
    {
        FileInputStream in = main.getApplicationContext().openFileInput("inventory.txt");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            Log.d("loading", "decoding...");
            Inventory.inventory = decodeAnimalList(new String(in.readAllBytes()));
            loadActive();
            Inventory.UpdateInventory();
        }
    }
    void saveActive() throws IOException
    {
        ArrayList<Animal> curList = new ArrayList<Animal>();
        for (Inventory.ActiveSlot curSlot : Inventory.activeSlots) {curList.add(curSlot.content);}
        FileOutputStream out = main.getApplicationContext().openFileOutput("active.txt", MODE_PRIVATE);
        out.write(encodeAnimalList(curList).getBytes());
    }
    void loadActive() throws IOException
    {
        FileInputStream in = main.getApplicationContext().openFileInput("active.txt");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            String[] animals = new String(in.readAllBytes()).split("/");
            for (int i = 0; i < Inventory.activeSlots.length; i++)
            {
                Inventory.activeSlots[i].content = deCodeAnimal(deEncodeString(animals[i],-1));
                Inventory.activeSlots[i].UpdateIcon();
            }
        }
    }
}
