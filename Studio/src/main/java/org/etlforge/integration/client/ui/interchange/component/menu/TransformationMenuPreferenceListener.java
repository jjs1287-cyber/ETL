package org.etlforge.integration.client.ui.interchange.component.menu;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.etlforge.application.prefs.Preference;
import org.etlforge.application.prefs.PreferenceListener;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.window.ApplicationWindow;

class TransformationMenuPreferenceListener implements PreferenceListener<Boolean> {
   private final ApplicationWindow appWin;
   private final Menu menu;

   public TransformationMenuPreferenceListener(ApplicationWindow var1, Menu var2) {
      this.appWin = var1;
      this.menu = var2;
   }

   public void valueChanged(Preference<Boolean> var1, Boolean var2, final Boolean var3) {
      (new BusyWorker(this.appWin) {
         @Override
         protected void doWork() {
            TransformationMenuPreferenceListener.this.updateWindowMenuManager(var3);
            TransformationMenuPreferenceListener.this.rebuildMenuBar();
         }
      }).run();
   }

   private void updateWindowMenuManager(boolean var1) {
      if (var1) {
         this.addMenu();
      } else {
         this.removeMenu();
      }
   }

   private void rebuildMenuBar() {
      JFrame var1 = this.appWin.getFrame();
      var1.setJMenuBar(this.createNewMenuBar());
   }

   private void addMenu() {
      this.appWin.getMenuManager().addExtension(MenuExtensionPoint.EXTENSION_POINT_1, this.menu, InterchangeMenuDictionary.DICTIONARY);
   }

   private void removeMenu() {
      this.appWin.getMenuManager().removeExtension(MenuExtensionPoint.EXTENSION_POINT_1);
   }

   private JMenuBar createNewMenuBar() {
      JMenuBar var1 = new JMenuBar();

      for (JMenu var3 : this.appWin.getMenuManager().getJMenus()) {
         var1.add(var3);
      }

      return var1;
   }
}
