/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritgraahknatures.methods;

import java.util.Arrays;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.ChatOptions;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.ChatOption;
import spiritgraahknatures.Data;
<<<<<<< HEAD
import spiritgraahknatures.SpiritGraahkNatures;
import spiritgraahknatures.methods.Utils;
=======
import spiritgraahknatures.impls.Condition;
>>>>>>> final

/**
 *
 * @author VOLT
 */
public class MyPlayer {

    /**
     *
     * @return if the player is close to a bank
     */
    public static boolean isAtBank() {
        return Bank.getNearest() != null;
    }

    /**
     *
     * @return returns if your in the area
     */
    public static boolean isAtAltar() {
        return SceneEntities.getNearest(Data.ALTAR_ID) != null;
    }

    /**
     *
     * @return if the player has a ring with them
     */
    public static boolean teleportToBank() {
        if (Calculations.distanceTo(Data.DAEMONHEIM_BANK_LOC) < 100) {
            walk(Data.DAEMONHEIM_BANK_LOC);
            return true;
        }
        Item item = Inventory.getItem(Data.RING);
        if (item != null) {
            item.getWidgetChild().interact("Wear");
        }
        Tabs.EQUIPMENT.open(true);
        item = Equipment.getItem(Equipment.Slot.RING);
        if (item != null) {
            String[] actions = item.getWidgetChild().getActions();
            for (String str : actions) {
                if (str.contains("Castle Wars") || str.contains("Teleport")) {
                    if (item.getWidgetChild().interact(str)) {
                        Tabs.INVENTORY.open();
                        Utils.waitFor(new Condition() {
                            @Override
                            public boolean validate() {
                                return MyPlayer.isAtBank();
                            }
                        }, 10000);
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    public static void repairUsingWicked() {
        Item hood = Inventory.getItem(Data.WICKED_HOOD);
        SceneObject portal = SceneEntities.getNearest(Data.PORTAL);
        ChatOption tele = ChatOptions.getOption(new Filter<ChatOption>() {
            @Override
            public boolean accept(ChatOption t) {
                return t.getWidgetChild().getText().contains("The Runecrafting");
            }
        });
        if (Calculations.distanceTo(Data.KORVAK_LOC) < 45) {
            NPC korvak = NPCs.getNearest(Data.KORVAK);
            if (korvak != null) {
                ChatOption repair = ChatOptions.getOption(new Filter<ChatOption>() {
                    @Override
                    public boolean accept(ChatOption t) {
                        return t.getWidgetChild().getText().contains("I'd like to have");
                    }
                });
                ChatOption help = ChatOptions.getOption(new Filter<ChatOption>() {
                    @Override
                    public boolean accept(ChatOption t) {
                        return t.getWidgetChild().getText().contains("help me with my");
                    }
                });
                ChatOption buy = null;
                int dep = Data.POUCHES.getOneDeprecated();
                if (dep == Data.POUCHES.GIANT.getIds()[1]) {
                    buy = ChatOptions.getOption(new Filter<ChatOption>() {
                        @Override
                        public boolean accept(ChatOption t) {
                            return t.getWidgetChild().getText().contains("12,000");
                        }
                    });
                }
                if (dep == Data.POUCHES.LARGE.getIds()[1]) {
                    buy = ChatOptions.getOption(new Filter<ChatOption>() {
                        @Override
                        public boolean accept(ChatOption t) {
                            return t.getWidgetChild().getText().contains("9,000");
                        }
                    });
                }
                ChatOption optionsToIterate[] = {repair, help, buy};
                boolean found = false;
                if (ChatOptions.canContinue()) {
                    found = true;
                    final ChatOption continueOp = ChatOptions.getContinueOption();
                    if (continueOp != null) {
                        if (continueOp.getWidgetChild().click(true)) {
                            Task.sleep(1500);
                        }
                    }
                } else {
                    for (final ChatOption option : optionsToIterate) {
                        if (option != null) {
                            found = true;
                            option.getWidgetChild().click(true);
                            if (option.getWidgetChild().click(true)) {
                                Task.sleep(1500);
                            }
                            break;
                        }
                    }
                }
                if (!found) {
                    if (korvak.isOnScreen()) {
                        korvak.interact("Talk");
                        Utils.waitFor(new Condition() {
                            @Override
                            public boolean validate() {
                                return ChatOptions.canContinue();
                            }
                        }, 2000);
                    } else {
                        walk(korvak);
                    }
                }
            } else {
                MyPlayer.walk(Data.KORVAK_LOC);
            }
        } else if (tele != null) {
            tele.getWidgetChild().click(true);
            Utils.waitForLocChange();
        } else if (portal != null) {
            if (portal.isOnScreen()) {
                portal.interact("Enter");
                Task.sleep(1000);
            } else {
                portal.getLocation().clickOnMap();
            }
        } else if (hood != null) {
            hood.getWidgetChild().interact("Teleport");
            Utils.waitForLocChange();
        } else {
            if (Bank.isOpen()) {
                if (Bank.withdraw(Data.WICKED_HOOD, Bank.Amount.ONE)) {
                    Bank.close();
                }
            } else {
                Bank.open();
            }
        }
    }

    public static void repairPouches() {
        if (Data.repairingMethod == Data.ReparingMethod.WICKED_HOOD) {
            repairUsingWicked();
        } else {
            ChatOption repair = ChatOptions.getOption(new Filter<ChatOption>() {
                @Override
                public boolean accept(ChatOption t) {
                    return t.getWidgetChild().getText().contains("repair my");
                }
            });
            if (ChatOptions.canContinue()) {
                Task.sleep(1000);
                ChatOptions.getContinueOption().getWidgetChild().click(true);
            } else if (repair != null) {
                Task.sleep(1000);
                repair.getWidgetChild().click(true);
            } else {
                Tabs.INVENTORY.open();
                if (!Inventory.containsAll(Data.ASTRAL, Data.COSMIC)
                        || !(Inventory.containsAll(Data.AIR) || Equipment.getAppearanceId(Equipment.Slot.WEAPON) == Data.AIR_STAFF)) {
                    if (!Bank.isOpen()) {
                        Bank.open();
                    }
                    if (Bank.isOpen()) {
                        if (Inventory.getItem(Data.ASTRAL) == null) {
                            Bank.withdraw(Data.ASTRAL, 1);
                        } else if (Inventory.getItem(Data.COSMIC) == null) {
                            Bank.withdraw(Data.COSMIC, 1);
                        } else if (Inventory.getItem(Data.AIR) == null) {
                            Bank.withdraw(Data.AIR, 2);
                        } else {
                            Bank.close();
                        }
                    }
                } else {
                    if (Bank.isOpen()) {
                        Bank.close();
                    } else {
                        Tabs.MAGIC.open();
                        Widgets.get(430, 26).click(true);
                        Task.sleep(2000);
                        Mouse.click(432, 365, true);
                        Utils.waitFor(new Condition() {
                            @Override
                            public boolean validate() {
                                return ChatOptions.canContinue();
                            }
                        }, 9000);
                    }
                }
            }
        }
    }

    /**
     *
     * @return if the player has all the things needed to leave the bank
     */
    public static boolean isReadyToLeaveBank() {
        return Data.POUCHES.isAllReady() && (Familiar.isSummoned() || Familiar.summonFamiliar())
                && Inventory.getCount(Data.ESSENCE) > 5 && Inventory.isFull();
    }

    public static void walk(Locatable locatable) {
        if (Walking.getEnergy() > 15 && !Walking.isRunEnabled()) {
            Walking.setRun(true);
        }
        Tile loc = locatable.getLocation();
        Tile dest = Walking.getDestination();
        if ((dest != null && Calculations.distance(loc, dest) > 5) || !Players.getLocal().isMoving()) {
            if (loc.isOnScreen()) {
                loc.click(true);
            } else {
                loc.clickOnMap();
            }
        }
    }

    /**
     * does the banking part
     */
    public static void bank() {
        if (Data.POUCHES.isOneDeprecated()) {
            if (Bank.isOpen()) {
                Bank.deposit(Data.ESSENCE, Bank.Amount.ALL);
            }
            repairPouches();
            return;
        }
        if (!Bank.isOpen()) {
            Tabs.EQUIPMENT.open();
            Tabs.INVENTORY.open();
            Bank.open();
            return;
        }
        if (Inventory.isFull()) {
            Data.POUCHES.fillAll(true, false);
            Filter<Item> allExceptGoods = new Filter<Item>() {
                @Override
                public boolean accept(Item t) {
                    return t.getId() != Data.ESSENCE
                            && !t.getName().toLowerCase().contains("pouch")
                            && !t.getName().toLowerCase().contains("Ring");
                }
            };
            boolean changed = false;
            for (Item item : Inventory.getItems(allExceptGoods)) {
                Bank.deposit(item.getId(), Bank.Amount.ALL);
                changed = true;
            }
            if (changed) {
//                return;
            }
        }

        System.out.println(Widgets.get(387, 33).getChildId());
        if (Widgets.get(387, 33).getChildId() == -1) {
            if (Data.bankTeleMethod == Data.BankTeleMethod.ROK) {
                Bank.withdraw(Data.RING[0], Bank.Amount.ONE);
            } else {
                Item item = Bank.getItem(Data.ONLY_DUELINGS);
                if (item != null && Inventory.getItem(Data.ONLY_DUELINGS) == null) {
                    Bank.withdraw(item.getId(), Bank.Amount.ONE);
                }
            }
        }
        Item ring = Inventory.getItem(Data.RING);
        if (ring != null) {
            ring.getWidgetChild().interact("Wear");
        }

        if (!Familiar.isSummoned()) {
            if (!Inventory.isFull()) {
                Item pot;
                if (Data.usePots && Familiar.getPoints() == 0 && (pot = Bank.getItem(Data.SUMMONING_POTS)) != null) {
                    Bank.withdraw(pot.getId(), Bank.Amount.ONE);
                }
                if (Bank.withdraw(Data.GRAAHK_POUCH, Bank.Amount.ONE)) {
                    System.out.println("Hi");
//                    if (Inventory.containsAll(Data.GRAAHK_POUCH)) {
                    Bank.close();
                    Familiar.summonFamiliar();
//                    }
                } else if (Bank.isOpen()) {
                    System.out.println("Out of graahk pouches, please buy some more.");
//                    SpiritGraahkNatures.activescript.stop();
                }
            } else {
                int i = 0;
                for (Item item : Inventory.getItems()) {
                    if (i >= 2) {
                        break;
                    }
                    if (Bank.deposit(item.getId(), Bank.Amount.ONE)) {
                        i++;
                    }
                }
                if (i == 0) {
                    Bank.depositInventory();
                }
                bank();
            }
        }

        Bank.open();
        for (Item i : Bank.getItems(Data.POUCHES.POUCH_FILTER)) { //withdraws all the pouches
            System.out.println(Arrays.toString(Data.POUCHES.MASSIVE.getAllIds()));
            if (i.getId() == Data.POUCHES.MASSIVE.getAllIds()[0] && Inventory.contains(Data.POUCHES.MASSIVE.getAllIds()) || Bank.getItem(Data.POUCHES.MASSIVE.getAllIds()[1]) != null) {
                continue;
            }
            if (!Inventory.isFull()) {
                boolean did;
                if (Utils.random(15)) {
                    did = Bank.withdraw(i.getId(), 28);
                    Task.sleep(500);
                } else {
                    did = Bank.withdraw(i.getId(), Bank.Amount.ONE);
                    Task.sleep(500);
                }
                if (!did && Bank.isOpen()) {
                    System.out.println("Out of ess pouches, please buy some more.");
//                    JOptionPane.showMessageDialog(null, "Out of ess pouches, please buy some more.");
//                    SpiritGraahkNatures.activescript.stop();
                }
            } else {
                break;
            }
        }
        Task.sleep(500, 1000);
        do {
            if (!Inventory.isFull()) {
                if (Bank.getItem(Data.ESSENCE) != null) {
                    if (Utils.random(15)) {
                        Bank.withdraw(Data.ESSENCE, 28);
                    } else {
                        Bank.withdraw(Data.ESSENCE, Bank.Amount.ALL);
                    }
//                    Utils.waitForInventChange();
                } else if (Bank.isOpen()) {
                    System.out.println("Out of ess, please buy some more.");
//                    JOptionPane.showMessageDialog(null, "Out of ess, please buy some more.");
//                    SpiritGraahkNatures.activescript.stop();
                    break;
                }
            } else {
                break;
            }
        } while (Data.POUCHES.fillAll(true, true)); //fills all the essenses until it cant hold anymore
    }
}
