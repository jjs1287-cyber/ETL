package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.entity.wizard.AbstractEntityWizard;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class WebServiceWizard extends AbstractEntityWizard<WebServiceCall> {
   private final WsdlFileStore wsdlFileStore;
   private final InputAndOutputMappingTreeStructureCache projectCache;
   private final StructureCache structureCache;

   public WebServiceWizard(ApplicationWindow var1, WebServiceCall var2, Folder var3, WsdlFileStore var4, InputAndOutputMappingTreeStructureCache var5) {
      super(var1, var2, var3);
      this.wsdlFileStore = Preconditions.checkNotNull(var4, "wsdlFileStore");
      this.projectCache = Preconditions.checkNotNull(var5, "projectCache");
      this.structureCache = new StructureCache();
   }

   public WsdlFileStore getFileStore() {
      return this.wsdlFileStore;
   }

   protected WizardPageChain createPageChain(WebServiceCall var1, Folder var2) {
      return new WebServiceWizardPageChain(this, var1, var2);
   }

   @Override
   protected String getStartSection() {
      return "WSDL";
   }

   protected void applyPageInformation(WebServiceCall var1) throws WizardPageException {
      try {
         for (WizardPage var3 : this.getAllApplicablePages()) {
            if (var3 instanceof WebServiceWizardPage) {
               ((WebServiceWizardPage)var3).applyToWebServiceCall(var1);
            }
         }
      } catch (WizardPageException var4) {
         this.handlePageException(var4.getMessage(), var4);
         throw var4;
      }
   }

   protected void doAdditionalWorkOnFinish(WebServiceCall var1) {
      this.structureCache.moveToProjectCache(var1, this.projectCache);
   }

   StructureCache getStructureCache() {
      return this.structureCache;
   }
}
