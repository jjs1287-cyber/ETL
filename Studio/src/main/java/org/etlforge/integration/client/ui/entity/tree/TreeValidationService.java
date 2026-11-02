package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.project.validation.ValidationModelEvent;
import org.etlforge.integration.data.project.validation.ValidationModelListener;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;

public class TreeValidationService implements Disposable {
   private ValidationModel model;
   private final List<IntegrationEntityTree> trees = Lists.newCopyOnWriteArrayList();
   private final List<ValidationModelListener> listeners = Lists.newCopyOnWriteArrayList();

   public TreeValidationService() {
      this.listeners.add(new ValidationModelListener() {
         @Override
         public void validationWasPerformed(ValidationModelEvent var1) {
            TreeValidationService.this.repaintTree();
         }
      });
   }

   public void setModel(ValidationModel var1) {
      if (var1 != this.model) {
         this.removeModelListeners();
         if (var1 != null) {
            for (ValidationModelListener var3 : this.listeners) {
               var1.addListener(var3);
            }
         }

         this.model = var1;
         this.repaintTree();
      }
   }

   public ValidationModel getModel() {
      return this.model;
   }

   @Override
   public void dispose() {
      this.removeModelListeners();
      this.trees.clear();
   }

   private void removeModelListeners() {
      if (this.model != null) {
         for (ValidationModelListener var2 : this.listeners) {
            this.model.removeListener(var2);
         }
      }
   }

   public void addTree(IntegrationEntityTree var1) {
      this.trees.add(var1);
   }

   public void removeTree(IntegrationEntityTree var1) {
      this.trees.remove(var1);
   }

   public ValidationStatus getValidationStatus(IntegrationEntityTreeNode var1) {
      return this.model != null ? this.model.getStatus(var1.getUserObject(), true) : ValidationStatus.VALID;
   }

   public void addValidationListener(ValidationModelListener var1) {
      this.listeners.add(var1);
      if (this.model != null) {
         this.model.addListener(var1);
      }
   }

   public void removeValidationListener(ValidationModelListener var1) {
      this.listeners.remove(var1);
      if (this.model != null) {
         this.model.removeListener(var1);
      }
   }

   private void repaintTree() {
      for (IntegrationEntityTree var2 : this.trees) {
         if (var2.isIndicatingValidationStatus()) {
            var2.repaint();
         }
      }
   }
}
