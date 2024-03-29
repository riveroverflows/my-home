package com.bluuminn.myhome.area;

import com.bluuminn.myhome.character.Player;
import com.bluuminn.myhome.etc.MyHomeUtils;
import com.bluuminn.myhome.harvestgame.BearCatchesFishGame;
import com.bluuminn.myhome.inventory.ItemEntry;
import com.bluuminn.myhome.item.GrowthItem;
import com.bluuminn.myhome.item.ItemStorage;
import com.bluuminn.myhome.timer.CultivateTimer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Forest extends Area {
    private final List<GrowthItem> items;

    public Forest(ItemStorage itemStorage) {
        super("숲");
        items = itemStorage.getForestItems();
    }

    public void growTrees(Player player, Scanner scanner) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        while (true) {
            MyHomeUtils.printLineAsCount(100);
            System.out.println("┌──────────────────────────────────────────────────┐");
            System.out.println("                   " + getName() + "에 도착했어요.");
            System.out.println();

            int playerLevel = player.getLevel();
            for (int i = 0; i < items.size(); i++) {
                GrowthItem item = items.get(i);
                System.out.printf("%-8s", (i + 1) + ". " + item.getName());


                // 아이템의 레벨이 플레이어 레벨과 같거나 작으면 => 재배가능
                if (!item.isPlantable(playerLevel)) {
                    System.out.println("     [ 🔒 ] LV." + item.getLevel() + " 이상");
                    continue;
                }
                if (!item.isPlanted()) {
                    System.out.println(" (재배시간: " + item.getGrowingPeriod() + "초 / 비용: " + item.getCost() + " G)");
                    continue;
                }
                if (item.isHarvestable()) {
                    System.out.println(" (수확 가능)");
                    continue;
                }
                System.out.println(" (기르는중..)");
            }

            MyHomeUtils.printLineAsCount(2);
            System.out.println("얻고 싶은 작물의 번호를 입력하세요. (0. 이전 단계로)");
            System.out.print("입력 >> ");
            String inputValue = MyHomeUtils.input(scanner);
            if (!MyHomeUtils.isInteger(inputValue)) {
                MyHomeUtils.enterAgain(scanner);
                continue;
            }
            int input = MyHomeUtils.stringToInt(inputValue);
            if (input == 0) {
                return;
            }
            if (input >= items.size() || input < 0) {
                MyHomeUtils.enterAgain(scanner);
                continue;
            }

            while (true) {
                GrowthItem item = items.get(input - 1);
                // 레벨이 안되면 아무것도 못한다고 알려주기
                if (!item.isPlantable(playerLevel)) {
                    printNotPlantable();
                    scanner.nextLine();
                    break;
                }
                // 아무것도 안한 상태면 재배시작하기
                if (!item.isPlanted()) {
                    MyHomeUtils.printLineAsCount(100);
                    System.out.println("┌──────────────────────────────────────────────────┐");
                    System.out.println("        자라고 있거나 수확 가능한 아이템이 없습니다.");
                    System.out.println();
                    System.out.println(item.getName() + " 을(를) 기를까요?");
                    System.out.println();
                    System.out.println("1. 기르기        0. 이전 메뉴로 가기");
                    System.out.print("입력 >> ");
                    String subInputValue = MyHomeUtils.input(scanner);
                    if (!MyHomeUtils.isInteger(subInputValue)) {
                        MyHomeUtils.enterAgain(scanner);
                        continue;
                    }
                    int subInput = MyHomeUtils.stringToInt(subInputValue);
                    if (subInput < 0 || 1 < subInput) {
                        MyHomeUtils.enterAgain(scanner);
                        continue;
                    }
                    if (player.getGold() < item.getCost()) {
                        MyHomeUtils.printLineAsCount(100);
                        System.out.println("┌──────────────────────────────────────────────────┐");
                        System.out.println("                   골드가 부족합니다.");
                        scanner.nextLine();
                        continue;
                    }

                    int playerGold = player.getGold();
                    int plantCost = item.getCost();
                    int remainGold = playerGold - plantCost;
                    if (remainGold < 0) {
                        remainGold = 0;
                    }
                    player.updateGold(remainGold);
                    item.plant();
                    new Thread(new CultivateTimer(item)).start();
                    MyHomeUtils.printLineAsCount(100);
                    System.out.println("┌──────────────────────────────────────────────────┐");
                    System.out.println("                  " + item.getName() + " 을(를) 기릅니다.");
                    System.out.println("                다 자라면 알려드릴게요!");
                    System.out.println("└──────────────────────────────────────────────────┘");
                    scanner.nextLine();
                    break;
                }

                // 기다리기
                if (!item.isHarvestable()) {
                    MyHomeUtils.printLineAsCount(100);
                    System.out.println("┌──────────────────────────────────────────────────┐");
                    System.out.println("               아직 " + item.getName() + " 을(를) 기르는 중이에요.");
                    System.out.println("                다 자라면 알려드릴게요!");
                    System.out.println("└──────────────────────────────────────────────────┘");
                    scanner.nextLine();
                    break;
                }
                // 수확가능 한 양을 모두 수확했을 때
                if (item.getHarvestRemainQuantity() < 1) {
                    MyHomeUtils.printLineAsCount(100);
                    item.initHarvestable();
                    System.out.println("┌──────────────────────────────────────────────────┐");
                    System.out.println("            수확할 수 있는 양을 모두 수확했어요.");
                    System.out.println("          " + item.getName() + " 획득량: " + item.getHarvestCount());
                    System.out.println();
                    System.out.println("               이전 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                }
                // 수확하기
                BearCatchesFishGame game = new BearCatchesFishGame();
                game.start(item);
                if (!game.haveWon()) {
                    MyHomeUtils.printLineAsCount(100);
                    break;
                }
                playSound();
                player.saveItem(ItemEntry.of(item, 1));
                item.decreaseHarvestRemainQuantityBy1();
                int exp = player.getExp() + item.getExp();
                player.updateExp(exp);
                player.updateFatigability(player.getFatigability() + 15);

                System.out.println();
                System.out.println("┌──────────────────────────────────────────────────┐");
                System.out.println("              " + item.getName() + " 1개 획득!");
                scanner.nextLine();

                MyHomeUtils.printLineAsCount(100);
            }
        }
    }
}