package gmail.aryanj1010.statsoverall;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public final class StatsOverAll extends JavaPlugin implements Listener {

    HashMap<ItemStack, int[]> itemsAndStats = new HashMap<>();
    ArrayList<ItemStack> items = new ArrayList<>();

    ItemStack returnButton = new ItemStack(Material.PAPER);
    ItemMeta rbMeta = returnButton.getItemMeta();
    Inventory inv = getServer().createInventory(null, 18, "Item Creator");
    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::updateStats, 0, 50);
        getServer().getPluginManager().registerEvents(this, this);

        assert rbMeta != null;
        rbMeta.setDisplayName(ChatColor.RED + "Create");
        returnButton.setItemMeta(rbMeta);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("create")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                //36-53
                ItemStack amtItem = new ItemStack(Material.PAPER);
                ItemMeta amtMeta = amtItem.getItemMeta();
                assert amtMeta != null;
                amtMeta.setDisplayName("amount");
                amtItem.setItemMeta(amtMeta);

                ItemStack matItem = new ItemStack(Material.GRASS_BLOCK);
                ItemMeta matMeta = matItem.getItemMeta();
                assert matMeta != null;
                matMeta.setDisplayName("Material");
                matItem.setItemMeta(matMeta);

                ItemStack dnItem = new ItemStack(Material.PAPER);
                ItemMeta dnMeta = dnItem.getItemMeta();
                assert dnMeta != null;
                dnMeta.setDisplayName("Name");
                dnItem.setItemMeta(dnMeta);

                inv.setItem(0, amtItem);
                inv.setItem(1, matItem);
                inv.setItem(2, dnItem);
                inv.setItem(3, new ItemStack(Material.LEATHER_BOOTS));
                inv.setItem(4, new ItemStack(Material.GOLDEN_PICKAXE));
                inv.setItem(5, new ItemStack(Material.RED_DYE));
                inv.setItem(6, new ItemStack(Material.SHIELD));
                inv.setItem(7, new ItemStack(Material.IRON_SWORD));
                inv.setItem(17, returnButton);
                p.openInventory(inv);
            }
        }
        return true;
    }

    @EventHandler
    public void onInventoryPress (InventoryClickEvent e) {
        Player p = null;
        Inventory inv = e.getInventory();
        if (e.getInventory().equals(inv)) {
            if (e.getWhoClicked() instanceof Player) {
                p = (Player) e.getWhoClicked();
            }
            if (e.isLeftClick() && Objects.equals(e.getCurrentItem(), returnButton)) {
                p.getInventory().addItem(CreateAdvancedItem(inv.getItem(9).getAmount(),inv.getItem(10).getType(),inv.getItem(11).getItemMeta().getDisplayName(),inv.getItem(12).getAmount(), inv.getItem(13).getAmount(),inv.getItem(14).getAmount(),inv.getItem(15).getAmount(),inv.getItem(16).getAmount()));
                p.closeInventory();

            }
        }
    }

    public ItemStack CreateAdvancedItem(int Amount, Material material, String DisplayName, int SPEED, int HASTE, int HEALTH, int DEFENSE, int STRENGTH)  {
        ItemStack is = new ItemStack(material, Amount);
        ItemMeta isMeta = is.getItemMeta();
        assert isMeta != null;
        isMeta.setDisplayName(DisplayName);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "SPEED: " + SPEED);
        lore.add(ChatColor.YELLOW + "MINING SPEED: " + HASTE);
        lore.add(ChatColor.RED + "HEALTH: " + HEALTH);
        lore.add(ChatColor.GRAY + "DEFENSE: " + DEFENSE);
        lore.add(ChatColor.DARK_GREEN + "DAMAGE: " + STRENGTH);
        isMeta.setLore(lore);
        isMeta.setUnbreakable(true);
        is.setItemMeta(isMeta);
        itemsAndStats.put(is, new int[]{SPEED, HASTE, HEALTH, DEFENSE, STRENGTH});
        items.add(is);
        return is;
    }
    public void updateStats(){

        for (Player p:
                getServer().getOnlinePlayers()) {

            ItemStack boots = p.getInventory().getBoots();
            ItemStack leggings = p.getInventory().getLeggings();
            ItemStack chestplate = p.getInventory().getChestplate();
            ItemStack helmet =  p.getInventory().getHelmet();
            ItemStack heldItem = p.getInventory().getItem(p.getInventory().getHeldItemSlot());

            ItemStack[] contents = {boots, leggings, chestplate, heldItem, helmet};
            int playerSpeed = 0;
            int playerHaste = 0;
            int playerHealth = 0;
            int playerDefense = 0;
            int playerStrength = 0;
            for (ItemStack items:
                 items) {
                for (ItemStack item : contents) {
                    if (items.equals(item)) {
                        int[] stats = itemsAndStats.get(items);
                        playerSpeed = playerSpeed + stats[0];
                        playerHaste = playerHaste + stats[1];
                        playerHealth = playerHealth + stats[2];
                        playerDefense = playerDefense + stats[3];
                        playerStrength = playerStrength + stats[4];
                    }
                }
            }
            p.removePotionEffect(PotionEffectType.SPEED);
            p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            p.addPotionEffect(PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, playerSpeed/4));
            p.addPotionEffect(PotionEffectType.FAST_DIGGING.createEffect(Integer.MAX_VALUE, playerHaste/4));
            p.setMaxHealth(playerHealth+20);
            p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(Integer.MAX_VALUE, playerDefense/4));
            p.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE, playerStrength/4));
        }
    }
}
