package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.EditStructureException;
import org.etlforge.integration.client.ui.structure.editor.EditedStructure;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.system.ThreeValuedBool;

public final class DatabaseStructureWizard extends ApplicationWindowWizard<EditedStructure> {
   private final SourceTarget use;
   private final StructureToEdit toEdit;
   private DatabaseStructure definedStructure;
   private Locatable database;

   public DatabaseStructureWizard(SourceTarget var1, EntityExplorerSupport var2) {
      this(null, null, var1, var2);
   }

   public DatabaseStructureWizard(Target var1) {
      this(null, var1, SourceTarget.Target, null);
   }

   public DatabaseStructureWizard(StructureToEdit var1, EntityExplorerSupport var2) {
      this(var1, null, var1.getUse(), var2);
   }

   private DatabaseStructureWizard(StructureToEdit var1, Locatable var2, SourceTarget var3, EntityExplorerSupport var4) {
      super(var4.getWindow());
      this.toEdit = var1;
      this.use = var3;
      DatabaseStructureWizardPageChain var5;
      if (var2 != null) {
         var5 = new DatabaseStructureWizardPageChain(this, (Target)var2);
      } else {
         var5 = new DatabaseStructureWizardPageChain(this, var4, var3);
      }

      if (var1 != null) {
         var5.editStructure(var1);
      }

      this.setPageChain(var5);
   }

   protected DatabaseStructureWizardPageChain getPageChain() {
      return (DatabaseStructureWizardPageChain)super.getPageChain();
   }

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }

   protected EditedStructure finishWizard() {
      if (this.toEdit != null) {
         return this.getEditedStructue();
      }

      DatabaseStructureWizardPageChain var1 = this.getPageChain();
      this.definedStructure = var1.getDefinedStructure();
      this.database = var1.getDatabase();
      return new DatabaseStructureWizard.NewStructureImpl(this.definedStructure, this.use);
   }

   public EditedStructure getEditedStructue() {
      return this.toEdit == null ? null : new DatabaseStructureWizard.EditedStructureImpl();
   }

   public DatabaseStructure getDefinedStructure() {
      return this.definedStructure;
   }

   public Locatable getDatabase() {
      return this.database;
   }

   public void setUpdateSourceTableAvailable(ThreeValuedBool var1) {
      Preconditions.checkState(this.use == SourceTarget.Source, "Only applicable for source structures");
      DatabaseStructureWizardPageChain var2 = this.getPageChain();
      var2.setUpdateSourceTableAvailable(var1);
   }

   private class EditedStructureImpl implements EditedStructure {
      private EditedStructureImpl() {
      }

      @Override
      public void apply(Transformation var1) throws EditStructureException {
         try {
            DatabaseStructureWizardPageChain var2 = DatabaseStructureWizard.this.getPageChain();
            var2.applyToEditedTransformation();
         } catch (WizardPageException var3) {
            throw new EditStructureException("Failed to apply the changes to the transformation.", var3);
         }
      }
   }

   private static final class NewStructureImpl implements EditedStructure {
      private final DatabaseStructure structure;
      private final SourceTarget use;

      public NewStructureImpl(DatabaseStructure var1, SourceTarget var2) {
         this.structure = var1;
         this.use = var2;
      }

      @Override
      public void apply(Transformation var1) throws EditStructureException {
         if (this.use == SourceTarget.Source) {
            var1.setInputStructure(this.structure);
         } else {
            var1.setOutputStructure(this.structure);
         }
      }
   }
}
