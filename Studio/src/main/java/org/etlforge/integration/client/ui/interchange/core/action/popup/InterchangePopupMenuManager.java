package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPopupMenu;
import org.etlforge.application.DebugMode;
import org.etlforge.application.ui.action.popup.DefaultPopupMenuManager;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.action.popup.SectionFactories;
import org.etlforge.integration.client.ui.interchange.core.action.EntityDebugActions;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class InterchangePopupMenuManager extends DefaultPopupMenuManager {
   private final EntityDebugActions debugActions = new EntityDebugActions();

   public InterchangePopupMenuManager() {
      this(new ArrayList<>());
   }

   public InterchangePopupMenuManager(List<SectionFactories> var1) {
      for (SectionFactories var3 : var1) {
         this.addFactories(var3);
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            InterchangePopupMenuManager.this.preloadMenuItems();
         }
      });
   }

   @Override
   public JPopupMenu getPopupMenu(PopupMenuSite var1, List<?> var2) {
      JPopupMenu var3 = super.getPopupMenu(var1, var2);
      if (this.isDebugActionsApplicable(var2)) {
         boolean var4 = true;
         if (var3 == null) {
            var3 = new KongaPopupMenu();
            var4 = false;
         }

         this.addDebugMenuTo(var3, var4);
      }

      return var3;
   }

   private boolean isDebugActionsApplicable(List<?> var1) {
      return DebugMode.IS_ENABLED && var1.size() == 1 && var1.get(0) instanceof IntegrationEntity;
   }

   private void addDebugMenuTo(JPopupMenu var1, boolean var2) {
      if (var2) {
         var1.addSeparator();
      }

      KongaMenu var3 = new KongaMenu("Debug");
      this.debugActions.addToMenu(var3);
      var1.add(var3);
   }
}
