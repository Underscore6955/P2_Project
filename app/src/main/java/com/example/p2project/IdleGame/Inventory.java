package com.example.p2project.IdleGame;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.p2project.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class Inventory
{
    static Dialog curDialog;
    static String defaultIcon = "inv_background";
    public static InventorySlot[] inventorySlots = new InventorySlot[12];
    public static ActiveSlot[] activeSlots = new ActiveSlot[5];
    static Integer curSelect;
    public static ArrayList<Animal> inventory = new ArrayList<Animal>();
    public static Animal[] activeAnimals = new Animal[5];
    static void EquipAnimal(Integer inventorySelec, Integer activeSelec)
    {
        Animal curAnimal = inventory.get(inventorySelec);
        // find selection here
        Animal prevAnimal = activeAnimals[activeSelec];

        if (prevAnimal == null)
        { inventory.remove((int) inventorySelec); }
        else { inventory.set(inventorySelec, prevAnimal); }
        activeAnimals[activeSelec] = curAnimal;
        updateInventory();
    }
    public static void updateInventory()
    {
        if (inventorySlots[0] == null) return;
        for (int i = 0; i < inventorySlots.length; i++)
        {
            try { inventorySlots[i].content = inventory.get(i); }
            catch (Exception e) { inventorySlots[i].content = null; }
            inventorySlots[i].UpdateIcon();
        }
        for (int i = 0; i < activeSlots.length; i++)
        {
            activeSlots[i].content = activeAnimals[i];
            activeSlots[i].UpdateIcon();
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
        protected void showMenu()
        {
            curDialog = new Dialog(button.getContext(), R.style.FullScreenDialog);
            curDialog.setContentView(R.layout.animal_menu);
            curDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView name = curDialog.findViewById(R.id.animal_name);
            TextView earnText = curDialog.findViewById(R.id.earn_text);
            ImageView image = curDialog.findViewById(R.id.animal_image);
            ProgressBar xpBar = curDialog.findViewById(R.id.xp_bar);
            TextView xpText = curDialog.findViewById(R.id.xp_text);
            name.setText(content.name);
            earnText.setText("Earns " + content.production + " treats per second when active\nGives " + content.passive.intValue() + "% passive production");
            image.setImageResource(button.getContext().getResources().getIdentifier(content.name.toLowerCase(), "drawable", button.getContext().getPackageName()));
            xpBar.setProgress((int) (content.xp / 10));
            String xpTextString = ((int) (content.xp / 10) + "% of the way to level " + (content.level + 1));
            xpText.setText(xpTextString);
            curDialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        curDialog.dismiss();
                    }
                }
            );
            curDialog.show();
        }
    }
    public static class InventorySlot extends InventoryButton
    {
        public InventorySlot(Integer curId, ImageButton curButton)
        {
            button = curButton;
            id = curId;
            curSelect = null;
            curButton.setOnClickListener(new View.OnClickListener()
                {
                public void onClick(View v)
                    {
                        if (content != null) {showMenu();}
                        Button equipButton = curDialog.findViewById(R.id.equip_button);
                        equipButton.setOnClickListener(new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                curDialog.dismiss();
                                curSelect = id;
                            }
                        }
                        );
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
                if (curSelect != null)
                {
                    EquipAnimal(curSelect, id);
                    curSelect = null;
                }
                else if (activeAnimals[id] != null)
                {
                    showMenu();
                    TextView equipText = curDialog.findViewById(R.id.equip_text);
                    equipText.setText("Stash");
                    Button equipButton = curDialog.findViewById(R.id.equip_button);
                    equipButton.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            if (inventory.size() < inventorySlots.length)
                            {
                                curDialog.dismiss();
                                inventory.add(activeAnimals[id]);
                                activeAnimals[id] = null;
                                updateInventory();
                            }
                        }
                    }
                    );
                }
                CurrencyTracker.earnThread.updateText();
                }
            }
            );
        }
    }
}

