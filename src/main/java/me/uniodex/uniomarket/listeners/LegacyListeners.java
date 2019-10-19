package me.uniodex.uniomarket.listeners;

import me.UnioDex.UnioSpawners.UnioSpawnersPlugin;
import me.UnioDex.UnioSpawners.api.UnioSpawnersAPI;
import me.UnioDex.UnioSpawners.api.events.SpawnerChangeEvent;
import me.UnioDex.UnioSpawners.api.events.SpawnerPlaceEvent;
import me.UnioDex.UnioSpawners.api.spawner.SpawnerData;
import me.UnioDex.UnioSpawners.spawners.spawner.ESpawner;
import me.UnioDex.UnioSpawners.spawners.spawner.ESpawnerStack;
import me.uniodex.uniomarket.UnioMarket;
import me.uniodex.uniomarket.enums.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LegacyListeners implements Listener {

    private UnioMarket plugin;

    public LegacyListeners(UnioMarket plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        if (UnioMarket.getServerType().equals(ServerType.FACTIONS)) return;
        Bukkit.getPluginManager().registerEvents(new OldCreditChecker(), plugin);
        if (Bukkit.getPluginManager().isPluginEnabled("UnioSpawners")) {
            Bukkit.getPluginManager().registerEvents(new OldSkyblockSpawnerFixer(), plugin);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0].replace("/", "").toLowerCase();

        // TODO change the vipodul commands if necessary

        if (command.equalsIgnoreCase("vipodul")) {
            event.setCancelled(true);
            player.performCommand("uniomarket activate vipodul");
        }

        if (command.equalsIgnoreCase("uvipodul")) {
            event.setCancelled(true);
            player.performCommand("uniomarket activate uvipodul");
        }
    }

    public class OldCreditChecker implements Listener {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void on(InventoryClickEvent event) {
            checkItemsNextTick(event.getWhoClicked());
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void on(PlayerInteractEvent event) {
            checkItemsNextTick(event.getPlayer());
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void on(EntityPickupItemEvent event) {
            if (event.getEntity() instanceof Player) checkItemsNextTick((Player) event.getEntity());
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void on(PlayerJoinEvent event) {
            checkItemsNextTick(event.getPlayer());
        }

        private void checkItemsNextTick(HumanEntity player) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (player == null) return;
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item == null) continue;
                    if (!item.getType().equals(Material.PAPER)) continue;
                    if (item.getItemMeta().getDisplayName() == null) continue;
                    if (!item.getItemMeta().getDisplayName().startsWith("§9§lKredi Çeki:")) continue;
                    player.sendMessage(plugin.getMessage("protections.illegalItemsFoundAndRemoved")
                            .replaceAll("%player%", player.getName()
                                    .replaceAll("%item%", item.getItemMeta().getDisplayName())));
                    plugin.getLogManager().logIllegalItem(player.getName(), "Eski kredi çeki: " + item.getItemMeta().getDisplayName());
                    player.getInventory().removeItem(item);
                }
            });
        }
    }

    public class OldSkyblockSpawnerFixer implements Listener {
        private void fixSpawners(Inventory inventory) {
            ItemStack[] items = inventory.getContents();

            for (ItemStack item : items) {
                if (item == null) continue;
                String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

                if (item.getType() == Material.MOB_SPAWNER) {
                    if (displayName != null) {
                        if (displayName.equalsIgnoreCase("§9§lIron Golem Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Iron Golem").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lZombi Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Zombie").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lİskelet Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Skeleton").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lÖrümcek Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Spider").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lZombi Domuzadam Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Pig Zombie").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lBalçık Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Slime").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lİnek Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Cow").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lTavuk Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Chicken").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lKoyun Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Sheep").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lDomuz Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Pig").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lAt Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Horse").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lMööntar Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Mushroom Cow").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lKöylü Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Villager").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        } else if (displayName.equalsIgnoreCase("§9§lDemir Spawner")) {
                            inventory.remove(item);

                            ItemStack newItem = UnioSpawnersAPI.getSpawnerManager().getSpawnerData("Custom 1").toItemStack();
                            newItem.setAmount(item.getAmount());

                            inventory.addItem(newItem);
                            if (inventory.getHolder() instanceof Player) {
                                ((Player) inventory.getHolder()).updateInventory();
                            }
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            ItemStack[] items = player.getInventory().getContents();

            for (ItemStack item : items) {
                if (item == null) continue;
                String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;
                if (item.getType() == Material.MOB_SPAWNER) {
                    if (displayName != null) {
                        if (displayName.startsWith("§9§l")) {
                            player.sendMessage(UnioMarket.hataPrefix + "Envanterinizdeki eski sistemden kalma spawnerlar güncellendi.");
                            fixSpawners(player.getInventory());
                            return;
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onInventoryOpen(InventoryOpenEvent event) {
            Player player = (Player) event.getPlayer();

            for (ItemStack item : event.getInventory().getContents()) {
                if (item == null) continue;
                String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;
                if (item.getType() == Material.MOB_SPAWNER) {
                    if (displayName != null) {
                        if (displayName.startsWith("§9§l")) {
                            player.sendMessage(UnioMarket.hataPrefix + "Açtığınız envanterdeki eski sistemden kalma spawnerlar güncellendi.");
                            fixSpawners(event.getInventory());
                            break;
                        }
                    }
                }
            }

            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) continue;
                String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;
                if (item.getType() == Material.MOB_SPAWNER) {
                    if (displayName != null) {
                        if (displayName.startsWith("§9§l")) {
                            player.sendMessage(UnioMarket.hataPrefix + "Envanterinizdeki eski sistemden kalma spawnerlar güncellendi.");
                            fixSpawners(player.getInventory());
                            return;
                        }
                    }
                }
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onItemPickup(PlayerPickupItemEvent event) {
            Player player = (Player) event.getPlayer();

            if (!event.getItem().getItemStack().getType().equals(Material.MOB_SPAWNER)) return;

            ItemStack[] items = player.getInventory().getContents();

            for (ItemStack item : items) {
                if (item == null) continue;
                String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;
                if (item.getType() == Material.MOB_SPAWNER) {
                    if (displayName != null) {
                        if (displayName.startsWith("§9§l")) {
                            player.sendMessage(UnioMarket.hataPrefix + "Envanterinizdeki eski sistemden kalma spawnerlar güncellendi.");
                            fixSpawners(player.getInventory());
                            return;
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onPlace(BlockPlaceEvent event) {
            if (event.isCancelled()) return;

            Player player = event.getPlayer();
            ItemStack item = event.getPlayer().getItemInHand();
            String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

            if (item.getType() == Material.MOB_SPAWNER) {
                if (displayName != null) {
                    if (displayName.startsWith("§9§l")) {
                        event.setCancelled(true);
                        player.sendMessage(UnioMarket.hataPrefix + "Envanterinizdeki eski sistemden kalma spawnerlar güncellendi. Lütfen spawnerı yeniden koyunuz.");
                        fixSpawners(player.getInventory());
                        return;
                    }
                }
            }
        }

        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            if (event.isCancelled()) return;

            if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            if (item == null) return;

            String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

            if (item.getType() == Material.MOB_SPAWNER) {
                if (displayName != null) {
                    if (displayName.startsWith("§9§l")) {
                        event.setCancelled(true);
                        player.sendMessage(UnioMarket.hataPrefix + "Envanterinizdeki eski sistemden kalma spawnerlar güncellendi. Lütfen spawnerı yeniden koyunuz.");
                        fixSpawners(player.getInventory());
                        return;
                    }
                }
            }
        }

        @EventHandler
        public void onSpawnerPlace(SpawnerPlaceEvent event) {
            if (event.isCancelled()) return;

            Player player = event.getPlayer();
            ItemStack item = event.getPlayer().getItemInHand();
            String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

            if (item.getType() == Material.MOB_SPAWNER) {
                if (displayName != null) {
                    if (displayName.startsWith("§9§l")) {
                        event.setCancelled(true);
                        player.sendMessage(UnioMarket.hataPrefix + "Envanterinizdeki eski sistemden kalma spawnerlar güncellendi. Lütfen spawnerı yeniden koyunuz.");
                        fixSpawners(player.getInventory());
                        return;
                    }
                }
            }
        }

        @EventHandler
        public void onSpawnerChange(SpawnerChangeEvent event) {
            if (event.isCancelled()) return;

            Player player = event.getPlayer();
            ItemStack item = event.getPlayer().getItemInHand();
            String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

            if (item.getType() == Material.MOB_SPAWNER) {
                if (displayName != null) {
                    if (displayName.startsWith("§9§l")) {
                        event.setCancelled(true);
                        player.sendMessage(UnioMarket.hataPrefix + "Envanterinizdeki eski sistemden kalma spawnerlar güncellendi. Lütfen spawnerı yeniden koyunuz.");
                        fixSpawners(player.getInventory());
                        return;
                    }
                }
            }
        }

        @EventHandler
        public void fixOldSkyblockSpawners(SpawnerSpawnEvent event) {
            if (event.getEntityType().equals(EntityType.DROPPED_ITEM)) {
                Block block = event.getSpawner().getBlock();
                Location loc = block.getLocation();

                try {
                    if (UnioSpawnersAPI.getSpawnerManager().getSpawnerFromWorld(loc).getIdentifyingName().toLowerCase().startsWith("custom")) {
                        return;
                    }
                } catch (NullPointerException ignored) {
                }

                block.setType(Material.AIR);
                UnioSpawnersAPI.getSpawnerManager().removeSpawnerFromWorld(loc);

                block.setType(Material.MOB_SPAWNER);
                ((CreatureSpawner) block.getState()).setSpawnedType(EntityType.DROPPED_ITEM);
                ESpawner spawner = new ESpawner(loc);
                UnioSpawnersAPI.getSpawnerManager().addSpawnerToWorld(loc, spawner);

                SpawnerData spawnerData = null;

                for (SpawnerData sd : UnioSpawnersAPI.getSpawnerManager().getAllSpawnerData()) {
                    if (sd.getDisplayName().equals("Demir")) {
                        spawnerData = sd;
                    }
                }

                spawner.addSpawnerStack(new ESpawnerStack(spawnerData, 1));
                spawner.setDelay(1);
                spawner.getCreatureSpawner().setDelay(300);
                spawner.getCreatureSpawner().update();

                if (UnioSpawnersPlugin.getInstance().getHologram() != null) {
                    UnioSpawnersPlugin.getInstance().getHologram().processChange(block);
                    UnioSpawnersPlugin.getInstance().getHologram().add(spawner);
                }
                UnioSpawnersPlugin.getInstance().getAppearanceHandler().updateDisplayItem(spawner, spawnerData);
            }
        }
    }
}
