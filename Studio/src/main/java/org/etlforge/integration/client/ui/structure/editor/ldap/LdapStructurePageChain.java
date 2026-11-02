package org.etlforge.integration.client.ui.structure.editor.ldap;

import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.ldap.ui.SelectedObjectClasses;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.ldap.source.LdapSourceSelectionPage;
import org.etlforge.integration.client.ui.structure.editor.ldap.source.SearchParametersDefinitionPage;
import org.etlforge.integration.client.ui.structure.editor.ldap.target.LdapTargetSelectionPage;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;

public final class LdapStructurePageChain implements WizardPageChain {
   private final LdapLocationSelectionPage locationSelectionPage;
   private final ObjectClassSelectionPage classSelectionPage;
   private final ClassStructureReviewPage structureReviewPage;
   private final SearchParametersDefinitionPage searchParamsPage;
   private final LdapStructure toModify;
   private boolean updateClassSelectionPage;
   private boolean updateSearchParamsPage;
   private LdapLocation fixedLocation;

   public static LdapStructurePageChain newTargetChain(ApplicationWindowWizard var0, EntityExplorerSupport var1) {
      return new LdapStructurePageChain(var0, SourceTarget.Target, var1);
   }

   public static LdapStructurePageChain newEditTargetChain(ApplicationWindowWizard var0, LdapStructure var1, EntityExplorerSupport var2) {
      return new LdapStructurePageChain(var0, var1, SourceTarget.Target, var2);
   }

   public static LdapStructurePageChain newSourceChain(ApplicationWindowWizard var0, EntityExplorerSupport var1) {
      return new LdapStructurePageChain(var0, SourceTarget.Source, var1);
   }

   public static LdapStructurePageChain newEditSourceChain(ApplicationWindowWizard var0, LdapStructure var1, EntityExplorerSupport var2) {
      return new LdapStructurePageChain(var0, var1, SourceTarget.Source, var2);
   }

   private LdapStructurePageChain(ApplicationWindowWizard var1, SourceTarget var2, EntityExplorerSupport var3) {
      this(var1, null, var2, var3);
   }

   private LdapStructurePageChain(ApplicationWindowWizard var1, LdapStructure var2, SourceTarget var3, EntityExplorerSupport var4) {
      boolean var5 = var3 == SourceTarget.Source;
      this.locationSelectionPage = var5 ? new LdapSourceSelectionPage(var1, var4) : new LdapTargetSelectionPage(var1, var4);
      LdapStructureServerCache var6 = getLdapCache();
      this.classSelectionPage = new ObjectClassSelectionPage(var1);
      this.classSelectionPage.setLdapCache(var6);
      ClassStructureReviewModel var7 = new ClassStructureReviewModel(var3, var2, var6);
      this.structureReviewPage = new ClassStructureReviewPage(var1, var7);
      this.searchParamsPage = var5 ? new SearchParametersDefinitionPage(var1) : null;
      if (var2 != null) {
         this.locationSelectionPage.setSelectedLdapLocation(var2.getLdapLocation());
         this.updateClassSelectionPage = true;
         this.updateSearchParamsPage = true;
      }

      this.toModify = var2;
   }

   private static LdapStructureServerCache getLdapCache() {
      DataCacheService var0 = Application.getCacheService();
      return var0.getCache(LdapStructureServerCache.class);
   }

   public void setSelectedLocation(LdapLocation var1) {
      this.locationSelectionPage.setSelectedLdapLocation(var1);
   }

   public void setFixedLocation(LdapLocation var1) {
      this.fixedLocation = var1;
      this.classSelectionPage.setLdapLocation(var1);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.fixedLocation == null ? this.locationSelectionPage : this.classSelectionPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.searchParamsPage != null ? this.searchParamsPage : this.structureReviewPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.locationSelectionPage) {
         this.prepareClassSelectionPage();
         return this.classSelectionPage;
      } else if (var1 == this.classSelectionPage) {
         this.prepareStructureReviewPage();
         return this.structureReviewPage;
      } else if (var1 == this.structureReviewPage && this.searchParamsPage != null) {
         this.prepareSearchParamsPage();
         return this.searchParamsPage;
      } else {
         return null;
      }
   }

   private void prepareClassSelectionPage() {
      LdapLocation var1 = this.locationSelectionPage.getSelectedLdapLocation();
      this.classSelectionPage.setLdapLocation(var1);
      if (this.updateClassSelectionPage) {
         assert this.toModify != null;
         this.classSelectionPage.setStructureToEdit(this.toModify);
         this.updateClassSelectionPage = false;
      }
   }

   private void prepareStructureReviewPage() {
      SelectedObjectClasses var1 = this.classSelectionPage.getSelectedClasses();
      this.structureReviewPage.setObjectClasses(var1);
   }

   private void prepareSearchParamsPage() {
      LdapLocation var1 = this.fixedLocation == null ? this.locationSelectionPage.getSelectedLdapLocation() : this.fixedLocation;
      LdapStructureEntryNode[] var2 = this.structureReviewPage.getDefinedEntryNodes();
      this.searchParamsPage.populate(var1, var2);
      if (this.updateSearchParamsPage) {
         assert this.toModify != null;
         this.searchParamsPage.setStructureToEdit(this.toModify);
         this.updateSearchParamsPage = false;
      }
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.classSelectionPage && this.fixedLocation == null) {
         return this.locationSelectionPage;
      } else if (var1 == this.structureReviewPage) {
         return this.classSelectionPage;
      } else {
         return var1 == this.searchParamsPage ? this.structureReviewPage : null;
      }
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return this.searchParamsPage != null ? var1 != this.searchParamsPage : var1 != this.structureReviewPage;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      if (var1.canContinue()) {
         return this.searchParamsPage != null ? var1 == this.searchParamsPage : var1 == this.structureReviewPage;
      } else {
         return false;
      }
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      if (var1 == this.locationSelectionPage) {
         return false;
      } else {
         return var1 == this.classSelectionPage ? this.fixedLocation == null : true;
      }
   }

   public LdapStructure getDefinedStructure() {
      SelectedObjectClasses var1 = this.classSelectionPage.getSelectedClasses();
      assert var1 != null : "No LDAP class selected. The wizard should have prevented the user from continuing.";
      LdapStructure var2 = var1.asLdapStructure();
      this.structureReviewPage.applyTo(var2);
      if (this.searchParamsPage != null) {
         this.searchParamsPage.applyTo(var2);
      }

      return var2;
   }
}
