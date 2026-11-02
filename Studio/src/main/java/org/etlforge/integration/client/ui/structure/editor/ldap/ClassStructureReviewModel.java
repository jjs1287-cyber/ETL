package org.etlforge.integration.client.ui.structure.editor.ldap;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.integration.client.ldap.ui.SelectedObjectClasses;
import org.etlforge.integration.client.ldap.ui.SelectedObjectStructures;
import org.etlforge.integration.client.ui.structure.editor.ldap.tree.AttributeSelectionModel;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

final class ClassStructureReviewModel extends AbstractPropertyBasedModel {
   static final String CLASSES = "classes";
   static final String TREEMODEL = "treeModel";
   private final SourceTarget type;
   private final Property<SelectedObjectClasses> classes;
   private final Property<AttributeSelectionModel> treeModel;
   private final LdapStructure ldapStructure;
   private final LdapStructureServerCache ldapCache;

   public ClassStructureReviewModel(SourceTarget var1, LdapStructure var2, LdapStructureServerCache var3) {
      this.ldapStructure = var2;
      this.ldapCache = var3;
      this.type = var1;
      this.classes = this.newProperty("classes");
      this.treeModel = this.newProperty("treeModel", this.createEmptyModel(var1));
   }

   private AttributeSelectionModel createEmptyModel(SourceTarget var1) {
      return new AttributeSelectionModel(var1, this.ldapCache);
   }

   public LdapStructureServerCache getLdapCache() {
      return this.ldapCache;
   }

   public void setSelectedClasses(SelectedObjectClasses var1) {
      Preconditions.checkNotNull(var1, "classes");
      this.classes.set(var1);
      this.loadModelStructures();
   }

   public SelectedObjectClasses getSelectedClasses() {
      return this.classes.get();
   }

   public void setSelectedStructures(SelectedObjectStructures var1) {
      ApplicationUiUtils.logIfOnEDT(this.getClass(), "setSelectedStructures");
      Preconditions.checkNotNull(var1, "structures");
      this.updateTreeModel(var1);
   }

   private void updateTreeModel(SelectedObjectStructures var1) {
      AttributeSelectionModel var2 = this.treeModel.get();
      Object var3 = var2.getMemento();
      AttributeSelectionModel var4 = new AttributeSelectionModel(this.classes.get().getLdapLocation(), this.type, var1, this.ldapStructure, this.ldapCache);
      var4.restore(var3);
      this.treeModel.set(var4);
   }

   public AttributeSelectionModel getTreeModel() {
      return this.treeModel.get();
   }

   private void loadModelStructures() {
      assert !this.classes.isNull() : "loadModelStructures() may not be called without class names";
      ClassStructureReviewModel.StructureLoader var1 = new ClassStructureReviewModel.StructureLoader(this.classes.get());
      Application.getWorker().submitForParallel(var1);
   }

   private final class StructureLoader implements Runnable {
      private final SelectedObjectClasses classes;

      public StructureLoader(SelectedObjectClasses var2) {
         this.classes = var2;
      }

      @Override
      public void run() {
         SelectedObjectStructures var1 = this.readStructures();
         ClassStructureReviewModel.this.setSelectedStructures(var1);
      }

      private SelectedObjectStructures readStructures() {
         ModelStructuresReader var1 = new ModelStructuresReader();
         return var1.getModelStructures(this.classes, ClassStructureReviewModel.this.ldapCache);
      }
   }
}
