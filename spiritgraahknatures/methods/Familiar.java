/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritgraahknatures.methods;

import javax.swing.JOptionPane;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.ChatOptions;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.ChatOption;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import spiritgraahknatures.Data;
import spiritgraahknatures.impls.Condition;
<<<<<<< HEAD
import spiritgraahknatures.methods.Utils;
=======
>>>>>>> final

/**
 *
 * @author VOLT
 */
public class Familiar {

    /**
     *
     * @return if the familiar is summoned
     */
    public static boolean isSummoned() {
        return Settings.get(448) != -1;
    }

    public static boolean renew() {
        if (Data.usePots) {
            Item item = Inventory.getItem(Data.SUMMONING_POTS);
            if (item != null) {
                item.getWidgetChild().interact("Drink");
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * summons the familiar
     *
     * @return if there is a graahk pouch in the inventory
     */
    public static boolean summonFamiliar() {
        if (getPoints() == 0) {
            if (!renew()) {
                return false;
            }
        }
        if (Bank.isOpen()) {
            if (!Bank.close()) {
                return false;
            }
        }
        Item item = Inventory.getItem(Data.GRAAHK_POUCH);
        if (item != null) {
            item.getWidgetChild().interact("Summon");
            return true;
        }
        return false;
    }

    public static int getPoints() {
        return Integer.valueOf(Widgets.get(747, 7).getText());
    }

    /**
     *
     * @return if the widget (747, 7) is not null
     */
    private static boolean clickOrb(String action) {
        WidgetChild child = Widgets.get(747, 7);
        if (child != null) {
            child.interact(action);
            if (Utils.random(30)) {
                Utils.moveMouseRandomly(5);
            }
            return true;
        } else {
            System.out.println("Widget 747, 7 is null for some reason");
            return false;
        }
    }

    /**
     *
     * @return if the widget (747, 7) is not null
     */
    public static boolean callFamiliar() {
        return clickOrb("Call Follower");
    }

    /**
     *
     * @return if the widget (747, 7) is not null
     */
    public static boolean interact() {
        return clickOrb("Interact");
    }

    /**
     * Summons the familiar if not summoned. Calls the familiar if it's not
     * within range. Doesn't do anything if it can't summon familiar when the
     * familiar is not summoned.
     *
     * @param location teleports to the location using the spirit graahk
     */
    public static boolean teleport() {
        if (!Familiar.isSummoned() && !Familiar.summonFamiliar()) {
            return false;
        }
        ChatOption option = ChatOptions.getOption("Teleport");
        if (option != null || interact()) {
            Condition c = new Condition() {
                @Override
                public boolean validate() {
                    return ChatOptions.getOptions().size() > 0;
                }
            };
            if (Utils.waitFor(c, Random.nextInt(2000, 2500))) {
                if (option == null) {
                    option = ChatOptions.getOption("Teleport");
                }
                if (option != null) {
                    option.getWidgetChild().click(true);
                    if (Utils.waitForAnimationChange()) {
                        Utils.waitForAnimationChange();
                    } else {
                        teleport();
                    }
                } else {
                    System.out.println("For some reason option is null. Maybe wrong location name?");
                }
            }
        } else {
            if (Familiar.callFamiliar()) {
                teleport();
            }
        }
        return true;
    }
}
