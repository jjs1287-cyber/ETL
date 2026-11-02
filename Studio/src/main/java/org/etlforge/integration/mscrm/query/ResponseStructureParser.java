package org.etlforge.integration.mscrm.query;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.util.name.Nameables;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public final class ResponseStructureParser {
   private final MSCrmObjectLookup objectLookup;
   private final Map<String, MSCrmObject> knownObjects = Maps.newHashMap();

   public ResponseStructureParser(MSCrmObjectLookup var1) {
      this.objectLookup = Preconditions.checkNotNull(var1, "objectLookup");
   }

   public ResponseStructure parse(String var1) throws MSCrmMetaDataException {
      try {
         ResponseStructureParser.XmlHandler var2 = new ResponseStructureParser.XmlHandler();
         KongaSaxParser var3 = new KongaSaxParser(var2);
         var3.parseString(var1);
         ResponseStructure.Record var4 = var2.getResult();
         return new ResponseStructure(var4);
      } catch (SAXException var5) {
         throw new MSCrmMetaDataException(var5);
      }
   }

   private static class Entity {
      private final String name;
      private final MSCrmObject object;
      private final ImmutableMap<String, MSCrmAttribute> attributeLookup;
      private final List<MSCrmAttribute> selectedAttributes = Lists.newArrayList();
      private final List<ResponseStructureParser.Entity> children = Lists.newArrayList();
      private boolean allAttributes;
      private boolean primaryIdIsSelected;

      public Entity(String var1, MSCrmObject var2) {
         this.name = var1;
         this.object = var2;
         this.attributeLookup = Nameables.toImmutableMap(var2.getAttributes());
      }

      public void addAttribute(String var1) throws MSCrmMetaDataException {
         MSCrmAttribute var2 = this.attributeLookup.get(var1);
         if (var2 == null) {
            throw new MSCrmMetaDataException("Invalid fetchXML: Unknown " + this.object.getName() + " attribute: " + var1);
         }

         this.selectedAttributes.add(var2);
         if (var1.equals(this.object.getPrimaryIdAttribute())) {
            this.primaryIdIsSelected = true;
         }
      }

      public void useAllAttributes() {
         this.allAttributes = true;
      }

      public void addChild(ResponseStructureParser.Entity var1) {
         this.children.add(var1);
      }

      public ResponseStructure.Record toRecord() {
         Builder var1 = ImmutableList.builder();

         for (ResponseStructureParser.Entity var3 : this.children) {
            var1.add(var3.toRecord());
         }

         ImmutableList var4 = this.getAttributesToInclude();
         return new ResponseStructure.Record(this.name, var4, this.allAttributes, var1.build());
      }

      private ImmutableList<MSCrmAttribute> getAttributesToInclude() {
         if (this.allAttributes) {
            return this.object.getAttributes();
         }

         this.addPrimaryIdIfNecessary();
         return ImmutableList.copyOf(this.selectedAttributes);
      }

      private void addPrimaryIdIfNecessary() {
         if (!this.primaryIdIsSelected) {
            MSCrmAttribute var1 = this.attributeLookup.get(this.object.getPrimaryIdAttribute());
            if (var1 != null) {
               this.selectedAttributes.add(var1);
            }
         }
      }
   }

   private class XmlHandler {
      private final Stack<ResponseStructureParser.Entity> entities = new Stack<>();
      private ResponseStructureParser.Entity root;

      private XmlHandler() {
      }

      public ResponseStructure.Record getResult() throws MSCrmMetaDataException {
         if (this.root == null) {
            throw new MSCrmMetaDataException("Invalid fetchXML: No <entity> element.");
         } else {
            return this.root.toRecord();
         }
      }

      @StartElementHandler("fetch/entity")
      public void startEntity(Attributes var1) throws MSCrmMetaDataException {
         String var2 = var1.getValue("name");
         this.startEntity(var2, var2);
      }

      private void startEntity(String var1, String var2) throws MSCrmMetaDataException {
         MSCrmObject var3 = this.getObjectDefinition(var2);
         ResponseStructureParser.Entity var4 = new ResponseStructureParser.Entity(var1, var3);
         this.entities.push(var4);
      }

      private MSCrmObject getObjectDefinition(String var1) throws MSCrmMetaDataException {
         MSCrmObject var2 = ResponseStructureParser.this.knownObjects.get(var1);
         if (var2 == null) {
            var2 = ResponseStructureParser.this.objectLookup.describeObject(var1);
            ResponseStructureParser.this.knownObjects.put(var1, var2);
         }

         return var2;
      }

      @StartElementHandler
      public void attribute(Attributes var1) throws MSCrmMetaDataException {
         String var2 = var1.getValue("name");
         this.entities.peek().addAttribute(var2);
      }

      @StartElementHandler("all-attributes")
      public void allAttributes() {
         this.entities.peek().useAllAttributes();
      }

      @StartElementHandler("link-entity")
      public void startLinkEntity(Attributes var1) throws MSCrmMetaDataException {
         String var2 = var1.getValue("name");
         String var3 = var1.getValue("to");
         String var4 = var1.getValue("alias");
         String var5 = Strings.isNullOrEmpty(var4) ? var3 : var4;
         this.startEntity(var5, var2);
      }

      @EndElementHandler("link-entity")
      public void endLinkEntity() throws MSCrmMetaDataException {
         ResponseStructureParser.Entity var1 = this.entities.pop();
         if (this.entities.isEmpty()) {
            throw new MSCrmMetaDataException("Invalid fetchXML: link-entity without a parent.");
         }

         this.entities.peek().addChild(var1);
      }

      @EndElementHandler("fetch/entity")
      public void endEntity() throws MSCrmMetaDataException {
         ResponseStructureParser.Entity var1 = this.entities.pop();
         if (this.entities.isEmpty()) {
            this.root = var1;
         } else {
            throw new MSCrmMetaDataException("Invalid fetchXML: Multiple <entry> elements.");
         }
      }
   }
}
