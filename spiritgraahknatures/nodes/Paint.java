/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritgraahknatures.nodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import spiritgraahknatures.Data;

/**
 *
 * @author VOLT
 */
public class Paint {

    public static Timer start = new Timer(0);
    public static float profitHr = -1;
    public static int profit = 0;
    public static int natsMade = 0;
    public static float natsPrHr = -1;
    public static int natPrice = 0;
    public static int lastCountNats = 0;

    public static void update() {
        if (natPrice == 0) {
            int price = updateNatPrice();
            if (price != -1) {
                natPrice = price;
            }
        }
        Item item = Inventory.getItem(Data.NATURE);
        if (item != null) {
            int currStack = item.getStackSize();
            int diff = currStack - lastCountNats;
            if (diff > 0) {
                lastCountNats = currStack;
                natsMade += diff;
                updateCalculations();
            } else if (diff < 0) {
                lastCountNats = 0;
            }
        }
    }

    private static void updateCalculations() {
        float prHr = ((1000 * 60 * 60) / start.getElapsed());
        natsPrHr = (natsMade * prHr);
        profitHr = natsPrHr * natPrice;
        profit = natsMade * natPrice;
    }

    private static int updateNatPrice() {
        try {
            URL url = new URL("http://scripts.audefy.com/l/" + Data.NATURE);
            InputStream in_stream = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in_stream));
            int price = Integer.parseInt(reader.readLine());
            return ((price == 0) ? -1 : price);
        } catch (IOException | NumberFormatException e) {
        }
        return -1;
    }
}
