package org.etlforge.integration.client.ui.entity.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class DynamicTreeModelUpdater implements Disposable {
   private final DynamicProjectTreeModel dynamicTreeModel = new DynamicProjectTreeModel();
   private final ProjectPropertyChangeSupport changeSupport;

   public DynamicTreeModelUpdater(IntegrationProject var1, IntegrationEntityTreeModel var2) {
      this(var1, null, var2);
   }

   public DynamicTreeModelUpdater(IntegrationProject var1, EntityType var2, IntegrationEntityTreeModel var3) {
      this.dynamicTreeModel.setProject(var1);
      this.dynamicTreeModel.setTreeModel(var3);
      this.changeSupport = new ProjectPropertyChangeSupport(var1, var2, false);
      this.changeSupport.addPropertyChangeListener("Name", new DynamicTreeModelUpdater.NameChangeHandler());
   }

   @Override
   public void dispose() {
      this.dynamicTreeModel.dispose();
      this.changeSupport.dispose();
   }

   private class NameChangeHandler implements PropertyChangeListener {
      private NameChangeHandler() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.updateNodeForEventSource(var1);
      }

      private void updateNodeForEventSource(final PropertyChangeEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               KongaTreeNode var1x = NameChangeHandler.this.findNodeForEventSource(var1);
               if (var1x != null) {
                  DynamicTreeModelUpdater.this.dynamicTreeModel.getTreeModel().nodeChanged(var1x);
               }
            }
         });
      }

      private KongaTreeNode findNodeForEventSource(PropertyChangeEvent var1) {
         Object var2 = var1.getSource();
         return var2 instanceof IntegrationEntity
            ? DynamicTreeModelUpdater.this.dynamicTreeModel.getTreeModel().findNodeForObject((IntegrationEntity)var2)
            : null;
      }
   }
}
