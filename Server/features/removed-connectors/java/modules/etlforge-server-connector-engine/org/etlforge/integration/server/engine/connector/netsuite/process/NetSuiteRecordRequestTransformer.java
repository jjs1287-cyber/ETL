package org.etlforge.integration.server.engine.connector.netsuite.process;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.design.util.XmlSaxHandler;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteRecordRequestTransformer {
   private final int recordLevel;
   private final XsdGlobalElementName objectType;
   private final String objectNamespacePrefix;
   private final ConnectorNetSuiteNamespaces ns;
   private final NetSuiteRecordRefWrapper recordRef;

   NetSuiteRecordRequestTransformer(ConnectorNetSuiteNamespaces var1, int var2, XsdGlobalElementName var3, String var4, NetSuiteRecordRefWrapper var5) {
      this.ns = var1;
      this.recordLevel = var2;
      this.objectType = var3;
      this.objectNamespacePrefix = var4;
      this.recordRef = var5;
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
      NetSuiteRecordRequestTransformer.Handler var3 = new NetSuiteRecordRequestTransformer.Handler(var2);
      SaxParsers.parseFile(new File(var1), var3, true);
   }

   private class Handler extends XmlSaxHandler {
      boolean shouldNoteWrite = false;
      boolean isInsideRecord = false;
      final BufferedWriter writer;
      private final Map<String, String> recordDefinedPrefixNamespaceMap = Maps.newHashMap();
      private final Map<String, String> recordObjectDefinedPrefixNamespaceMap = Maps.newHashMap();
      private boolean isFirstRecord = true;
      private String recordQName = null;
      private String derivedTypeQName = null;
      private String nullfieldListPrefix = null;
      private NetSuiteRequestRecordFieldsHandler fieldsHandler = null;

      public Handler(BufferedWriter var2) {
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

      private boolean isRecord(String var1, String var2) {
         return this.getLevel() == NetSuiteRecordRequestTransformer.this.recordLevel
            && var2.equals("record")
            && var1.equals(NetSuiteRecordRequestTransformer.this.ns.getRecordElementNamespace());
      }

      private void combinePrefixNamespaceDef(String var1, String var2, String var3) {
         this.recordObjectDefinedPrefixNamespaceMap.clear();
         this.recordObjectDefinedPrefixNamespaceMap.putAll(this.getPrefixNamespace().getCurrentPrefixNamespaces());
         String var4 = NetSuiteRecordRequestTransformer.this.objectType.getNamespace();
         String var5 = this.getPrefixNamespace().getPrefix(var4);
         if (var5 == null) {
            var5 = addDefinedPrefixNamespaceMap(NetSuiteRecordRequestTransformer.this.objectNamespacePrefix, var4, this.recordObjectDefinedPrefixNamespaceMap);
         }

         this.derivedTypeQName = XmlElementUtils.getQName(var5, NetSuiteRecordRequestTransformer.this.objectType.getLocalName());
         String var6 = XmlElementUtils.getPrefixFromQName(this.recordQName);
         String var7 = this.recordObjectDefinedPrefixNamespaceMap.get(var6);
         if (var7 != null && !var7.equals(NetSuiteRecordRequestTransformer.this.ns.getRecordElementNamespace())) {
            String var8 = addDefinedPrefixNamespaceMap(
               NetSuiteRecordRequestTransformer.this.ns.getRecordElementNamespacePrefix(),
               NetSuiteRecordRequestTransformer.this.ns.getRecordElementNamespace(),
               this.recordObjectDefinedPrefixNamespaceMap
            );
            this.recordQName = XmlElementUtils.getQName(var8, "record");
         }

         for (Entry var9 : this.recordDefinedPrefixNamespaceMap.entrySet()) {
            String var10 = (String)var9.getKey();
            String var11 = (String)var9.getValue();
            if (!this.recordObjectDefinedPrefixNamespaceMap.containsKey(var10)) {
               this.recordObjectDefinedPrefixNamespaceMap.put(var10, var11);
            }
         }
      }

      private void startElementDerivedType(String var1, String var2, String var3, Attributes var4) {
         if (this.isFirstRecord) {
            this.isFirstRecord = false;
            this.combinePrefixNamespaceDef(var1, var2, var3);
         }

         XmlElementUtils.startElementTagDerivedType(this.writer, this.recordQName, var4, this.recordObjectDefinedPrefixNamespaceMap, this.derivedTypeQName);
         this.fieldsHandler = new NetSuiteRequestRecordFieldsHandler(
            NetSuiteRecordRequestTransformer.this.recordLevel, NetSuiteRecordRequestTransformer.this.recordRef
         );
         this.nullfieldListPrefix = XmlElementUtils.getPrefixFromQName(var3);
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.shouldNoteWrite = this.isRecord(var1, var2);
         if (this.shouldNoteWrite) {
            this.isInsideRecord = true;
            this.recordQName = var3;
            this.recordDefinedPrefixNamespaceMap.clear();
            this.recordDefinedPrefixNamespaceMap.putAll(this.getPrefixNamespace().getCurrentPrefixNamespaces());
         } else {
            if (this.isInsideRecord) {
               if (this.getLevel() == NetSuiteRecordRequestTransformer.this.recordLevel + 1) {
                  this.startElementDerivedType(var1, var2, var3, var4);
               } else {
                  this.fieldsHandler.startElementImpl(this.getLevel(), var1, var2, var3, var4, this.prefixNamespace.getCurrentPrefixNamespaces());
               }
            } else {
               if (this.isRoot()) {
                  Map var5 = this.getPrefixNamespace().getCurrentPrefixNamespaces();
                  var5.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
               }

               XmlElementUtils.startElementTag(this.writer, var3, var4, this.getPrefixNamespace().getCurrentPrefixNamespaces());
            }
         }
      }

      private void writeNullFieldList(List<String> var1) {
         boolean var2 = var1.size() > 0;
         if (var2) {
            HashSet var3 = Sets.newHashSet();
            String var4 = XmlElementUtils.getQName(this.nullfieldListPrefix, "nullFieldList");
            XmlElementUtils.startElementTag(this.writer, var4);

            for (String var6 : var1) {
               if (!var3.contains(var6)) {
                  var3.add(var6);
                  String var7 = XmlElementUtils.getQName(this.nullfieldListPrefix, "name");
                  XmlElementUtils.startElementTag(this.writer, var7);
                  XmlElementUtils.appendXmlValue(this.writer, var6);
                  XmlElementUtils.endElementTag(this.writer, var7);
               }
            }

            XmlElementUtils.endElementTag(this.writer, var4);
         }
      }

      private void endElementDerivedType(String var1, String var2, String var3) {
         List var4 = this.fieldsHandler.getNullFieldList();
         this.writeNullFieldList(var4);
         XmlElementUtils.appendString(this.writer, this.fieldsHandler.getFieldsContent());
         XmlElementUtils.endElementTag(this.writer, this.recordQName);
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         this.shouldNoteWrite = this.isRecord(var1, var2);
         if (this.shouldNoteWrite) {
            this.isInsideRecord = false;
         } else {
            if (this.isInsideRecord) {
               if (this.getLevel() == NetSuiteRecordRequestTransformer.this.recordLevel + 1) {
                  this.endElementDerivedType(var1, var2, var3);
               } else {
                  this.fieldsHandler.endElementImpl(this.getLevel(), var1, var2, var3);
               }
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
            if (this.isInsideRecord) {
               if (this.getLevel() > NetSuiteRecordRequestTransformer.this.recordLevel) {
                  this.fieldsHandler.characters(var1, var2, var3);
               }
            } else {
               XmlElementUtils.appendXmlValue(this.writer, var4);
            }
         }
      }
   }
}
