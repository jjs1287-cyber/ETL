package org.etlforge.integration.client.ui.interchange.transformation.wizard.ldap;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.AbstractTransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.structure.editor.ldap.LdapStructurePageChain;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;

public final class LdapTargetPageChainFactory implements PageChainFactory {
   @Override
   public TransformationStructurePageChain createChain(TransformationWizard var1) {
      return new LdapTargetPageChainFactory.ChainImpl(var1);
   }

   private static class ChainImpl extends AbstractTransformationStructurePageChain {
      private final LdapStructurePageChain pages;

      public ChainImpl(TransformationWizard var1) {
         this.pages = LdapStructurePageChain.newTargetChain(var1, var1.getEntityExplorerSupport());
         this.setPreselectedTarget(var1);
      }

      private void setPreselectedTarget(TransformationWizard var1) {
         Transformation var2 = var1.getTransformation();
         TargetId var3 = var2.getTargetId();
         if (var3 != null) {
            IntegrationEntityLookup var4 = var1.getItemLookup();
            Target var5 = var4.getEntity(var3, Target.class);
            if (var5 != null) {
               DataLocation var6 = var5.getLocation();
               if (var6 instanceof LdapLocation) {
                  this.pages.setSelectedLocation((LdapLocation)var6);
               }
            }
         }
      }

      @Override
      public void applyTo(Transformation var1) throws WizardPageException {
         LdapStructure var2 = this.pages.getDefinedStructure();
         var1.setOutputStructure(var2);
         var1.setTargetId(var2.getLdapLocation().getTargetId());
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
   }
}
