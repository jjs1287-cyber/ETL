package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.util.HttpCallUrlUtils;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.SAXException;

public class AutoDeskMetaField {
   String fieldID;
   String fieldName;
   String fieldType;
   String editable;
   String sectionID;
   String sectionURI;

   public String getfieldID() {
      return this.fieldID;
   }

   public static List<AutoDeskMetaField> getAutoDeskMetaFields(AutoDeskEndpoint var0, String var1) {
      String var3 = "api/rest/v1/workspaces/" + var1 + "/metafields/assigned";
      String var4 = HttpCallUrlUtils.getFullUrl(var0.serviceBaseUrl, var3);
      AutoDeskRestServiceHandler var5 = new AutoDeskRestServiceHandler("GET", var4);
      AutoDeskCallResponse var6 = var5.execute(var0.getProxySettings(), var0.autodeskSession);
      var6.throwRuntimeExceptionOnBadResponse();
      return parseAutoDeskMetaFieldListResponse(var6.getHttpCallResponse().getResponse());
   }

   public static List<AutoDeskMetaField> parseAutoDeskMetaFieldListResponse(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      AutoDeskMetaField.AutoDeskMetaFieldHandler var2 = new AutoDeskMetaField.AutoDeskMetaFieldHandler();
      var1.registerHandler(var2);

      try {
         var1.parseString(var0);
      } catch (SAXException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      }

      return var2.metafields;
   }

   @RelativePathRoot("payload/list/meta")
   private static class AutoDeskMetaFieldHandler {
      public final List<AutoDeskMetaField> metafields = Lists.newArrayList();
      private AutoDeskMetaField metafield;

      private AutoDeskMetaFieldHandler() {
      }

      @StartElementHandler("/")
      public void startMetafields() {
         this.metafield = new AutoDeskMetaField();
      }

      @EndElementHandler("fieldID")
      public void fieldID(String var1) {
         this.metafield.fieldID = var1;
      }

      @EndElementHandler("fieldName")
      public void fieldName(String var1) {
         this.metafield.fieldName = var1;
      }

      @EndElementHandler("fieldType")
      public void fieldType(String var1) {
         this.metafield.fieldType = var1;
      }

      @EndElementHandler("editable")
      public void editable(String var1) {
         this.metafield.editable = var1;
      }

      @EndElementHandler("sectionID")
      public void sectionID(String var1) {
         this.metafield.sectionID = var1;
      }

      @EndElementHandler("sectionURI")
      public void sectionURI(String var1) {
         this.metafield.sectionURI = var1;
      }

      @EndElementHandler("/")
      public void endMetafields() {
         this.metafields.add(this.metafield);
      }
   }
}
