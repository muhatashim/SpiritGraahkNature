/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritgraahknatures.nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import spiritgraahknatures.Data;
import spiritgraahknatures.impls.Condition;
import spiritgraahknatures.methods.Familiar;
import spiritgraahknatures.methods.MyPlayer;
import spiritgraahknatures.methods.Utils;

/**
 *
 * @author VOLT
 */
public class Main extends Node {

    Timer randomPointsUpdater = new Timer(1000 * 60 * 10);

    @Override
    public boolean activate() {
        return Game.isLoggedIn();
    }

    @Override
    public void execute() {
        //Close report players
        if (Widgets.get(594, 17).validate()) {
            System.out.println("Closing out report player interface.");
            Widgets.get(594, 17).click(true);
        }
        if (!MyPlayer.isAtBank()) {
            if ((MyPlayer.isAtAltar() && Data.POUCHES.isOnePouchReadyInInvent()) || Data.POUCHES.isAllReady()
                    || (Inventory.getCount(Data.ESSENCE) > 0 && Data.POUCHES.fillAll(true, false))) {
                if (MyPlayer.isAtAltar()) {
                    SceneObject altar = SceneEntities.getNearest(Data.ALTAR_ID);
                    if (altar.isOnScreen()) {
                        Item item = Inventory.getItem(Data.ESSENCE);
                        if (item != null) {
                            //item.getWidgetChild().interact("Use");
                            boolean clicked = altar.click(true);
                            if (clicked && !Data.POUCHES.isOnePouchReadyInInvent()) {
                                Utils.waitForInventChange();
                                MyPlayer.teleportToBank();
                                return;
                            }
                            if (clicked) {
                                Utils.waitForInventChange();
                            }
                        }
                        while (Data.POUCHES.isOnePouchReadyInInvent() && !Inventory.isFull()) {
                            Data.POUCHES.fillAll(false, false);
                            altar.click(true);
                            Utils.waitForInventChange();
                            break;
                        }
                        if (Data.POUCHES.isOnePouchReadyInInvent()) {
                            Mouse.move(altar.getNextViewportPoint());
                            //Utils.waitForInventChange();
                        }
                    } else {
                        MyPlayer.walk(altar);
                    }
                } else {
                    SceneObject ruins = SceneEntities.getNearest(Data.RUINS_ID);
                    if (ruins != null && ruins.isOnScreen()) {
                        ruins.interact("Enter");
                        Utils.waitFor(new Condition() {
                            @Override
                            public boolean validate() {
                                return SceneEntities.getNearest(Data.ALTAR_ID) != null;
                            }
                        }, 3500);
                    } else {
                        boolean goOn = true;
                        if (!Data.usePots) {
                            SceneObject obby = SceneEntities.getNearest(Data.OBBY);
                            int low = 20;
                            if (!randomPointsUpdater.isRunning()) {
                                low = Random.nextInt(17, 25);
                                randomPointsUpdater.reset();
                            }
                            if (obby != null && Familiar.getPoints() <= low) {
                                if (obby.isOnScreen()) {
                                    if (obby.interact("Renew")) {
                                        goOn = Utils.waitForAnimationChange();
                                    } else {
                                        goOn = false;
                                    }
                                } else {
                                    MyPlayer.walk(Data.OBBY_LOC);
                                    goOn = false;
                                }
                            }
                        }
                        if (goOn) {
                            if (Calculations.distanceTo(Data.RUINS) < 100) {
                                MyPlayer.walk(Data.RUINS);
                            } else {
                                if (!Familiar.teleport()) {
                                    MyPlayer.teleportToBank();
                                }
                            }
                        }
                    }
                }
            } else {
                MyPlayer.teleportToBank();
            }
        } else {
            if (MyPlayer.isReadyToLeaveBank()) {
//                if (Bank.isOpen()) {
//                    Bank.close();
//                }
                Familiar.teleport();
            } else {
                MyPlayer.bank();
            }
        }
    }
}
