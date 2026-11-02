package org.etlforge.integration.client.ui.interchange.transformation.wizard.ldap;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.AbstractTransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.structure.editor.ldap.LdapStructurePageChain;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;

public final class LdapSourcePageChainFactory implements PageChainFactory {
   @Override
   public TransformationStructurePageChain createChain(TransformationWizard var1) {
      return new LdapSourcePageChainFactory.ChainImpl(var1);
   }

   private static class ChainImpl extends AbstractTransformationStructurePageChain {
      private final LdapStructurePageChain pages;

      public ChainImpl(TransformationWizard var1) {
         EntityExplorerSupport var2 = var1.getEntityExplorerSupport();
         this.pages = LdapStructurePageChain.newSourceChain(var1, var2);
         this.setPreselectedSource(var1);
      }

      private void setPreselectedSource(TransformationWizard var1) {
         Transformation var2 = var1.getTransformation();
         SourceId var3 = var2.getSourceId();
         if (var3 != null) {
            IntegrationEntityLookup var4 = var1.getItemLookup();
            Source var5 = var4.getEntity(var3, Source.class);
            if (var5 != null) {
               DataLocation var6 = var5.getLocation();
               if (var6 instanceof LdapLocation) {
                  this.pages.setSelectedLocation((LdapLocation)var6);
               }
            }
         }
      }

      @Override
      public void applyTo(Transformation var1) {
         LdapStructure var2 = this.pages.getDefinedStructure();
         var1.setInputStructure(var2);
         var1.setSourceId(var2.getLdapLocation().getSourceId());
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
