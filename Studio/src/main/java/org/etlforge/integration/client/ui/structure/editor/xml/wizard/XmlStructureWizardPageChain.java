package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public final class XmlStructureWizardPageChain implements WizardPageChain {
   private final Wizard wizard;
   private final XsdFileStore fileStore;
   private final SourceTarget sourceOrTarget;
   private final XmlStructureWizardStartPage startPage;
   private final PageAfterStartFactory nextPageFactory;
   private XmlStructureWizardPage rootSelectionPage;
   private final XmlStructureWizardPage optionsPage;
   private CreateXsdFromXmlPage xsdFromXmlPage;
   private boolean isEdi;
   private Optional<File> sampleFile = Optional.absent();

   public XmlStructureWizardPageChain(Wizard var1, SourceTarget var2, XsdFileStore var3) {
      this(var1, null, var2, var3);
   }

   public XmlStructureWizardPageChain(Wizard var1, XmlStructure var2, SourceTarget var3, XsdFileStore var4) {
      this.wizard = Preconditions.checkNotNull(var1, "wizard");
      this.fileStore = Preconditions.checkNotNull(var4, "fileStore");
      this.sourceOrTarget = Preconditions.checkNotNull(var3, "sourceOrTarget");
      this.startPage = new XmlStructureWizardStartPage(var1, var2, var4);
      this.nextPageFactory = new PageAfterStartFactory(this.startPage, var2);
      this.optionsPage = new XmlStructureOptionsPage(var1, var2, var3);
   }

   public void setStartPageSubTitle(String var1) {
      this.startPage.setSubTitle(var1);
   }

   public void setPreselectedSampleFile(File var1) {
      this.sampleFile = Optional.of(var1);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.startPage;
   }

   @Override
   public WizardPage getLastPage() {
      if (this.isEdi) {
         return this.rootSelectionPage == null ? this.startPage : this.rootSelectionPage;
      } else {
         return this.optionsPage;
      }
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.startPage) {
         return this.getPageAfterStartPage();
      } else {
         return var1 != this.xsdFromXmlPage && (var1 != this.rootSelectionPage || this.isEdi) ? null : this.optionsPage;
      }
   }

   private WizardPage getPageAfterStartPage() {
      if (this.startPage.isSampleFileMode()) {
         return this.xsdFromSampleFilePage();
      } else {
         this.rootSelectionPage = this.nextPageFactory.getNextPage();
         if (this.rootSelectionPage != null) {
            return this.rootSelectionPage;
         } else {
            return !this.isEdi ? this.optionsPage : null;
         }
      }
   }

   private WizardPage xsdFromSampleFilePage() {
      if (this.xsdFromXmlPage == null) {
         this.xsdFromXmlPage = new CreateXsdFromXmlPage(this.wizard, this.fileStore);
         if (this.sampleFile.isPresent()) {
            this.xsdFromXmlPage.setPreselectedFile(this.sampleFile.get());
         }
      }

      this.xsdFromXmlPage.setExistingFiles(this.startPage.getExistingFiles());
      return this.xsdFromXmlPage;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.rootSelectionPage) {
         return this.startPage;
      }

      if (var1 == this.xsdFromXmlPage) {
         return this.startPage;
      }

      if (var1 == this.optionsPage) {
         if (this.startPage.isSampleFileMode()) {
            assert this.xsdFromXmlPage != null;
            return this.xsdFromXmlPage;
         } else {
            return this.rootSelectionPage != null ? this.rootSelectionPage : this.startPage;
         }
      } else {
         return null;
      }
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return this.isEdi ? var1 != this.rootSelectionPage : var1 != this.optionsPage;
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.startPage;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return false;
   }

   public XmlStructure getXmlStructure() {
      XmlStructure var1 = new XmlStructure();

      for (XmlStructureWizardPage var3 : this.getApplicablePages()) {
         var3.applyTo(var1);
      }

      return var1;
   }

   private List<XmlStructureWizardPage> getApplicablePages() {
      ArrayList var1 = Lists.newArrayList();
      var1.add(this.startPage);
      if (this.startPage.isSampleFileMode()) {
         assert this.xsdFromXmlPage != null;
         var1.add(this.xsdFromXmlPage);
      } else if (this.rootSelectionPage != null) {
         var1.add(this.rootSelectionPage);
      }

      var1.add(this.optionsPage);
      return var1;
   }

   public void useForEdi() {
      if (!this.isEdi) {
         this.isEdi = true;
         this.startPage.useForEDI();
         if (this.sourceOrTarget == SourceTarget.Source) {
            this.startPage.setSubTitle("Source EDI Transaction Set");
         } else {
            this.startPage.setSubTitle("Target EDI Transaction Set");
         }
      }
   }
}
