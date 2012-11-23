/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritgraahknatures.nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.tab.Inventory;
import spiritgraahknatures.Data;
import spiritgraahknatures.methods.Familiar;
import spiritgraahknatures.methods.MyPlayer;

/**
 *
 * @author VOLT
 */
public class WickedPouchRepair extends Node {

    @Override
    public boolean activate() {
        return Data.repairingMethod == Data.ReparingMethod.WICKED_HOOD && Data.POUCHES.isOneDeprecated() && !MyPlayer.isAtBank() && Inventory.containsAll(Data.WICKED_HOOD) || Calculations.distanceTo(Data.KORVAK_LOC) < 45;
    }

    @Override
    public void execute() {
        if (Data.repairingMethod == Data.ReparingMethod.WICKED_HOOD && Data.POUCHES.isOneDeprecated() && !MyPlayer.isAtBank()) {
            MyPlayer.repairUsingWicked();
            return;
        }
        if (!MyPlayer.teleportToBank()) {
            Familiar.teleport();
        }
    }
}
