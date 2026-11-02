package org.etlforge.integration.client.ui.interchange.transformation.wizard.xml;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.AbstractTransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.resources.TransWizResources;
import org.etlforge.integration.client.ui.structure.editor.xml.wizard.XmlStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public final class XmlPageChain extends AbstractTransformationStructurePageChain {
   private final XmlStructureWizardPageChain pages;
   private final SourceTarget sourceTarget;
   private final boolean isEdi;

   public static XmlPageChain createForEdi(TransformationWizard var0, SourceTarget var1) {
      return new XmlPageChain(var0, var1, true);
   }

   public XmlPageChain(TransformationWizard var1, SourceTarget var2) {
      this(var1, var2, false);
   }

   private XmlPageChain(TransformationWizard var1, SourceTarget var2, boolean var3) {
      this.pages = new XmlStructureWizardPageChain(var1, var2, new XsdFileStore(var1.getFileStore()));
      if (var3) {
         this.pages.useForEdi();
      }

      this.pages.setStartPageSubTitle(this.getStartPageSubTitle(var2, var3));
      this.sourceTarget = var2;
      this.isEdi = var3;
   }

   private String getStartPageSubTitle(SourceTarget var1, boolean var2) {
      switch (var1) {
         case Source:
            if (var2) {
               return "Source EDI Transaction Set";
            }

            return TransWizResources.getString("Xml.StartPage.Source.SubTitle");
         case Target:
            if (var2) {
               return "Target EDI Transaction Set";
            }

            return TransWizResources.getString("Xml.StartPage.Target.SubTitle");
         default:
            throw new RuntimeException("Unexpected SourceTarget: " + this.sourceTarget);
      }
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
   public boolean canFinish(WizardPage var1) {
      return this.pages.canFinish(var1);
   }

   @Override
   public void applyTo(Transformation var1) throws WizardPageException {
      XmlStructure var2 = this.pages.getXmlStructure();
      this.applyStructureToTransformation(var1, var2);
   }

   private void applyStructureToTransformation(Transformation var1, XmlStructure var2) {
      if (this.sourceTarget == SourceTarget.Source) {
         var1.setInputStructure(var2);
         var1.setXmlSourceIsEdi(this.isEdi);
      } else {
         var1.setOutputStructure(var2);
         var1.setXmlTargetIsEdi(this.isEdi);
      }
   }
}
