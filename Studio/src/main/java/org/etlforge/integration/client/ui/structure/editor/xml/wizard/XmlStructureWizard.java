package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public class XmlStructureWizard extends Wizard<XmlStructure> {
   private XmlStructure createdStructure;

   public XmlStructureWizard(XsdFileStore var1, SourceTarget var2) {
      this(null, var2, var1);
   }

   public XmlStructureWizard(XmlStructure var1, SourceTarget var2, XsdFileStore var3) {
      this(null, var1, var2, var3);
   }

   public XmlStructureWizard(Wizard var1, XmlStructure var2, SourceTarget var3, XsdFileStore var4) {
      Preconditions.checkNotNull(var4, "fileStore");
      this.setPageChain(new XmlStructureWizardPageChain(var1 != null ? var1 : this, var2, var3, var4));
      this.setTitle(PackageResources.TITLE);
   }

   public XmlStructure getCreatedStructure() {
      return this.createdStructure != null ? new XmlStructure(this.createdStructure) : null;
   }

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }

   protected XmlStructure finishWizard() throws WizardPageException {
      XmlStructure var1 = new XmlStructure();

      for (WizardPage var3 : this.getAllApplicablePages()) {
         if (var3 instanceof XmlStructureWizardPage) {
            ((XmlStructureWizardPage)var3).applyTo(var1);
         }
      }

      this.createdStructure = var1;
      return var1;
   }

   public void useForEdi() {
      XmlStructureWizardPageChain var1 = (XmlStructureWizardPageChain)this.getPageChain();
      var1.useForEdi();
   }
}
