/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritgraahknatures;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;
import spiritgraahknatures.nodes.Main;
import spiritgraahknatures.nodes.Paint;
import spiritgraahknatures.nodes.WickedPouchRepair;

/**
 *
 * @author VOLT
 */
@Manifest(
authors = "ExKALLEBur",
name = "Spirit Graahk Natures ExKALLEBur",
description = "Gets them natures for you.",
topic = 829368,
version = 2.443)
public class SpiritGraahkNatures extends ActiveScript implements PaintListener {

    private static final List<Node> nodes = new ArrayList<>();
    public static ActiveScript activescript = null;
    public static boolean started = false;
    public static GUI gui;

    public void startup() {
//        JOptionPane.showMessageDialog(null, "The script is not completely finished, but it should work. If you want this to be finished, please pm Frontier on the forums.");
        activescript = this;
        nodes.add(new WickedPouchRepair());
        nodes.add(new Main());
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    gui = new GUI();
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(SpiritGraahkNatures.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (gui == null) {
            Task.sleep(10);
        }
        gui.setVisible(true);
        gui.setLocationRelativeTo(null);
        while (gui.isVisible()) {
            Task.sleep(10);
        }
        Mouse.setSpeed(Mouse.Speed.VERY_FAST);
        Item nat = Inventory.getItem(Data.NATURE);
        if (nat != null) {
            Paint.lastCountNats = nat.getStackSize();
        }
    }

    @Override
    public int loop() {
        try {
            if (started) {
                for (final Node node : nodes) {
                    if (node.activate()) {
                        node.execute();
                        break;
                    }
                }
                Paint.update();
            } else {
                startup();
            }
        } catch (Exception e) {
        }
        return 1;
    }

    @Override
    public void onRepaint(Graphics g) {
        appendInfo(new String[]{
                    "Paint is NOT FINISHED! Please do not post about this!",
                    "Profit hr: " + Paint.profitHr,
                    "Profit: " + Paint.profit,
                    "Nats made: " + Paint.natsMade,
                    "Nats pr hr: " + Paint.natsPrHr,
                    "Nats price: " + Paint.natPrice,
                    "Time ran: " + Paint.start.toElapsedString(),
                    "Setting: " + Integer.toBinaryString(Settings.get(Data.POUCH_COUNT_SETTING))}, g);
    }

    private void appendInfo(String[] strs, Graphics g) {
        int y = 100;
        for (String str : strs) {
            g.drawString(str, 10, y += 13);
        }
    }
}
