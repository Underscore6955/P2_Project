package IdleGame;

import com.example.p2project.MainActivity;

import java.util.ArrayList;
public class Inventory
{
    static Animal[] activeAnimals = new Animal[5];
    static ArrayList<Animal> inventory = new ArrayList<Animal>();
    void EquipAnimal(Integer curAnimalIndex)
    {
        Animal curAnimal = inventory.get(curAnimalIndex);
        // find selection here
        int selection = 3;
        Animal prevAnimal = activeAnimals[selection];

        if (prevAnimal == null)
        { inventory.remove((int) curAnimalIndex); }
        else { inventory.set(curAnimalIndex, prevAnimal); }
        activeAnimals[selection] = curAnimal;
        UpdateInventory();
    }
    void UpdateInventory()
    {
        for (int i = 0; i < inventory.toArray().length; i++)
        {
            MainActivity.inventorySlots[i].content = inventory.get(i);
        }
    }
}

