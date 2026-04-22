package IdleGame;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.p2project.MainActivity;
import com.example.p2project.R;
import java.io.File;
import java.util.ArrayList;
public class Inventory
{
    public static InventorySlot[] inventorySlots = new InventorySlot[12];
    public static ActiveSlot[] activeSlots = new ActiveSlot[5];
    static ArrayList<Animal> inventory = new ArrayList<Animal>();
    void EquipAnimal(Integer curAnimalIndex)
    {
        Animal curAnimal = inventory.get(curAnimalIndex);
        // find selection here
        int selection = 3;
        Animal prevAnimal = activeSlots[selection].content;

        if (prevAnimal == null)
        { inventory.remove((int) curAnimalIndex); }
        else { inventory.set(curAnimalIndex, prevAnimal); }
        activeSlots[selection].content = curAnimal;
        activeSlots[selection].UpdateIcon();
        UpdateInventory();
    }
    void UpdateInventory()
    {
        for (int i = 0; i < inventorySlots.length; i++)
        {
            try { inventorySlots[i].content = inventory.get(i); }
            catch (ArrayIndexOutOfBoundsException e) { inventorySlots[i].content = null; }
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
            button.setBackground(button.getContext().getResources().getDrawable(button.getContext().getResources().getIdentifier(content.name, "drawable", button.getContext().getPackageName())));
        }
    }
    public static class InventorySlot extends InventoryButton
    {
        public InventorySlot(Integer curId, ImageButton curButton)
        {
            id = curId;
            button = curButton;
            curButton.setOnClickListener(new View.OnClickListener()
                {
                public void onClick(View v)
                {
                    Log.d("Press", ("You pressed button " + id));
                }
                }
            );
        }
    }
    public static class ActiveSlot extends InventoryButton
    {
        public ActiveSlot(Integer curId, ImageButton curButton)
        {
            id = curId;
            button = curButton;
            curButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                Log.d("Press", ("You pressed button " + id));
                }
            }
            );
        }
    }
}

