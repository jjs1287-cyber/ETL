package org.etlforge.integration.client.ui.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.awt.Component;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.integration.data.validation.ValidationMessage;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class EntityValidationTreeDisplayer implements UiProvider {
   private final KongaTree tree;
   private boolean includeRoot;

   public EntityValidationTreeDisplayer() {
      DefaultKongaTreeModel var1 = emptyModel();
      this.tree = this.createTree(var1);
   }

   private static DefaultKongaTreeModel emptyModel() {
      return new DefaultKongaTreeModel(new DefaultKongaTreeNode("", false));
   }

   public void setIncludeRoot(boolean var1) {
      this.includeRoot = var1;
   }

   public void clear(String var1) {
      DefaultKongaTreeModel var2;
      if (StringUtils.isEmpty(var1)) {
         var2 = emptyModel();
      } else {
         DefaultKongaTreeNode var3 = new DefaultKongaTreeNode("", true);
         var3.addChild(new DefaultKongaTreeNode(var1, false));
         var2 = new DefaultKongaTreeModel(var3);
      }

      this.updateTreeWithNewModel(var2);
   }

   public void display(EntityCollectionValidationResult var1) {
      KongaTreeModel var2 = this.createTreeModel(var1);
      this.updateTreeWithNewModel(var2);
   }

   private void updateTreeWithNewModel(final KongaTreeModel var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            EntityValidationTreeDisplayer.this.tree.setModel(var1);
            EntityValidationTreeDisplayer.this.tree.expandAll();
         }
      });
   }

   private KongaTreeModel createTreeModel(EntityCollectionValidationResult var1) {
      EntityValidationTreeDisplayer.ModelBuilder var2 = new EntityValidationTreeDisplayer.ModelBuilder();
      return var2.buildTreeModel(var1, this.includeRoot);
   }

   private KongaTree createTree(KongaTreeModel var1) {
      KongaTree var2 = new KongaTree(var1);
      var2.setRowHeight(0);
      this.customizeTree(var2);
      return var2;
   }

   private void customizeTree(KongaTree var1) {
      var1.setRootVisible(false);
      var1.setShowsRootHandles(true);
      var1.setDragSelectionEnabled(false);
      var1.setCellRenderer(new EntityValidationTreeDisplayer.Renderer());
      var1.expandAll();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.createDisplayer());
   }

   private JComponent createDisplayer() {
      return new JScrollPane(this.tree);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   private static class EntityNodeObject {
      public final IntegrationEntity entity;
      public final ValidationStatus status;

      public EntityNodeObject(IntegrationEntity var1, ValidationStatus var2) {
         this.entity = var1;
         this.status = var2;
      }

      @Override
      public String toString() {
         return this.entity.getName();
      }
   }

   private static class ModelBuilder {
      private IntegrationEntity mainEntity;
      private boolean includeRoot;
      private EntityCollectionValidationResult result;
      private final Map<IntegrationEntity, KongaTreeNode> existingEntityNodes = Maps.newHashMap();

      private ModelBuilder() {
      }

      public KongaTreeModel buildTreeModel(EntityCollectionValidationResult var1, boolean var2) {
         this.mainEntity = var1.getRoot().orNull();
         this.result = var1;
         this.includeRoot = var2;
         DefaultKongaTreeNode var3 = new DefaultKongaTreeNode("Validation", true);
         this.insertFirstHandValidationProblems(var3);
         this.insertSecondHandValidationProblems(var3);
         return new DefaultKongaTreeModel(var3);
      }

      private void insertFirstHandValidationProblems(DefaultKongaTreeNode var1) {
         if (this.mainEntity != null) {
            ValidationResult var2 = this.result.getValidationResult(this.mainEntity);
            if (!var2.isEmpty()) {
               if (this.includeRoot) {
                  this.addEntityNode(this.result, this.mainEntity, var1);
               } else {
                  this.insertMessages(var1, var2);
               }
            }
         }
      }

      private void insertMessages(DefaultKongaTreeNode var1, ValidationResult var2) {
         for (ValidationMessage var4 : var2.allMessages()) {
            new DefaultKongaTreeNode(var1, var4, false);
         }
      }

      private KongaTreeNode addEntityNode(EntityCollectionValidationResult var1, IntegrationEntity var2, KongaTreeNode var3) {
         ValidationResult var4 = this.getResult(var2);
         EntityValidationTreeDisplayer.EntityNodeObject var5 = new EntityValidationTreeDisplayer.EntityNodeObject(var2, var4.status());
         DefaultKongaTreeNode var6 = new DefaultKongaTreeNode(var3, var5, true);
         this.insertMessages(var6, var4);
         return var6;
      }

      private ValidationResult getResult(IntegrationEntity var1) {
         return this.result.getValidatedEntities().contains(var1) ? this.result.getValidationResult(var1) : ValidationResult.VALID;
      }

      private void insertSecondHandValidationProblems(DefaultKongaTreeNode var1) {
         for (IntegrationEntity var4 : Ordering.from(new EntityTypeAndNameSorter()).sortedCopy(this.result.getValidatedEntities())) {
            if (var4 != this.mainEntity) {
               ValidationResult var5 = this.result.getValidationResult(var4);
               if (!var5.isEmpty()) {
                  this.fillInPath(var4, var1);
               }
            }
         }
      }

      private void fillInPath(IntegrationEntity var1, KongaTreeNode var2) {
         for (IntegrationEntity var5 : this.getPath(var1)) {
            KongaTreeNode var6 = this.existingEntityNodes.get(var5);
            if (var6 == null) {
               var6 = this.addEntityNode(this.result, var5, var2);
               this.existingEntityNodes.put(var5, var6);
            }

            var2 = var6;
         }
      }

      private Iterable<IntegrationEntity> getPath(IntegrationEntity var1) {
         ImmutableList var2 = this.result.getPathFromRoot(var1);
         return !this.includeRoot && this.mainEntity != null ? Iterables.skip(var2, 1) : var2;
      }
   }

   private static class Renderer extends DefaultTreeCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         this.setFont(var1.getFont());
         this.customize(var2);
         return this;
      }

      private void customize(Object var1) {
         Object var2 = ((KongaTreeNode)var1).getUserObject();
         if (var2 instanceof EntityValidationTreeDisplayer.EntityNodeObject) {
            this.customizeImpl((EntityValidationTreeDisplayer.EntityNodeObject)var2);
         } else if (var2 instanceof IntegrationEntity) {
            this.customizeImpl((IntegrationEntity)var2);
         } else if (var2 instanceof ValidationMessage) {
            this.customizeImpl((ValidationMessage)var2);
         } else if (var2 instanceof String) {
            this.customizeImpl((String)var2);
         }
      }

      private void customizeImpl(EntityValidationTreeDisplayer.EntityNodeObject var1) {
         RendererUtils.decorateRenderer(this, var1.entity, var1.status, false);
      }

      private void customizeImpl(IntegrationEntity var1) {
         RendererUtils.decorateRenderer(this, var1, false);
      }

      private void customizeImpl(ValidationMessage var1) {
         this.setIcon(ValidationIcons.Indicators.fromMessage(var1));
         this.setText(var1.message());
      }

      private void customizeImpl(String var1) {
         this.setIcon(null);
         this.setFont(Fonts.italic(this));
      }
   }
}
