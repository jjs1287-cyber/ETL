package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import java.awt.EventQueue;
import javax.swing.Action;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.entity.webservice.CallStructureRetriever;
import org.etlforge.integration.client.ui.interchange.entity.webservice.GetJtrJob;
import org.etlforge.integration.client.ui.interchange.entity.webservice.WebServiceStructureCacher;
import org.etlforge.integration.client.ui.interchange.entity.webservice.prefs.ShowWebServiceStructuresPreference;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public final class WebServiceCallPage extends IntegrationEntityPage {
   private final WebServiceCallPage.StructureLoader structureLoader = new WebServiceCallPage.StructureLoader();
   private final InputAndOutputMappingTreeStructureCache structureCache;
   private Action editWsdlAction;

   public WebServiceCallPage(WebServiceCall var1) {
      super(new IntegrationEntityPageModel(var1));
      this.structureCache = this.getStructureCache(var1);
      this.setHelpId("Create_a_Web_Service_Method");
   }

   private InputAndOutputMappingTreeStructureCache getStructureCache(WebServiceCall var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      return var2.getWebServiceMappingStructureCache();
   }

   protected WebServiceCallPanel createDataPanel() {
      WebServiceCallPanel var1 = new WebServiceCallPanel(this.getWindow(), this.getObject());
      var1.setEditWsdlAction(this.editWsdlAction);
      IntegrationEntityPageModel var2 = this.getPageModel();
      var2.bindProperties(var1.getBoundPropertyControls());
      return var1;
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      if (ShowWebServiceStructuresPreference.showStructures()) {
         this.loadRequestAndResponseStructures();
      }
   }

   private void loadRequestAndResponseStructures() {
      try {
         WebServiceCall var1 = this.getObject();
         final MappingTreeStructure var2 = this.structureCache.getInputStructure(var1.getID()).orNull();
         final MappingTreeStructure var3 = this.structureCache.getOutputStructure(var1.getID()).orNull();
         if (var2 != null && var3 != null) {
            WebServiceCallStructures var4 = new WebServiceCallStructures() {
               @Override
               public MappingTreeStructure request() {
                  return var2;
               }

               @Override
               public MappingTreeStructure response() {
                  return var3;
               }
            };
            ((WebServiceCallPanel)this.getDetailsPanel()).displayStructures(var4);
         } else {
            this.refreshTreeStructures();
         }
      } catch (DataCacheException var5) {
         this.refreshTreeStructures();
      }
   }

   public void refreshTreeStructures() {
      this.structureLoader.load();
   }

   public WebServiceCall getObject() {
      return (WebServiceCall)super.getObject();
   }

   public void setEditWsdlAction(Action var1) {
      this.editWsdlAction = var1;
   }

   private class StructureLoader implements CallStructureRetriever {
      private StructureLoader() {
      }

      public synchronized void load() {
         WebServiceOperationInfo var1 = WebServiceCallPage.this.getObject().getOperationInfo();
         if (var1 == null || org.apache.commons.lang.StringUtils.isBlank(var1.getProperty(WebServiceOperationInfo.WSDL_LOCATOR))
            || org.apache.commons.lang.StringUtils.isBlank(var1.getProperty(WebServiceOperationInfo.OPERATION_NAME))) {
            return;
         }
         GetJtrJob var2 = new GetJtrJob(this, var1, WebServiceCallPage.this.getWaitService());
         var2.submit();
      }

      @Override
      public void onJTRsRetrieved(WebServiceCallStructures var1) {
         this.cacheStructures(var1);
         this.updateUi(var1);
      }

      private void cacheStructures(WebServiceCallStructures var1) {
         WebServiceStructureCacher var2 = new WebServiceStructureCacher(WebServiceCallPage.this.getObject().getID(), WebServiceCallPage.this.structureCache);
         var2.cacheStructures(var1);
      }

      private void updateUi(final WebServiceCallStructures var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ((WebServiceCallPanel)WebServiceCallPage.this.getDetailsPanel()).displayStructures(var1);
            }
         });
      }
   }
}
