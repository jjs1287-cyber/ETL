package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import org.etlforge.application.prefs.Preference;
import org.etlforge.application.prefs.PreferenceListener;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuContribution;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.menu.MenuManager;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorHistoryPoint;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.RecentlyClosedEditorsPreference;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.MenuAdapter;
import org.etlforge.ui.widget.menu.MenuDisabler;

public final class RecentlyClosedEditors implements Disposable {
   private final EditorService editorService;
   private final LinkedList<EditorHistoryPoint> points = Lists.newLinkedList();
   private final JMenu menu = new KongaMenu("&Recently Closed");
   private final RecentlyClosedEditors.EditorListener editorListener = new RecentlyClosedEditors.EditorListener();

   public RecentlyClosedEditors(EditorService var1) {
      this.editorService = var1;
      var1.addEditorServiceListener(this.editorListener);
      MenuDisabler.ignore(this.menu);
      this.menu.addMenuListener(new RecentlyClosedEditors.MenuListenerImpl());
      RecentlyClosedEditorsPreference.INSTANCE.addListener(new RecentlyClosedEditors.PreferenceListenerImpl());
      this.checkEnabledState();
   }

   private void checkEnabledState() {
      this.menu.setEnabled(RecentlyClosedEditorsPreference.INSTANCE.get() > 0);
   }

   public MenuContribution getMenuContribution() {
      MenuContribution var1 = new MenuContribution("Windows.RecentlyClosed");
      var1.addItem(this.menu);
      return var1;
   }

   public void contributeToWindowsMenu(ApplicationWindow var1) {
      MenuManager var2 = var1.getMenuManager();
      Menu var3 = var2.getMenu("menu.Windows");
      MenuContribution var4 = this.getMenuContribution();
      var3.addExtension(MenuExtensionPoint.EXTENSION_POINT_1, var4);
   }

   public Action getReOpenLastClosedAction() {
      return new RecentlyClosedEditors.ReOpenLastClosedAction();
   }

   @Override
   public void dispose() {
      this.editorService.removeEditorServiceListener(this.editorListener);
   }

   private void checkSizeLimit() {
      int var1 = RecentlyClosedEditorsPreference.INSTANCE.get();

      while (this.points.size() > var1) {
         this.points.removeLast().dispose();
      }
   }

   private final class EditorListener extends EditorServiceAdapter {
      private EditorListener() {
      }

      @Override
      public void editorClosed(EditorServiceEvent var1) {
         EditorHistoryPoint var2 = var1.getEditor().getHistoryPoint();
         if (var2 != null) {
            this.onEditorClosed(var2);
         }
      }

      private void onEditorClosed(EditorHistoryPoint var1) {
         this.removePoint(var1);
         RecentlyClosedEditors.this.points.addFirst(var1);
         this.removeDisposedPoints();
         RecentlyClosedEditors.this.checkSizeLimit();
      }

      private void removePoint(EditorHistoryPoint var1) {
         for (EditorHistoryPoint var3 : RecentlyClosedEditors.this.points) {
            if (var3 == var1) {
               RecentlyClosedEditors.this.points.remove(var3);
               break;
            }

            if (var3.getId().equals(var1.getId())) {
               RecentlyClosedEditors.this.points.remove(var3);
               var3.dispose();
               break;
            }
         }
      }

      private void removeDisposedPoints() {
         Iterator var1 = RecentlyClosedEditors.this.points.iterator();

         while (var1.hasNext()) {
            if (((EditorHistoryPoint)var1.next()).isDisposed()) {
               var1.remove();
            }
         }
      }

      @Override
      public void editorOpened(EditorServiceEvent var1) {
         EditorHistoryPoint var2 = var1.getEditor().getHistoryPoint();
         if (var2 != null) {
            this.onEditorOpened(var2);
         }
      }

      private void onEditorOpened(EditorHistoryPoint var1) {
         this.removePoint(var1);
         this.removeDisposedPoints();
      }
   }

   private final class MenuListenerImpl extends MenuAdapter {
      private MenuListenerImpl() {
      }

      @Override
      public void menuSelected(MenuEvent var1) {
         this.buildMenu();
      }

      private void buildMenu() {
         RecentlyClosedEditors.this.menu.removeAll();
         this.addPointMenuItems();
         if (RecentlyClosedEditors.this.menu.getItemCount() == 0) {
            this.addEmptyMenuItem();
         }
      }

      private void addEmptyMenuItem() {
         JMenuItem var1 = RecentlyClosedEditors.this.menu.add("(none)");
         var1.setEnabled(false);
      }

      private void addPointMenuItems() {
         boolean var1 = true;

         for (EditorHistoryPoint var3 : RecentlyClosedEditors.this.points) {
            if (var3.isAvailable()) {
               RecentlyClosedEditors.ReOpenAction var4 = RecentlyClosedEditors.this.new ReOpenAction(var3);
               if (var1) {
                  var4.putValue("AcceleratorKey", KeyUtils.getCtrlShiftDown(84));
               }

               RecentlyClosedEditors.this.menu.add(var4);
               var1 = false;
            }
         }
      }
   }

   private final class PreferenceListenerImpl implements PreferenceListener<Integer> {
      private PreferenceListenerImpl() {
      }

      public void valueChanged(Preference<Integer> var1, Integer var2, Integer var3) {
         RecentlyClosedEditors.this.checkSizeLimit();
         RecentlyClosedEditors.this.checkEnabledState();
      }
   }

   private final class ReOpenAction extends AbstractAction {
      private final EditorHistoryPoint point;

      public ReOpenAction(EditorHistoryPoint var2) {
         super(ApplicationResources.MNEMONIC_FACTORY.escape(var2.getDisplayString()), var2.getIcon());
         this.point = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ReOpenAction.this.point.restore(RecentlyClosedEditors.this.editorService);
            }
         });
      }
   }

   private final class ReOpenLastClosedAction extends AbstractAction {
      public ReOpenLastClosedAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlShiftDown(84));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         for (EditorHistoryPoint var3 : RecentlyClosedEditors.this.points) {
            if (var3.isAvailable()) {
               var3.restore(RecentlyClosedEditors.this.editorService);
               break;
            }
         }
      }
   }
}
