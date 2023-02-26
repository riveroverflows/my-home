package com.bluuminn.myhome.inventory;

import java.util.ArrayList;

public class Inventory {

    // 인벤토리가 담을 수 있는 총 개수
    private static final int MAX_NUMBER_OF_ITEMS = 10;


    // 인벤토리에 들어있는 아이템 목록
    private ArrayList<ItemEntry> items;

    public Inventory() {
        items = new ArrayList<>(MAX_NUMBER_OF_ITEMS);
        for (int i = 0; i < MAX_NUMBER_OF_ITEMS; i++) {
            items.add(null);
        }
    }

    public boolean add(ItemEntry entry, int cnt) {
        // 해당 아이템이 있는지 검사
        int index = find(entry);
        if (index < 0) { // 아이템이 없다면

            // 인벤토리에 빈자리가 있는지 검사
            boolean ck = isFull();

            // 인벤토리가 꽉찼으면 (ckInventory() == true -> 꽉참을 의미)
            if (ck) {

                System.out.println("인벤토리가 가득 차서 더 이상 아이템을 담을 수 없어요.");
                return false;

            } else {
                // 빈 자리에 추가
                for (int i = 0; i < MAX_NUMBER_OF_ITEMS; i++) {
                    if (items.get(i) == null) {
                        if (entry.entryType.equals("일반")) {
                            items.set(i, new ItemEntry(entry.item, cnt));

                        } else if (entry.entryType.equals("생산")) {
                            items.set(i, new ItemEntry(entry.growthItem, cnt));

                        } else if (entry.entryType.equals("제작")) {
                            items.set(i, new ItemEntry(entry.madeItem, cnt));

                        } else if (entry.entryType.equals("포션")) {
                            items.set(i, new ItemEntry(entry.potion, cnt));
                        } else {
                        }

                        items++;
                        break;
                    }
                }
            }
        } else {    // 아이템이 있다면
            items.get(index).quantity += cnt; // 수량을 cnt만큼 증가
        }
        return true;
    }


//    public boolean addItem(ItemEntry entry, int cnt) {
//
//        // 인벤토리에 빈자리가 있는지 검사
//        boolean ck = ckInventory();
//
//        // 인벤토리가 꽉찼으면 (ckInventory() == true -> 꽉참을 의미)
//        if (ck) {
//
//            System.out.println("인벤토리가 가득 차서 더 이상 아이템을 담을 수 없어요.");
//            return false;
//
//            // 인벤토리에 빈자리가 있으면
//        } else {
//            // 해당 아이템이 있는지 검사
//            int index = findItem(entry);
//            if (index < 0) { // 아이템이 없다면
//                // 빈 자리에 추가
//                for (int i = 0; i < MAX_ITEMS; i++) {
//                    if (itemList.get(i) == null) {
//                        if (entry.entryType.equals("일반")) {
//                            itemList.set(i, new ItemEntry(entry.item, cnt));
//
//                        } else if (entry.entryType.equals("생산")) {
//                            itemList.set(i, new ItemEntry(entry.growthItem, cnt));
//
//                        } else if (entry.entryType.equals("제작")) {
//                            itemList.set(i, new ItemEntry(entry.madeItem, cnt));
//
//                        } else if (entry.entryType.equals("포션")) {
//                            itemList.set(i, new ItemEntry(entry.potion, cnt));
//                        } else {
//                        }
//
//                        items++;
//                        break;
//                    }
//                }
//            } else {    // 아이템이 있다면
//                itemList.get(index).count += cnt; // 수량을 cnt만큼 증가
//            }
//            return true;
//        }
//    }


    //============================== 아이템 개수 감소(0이면 제거) =================================
    // 해당하는 칸의 아이템의 개수를 cnt만큼 감소시킨다
    public boolean removeItem(int index, int cnt) {
        ItemEntry entry = items.get(index);
        if (entry != null) {
            items.get(index).quantity -= cnt;
            if (entry.quantity <= 0) {
                items.remove(index);
                items.add(null);
                items--;
            }
            return true;
        }
        return false;
    }

    public int find(ItemEntry item) {
        for (int i = 0; i < MAX_NUMBER_OF_ITEMS; i++) {
            if (items.get(i) != null && items.get(i).entryName.equals(item.entryName)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isFull() {
        return items.size() >= MAX_NUMBER_OF_ITEMS;
    }

    // ======================= 아이템 객체 반환 ======================
    // 해당하는 인벤토리 칸의 아이템 객체를 반환한다
    public ItemEntry getItem(int index) {
        ItemEntry entry = items.get(index);
        if (entry != null) {
            return entry;
        }
        return null;
    }

    public int getItemIndex(ItemEntry itemEntry) {
        for (int i = 0; i < MAX_NUMBER_OF_ITEMS; i++) {
            if (items.get(i) != null && items.get(i).entryName.equals(itemEntry.entryName)) {
                return i;
            }
        }
        return -1;
    }

    // ==================== 아이템 이름 반환 ===================
    // 해당하는 아이템의 이름을 반환한다
    public String getItemName(ItemEntry item) {
        if (item.entryType.equals("일반")) {
            return item.item.name;
        } else if (item.entryType.equals("생산")) {
            return item.growthItem.name;
        } else if (item.entryType.equals("제작")) {
            return item.madeItem.name;
        } else if (item.entryType.equals("포션")) {
            return item.potion.name;
        } else {
            return null;
        }
    }

    // ==================== 아이템 가격 반환 ===================
    // 해당하는 아이템의 가격을 반환한다
    public int getItemPrice(ItemEntry item) {

        if (item.entryType.equals("일반")) {
            return item.item.price;
        } else if (item.entryType.equals("생산")) {
            return item.growthItem.price;
        } else if (item.entryType.equals("제작")) {
            return item.madeItem.price;
        } else if (item.entryType.equals("포션")) {
            return item.potion.price;
        } else {
            return -1;
        }
    }


    // ============ 해당 인벤토리 한 칸에 들어있는 아이템 개수 반환 ==========
    // 해당하는 칸의 아이템의 개수를 반환한다
    public int getItemCount(int index) {
        ItemEntry entry = items.get(index);
        if (entry != null) {
            return entry.quantity;
        }
        return -1;
    }


    // ================ 인벤토리에 들어있는 전체 아이템 객체 수 반환 =================
    // 인벤토리에 들어가있는 아이템 객체의 개수를 반환한다
    public int getAvailableItems() {
        return items;
    }
}