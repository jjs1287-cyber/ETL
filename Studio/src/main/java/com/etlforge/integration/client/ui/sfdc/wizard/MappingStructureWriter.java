package com.etlforge.integration.client.ui.sfdc.wizard;

import java.io.Writer;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.wsdl.partner.crom.AbstractCallStructureBuilder;
import org.etlforge.io.KongaIoUtils;

public final class MappingStructureWriter {
   private final Transformation request;
   private final Transformation response;
   private final InputAndOutputMappingTreeStructureCache cache;
   private final AbstractCallStructureBuilder requestBuilder;
   private final AbstractCallStructureBuilder responseBuilder;

   public MappingStructureWriter(
      Transformation var1, Transformation var2, AbstractCallStructureBuilder var3, AbstractCallStructureBuilder var4, IntegrationProject var5
   ) {
      this.request = var1;
      this.response = var2;
      this.requestBuilder = var3;
      this.responseBuilder = var4;
      ManagedProject var6 = ProjectUtils.getManagedProject(var5);
      this.cache = var6.getTransformationMappingStructureCache();
   }

   public void writeJtrs() {
      this.writeRequestTree();
      this.writeResponseTree();
   }

   private void writeRequestTree() {
      if (!(this.request instanceof BuiltInIntegrationEntity)) {
         Writer var1 = null;

         try {
            var1 = this.cache.getOutputStructureWriter(this.request.getID());
            this.requestBuilder.writeJtr(var1);
         } catch (Exception var6) {
            ErrorLog.internal(this.getClass(), null, var6);
         } finally {
            KongaIoUtils.close(var1);
         }
      }
   }

   private void writeResponseTree() {
      if (!(this.response instanceof BuiltInIntegrationEntity)) {
         Writer var1 = null;

         try {
            var1 = this.cache.getInputStructureWriter(this.response.getID());
            this.responseBuilder.writeJtr(var1);
         } catch (Exception var6) {
            ErrorLog.internal(this.getClass(), null, var6);
         } finally {
            KongaIoUtils.close(var1);
         }
      }
   }
}
