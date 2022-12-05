package gmail.aryanj1010.statsoverall;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;


import java.util.ArrayList;
import java.util.HashMap;


public final class StatsOverAll extends JavaPlugin {

    HashMap<ItemStack, int[]> itemsAndStats = new HashMap<>();
    ArrayList<ItemStack> items = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::updateStats, 0, 50);

        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("create")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                Inventory inv = p.getInventory();

                    p.sendMessage("command has ran");
                    p.getInventory().addItem(CreateAdvancedItem(
                            inv.getItem(0).getAmount(),
                            inv.getItem(1).getType(),
                            inv.getItem(2).getItemMeta().getDisplayName(),
                            (ArrayList<String>) inv.getItem(3).getItemMeta().getLore(),
                            true,
                            inv.getItem(4).getAmount(),
                            inv.getItem(5).getAmount(),
                            inv.getItem(6).getAmount(),
                            inv.getItem(7).getAmount(),
                            inv.getItem(8).getAmount()));
            }
        }
        return true;
    }

    public ItemStack CreateAdvancedItem(int Amount, Material material, String DisplayName, ArrayList<String> lore, Boolean unbreakable, int SPEED, int HASTE, int HEALTH, int DEFENSE, int STRENGTH)  {
        ItemStack is = new ItemStack(material, Amount);
        ItemMeta isMeta = is.getItemMeta();
        assert isMeta != null;
        isMeta.setDisplayName(DisplayName);
        lore.add("");
        lore.add("");
        lore.add("");
        lore.add(ChatColor.WHITE + "SPEED: " + SPEED);
        lore.add(ChatColor.YELLOW + "MINING SPEED: " + HASTE);
        lore.add(ChatColor.RED + "HEALTH: " + HEALTH);
        lore.add(ChatColor.GRAY + "DEFENSE: " + DEFENSE);
        lore.add(ChatColor.DARK_GREEN + "DAMAGE: " + STRENGTH);
        isMeta.setLore(lore);
        isMeta.setUnbreakable(unbreakable);
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

    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }
}
