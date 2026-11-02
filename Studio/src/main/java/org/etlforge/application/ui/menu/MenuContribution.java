package org.etlforge.application.ui.menu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;

public final class MenuContribution {
   private String id;
   private final List<JMenuItem> menuItems;

   public MenuContribution(String var1) {
      this.id = var1;
      this.menuItems = new ArrayList<>();
   }

   public String getId() {
      return this.id;
   }

   public List<JMenuItem> getMenuItems() {
      return new ArrayList<>(this.menuItems);
   }

   public void addItem(JMenuItem var1) {
      this.menuItems.add(var1);
   }

   public void addSeparator() {
      this.menuItems.add(null);
   }

   public int size() {
      return this.menuItems.size();
   }
}
