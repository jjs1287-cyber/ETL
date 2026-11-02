package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.webservice.HostedWebServicePage;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;

public final class TransformationWizardPageChain implements WizardPageChain {
   private final TransformationWizard wizard;
   private final Transformation transformation;
   private final TransformationWizardStartPage startPage;
   private final TransformationWizardPageChain.PageChainProvider sourceChainProvider;
   private final TransformationWizardPageChain.PageChainProvider targetChainProvider;
   private TransformationStructurePageChain sourceChain;
   private TransformationStructurePageChain targetChain;
   private TransformationStructurePageChain currentChain;
   private HostedWebServicePage hostedWebServicePage;

   public TransformationWizardPageChain(TransformationWizard var1, Transformation var2, Folder var3) {
      this.wizard = var1;
      this.transformation = var2;
      this.sourceChainProvider = new TransformationWizardPageChain.PageChainProvider(new TransformationWizardPageChain.SourceChainFactories());
      this.targetChainProvider = new TransformationWizardPageChain.PageChainProvider(new TransformationWizardPageChain.TargetChainFactories());
      this.startPage = new TransformationWizardStartPage(var1, var2, var3);
   }

   public void applyTo(Transformation var1) throws WizardPageException {
      this.startPage.applyToTransformation(var1);
      if (this.hostedWebServicePage != null) {
         this.hostedWebServicePage.applyToTransformation(var1);
      } else {
         if (this.sourceChain != null) {
            this.sourceChain.applyTo(var1);
         }

         if (this.targetChain != null) {
            this.targetChain.applyTo(var1);
         }
      }
   }

   @Override
   public WizardPage getFirstPage() {
      return this.startPage;
   }

   @Override
   public WizardPage getLastPage() {
      return null;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      WizardPage var2 = null;
      if (var1 == this.startPage) {
         var2 = this.getFirstPageAfterStartPage();
      }

      if (var2 == null && !this.isPreconfiguredHostedWebService()) {
         var2 = this.currentChain.getNextPage(var1);
         if (var2 == null && this.needsToConfigureTargetStructure()) {
            var2 = this.switchToTargetChain();
         }

         return var2;
      } else {
         return var2;
      }
   }

   private WizardPage getFirstPageAfterStartPage() {
      if (this.startPage.isHostedWebService()) {
         return this.getHostedWebServicePage();
      } else {
         return this.needsSourceStructure() ? this.switchToSourceChain() : this.switchToTargetChain();
      }
   }

   private boolean needsSourceStructure() {
      return this.transformation.getInputStructure() == null;
   }

   private WizardPage switchToSourceChain() {
      this.inSourceSection();
      this.sourceChain = this.sourceChainProvider.getChain(this.startPage.getSourceType());
      WizardType.Nature var1 = this.startPage.getTargetType().getNature();
      this.sourceChain.setOpposingStructureNature(var1);
      this.currentChain = this.sourceChain;
      return this.getFirstPageOfCurrentChain();
   }

   private void inSourceSection() {
      this.wizard.getDisplayer().setSection("Source");
   }

   private WizardPage getFirstPageOfCurrentChain() {
      return this.currentChain != null ? this.currentChain.getFirstPage() : null;
   }

   private WizardPage switchToTargetChain() {
      this.inTargetSection();
      this.targetChain = this.targetChainProvider.getChain(this.startPage.getTargetType());
      WizardType.Nature var1 = this.startPage.getSourceType().getNature();
      this.targetChain.setOpposingStructureNature(var1);
      this.currentChain = this.targetChain;
      return this.getFirstPageOfCurrentChain();
   }

   private void inTargetSection() {
      this.wizard.getDisplayer().setSection("Target");
   }

   private boolean needsToConfigureTargetStructure() {
      return this.currentChain == this.sourceChain && this.transformation.getOutputStructure() == null;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.startPage) {
         return null;
      }

      if (var1 == this.hostedWebServicePage) {
         this.inStartPage();
         return this.startPage;
      }

      WizardPage var2 = this.currentChain.getPreviousPage(var1);
      if (var2 == null && this.currentChain == this.targetChain && this.sourceChain != null) {
         var2 = this.sourceChain.getLastPage();
         this.inSourceSection();
         this.currentChain = this.sourceChain;
      }

      if (var2 == null) {
         this.inStartPage();
         var2 = this.startPage;
         this.currentChain = null;
      }

      return var2;
   }

   private void inStartPage() {
      this.wizard.getDisplayer().setSection("Name");
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      if (var1 == this.startPage) {
         return !this.outputStructureIsPredefinedAndNoneIsSelectedAsInput();
      } else if (var1 == this.hostedWebServicePage) {
         return false;
      } else {
         return this.currentChain.hasNextPage(var1) ? true : this.currentChain == this.sourceChain && this.transformation.getOutputStructure() == null;
      }
   }

   private boolean outputStructureIsPredefinedAndNoneIsSelectedAsInput() {
      boolean var1 = this.transformation.getOutputStructure() != null && this.currentChain == this.sourceChain;
      return var1 && this.startPage.isNoneSelectedAsSource();
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.startPage;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.startPage && this.isPreconfiguredHostedWebService();
   }

   private WizardPage getHostedWebServicePage() {
      if (this.isPreconfiguredHostedWebService()) {
         return null;
      }

      if (this.hostedWebServicePage == null) {
         this.hostedWebServicePage = new HostedWebServicePage(this.wizard);
      }

      return this.hostedWebServicePage;
   }

   private boolean isPreconfiguredHostedWebService() {
      return this.transformation.getInputStructure() instanceof WebServiceRequestStructure
         && this.transformation.getOutputStructure() instanceof WebServiceResponseStructure;
   }

   private abstract class ChainFactories {
      private ChainFactories() {
      }

      public abstract PageChainFactory getChainFactory(WizardType var1);
   }

   private class PageChainProvider {
      private final TransformationWizardPageChain.ChainFactories factories;
      private final Map<WizardType, TransformationStructurePageChain> cache;

      public PageChainProvider(TransformationWizardPageChain.ChainFactories var2) {
         this.factories = var2;
         this.cache = Maps.newHashMap();
      }

      public TransformationStructurePageChain getChain(WizardType var1) {
         TransformationStructurePageChain var2 = this.cache.get(var1);
         if (var2 == null) {
            var2 = this.createNewChain(var1);
         }

         return var2;
      }

      private TransformationStructurePageChain createNewChain(WizardType var1) {
         TransformationStructurePageChain var2 = null;
         PageChainFactory var3 = this.factories.getChainFactory(var1);
         if (var3 != null) {
            var2 = var3.createChain(TransformationWizardPageChain.this.wizard);
            this.cache.put(var1, var2);
         }

         return var2;
      }
   }

   private class SourceChainFactories extends TransformationWizardPageChain.ChainFactories {
      private SourceChainFactories() {
      }

      @Override
      public PageChainFactory getChainFactory(WizardType var1) {
         return var1.getSourceChainFactory();
      }
   }

   private class TargetChainFactories extends TransformationWizardPageChain.ChainFactories {
      private TargetChainFactories() {
      }

      @Override
      public PageChainFactory getChainFactory(WizardType var1) {
         return var1.getTargetChainFactory();
      }
   }
}
