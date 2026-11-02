package com.etlforge.integration.client.ui.admin.scheduler;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import org.etlforge.integration.client.ui.project.info.AbstractTreeFilterControl;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class DeployedSchedulesTreeFilter extends AbstractTreeFilterControl {
   private final JCheckBox hideProjectsSwitch = new KongaCheckBox("&Hide projects with no schedules", true);

   public DeployedSchedulesTreeFilter() {
      this.hideProjectsSwitch.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            DeployedSchedulesTreeFilter.this.update();
         }
      });
      this.setNonTextFilter(new DeployedSchedulesTreeFilter.Filter());
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.hideProjectsSwitch);
   }

   @Override
   public void requestFocus() {
      this.hideProjectsSwitch.requestFocusInWindow();
   }

   private class Filter implements KongaTreeFilter {
      private Filter() {
      }

      @Override
      public boolean apply(KongaTreeNode var1) {
         Object var2 = var1.getUserObject();
         if (var2 instanceof Operation) {
            return this.isParentSchedule(var1);
         } else if (var2 instanceof Folder) {
            return ((Folder)var2).getItemType() == EntityType.Schedule;
         } else {
            return var2 instanceof IntegrationProject && DeployedSchedulesTreeFilter.this.hideProjectsSwitch.isSelected() ? this.hasSchedule(var1) : true;
         }
      }

      private boolean isParentSchedule(KongaTreeNode var1) {
         KongaTreeNode var2 = var1.getParent();
         return var2 != null && var2.getUserObject() instanceof Schedule;
      }

      private boolean hasSchedule(KongaTreeNode var1) {
         for (KongaTreeNode var3 : var1.depthFirstEnumeration()) {
            if (var3.getUserObject() instanceof Schedule) {
               return true;
            }
         }

         return false;
      }
   }
}
