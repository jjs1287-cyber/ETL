package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class AutoDeskGetOperationOptions {
   String page;
   String size;
   boolean filterDeleted;
   String dmsID;
   String alias;
   boolean isSigleItem;
   boolean isItemsByAlias;

   AutoDeskGetOperationOptions() {
      this.page = this.size = this.dmsID = this.alias = "";
      this.filterDeleted = this.isSigleItem = this.isItemsByAlias = false;
   }

   private void initListSetting() {
      this.isSigleItem = !Strings.isNullOrEmpty(this.dmsID);
      this.isItemsByAlias = this.isSigleItem ? false : !Strings.isNullOrEmpty(this.alias);
   }

   boolean isSigleItem() {
      return this.isSigleItem;
   }

   boolean isItemsByAlias() {
      return this.isItemsByAlias;
   }

   String toGetSettingParameters() {
      if (this.isSigleItem()) {
         return "/" + this.dmsID;
      }

      if (this.isItemsByAlias()) {
         return "/alias/" + this.alias;
      }

      StringBuilder var1 = new StringBuilder();
      if (!Strings.isNullOrEmpty(this.page)) {
         var1.append("&page=" + this.page);
      }

      if (!Strings.isNullOrEmpty(this.size)) {
         var1.append("&size=" + this.size);
      }

      if (this.filterDeleted) {
         var1.append("&filterDeleted=true");
      }

      return var1.length() > 1 ? "?" + var1.substring(1) : "";
   }

   static AutoDeskGetOperationOptions parseGetListSetting(String var0) {
      File var1 = new File(var0);
      if (var1.length() == 0L) {
         return new AutoDeskGetOperationOptions();
      }

      KongaSaxParser var2 = new KongaSaxParser();
      AutoDeskGetOperationOptions.AutoDeskGetOperationOptionsHandler var3 = new AutoDeskGetOperationOptions.AutoDeskGetOperationOptionsHandler();
      var2.registerHandler(var3);

      try {
         var2.parseFile(var1);
         var3.options.initListSetting();
         return var3.options;
      } catch (SAXException var5) {
         throw new ConnectorServerRuntimeException(var5);
      } catch (IOException var6) {
         throw new ConnectorServerRuntimeException(var6);
      }
   }

   @RelativePathRoot("GetListSetting")
   private static class AutoDeskGetOperationOptionsHandler {
      AutoDeskGetOperationOptions options = new AutoDeskGetOperationOptions();

      private AutoDeskGetOperationOptionsHandler() {
      }

      private String trim(String var1) {
         return var1 == null ? var1 : var1.trim();
      }

      @EndElementHandler("ListSetting/Page")
      public void Page(String var1) {
         this.options.page = this.trim(var1);
      }

      @EndElementHandler("ListSetting/Size")
      public void Size(String var1) {
         this.options.size = this.trim(var1);
      }

      @EndElementHandler("ListSetting/FilterDeleted")
      public void FilterDeleted(Boolean var1) {
         this.options.filterDeleted = var1;
      }

      @EndElementHandler("SigleItem/dmsID")
      public void dmsID(String var1) {
         this.options.dmsID = this.trim(var1);
      }

      @EndElementHandler("ItemsByAlias/alias")
      public void alias(String var1) {
         this.options.alias = this.trim(var1);
      }
   }
}
