package gmail.aryanj1010.statsoverall;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class ItemWrapper implements Serializable {
    ItemStack item;
    public ItemWrapper (ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}
