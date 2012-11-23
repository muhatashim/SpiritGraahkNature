/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritgraahknatures.methods;

import java.awt.Point;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
<<<<<<< HEAD
=======
import org.powerbot.game.api.wrappers.Tile;
>>>>>>> final
import spiritgraahknatures.SpiritGraahkNatures;
import spiritgraahknatures.impls.Condition;

/**
 *
 * @author VOLT
 */
public class Utils {

    /**
     *
     * @param timeout how much time in ms to sleep for
     */
    public static void sleep(int timeout) {
        Task.sleep(timeout);
    }

    /**
     *
     * @param minTime minimum time in ms to sleep for
     * @param maxTime maximum time in ms to sleep for
     */
    public static void sleep(int minTime, int maxTime) {
        SpiritGraahkNatures.sleep(maxTime, maxTime);
    }

    /**
     * waits for the location of the player to change
     */
    public static boolean waitForLocChange() {
        final Tile start = Players.getLocal().getLocation();
        return waitFor(new Condition() {
            @Override
            public boolean validate() {
                System.out.println(Players.getLocal().getLocation());
                return Calculations.distanceTo(start) > 1;
            }
        }, 5000);
    }

    /**
     * waits for an animation change
     */
    public static boolean waitForAnimationChange() {
        final int start = Players.getLocal().getAnimation();
        return waitFor(new Condition() {
            @Override
            public boolean validate() {
                return Players.getLocal().getAnimation() != start;
            }
        }, 5000);
    }

    /**
     * waits for an inventory change
     */
    public static void waitForInventChange() {
        final int start = Inventory.getCount();
        Utils.waitFor(new Condition() {
            @Override
            public boolean validate() {
                return Inventory.getCount() != start;
            }
        }, 2000);
    }

    /**
     *
     * @param c condition to wait for
     * @param timeout maximum time to wait
     * @return if condition returned true
     */
    public static boolean waitFor(Condition c, int timeout) {
        Timer t = new Timer(timeout);
        boolean validated = false;
        while (!(validated = c.validate()) && t.isRunning()) {
            sleep(10);
        }
        return validated;
    }

    /**
     *
     * @param percentage the minimum amount to return true
     * @return if the random is less than the percentage
     */
    public static boolean random(int percentage) {
        return Random.nextInt(0, 101) < percentage;
    }

    /**
     * @param radius amount to move
     */
    public static void moveMouseRandomly(int radius) {
        Point p = Mouse.getLocation();
        p.translate(Random.nextInt(-radius, radius), Random.nextInt(-radius, radius));
        Mouse.move(p);
    }
}
