package gmail.aryanj1010.statsoverall;


import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;


public final class StatsOverAll extends JavaPlugin {
    String url = "jdbc:postgresql://localhost:5432/StatsPlugin?user=postgres&password=154150061";
    Object driver = Class.forName("org.postgresql.Driver").newInstance();
    Connection connection = DriverManager.getConnection(url);

    public StatsOverAll() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    }

    @Override
    public void onEnable() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            try {
                updateStats();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 50);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("create")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                Inventory inv = p.getInventory();

                try {
                    p.sendMessage("command has ran");
                    p.getInventory().addItem(CreateAdvancedItem(inv.getItem(0).getAmount(), inv.getItem(1).getType(), inv.getItem(2).getItemMeta().getDisplayName(), (ArrayList<String>) inv.getItem(3).getItemMeta().getLore(), true, inv.getItem(4).getAmount(), inv.getItem(5).getAmount(), inv.getItem(6).getAmount(), inv.getItem(7).getAmount(), inv.getItem(8).getAmount()));
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    public ItemStack CreateAdvancedItem(int Amount, Material material, String DisplayName, ArrayList<String> lore, Boolean unbreakable, int SPEED, int HASTE, int HEALTH, int DEFENSE, int STRENGTH) throws SQLException, IOException {
        ItemStack is = new ItemStack(material, Amount);
        ItemMeta isMeta = is.getItemMeta();
        assert isMeta != null;
        isMeta.setDisplayName(DisplayName);
        lore.add("");
        lore.add("");
        lore.add("");
        lore.add(Color.WHITE + "SPEED: " + SPEED);
        lore.add(Color.YELLOW + "MINING SPEED: " + HASTE);
        lore.add(Color.RED + "HEALTH: " + HEALTH);
        lore.add(Color.GRAY + "DEFENSE: " + DEFENSE);
        lore.add(Color.ORANGE + "DAMAGE: " + STRENGTH);
        isMeta.setLore(lore);
        isMeta.setUnbreakable(unbreakable);
        is.setItemMeta(isMeta);
        byte[] serialize = convertToBytes(is);
        PreparedStatement ps = connection.prepareStatement("INSERT INTO customitemstacks VALUES (?)");
        InputStream inputStream = new ByteArrayInputStream(serialize);
        ps.setBinaryStream(1, inputStream);
        ps.execute();
        ps.close();
        return is;
    }
    public void updateStats() throws SQLException, IOException {
        String query = "SELECT itemdata, speed, haste, health, defense, strength FROM customitemstacks";
        
        PreparedStatement psget = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = psget.executeQuery();
        for (Player p:
                getServer().getOnlinePlayers()) {
            byte[] boots = convertToBytes(p.getInventory().getBoots());
            byte[] leggings = convertToBytes(p.getInventory().getLeggings());
            byte[] chestplate = convertToBytes(p.getInventory().getChestplate());
            byte[] helmet = convertToBytes(p.getInventory().getHelmet());
            byte[] heldItem = convertToBytes(p.getItemInUse());
            int playerSpeed = 0;
            int playerHaste = 0;
            int playerHealth = 0;
            int playerDefense = 0;
            int playerStrength = 0;
            rs.beforeFirst();
            while(rs.next()) {
                if (Objects.equals(rs.getBytes("data"), (boots))) {
                    playerSpeed = playerSpeed + rs.getInt("speed");
                    playerHaste = playerHaste + rs.getInt("haste");
                    playerHealth = playerHealth + rs.getInt("health");
                    playerDefense = playerDefense + rs.getInt("defense");
                    playerStrength = playerStrength + rs.getInt("strength");
                }
                if (Objects.equals(rs.getBytes("data"), (leggings))) {
                    playerSpeed = playerSpeed + rs.getInt("speed");
                    playerHaste = playerHaste + rs.getInt("haste");
                    playerHealth = playerHealth + rs.getInt("health");
                    playerDefense = playerDefense + rs.getInt("defense");
                    playerStrength = playerStrength + rs.getInt("strength");
                }
                if (Objects.equals(rs.getBytes("data"), (chestplate))) {
                    playerSpeed = playerSpeed + rs.getInt("speed");
                    playerHaste = playerHaste + rs.getInt("haste");
                    playerHealth = playerHealth + rs.getInt("health");
                    playerDefense = playerDefense + rs.getInt("defense");
                    playerStrength = playerStrength + rs.getInt("strength");
                }
                if (Objects.equals(rs.getBytes("data"), (helmet))) {
                    playerSpeed = playerSpeed + rs.getInt("speed");
                    playerHaste = playerHaste + rs.getInt("haste");
                    playerHealth = playerHealth + rs.getInt("health");
                    playerDefense = playerDefense + rs.getInt("defense");
                    playerStrength = playerStrength + rs.getInt("strength");
                }
                if (Objects.equals(rs.getBytes("data"), (heldItem))) {
                    playerSpeed = playerSpeed + rs.getInt("speed");
                    playerHaste = playerHaste + rs.getInt("haste");
                    playerHealth = playerHealth + rs.getInt("health");
                    playerDefense = playerDefense + rs.getInt("defense");
                    playerStrength = playerStrength + rs.getInt("strength");
                }
            }
            p.addPotionEffect(PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, playerSpeed/4));
            p.addPotionEffect(PotionEffectType.FAST_DIGGING.createEffect(Integer.MAX_VALUE, playerHaste/4));
            p.addPotionEffect(PotionEffectType.HEALTH_BOOST.createEffect(Integer.MAX_VALUE, playerHealth/4));
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
