package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;

public class WizardPageList implements WizardPageChain {
   private final List<WizardPage> pages;

   public WizardPageList() {
      this.pages = Lists.newArrayList();
   }

   public WizardPageList(WizardPage... var1) {
      this.pages = Lists.newArrayList(var1);
   }

   public WizardPageList(List<WizardPage> var1) {
      this.pages = Lists.newArrayList(var1);
   }

   public void addPage(WizardPage var1) {
      this.pages.add(Preconditions.checkNotNull(var1));
   }

   public final WizardPage getPageAt(int var1) {
      return this.pages.get(var1);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.pages.isEmpty() ? null : this.pages.get(0);
   }

   @Override
   public WizardPage getLastPage() {
      return this.pages.isEmpty() ? null : this.pages.get(this.pages.size() - 1);
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      int var2 = this.pages.indexOf(var1);
      return var2 >= 0 && var2 < this.pages.size() - 1;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      int var2 = this.pages.indexOf(var1);
      if (var2 != this.pages.size() - 1 && var2 != -1) {
         WizardPage var3 = this.pages.get(var2 + 1);
         this.updateWizardSection(var3);
         return var3;
      } else {
         return null;
      }
   }

   private void updateWizardSection(WizardPage var1) {
      String var2 = this.getSectionForPage(var1);
      if (var2 != null) {
         var1.getWizard().getDisplayer().setSection(var2);
      }
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return this.pages.indexOf(var1) > 0;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      int var2 = this.pages.indexOf(var1);
      if (var2 <= 0) {
         return null;
      }

      WizardPage var3 = this.pages.get(var2 - 1);
      this.updateWizardSection(var3);
      return var3;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return false;
   }

   protected String getSectionForPage(WizardPage var1) {
      return null;
   }
}
