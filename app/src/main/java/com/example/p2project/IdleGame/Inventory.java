package com.example.p2project.IdleGame;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.R;
import com.bumptech.glide.Glide;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class    Inventory
{
    static Dialog curDialog;
    static String defaultIcon = "inv_background";
    public static InventorySlot[] inventorySlots = new InventorySlot[12];
    public static ActiveSlot[] activeSlots = new ActiveSlot[5];
    public static ArrayList<Animal> inventory = new ArrayList<Animal>();
    public static Animal[] activeAnimals = new Animal[5];
    static void EquipAnimal(Integer inventorySelec, Integer activeSelec)
    {
        Animal curAnimal = inventory.get(inventorySelec);
        Animal prevAnimal = activeAnimals[activeSelec];

        if (prevAnimal == null)
        { inventory.remove((int) inventorySelec); }
        else { inventory.set(inventorySelec, prevAnimal); }
        activeAnimals[activeSelec] = curAnimal;
        updateInventory(defaultIcon);
    }
    public static void updateInventory(String defaultIcon)
    {
        if (inventorySlots[0] != null) {
            for (int i = 0; i < inventorySlots.length; i++) {
                try {
                    inventorySlots[i].content = inventory.get(i);
                } catch (Exception e) {
                    inventorySlots[i].content = null;
                }
                inventorySlots[i].UpdateIcon(defaultIcon);
            }
        }
        updateActive(defaultIcon);
    }
    public static void updateActive(String defaultIcon)
    {
        for (int i = 0; i < activeSlots.length; i++)
        {
            activeSlots[i].content = activeAnimals[i];
            activeSlots[i].UpdateIcon(defaultIcon);
        }
    }
    public static class InventoryButton
    {
        public Animal content;
        Integer id;
        ImageButton button;
        public void UpdateIcon(String defaultIcon)
        {
            button.setImageResource(button.getContext().getResources().getIdentifier((content != null) ? content.name.toLowerCase() : defaultIcon, "drawable", button.getContext().getPackageName()));
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
            name.setText(content.name.replace('_',' '));
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
                                for (int i = 0; i < activeAnimals.length; i++) {if (activeAnimals[i] == null) {activeAnimals[i] = content;inventory.remove(content); break;}}
                                updateInventory(defaultIcon);
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
        String defaultIcon;
        public ActiveSlot(Integer curId, ImageButton curButton, String curDefaultIcon)
        {
            button = curButton;
            id = curId;
            this.defaultIcon = curDefaultIcon;
            curButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {if (activeAnimals[id] != null)
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
                                updateInventory(defaultIcon);
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
        public void startAnim()
        {
            Log.d("anim", "started anim");
            Drawable gif = null;
            try {gif = button.getContext().getResources().getDrawable(button.getContext().getResources().getIdentifier(content.name.toLowerCase() +"_"+ ((DayNightSystem.day) ? "idle" : "sleep"), "drawable", button.getContext().getPackageName()));}
            catch (android.content.res.Resources.NotFoundException | NullPointerException e){return;}
            if (content != null) Log.d("gif",(gif == null) + " " + content.name);
            if (gif != null) Glide.with(CurrencyTracker.earnThread.main).load(gif).override(button.getWidth(),button.getHeight()).into(button);
        }
    }
}

