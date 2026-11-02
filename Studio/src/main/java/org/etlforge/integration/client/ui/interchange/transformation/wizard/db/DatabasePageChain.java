package org.etlforge.integration.client.ui.interchange.transformation.wizard.db;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.system.ThreeValuedBool;

final class DatabasePageChain implements TransformationStructurePageChain {
   private final SourceTarget sourceTarget;
   private final DatabaseStructureWizardPageChain pages;

   public DatabasePageChain(TransformationWizard var1, SourceTarget var2) {
      this.sourceTarget = var2;
      this.pages = this.createPages(var1, var2);
   }

   private DatabaseStructureWizardPageChain createPages(TransformationWizard var1, SourceTarget var2) {
      Transformation var3 = var1.getTransformation();
      IntegrationEntityLookup var4 = var1.getItemLookup();
      switch (var2) {
         case Source:
            Source var5 = var4.getEntity(var3.getSourceId(), Source.class);
            if (var5 != null) {
               return new DatabaseStructureWizardPageChain(var1, var5);
            }
            break;
         case Target:
            Target var6 = var4.getEntity(var3.getTargetId(), Target.class);
            if (var6 != null) {
               return new DatabaseStructureWizardPageChain(var1, var6);
            }
            break;
         default:
            throw new RuntimeException();
      }

      EntityExplorerSupport var7 = var1.getEntityExplorerSupport();
      return new DatabaseStructureWizardPageChain(var1, var7, var2);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.pages.getFirstPage();
   }

   @Override
   public WizardPage getLastPage() {
      return this.pages.getLastPage();
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      return this.pages.getNextPage(var1);
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      return this.pages.getPreviousPage(var1);
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return this.pages.hasNextPage(var1);
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return this.pages.hasPreviousPage(var1);
   }

   @Override
   public void applyTo(Transformation var1) throws WizardPageException {
      DatabaseStructure var2 = this.pages.getDefinedStructure();
      if (this.sourceTarget == SourceTarget.Source) {
         var1.setInputStructure(var2);
         var1.setSourceId(var2.getSourceId());
      } else {
         var1.setOutputStructure(var2);
         var1.setTargetId(var2.getTargetId());
         this.pages.applyInsertMode(var1);
      }
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return this.pages.canFinish(var1);
   }

   @Override
   public void setOpposingStructureNature(WizardType.Nature var1) {
      if (this.sourceTarget == SourceTarget.Source) {
         ThreeValuedBool var2;
         switch (var1) {
            case FLAT:
               var2 = ThreeValuedBool.TRUE;
               break;
            case HIERARCHICAL:
               var2 = ThreeValuedBool.FALSE;
               break;
            case FLAT_OR_HIERARCHICAL:
               var2 = ThreeValuedBool.MAYBE;
               break;
            default:
               throw new RuntimeException("Unexpected nature: " + var1);
         }

         this.pages.setUpdateSourceTableAvailable(var2);
      }
   }
}
