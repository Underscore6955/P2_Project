package com.example.p2project.IdleGame;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Objects;

public class Inventory
{
    static String defaultIcon = "inv_background";
    public static InventorySlot[] inventorySlots = new InventorySlot[12];
    public static ActiveSlot[] activeSlots = new ActiveSlot[5];
    static Integer curSelect;
    public static ArrayList<Animal> inventory = new ArrayList<Animal>();
    static void EquipAnimal(Integer inventorySelec, Integer activeSelec)
    {
        Animal curAnimal = inventory.get(inventorySelec);
        // find selection here
        Animal prevAnimal = activeSlots[activeSelec].content;

        if (prevAnimal == null)
        { inventory.remove((int) inventorySelec); }
        else { inventory.set(inventorySelec, prevAnimal); }
        activeSlots[activeSelec].content = curAnimal;
        activeSlots[activeSelec].UpdateIcon();
        UpdateInventory();
    }
    public static void UpdateInventory()
    {
        for (int i = 0; i < inventorySlots.length; i++)
        {
            try { inventorySlots[i].content = inventory.get(i); }
            catch (Exception e) { inventorySlots[i].content = null; }
            inventorySlots[i].UpdateIcon();
        }
    }
    public static class InventoryButton
    {
        public Animal content;
        Integer id;
        ImageButton button;
        public void UpdateIcon()
        {
            button.setBackground(button.getContext().getResources().getDrawable(button.getContext().getResources().getIdentifier((content != null) ? content.name.toLowerCase() : defaultIcon, "drawable", button.getContext().getPackageName())));
        }
    }
    public static class InventorySlot extends InventoryButton
    {
        public InventorySlot(Integer curId, ImageButton curButton)
        {
            button = curButton;
            id = curId;
            curButton.setOnClickListener(new View.OnClickListener()
                {
                public void onClick(View v)
                    {
                    Log.d("Press", ("You pressed button " + id));
                    if (Objects.equals(curSelect, id) || content == null) { curSelect = null; return;}
                    curSelect = id;
                    }
                }
                );
        }
    }
    public static class ActiveSlot extends InventoryButton
    {
        public ActiveSlot(Integer curId, ImageButton curButton)
        {
            button = curButton;
            id = curId;
            curButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                Log.d("Press", ("You pressed button " + id));
                if (curSelect != null)
                {
                    EquipAnimal(curSelect, id);
                    curSelect = null;
                }
                else if (inventory.size() < inventorySlots.length && content != null)
                {
                    inventory.add(content);
                    content = null;
                    UpdateIcon();
                    UpdateInventory();
                }
                }
            }
            );
        }
    }
}

