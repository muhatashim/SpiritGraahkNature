/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritgraahknatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
<<<<<<< HEAD
import spiritgraahknatures.impls.Condition;
=======
import spiritgraahknatures.methods.Utils;
>>>>>>> final

/**
 *
 * @author VOLT
 */
public class Data {

    public static final int RING[] = {15707, 2552, 2553, 2554, 2555, 2556, 2557, 2558, 2559, 2560, 2561, 2562, 2563, 2564, 2565, 2566, 2567, 20922};
    public static final int ONLY_DUELINGS[] = {2552, 2553, 2554, 2555, 2556, 2557, 2558, 2559, 2560, 2561, 2562, 2563, 2564, 2565, 2566, 2567, 20922};
    public static int bankTeleMethod = 0;

    public class BankTeleMethod {

        public static final int DUELING = 0;
        public static final int ROK = 1;
    }
    public static int repairingMethod = 0;

    public class ReparingMethod {

        public static final int NPC_CONTACT = 0;
        public static final int WICKED_HOOD = 1;
    }
    public static boolean usePots = false;
    public static final int SUMMONING_POTS[] = {12140, 12141, 12142, 12143, 12144, 12145, 12146, 12147, 14277, 14278, 14279, 14280, 14281, 14282, 14283, 14284, 14285, 14286};
    public static final int OBBY = 29992;
    public static final int NATURE = 561;
    public static final int AIR = 556;
    public static final int AIR_STAFF = 1381;
    public static final int ASTRAL = 9075;
    public static final int COSMIC = 564;
    public static final int GRAAHK_POUCH = 12810;
    public static final int WICKED_HOOD = 22332;
    public static final int KORVAK = 8029;
    public static final int PORTAL = 38279;
    public static final int POUCH_COUNT_SETTING = 720;
    public static final int ESSENCE = 7936;
    public static final int RUINS_ID = 2460;
    public static final int ALTAR_ID = 2486;
    public static final Tile RUINS = new Tile(2869, 3021, 0);
    public static final Tile OBBY_LOC = new Tile(2850, 3027, 0);
    public static final Tile KORVAK_LOC = new Tile(1696, 5471, 2);
    public static final Tile DAEMONHEIM_BANK_LOC = new Tile(3449, 3721, 0);
    public static final Filter<NPC> NPCS_INTERACTING = new Filter<NPC>() {
        @Override
        public boolean accept(NPC t) {
            org.powerbot.game.api.wrappers.interactive.Character interacting = t.getInteracting();
            if (interacting != null) {
                return interacting.equals(Players.getLocal());
            }
            return false;
        }
    };

    public enum POUCHES {

        MASSIVE(new int[]{24205, 24204}, -1, 0b11111111111111111111111111111111, -1, false),
        GIANT(5514, 5515, 12, 0b10000000, true),
        LARGE(5512, 5513, 9, 0b100000, true),
        MEDIUM(5510, 5511, 0b1000, 8, true),
        SMALL(5509, -1, 3, 0b10, true);
        private boolean enabled;
        private final int deprecatedId;
        private final int amountHold;
        private final int mask;
        private final int id;
        private final int[] ids;
        public static final Filter<Item> POUCH_FILTER = new Filter<Item>() {
            @Override
            public boolean accept(Item t) {
                for (POUCHES p : POUCHES.getAvailable()) {
                    int ids[] = p.getAllIds();
                    Arrays.sort(ids);
                    if (Arrays.binarySearch(ids, t.getId()) >= 0) {
                        return true;
                    }
                }
                return false;
            }
        };

        /**
         * @param ids id of the item
         * @param deprecatedId broken pouch id
         * @param amountHold max amount of essence the pouch can hold
         * @param mask the mask of the setting
         * @param enabled if to use it
         */
        POUCHES(int[] ids, int deprecatedId, int amountHold, int mask, boolean enabled) {
            this.deprecatedId = deprecatedId;
            this.amountHold = amountHold;
            this.mask = mask;
            this.ids = ids;
            this.id = ids[0];
            this.enabled = enabled;
            Arrays.sort(this.ids);
        }

        /**
         * @param id id of the item
         * @param deprecatedId broken pouch id
         * @param amountHold max amount of essence the pouch can hold
         * @param mask the mask of the setting
         * @param enabled if to use it
         */
        POUCHES(int id, int deprecatedId, int amountHold, int mask, boolean enabled) {
            this.deprecatedId = deprecatedId;
            this.amountHold = amountHold;
            this.mask = mask;
            this.id = id;
            this.ids = new int[]{id};
            this.enabled = enabled;
        }
        private boolean lastTimeFilled = false;

        /**
         *
         * @return if the player has the pouch filled
         */
        public boolean isFilled() {
            if (this.getIds()[0] == Data.POUCHES.MASSIVE.getIds()[0]) {
                Item item = Inventory.getItem(24205);
                if (item != null) {
                    String[] actions = item.getWidgetChild().getActions();
                    if (!actions[0].equals("Deposit")) {
                        return lastTimeFilled = actions[0].equals("Empty");
                    } else {
                        return lastTimeFilled;
                    }
                } else {
                    return false;
                }
            }
            return (Settings.get(Data.POUCH_COUNT_SETTING) & this.mask) == this.mask;
        }

        /**
         *
         * @return all the ids for this pouch.
         */
        public int[] getAllIds() {
            List<Integer> list = new ArrayList();
            for (int i : this.ids) {
                list.add(i);
            }
            list.add(this.deprecatedId);
            Integer[] array = new Integer[list.size()];
            list.toArray(array);
            int[] iArray = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                iArray[i] = array[i];
            }
            return iArray;
        }

        /**
         *
         * @return all the ids for this pouch unless if it's a massive pouch. 0
         * is normal, 1 is broken id
         */
        public int[] getIds() {
            return new int[]{this.id, this.deprecatedId};
        }

        /**
         *
         * @return counts the amount of items in the inventory
         */
        public int getAllCount() {
            if (Tabs.INVENTORY.open()) {
                return Inventory.getCount(this.deprecatedId) + Inventory.getCount(this.ids);
            }
            return -1;
        }

        /**
         *
         * @param deprecated true if you want to see if the player has the
         * broken pouch
         * @return if the player has the pouch
         */
        public boolean hasPouch(boolean deprecated) {
            if (Tabs.INVENTORY.open()) {
                return Inventory.getCount(deprecated ? new int[]{this.deprecatedId} : this.ids) > 0;
            }
            return false;
        }

        /**
         *
         * @param on true to fill, false to take contents out
         */
        public void equip(boolean on) {
            if (getAllCount() > 0) {
                Item item = Inventory.getItem(new Filter<Item>() {
                    @Override
                    public boolean accept(Item t) {
                        return Arrays.binarySearch(ids, t.getId()) >= 0 || t.getId() == deprecatedId;
                    }
                });

                item.getWidgetChild().click(false);
                if (on) {
                    if (Inventory.containsAll(Data.ESSENCE) && item.getWidgetChild().interact("Fill")) {
                        lastTimeFilled = on;
                    }
                } else {
                    if (!Inventory.isFull() && item.getWidgetChild().interact("Empty")) {
                        lastTimeFilled = on;
                    }
                }
                Task.sleep(100, 200);
            }
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return this.name() + " enabled=" + this.enabled;
        }

        /**
         *
         * @return the amount this pouch can hold
         */
        public int getMaxAmount() {
            return this.amountHold;
        }

        /**
         * fills/unfills all the pouches
         *
         * @param fill true to fill, false to take contents out
         * @return if at least one is changed
         */
        public static boolean fillAll(boolean fill, boolean breakForFillNotEnough) {
            final int start = Inventory.getCount();
            boolean changed = false;
            POUCHES[] values = POUCHES.values();
            for (int i = 0; i < values.length; i++) {
                POUCHES p = values[i];
                if (fill == !p.isFilled()) {
                    if (fill && breakForFillNotEnough && Inventory.getCount(Data.ESSENCE) < p.getMaxAmount()) {
                        return changed;
                    }
                    p.equip(fill);
                    changed = true;
                    System.out.println((p.ordinal() == Data.POUCHES.MASSIVE.ordinal()) + ", " + p.lastTimeFilled + " : " + p);
                    if (breakForFillNotEnough && i + 1 < values.length) {
                        if (values[i + 1].getMaxAmount() > Inventory.getCount(Data.ESSENCE) - p.getMaxAmount()) {
                            Utils.waitForInventChange();
                            return changed;
                        }
                    }
                    if (p.ordinal() == Data.POUCHES.MASSIVE.ordinal() && p.lastTimeFilled) {
                        Utils.waitForInventChange();
                        return changed;
                    }
                }
            }
//            if (changed) {
//                Utils.waitFor(new Condition() {
//                    @Override
//                    public boolean validate() {
//                        return Inventory.getCount() != start;
//                    }
//                }, 2000);
//            Task.sleep(500);
//            }
            return changed;
        }

        /**
         *
         * @return if all the pouches are filled
         */
        public static boolean isAllReady() {
            for (POUCHES p : POUCHES.getAvailable()) {
                if (!p.isFilled()) {
                    return false;
                }
            }
            return true;
        }

        /**
         *
         * @return if atleast one is ready including inventory
         */
        public static int getAmountReady() {
            int i = 0;
            for (POUCHES p : POUCHES.getAvailable()) {
                if (p.isFilled()) {
                    i++;
                }
            }
            return i;
        }

        public static int getOneDeprecated() {
            for (POUCHES p : POUCHES.getAvailable()) {
                if (Inventory.containsAll(p.getIds()[1])) {
                    return p.getIds()[1];
                }
            }
            return -1;
        }

        public static boolean isOneDeprecated() {
            for (POUCHES p : POUCHES.getAvailable()) {
                if (Inventory.containsAll(p.getIds()[1])) {
                    return true;
                }
            }
            return false;
        }

        public static boolean isOnePouchReadyInInvent() {
            for (POUCHES p : POUCHES.getAvailable()) {
                if (p.isFilled() && Inventory.contains(p.getIds())) {
                    return true;
                }
            }
            return false;
        }

        public static boolean isOnePouchReady() {
            for (POUCHES p : POUCHES.getAvailable()) {
                if (p.isFilled()) {
                    return true;
                }
            }
            return false;
        }

        /**
         *
         * @return if atleast one is ready including inventory
         */
        public static boolean isOneReady() {
            for (POUCHES p : POUCHES.getAvailable()) {
                if (p.isFilled()) {
                    return true;
                }
            }
            return Inventory.containsAll(Data.ESSENCE);
        }

        /**
         *
         * @return the pouches enabled
         */
        public static POUCHES[] getAvailable() {
            List<POUCHES> pouches = new ArrayList<>();
            for (POUCHES pouch : POUCHES.values()) {
                if (pouch.enabled) {
                    pouches.add(pouch);
                }
            }
            return pouches.toArray(new POUCHES[pouches.size()]);
        }
    }
}
