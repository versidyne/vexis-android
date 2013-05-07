package com.versidyne.vexis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentMap {
	
    public static class DummyItem {
    	
        public String id;
        public String content;
        public String data;
        
        public DummyItem(String id, String content, String data) {
            this.id = id;
            this.content = content;
            this.data = data;
        }
        
        @Override
        public String toString() {
            return content;
        }
    }
    
    // make this portion inherit the dummy and add more to the specifications
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    
    static {
        //addItem(new DummyItem("1", "Account", "This is where a user will be able to edit their account."));
        addItem(new DummyItem("2", "Associates", "This is where a user will be able to set up who they're associated with."));
        addItem(new DummyItem("3", "Device", "This is where a user will be able to associate their MEID & SIM with their account."));
        //addItem(new DummyItem("4", "Email", "This is where a user will be able to set up email accounts."));
        addItem(new DummyItem("5", "Location", "This is where all GPS data will be displayed."));
        addItem(new DummyItem("6", "Messaging", "This is where the a user will be able to message other users."));
        //addItem(new DummyItem("7", "Notifications", "This is where a user will be able to set their notification preferences."));
        //addItem(new DummyItem("8", "Profile", "This is where all profile and account information will be edited."));
        //addItem(new DummyItem("9", "Wifi Hotspot", "This is where a user will be able to turn their phone into a mobile Wifi hotspot."));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
