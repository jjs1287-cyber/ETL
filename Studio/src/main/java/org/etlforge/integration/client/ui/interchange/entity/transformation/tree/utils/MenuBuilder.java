package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Stack;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public class MenuBuilder {
   private final Stack<JMenu> m_menu_stack;
   private JMenuBar m_menu_bar;
   private JPopupMenu m_popup_menu;
   private final ActionListener m_handler;
   private int width = 100;
   private FontMetrics fm;
   private List<String> commands;
   private int nCmd;
   private JMenuItem cmd;

   public MenuBuilder(ActionListener var1, JMenuBar var2) {
      this.m_menu_stack = new Stack<>();
      this.m_menu_bar = var2;
      this.m_handler = var1;
   }

   public MenuBuilder(ActionListener var1, JPopupMenu var2) {
      this.m_menu_stack = new Stack<>();
      this.m_popup_menu = var2;
      this.m_popup_menu.removeAll();
      this.m_handler = var1;
      this.fm = var2.getFontMetrics(var2.getFont());
   }

   public JMenu addMenu(String var1, JMenu var2) {
      JMenu var3 = new JMenu(var1);

      while (!this.m_menu_stack.empty() && this.m_menu_stack.peek() != var2) {
         this.m_menu_stack.pop();
      }

      if (this.m_menu_stack.empty()) {
         if (this.m_menu_bar != null) {
            this.m_menu_bar.add(var3);
         } else {
            this.m_popup_menu.add(var3);
            int var4 = this.fm.stringWidth(var1);
            if (var4 > this.width) {
               this.width = var4;
            }
         }
      } else {
         var2.add(var3);
      }

      this.m_menu_stack.push(var3);
      return var3;
   }

   public void endOfCurrentMenu() {
      if (!this.m_menu_stack.empty()) {
         this.m_menu_stack.pop();
      }
   }

   public JMenu addMenu(String var1) {
      return this.addMenu(var1, null);
   }

   public void addAction(Action var1) {
      this.addMenuItemImpl(new KongaMenuItem(var1));
   }

   public void addMenuItem(JMenuItem var1, boolean var2) {
      this.addMenuItemImpl(var1);
      var1.addActionListener(this.m_handler);
      var1.setEnabled(var2);
   }

   private void addMenuItemImpl(JMenuItem var1) {
      if (this.m_menu_stack.empty()) {
         if (this.m_menu_bar != null) {
            this.m_menu_bar.add(var1);
         } else {
            this.m_popup_menu.add(var1);
            this.nCmd++;
            this.cmd = var1;
         }
      } else {
         this.m_menu_stack.peek().add(var1);
      }
   }

   public JMenuItem addMenuItem(String var1, boolean var2) {
      JMenuItem var3 = new JMenuItem(var1);
      this.addMenuItem(var3, var2);
      return var3;
   }

   public JMenuItem addMenuItem(String var1) {
      return this.addMenuItem(var1, true);
   }

   public void addRadioButtonMenuItem(String var1, boolean var2) {
      if (this.commands == null) {
         this.commands = Lists.newArrayList();
      }

      String var3 = (var2 ? "T" : "F") + var1;
      this.commands.add(var3);
      this.nCmd++;
   }

   public void endRadioButtonMenuItem() {
      ButtonGroup var1 = new ButtonGroup();

      for (String var3 : this.commands) {
         boolean var4 = var3.startsWith("T");
         String var5 = var3.substring(1);
         JRadioButtonMenuItem var6 = new JRadioButtonMenuItem(var5, var4);
         var1.add(var6);
         this.addMenuItem(var6, true);
      }

      this.commands = null;
   }

   public void addMenuSeparator() {
      if (!this.m_menu_stack.empty()) {
         this.m_menu_stack.peek().addSeparator();
      } else if (this.m_popup_menu != null) {
         this.m_popup_menu.addSeparator();
      }
   }

   public void show(Component var1, int var2, int var3) {
      if (this.m_popup_menu != null) {
         int var4 = this.m_popup_menu.getComponentCount();
         if (var4 == 0) {
            return;
         }

         this.m_popup_menu.show(var1, var2, var3);
      }
   }

   public JMenuItem getSingleCmd() {
      return this.nCmd == 1 && this.m_menu_bar == null && this.m_popup_menu != null ? this.cmd : null;
   }
}
