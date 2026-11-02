package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.SAXException;

public class AutoDeskItem {
   String dmsid = "";
   String versionid = "";

   AutoDeskItem() {
   }

   public static String getAutoDeskItemDmsID(String var0) {
      AutoDeskItem var1 = getAutoDeskItem(var0);
      return var1 == null ? "" : var1.dmsid;
   }

   public static String getAutoDeskItemVersionId(String var0) {
      AutoDeskItem var1 = getAutoDeskItem(var0);
      return var1 == null ? "" : var1.versionid;
   }

   public static AutoDeskItem getAutoDeskItem(String var0) {
      if (var0.isEmpty()) {
         return null;
      }

      List var2 = parseAutoDeskItemResponse(var0, "payload/item/details");
      return var2.size() < 1 ? null : (AutoDeskItem)var2.get(0);
   }

   private static List<AutoDeskItem> parseAutoDeskItemResponse(String var0, String var1) {
      KongaSaxParser var2 = new KongaSaxParser();
      AutoDeskItem.AutoDeskItemHandler var3 = new AutoDeskItem.AutoDeskItemHandler(var1);
      var2.registerHandler(var3);

      try {
         var2.parseString(var0);
      } catch (SAXException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      }

      return var3.items;
   }

   private static class AutoDeskItemHandler {
      @RelativePathRoot
      public final String relativePathRoot;
      public final List<AutoDeskItem> items = Lists.newArrayList();
      private AutoDeskItem item;

      AutoDeskItemHandler(String var1) {
         this.relativePathRoot = var1;
      }

      @StartElementHandler("/")
      public void start() {
         this.item = new AutoDeskItem();
      }

      @EndElementHandler("dmsID")
      public void dmsID(String var1) {
         this.item.dmsid = var1;
      }

      @EndElementHandler("versionID")
      public void versionID(String var1) {
         this.item.versionid = var1;
      }

      @EndElementHandler("/")
      public void end(String var1) {
         this.items.add(this.item);
      }
   }
}
