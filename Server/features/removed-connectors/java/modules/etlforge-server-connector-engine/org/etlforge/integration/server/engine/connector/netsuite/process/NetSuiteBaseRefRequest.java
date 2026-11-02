package org.etlforge.integration.server.engine.connector.netsuite.process;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.design.util.XmlSaxHandler;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteBaseRefRequest {
   private final ConnectorNetSuiteNamespaces connectorNetsuiteNamespaces;

   public NetSuiteBaseRefRequest(ConnectorNetSuiteNamespaces var1) {
      this.connectorNetsuiteNamespaces = var1;
   }

   public void transform(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = Files.newWriter(new File(var2), Charsets.UTF_8);
         this.transform(var1, var3);
      } catch (SAXException var9) {
         throw new ConnectorServerRuntimeException(var9);
      } catch (IOException var10) {
         throw new ConnectorServerRuntimeException(var10);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   public void transform(String var1, BufferedWriter var2) throws SAXException, IOException {
      NetSuiteBaseRefRequest.NetSuiteBaseRefRequestHandler var3 = new NetSuiteBaseRefRequest.NetSuiteBaseRefRequestHandler(var2);
      SaxParsers.parseFile(new File(var1), var3, true);
   }

   public static void main(String[] var0) {
      try {
         String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/NETSUITE/Sample/SampleRecordConversion/";
         String var2 = var1 + "delete_jb_oneof.xml";
         String var3 = var1 + "delete_jb_oneof_ns.xml";
         NetSuiteBaseRefRequest var4 = new NetSuiteBaseRefRequest(null);
         var4.transform(var2, var3);
      } catch (Throwable var5) {
         var5.printStackTrace();
      }
   }

   private class NetSuiteBaseRefRequestHandler extends XmlSaxHandler {
      final BufferedWriter writer;
      private final Map<String, String> baseRefDefinedPrefixNamespaceMap = Maps.newHashMap();
      private final Map<String, String> baseRefObjectDefinedPrefixNamespaceMap = Maps.newHashMap();
      private boolean isFirstRecord = true;
      private String baseRefQName = null;
      private boolean isBaseRef = false;
      private int baseRefDerivedLevel = -1;
      private boolean shouldNoteWrite = false;

      public NetSuiteBaseRefRequestHandler(BufferedWriter var2) {
         this.writer = var2;
         this.append(XmlUtils.xmlUtf8Header);
      }

      private void append(String var1) {
         try {
            this.writer.append(var1);
         } catch (IOException var3) {
            throw new ConnectorServerRuntimeException(var3);
         }
      }

      private boolean isBaseElement(String var1, String var2) {
         return var2.equals("baseRef") && var1.equals(NetSuiteBaseRefRequest.this.connectorNetsuiteNamespaces.getBaseRefElementNamespace());
      }

      private void combinePrefixNamespaceDef(String var1, String var2, String var3) {
         this.baseRefObjectDefinedPrefixNamespaceMap.clear();
         this.baseRefObjectDefinedPrefixNamespaceMap.putAll(this.getPrefixNamespace().getCurrentPrefixNamespaces());
         String var4 = XmlElementUtils.getPrefixFromQName(this.baseRefQName);
         String var5 = this.baseRefObjectDefinedPrefixNamespaceMap.get(var4);
         if (var5 != null && !var5.equals(NetSuiteBaseRefRequest.this.connectorNetsuiteNamespaces.getBaseRefElementNamespace())) {
            String var6 = addDefinedPrefixNamespaceMap(
               NetSuiteBaseRefRequest.this.connectorNetsuiteNamespaces.getBaseRefElementNamespacePrefix(),
               NetSuiteBaseRefRequest.this.connectorNetsuiteNamespaces.getBaseRefElementNamespace(),
               this.baseRefObjectDefinedPrefixNamespaceMap
            );
            this.baseRefQName = XmlElementUtils.getQName(var6, "baseRef");
         }

         for (Entry var7 : this.baseRefDefinedPrefixNamespaceMap.entrySet()) {
            String var8 = (String)var7.getKey();
            String var9 = (String)var7.getValue();
            if (!this.baseRefObjectDefinedPrefixNamespaceMap.containsKey(var8)) {
               this.baseRefObjectDefinedPrefixNamespaceMap.put(var8, var9);
            }
         }
      }

      private boolean isBaseRefDerived() {
         return this.isBaseRef && this.baseRefDerivedLevel == this.getLevel();
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.shouldNoteWrite = this.isBaseElement(var1, var2);
         if (this.shouldNoteWrite) {
            this.isBaseRef = true;
            this.baseRefQName = var3;
            this.baseRefDerivedLevel = this.getLevel() + 1;
            this.baseRefDefinedPrefixNamespaceMap.clear();
            this.baseRefDefinedPrefixNamespaceMap.putAll(this.getPrefixNamespace().getCurrentPrefixNamespaces());
         } else {
            if (this.isBaseRef) {
               if (this.isBaseRefDerived()) {
                  if (this.isFirstRecord) {
                     this.isFirstRecord = false;
                     this.combinePrefixNamespaceDef(var1, var2, var3);
                  }

                  String var5 = var4.getValue("xsi:type");
                  var5 = Strings.isNullOrEmpty(var5) ? var3 : null;
                  XmlElementUtils.startElementTagDerivedType(this.writer, this.baseRefQName, var4, this.baseRefObjectDefinedPrefixNamespaceMap, var5);
               } else {
                  XmlElementUtils.startElementTag(this.writer, var3, var4, this.prefixNamespace.getCurrentPrefixNamespaces());
               }
            } else {
               if (this.isRoot()) {
                  Map var7 = this.getPrefixNamespace().getCurrentPrefixNamespaces();
                  var7.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
               }

               XmlElementUtils.startElementTag(this.writer, var3, var4, this.prefixNamespace.getCurrentPrefixNamespaces());
            }
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         this.shouldNoteWrite = this.isBaseElement(var1, var2);
         if (this.shouldNoteWrite) {
            this.isBaseRef = false;
         } else {
            if (this.isBaseRefDerived()) {
               XmlElementUtils.endElementTag(this.writer, this.baseRefQName);
            } else {
               XmlElementUtils.endElementTag(this.writer, var3);
            }
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         super.characters(var1, var2, var3);
         if (!this.shouldNoteWrite) {
            String var4 = new String(var1, var2, var3);
            XmlElementUtils.appendXmlValue(this.writer, var4);
         }
      }
   }
}
