package org.etlforge.tools.xml.xmlviewer.ui;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.etlforge.ui.util.opensave.OpenAndSaveManager;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.KongaMenuItem;

class FileMenu extends KongaMenu {
   private final OpenAndSaveManager openAndSaveManager;
   private final MainWindow mainWindow;
   private final Action openAction;
   private final Action saveAction;
   private final Action saveAsAction;
   private final Action saveAllAction;
   private final Action closeAllAction;
   private final Action exitAction;

   public FileMenu(OpenAndSaveManager var1, MainWindow var2) {
      super("&File");
      this.openAndSaveManager = var1;
      this.mainWindow = var2;
      this.openAction = var1.getOpenAction();
      this.saveAction = var1.getSaveAction();
      this.saveAction.setEnabled(false);
      this.saveAsAction = var1.getSaveAsAction();
      this.saveAsAction.setEnabled(false);
      this.saveAllAction = new FileMenu.SaveAllAction();
      this.saveAllAction.setEnabled(false);
      this.closeAllAction = new FileMenu.CloseAllAction();
      this.closeAllAction.setEnabled(false);
      this.exitAction = new FileMenu.ExitAction();
      this.createMenuItems();
   }

   public void createMenuItems() {
      this.add(new KongaMenuItem(this.openAction));
      this.add(new KongaMenuItem(this.saveAction));
      this.add(new KongaMenuItem(this.saveAsAction));
      this.add(new KongaMenuItem(this.saveAllAction));
      this.addSeparator();
      this.add(new KongaMenuItem(this.closeAllAction));
      this.addSeparator();
      List var1 = this.openAndSaveManager.getRecentFileMenuItems();
      if (var1 != null && !var1.isEmpty()) {
         for (Object __etlItem50 : var1) {
            JMenuItem var3 = (JMenuItem) __etlItem50;
            this.add(var3);
         }

         this.addSeparator();
      }

      this.add(new KongaMenuItem(this.exitAction));
   }

   public void refresh() {
      this.removeAll();
      this.setSaveEnabled(this.mainWindow.getDesktop().getAllFrames().length > 0);
      this.createMenuItems();
   }

   public void setSaveEnabled(boolean var1) {
      this.saveAction.setEnabled(var1);
      this.saveAsAction.setEnabled(var1);
      this.saveAllAction.setEnabled(var1);
      this.closeAllAction.setEnabled(var1);
   }

   private class CloseAllAction extends AbstractAction {
      public CloseAllAction() {
         super("Close All");
         this.putValue("ShortDescription", "Close All Open Documents");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         FileMenu.this.mainWindow.closeAll();
         this.setEnabled(false);
         FileMenu.this.saveAction.setEnabled(false);
         FileMenu.this.saveAsAction.setEnabled(false);
      }
   }

   private class ExitAction extends AbstractAction {
      public ExitAction() {
         super("E&xit");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(81, 2));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         FileMenu.this.mainWindow.onExit(0);
      }
   }

   private class SaveAllAction extends AbstractAction {
      public SaveAllAction() {
         super("Sav&e All");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         FileMenu.this.mainWindow.saveAll();
      }
   }
}
