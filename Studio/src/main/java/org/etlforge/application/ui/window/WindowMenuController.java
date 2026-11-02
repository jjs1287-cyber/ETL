package org.etlforge.application.ui.window;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuContribution;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.menu.MenuManager;
import org.etlforge.application.ui.window.action.QuickSwitchEditorAction;
import org.etlforge.application.ui.window.action.ShowOpenPagesDialogAction;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class WindowMenuController extends EditorServiceAdapter {
   private static final int MAX_NUMBER_OF_ITEMS = 10;
   private final EditorService editorService;
   private ImmutableMap<Editor, WindowMenuController.EditorMenuItem> menuItems = ImmutableMap.of();
   private final MenuManager menuManager;
   private final JMenuItem allWindowsMenuItem;
   private final JMenuItem quickSwitchMenuItem;
   private MenuContribution contribution;
   private Editor firstEditor;

   public WindowMenuController(ApplicationWindow var1) {
      this.editorService = var1.getEditorService();
      this.menuManager = var1.getMenuManager();
      this.allWindowsMenuItem = new KongaMenuItem(new ShowOpenPagesDialogAction(var1));
      this.quickSwitchMenuItem = new KongaMenuItem(new QuickSwitchEditorAction(this.editorService));
   }

   public void startup() {
      this.editorService.addEditorServiceListener(this);
   }

   public void synchronize() {
      Menu var1 = this.menuManager.getMenu("menu.Windows");
      if (this.contribution != null) {
         var1.removeExtension(MenuExtensionPoint.EXTENSION_POINT_2, this.contribution.getId());
      }

      this.contribution = this.buildContribution();
      var1.addExtension(MenuExtensionPoint.EXTENSION_POINT_2, this.contribution);
      JMenu var2 = this.menuManager.getJMenu("menu.Windows");
      if (var2 != null) {
         var1.populate(var2);
      }
   }

   private MenuContribution buildContribution() {
      MenuContribution var1 = new MenuContribution("Window.OpenEditors");
      var1.addSeparator();
      if (!this.menuItems.isEmpty()) {
         for (JMenuItem var3 : this.menuItems.values()) {
            var1.addItem(var3);
         }

         var1.addSeparator();
      }

      var1.addItem(this.allWindowsMenuItem);
      var1.addItem(this.quickSwitchMenuItem);
      return var1;
   }

   public void shutdown() {
      this.editorService.removeEditorServiceListener(this);
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
      Editor var2 = var1.getEditor();
      if (var2 != null) {
         this.moveEditorFirst(var2);
      }
   }

   @Override
   public void editorClosed(EditorServiceEvent var1) {
      Editor var2 = var1.getEditor();
      if (this.menuItems.containsKey(var2)) {
         this.rebuildMenu();
      }
   }

   @Override
   public void editorOpened(EditorServiceEvent var1) {
      this.moveEditorFirst(var1.getEditor());
   }

   private void moveEditorFirst(Editor var1) {
      if (var1 != this.firstEditor) {
         this.rebuildMenu();
      }
   }

   private void rebuildMenu() {
      this.menuItems = this.rebuildEditorMenuItems();
      this.synchronize();
   }

   private ImmutableMap<Editor, WindowMenuController.EditorMenuItem> rebuildEditorMenuItems() {
      this.firstEditor = null;
      Builder var1 = ImmutableMap.builder();
      Editor[] var2 = this.editorService.getNavigationOrder();
      int var3 = Math.min(10, var2.length);
      int var4 = 0;

      for (Editor var8 : var2) {
         if (var4 == 0) {
            this.firstEditor = var8;
         }

         var1.put(var8, this.createMenuItem(var8, ++var4));
         if (var4 == var3) {
            break;
         }
      }

      return var1.build();
   }

   private WindowMenuController.EditorMenuItem createMenuItem(final Editor var1, int var2) {
      WindowMenuController.EditorMenuItem var3 = new WindowMenuController.EditorMenuItem(var1, var2);
      var3.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  WindowMenuController.this.editorService.openEditor(var1);
               }
            });
         }
      });
      return var3;
   }

   private static class EditorMenuItem extends JMenuItem {
      private final Editor editor;
      private final int count;

      public EditorMenuItem(Editor var1, int var2) {
         super(var1.getTitle());
         this.count = var2;
         this.editor = var1;
         this.setMnemonic();
      }

      private void setMnemonic() {
         if (this.count < 10) {
            this.setMnemonic((char)(48 + this.count));
            this.setDisplayedMnemonicIndex(0);
         } else {
            this.setMnemonic('0');
            this.setDisplayedMnemonicIndex(1);
         }
      }

      @Override
      public String getText() {
         return this.editor == null ? super.getText() : this.count + " " + this.editor.getTitle();
      }

      @Override
      public Icon getIcon() {
         return this.editor != null ? this.editor.getSmallIcon() : super.getIcon();
      }
   }
}
