package com.bluuminn.myhome.character;

import com.bluuminn.myhome.quest.Quest;
import com.bluuminn.myhome.quest.QuestEvent;

import java.util.ArrayList;

public class NPC extends Character {
    static Player tmpPlayer = new Player();

    public ArrayList<Quest> npcQuestList = new ArrayList<>();

    public QuestEvent questEvent = new QuestEvent();

    private NPC(String name) {
        super(name);
    }
}