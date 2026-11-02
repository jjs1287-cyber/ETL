package org.etlforge.integration.client.ui.interchange.core.action.popup;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class OpenPageItemFactory extends AbstractPopupMenuItemFactory {
   @Override
   protected PopupMenuItem createItem() {
      return new OpenPageItemFactory.Item();
   }

   private static class ActionImpl extends AbstractAction {
      private List<IntegrationEntity> entitiesToOpen;

      public ActionImpl() {
         ActionDecorators.decorateFromDictionary(this, InterchangeActionDictionary.DICTIONARY, "action.ic.OpenPage");
      }

      public void setEntitiesToOpen(List<IntegrationEntity> var1) {
         this.entitiesToOpen = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.entitiesToOpen != null) {
            for (IntegrationEntity var3 : this.entitiesToOpen) {
               UiCompanion.of(var3).openPage();
            }
         }
      }
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item() {
         super(new OpenPageItemFactory.ActionImpl());
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         boolean var3 = this.isVisibleFor(var1, var2);
         boolean var4 = var3 && this.getEnabledState(var2);
         this.setVisible(var3);
         this.setEnabled(var4);
      }

      public boolean isVisibleFor(PopupMenuSite var1, List<?> var2) {
         if (var1.isTree()) {
            for (Object var4 : var2) {
               if (var4 instanceof IntegrationEntity && !(var4 instanceof IntegrationProject) && !(var4 instanceof Folder)) {
                  return true;
               }
            }
         }

         return false;
      }

      public boolean getEnabledState(List<?> var1) {
         ArrayList var2 = null;

         for (Object var4 : var1) {
            if (var4 instanceof IntegrationEntity && !(var4 instanceof IntegrationProject) && !(var4 instanceof Folder)) {
               if (var2 == null) {
                  var2 = Lists.newArrayList();
               }

               var2.add((IntegrationEntity)var4);
            }
         }

         ((OpenPageItemFactory.ActionImpl)this.getAction()).setEntitiesToOpen(var2);
         return var2 != null;
      }
   }
}
