package org.etlforge.integration.client.project.copy;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.entity.settings.FileResource;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.xml.Elements;
import org.etlforge.xml.XmlFileWriter;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

final class OperationGraphLayoutCopier {
   private static final String fileName = "GraphLayout.xml";
   private final EntityResources resources;
   private final Operation original;
   private final Operation copy;
   private final Map<String, String> idTranslation;

   public OperationGraphLayoutCopier(EntityResources var1, Operation var2, Operation var3) {
      this.resources = var1;
      this.original = var2;
      this.copy = var3;
      this.idTranslation = Maps.newHashMap();
      this.idTranslation.put(var2.getID().toString(), var3.getID().toString());
   }

   public OperationGraphLayoutCopier(EntityResources var1, Operation var2, Map<IntegrationEntity, IntegrationEntity> var3) {
      this.resources = var1;
      this.original = var2;
      this.copy = (Operation)var3.get(var2);
      Preconditions.checkArgument(this.copy != null, "Cannot find the clone of the original operation");
      this.idTranslation = Maps.newHashMap();

      for (Entry var5 : var3.entrySet()) {
         this.idTranslation.put(((IntegrationEntity)var5.getKey()).getID().toString(), ((IntegrationEntity)var5.getValue()).getID().toString());
      }
   }

   public void copy() {
      try {
         this.doCopy();
      } catch (Exception var2) {
         ErrorLog.log(EntityCopyMachine.class, "Failed to copy the operation graph layout.", var2);
      }
   }

   private void doCopy() throws Exception {
      Document var1 = this.readOriginalLayout();
      if (var1 != null) {
         boolean var2 = this.replaceGUIDs(var1);
         if (var2) {
            this.saveNewLayout(var1);
         }
      }
   }

   private Document readOriginalLayout() throws Exception {
      String var1 = this.getLayoutData(this.original);
      return var1 == null ? null : DomParser.parseXmlString(var1);
   }

   private String getLayoutData(Operation var1) {
      FileResource var2 = this.getResource(var1);
      return var2.exists() ? var2.read() : null;
   }

   private FileResource getResource(Operation var1) {
      return this.resources.getFileResource(var1, "GraphLayout.xml");
   }

   private boolean replaceGUIDs(Document var1) {
      boolean var2 = false;

      for (Element var4 : Elements.matching("/GraphLayout/Operation", var1)) {
         if (this.replaceIdAttribute(var4, "opId")) {
            var2 = true;
         }
      }

      for (Element var6 : Elements.matching("/GraphLayout/Email", var1)) {
         if (this.replaceIdAttribute(var6, "emailId")) {
            var2 = true;
         }
      }

      return var2;
   }

   private boolean replaceIdAttribute(Element var1, String var2) {
      String var3 = var1.getAttribute(var2);
      String var4 = this.idTranslation.get(var3);
      if (var4 != null) {
         var1.setAttribute(var2, var4);
         return true;
      } else {
         return false;
      }
   }

   private void saveNewLayout(Document var1) throws IOException {
      XmlFileWriter var2 = new XmlFileWriter();
      StringWriter var3 = new StringWriter();
      var2.write(var1, var3);
      FileResource var4 = this.getResource(this.copy);
      var4.write(var3.getBuffer().toString());
   }
}
