package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.awt.datatransfer.Transferable;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.TreePath;
import org.etlforge.integration.client.ui.entity.TransferableIntegrationEntities;
import org.etlforge.integration.client.ui.entity.move.EntityMover;
import org.etlforge.integration.client.ui.entity.tree.NodeToEntity;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ETLForgeConnectNames;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class TreeTransferHandler extends TransferHandler {
   private static final Predicate<KongaTreeNode> APPLICABLE_NODES = new Predicate<KongaTreeNode>() {
      public boolean apply(KongaTreeNode var1) {
         KongaTreeNode var2 = var1.getParent();
         if (var2 == null) {
            return false;
         }

         Object var3 = var2.getUserObject();
         if (var3 instanceof Operation) {
            return false;
         }

         if (!(var1.getUserObject() instanceof Folder)) {
            return var1.getUserObject() instanceof IntegrationEntity;
         }

         Folder var4 = (Folder)var1.getUserObject();
         return !var4.isEtlforgeConnectFolder() && !ETLForgeConnectNames.isConnectWizardFolder(var4);
      }
   };
   private final CompoundTreeDropStrategy dropStrategies;
   private WeakReference<IntegrationEntity> target;

   public TreeTransferHandler(EntityMover var1) {
      Preconditions.checkNotNull(var1);
      DragAndDropEntityMover var2 = new DragAndDropEntityMover(var1);
      this.dropStrategies = new CompoundTreeDropStrategy(var2);
   }

   public void addDropStrategy(TreeDropStrategy var1) {
      this.dropStrategies.add(var1);
   }

   @Override
   protected Transferable createTransferable(JComponent var1) {
      List var2 = this.getSelectedItems(var1);
      return var2.isEmpty() ? null : new TransferableIntegrationEntities(var2);
   }

   private List<IntegrationEntity> getSelectedItems(JComponent var1) {
      KongaTree var2 = (KongaTree)var1;
      return FluentIterable.from(var2.getSelectedNodes()).filter(APPLICABLE_NODES).transform(new NodeToEntity()).toImmutableList();
   }

   @Override
   public boolean canImport(TransferSupport var1) {
      this.target = null;
      if (!var1.isDataFlavorSupported(TransferableIntegrationEntities.ENTITIES_FLAVOR)) {
         return false;
      }

      try {
         IntegrationEntity var2 = this.getDropTarget(var1);
         if (var2 != null) {
            List var3 = this.getDraggedItems(var1);
            boolean var4 = this.dropStrategies.canDrop(var2, var3);
            if (var4) {
               this.target = new WeakReference<>(var2);
            }

            return var4;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return false;
   }

   private List<IntegrationEntity> getDraggedItems(TransferSupport var1) {
      try {
         Transferable var2 = var1.getTransferable();
         return (List<IntegrationEntity>)var2.getTransferData(TransferableIntegrationEntities.ENTITIES_FLAVOR);
      } catch (Exception var3) {
         var3.printStackTrace();
         return Collections.emptyList();
      }
   }

   private IntegrationEntity getDropTarget(TransferSupport var1) {
      JTree.DropLocation var2 = (JTree.DropLocation)var1.getDropLocation();
      ((KongaTree)var1.getComponent()).autoscroll(var2.getDropPoint());
      TreePath var3 = var2.getPath();
      if (var3 == null) {
         return null;
      }

      IntegrationEntityTreeNode var4 = this.getTargetNode(var3);
      return var4 == null ? null : var4.getUserObject();
   }

   private IntegrationEntityTreeNode getTargetNode(TreePath var1) {
      Object var2 = var1.getLastPathComponent();
      return var2 instanceof IntegrationEntityTreeNode ? (IntegrationEntityTreeNode)var2 : null;
   }

   @Override
   public boolean importData(TransferSupport var1) {
      if (this.target == null) {
         return false;
      }

      try {
         if (!var1.isDataFlavorSupported(TransferableIntegrationEntities.ENTITIES_FLAVOR)) {
            return false;
         } else {
            IntegrationEntity var2 = this.target.get();
            if (var2 != null) {
               List var3 = this.getDraggedItems(var1);
               return this.dropStrategies.handleDrop(var2, var3);
            } else {
               return false;
            }
         }
      } finally {
         this.target = null;
      }
   }

   @Override
   public int getSourceActions(JComponent var1) {
      return 3;
   }
}
