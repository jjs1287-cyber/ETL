package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.resources.InterchangeResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.util.KeyUtils;

public final class RenameNodeItemFactory extends AbstractPopupMenuItemFactory {
   private final ProjectContentViewerManager contentViewers;

   public RenameNodeItemFactory(ProjectContentViewerManager var1) {
      this.contentViewers = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new RenameNodeItemFactory.Item(new RenameNodeItemFactory.ActionImpl());
   }

   private final class ActionImpl extends AbstractAction {
      private WeakReference<IntegrationEntity> entityRef;

      public ActionImpl() {
         super(InterchangeResources.getString("Rename"));
         this.putValue("AcceleratorKey", KeyUtils.F2);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.entityRef != null && this.entityRef.get() != null) {
            IntegrationEntity var2 = this.entityRef.get();
            if (var2 != null) {
               ProjectContentViewer var3 = RenameNodeItemFactory.this.contentViewers.getViewer();
               var3.renameEntity(var2);
            }
         }
      }

      public void setEntity(IntegrationEntity var1) {
         this.entityRef = new WeakReference<>(var1);
         this.setEnabled(var1 != null && var1.isRenameable());
      }
   }

   private static final class Item extends AbstractActionBasedPopupMenuItem {
      public Item(RenameNodeItemFactory.ActionImpl var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         this.setVisible(this.isVisibleFor(var1, var2));
         this.setEnabled(this.isVisible() && this.getAction().isEnabled());
      }

      private boolean isVisibleFor(PopupMenuSite var1, List<?> var2) {
         IntegrationEntity var3 = null;
         if (var1.isTree()) {
            var3 = this.getEntityForTree(var2);
         } else {
            var3 = this.getEntityForNonTree(var2);
         }

         ((RenameNodeItemFactory.ActionImpl)this.getAction()).setEntity(var3);
         return var3 != null;
      }

      private IntegrationEntity getEntityForTree(List<?> var1) {
         for (Object var3 : var1) {
            if (var3 instanceof IntegrationEntityTreeNode) {
               IntegrationEntityTreeNode var4 = (IntegrationEntityTreeNode)var3;
               if (var4.isRenamable()) {
                  return var4.getUserObject();
               }
            } else if (var3 instanceof IntegrationEntity) {
               IntegrationEntity var5 = (IntegrationEntity)var3;
               if (var5.isRenameable()) {
                  return var5;
               }
            }
         }

         return null;
      }

      private IntegrationEntity getEntityForNonTree(List<?> var1) {
         if (var1.size() == 1) {
            Object var2 = var1.get(0);
            if (var2 instanceof IntegrationProject) {
               return (IntegrationProject)var2;
            }
         }

         return null;
      }
   }
}
