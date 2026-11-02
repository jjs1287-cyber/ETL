package org.etlforge.integration.salesforce.wsdl.partner.crom;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Xml;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.util.name.Nameable;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public final class QueryResponseCromBuilder extends AbstractCallStructureBuilder {
   private final SObjectSchema object;
   private SObjectSchemaProvider schemaProvider;

   public QueryResponseCromBuilder(SObjectSchema var1) {
      super(AbstractCallStructureBuilder.Mode.RESPONSE, new LimitInfoCromHeader());
      this.object = var1;
   }

   public void includeRelationships(SObjectSchemaProvider var1) {
      this.schemaProvider = var1;
   }

   @Override
   protected void populateBody(CROM var1) throws MappingTreeStructureException {
      this.buildQueryResponse(var1);
      CROM var2 = this.getTransactionNode();
      if (var2 != null) {
         this.insertRequestStructure(var2);
      }
   }

   private void buildQueryResponse(CROM var1) throws MappingTreeStructureException {
      CROM var2 = createElementInPartnerNamespace(var1, "queryResponse");
      CROM var3 = createElementInPartnerNamespace(var2, "result");
      this.insertDoneNode(var3);
      this.insertQueryLocatorNode(var3);
      this.insertRecordsNode(var3);
      this.insertSizeNode(var3);
   }

   private void insertDoneNode(CROM var1) {
      CROM var2 = createElementInPartnerNamespace(var1, "done");
      createBooleanValueNode(var2);
   }

   private void insertQueryLocatorNode(CROM var1) {
      CROM var2 = createElementInPartnerNamespace(var1, "queryLocator");
      CromUtils.makeNillable(var2);
      createStringValueNode(var2);
   }

   private void insertRecordsNode(CROM var1) throws MappingTreeStructureException {
      CROM var2 = createElementInPartnerNamespace(var1, "records");
      CromUtils.makeOptionalLoop(var2);
      CromUtils.makeNillable(var2);
      this.insertObjectNode(var2);
   }

   private void insertObjectNode(CROM var1) throws MappingTreeStructureException {
      if (this.schemaProvider == null) {
         FlatObjectCromBuilder var2 = new FlatObjectCromBuilder(this.object);
         var2.setAllGroup(true);
         var2.build(var1);
      } else {
         QueryResponseCromBuilder.RelationshipTreeBuilder var3 = new QueryResponseCromBuilder.RelationshipTreeBuilder(this.object, this.schemaProvider);
         var3.build(var1);
         this.setTypeMap(var3.getTypeMap());
      }
   }

   private void insertSizeNode(CROM var1) {
      CROM var2 = createElementInPartnerNamespace(var1, "size");
      createIntValueNode(var2);
   }

   private void insertRequestStructure(CROM var1) throws MappingTreeStructureException {
      QueryRequestCromBuilder var2 = new QueryRequestCromBuilder();
      CROM var3 = createElementInETLForgeNamespace(var1, "request");
      var2.setParent(var3);
      var2.getCromTree();
   }

   private static Collection<QueryResponseCromBuilder.CromChild> collectChildNodeDescriptors(SObjectSchema var0, boolean var1, boolean var2) {
      TreeSet var3 = Sets.newTreeSet();

      for (SObjectSchema.Field var5 : var0.getFields()) {
         var3.add(new QueryResponseCromBuilder.FieldNode(var5));
      }

      if (var1) {
         for (SObjectSchema.Parent var8 : var0.getParents()) {
            var3.add(new QueryResponseCromBuilder.ParentNode(var8));
         }
      }

      if (var2) {
         for (SObjectSchema.Child var9 : var0.getChildren()) {
            var3.add(new QueryResponseCromBuilder.ChildNode(var9));
         }
      }

      return var3;
   }

   private static class ChildNode extends QueryResponseCromBuilder.CromChild {
      private final SObjectSchema.Child child;

      public ChildNode(SObjectSchema.Child var1) {
         this.child = var1;
      }

      public SObjectSchema.Child getChild() {
         return this.child;
      }

      @Override
      public String getName() {
         return this.child.getName();
      }
   }

   private abstract static class CromChild implements Comparable<QueryResponseCromBuilder.CromChild>, Nameable {
      private CromChild() {
      }

      public final int compareTo(QueryResponseCromBuilder.CromChild var1) {
         String var2 = this.getName();
         String var3 = var1.getName();
         if (var2.equals("Id")) {
            return var3.equals("Id") ? 0 : -1;
         } else {
            return var3.equals("Id") ? 1 : var2.compareTo(var3);
         }
      }

      @Override
      public String toString() {
         return Objects.toStringHelper(this).add("name", this.getName()).toString();
      }
   }

   private static class FieldNode extends QueryResponseCromBuilder.CromChild {
      private final SObjectSchema.Field field;

      public FieldNode(SObjectSchema.Field var1) {
         this.field = var1;
      }

      public SObjectSchema.Field getField() {
         return this.field;
      }

      @Override
      public String getName() {
         return this.field.getName();
      }
   }

   private static class ParentNode extends QueryResponseCromBuilder.CromChild {
      private final SObjectSchema.Parent parent;

      public ParentNode(SObjectSchema.Parent var1) {
         this.parent = var1;
      }

      @Override
      public String getName() {
         return this.parent.getName();
      }

      public String getObjectType() {
         return this.parent.getObjectTypeInQuery();
      }
   }

   private static class RelationshipTreeBuilder {
      private final SObjectSchema rootObject;
      private final SObjectSchemaProvider schemaProvider;
      private final Map<String, CROM> typeMap = Maps.newHashMap();
      private final Set<String> inProcess = Sets.newHashSet();

      public RelationshipTreeBuilder(SObjectSchema var1, SObjectSchemaProvider var2) {
         this.rootObject = var1;
         this.schemaProvider = var2;
      }

      public CROM build(CROM var1) throws MappingTreeStructureException {
         CROM var2 = AbstractCallStructureBuilder.createElementInSObjectNamespace(var1, this.rootObject.getName());
         var2.addType(2097152L);
         List var3 = this.populateObjectNode(this.rootObject, var2, true, true);
         QueryResponseCromBuilder.RelationshipTreeBuilder.TypeBuilder var4 = new QueryResponseCromBuilder.RelationshipTreeBuilder.TypeBuilder(var3);
         var4.build();
         return var2;
      }

      private List<String> populateObjectNode(SObjectSchema var1, CROM var2, boolean var3, boolean var4) {
         ArrayList var5 = Lists.newArrayList();

         for (QueryResponseCromBuilder.CromChild var8 : QueryResponseCromBuilder.collectChildNodeDescriptors(var1, var3, var4)) {
            String var9 = null;
            if (var8 instanceof QueryResponseCromBuilder.FieldNode) {
               CromUtils.addField(((QueryResponseCromBuilder.FieldNode)var8).getField(), var2);
            } else if (var8 instanceof QueryResponseCromBuilder.ChildNode) {
               SObjectSchema.Child var10 = ((QueryResponseCromBuilder.ChildNode)var8).getChild();
               var9 = var10.getObjectType();
               this.buildRelationshipNode(var2, var10.getName(), var9, true);
            } else if (var8 instanceof QueryResponseCromBuilder.ParentNode) {
               QueryResponseCromBuilder.ParentNode var11 = (QueryResponseCromBuilder.ParentNode)var8;
               var9 = var11.getObjectType();
               this.buildRelationshipNode(var2, var11.getName(), var9, false);
            }

            if (var9 != null && this.needsProcessing(var9)) {
               var5.add(var9);
            }
         }

         return var5;
      }

      private void buildRelationshipNode(CROM var1, String var2, String var3, boolean var4) {
         CROM var5 = this.createRelationCromNodeShell(var1, var2, var3);
         CromUtils.makeNillable(var5);
         CROM_Xml var6 = (CROM_Xml)var5.getExtension();
         var6.setMinUse(0);
         if (var4) {
            var5.addType(8L);
            var6.setMaxUse(Integer.MAX_VALUE);
         } else {
            var5.addType(32L);
            var6.setMaxUse(1);
         }
      }

      private CROM createRelationCromNodeShell(CROM var1, String var2, String var3) {
         CROM var4 = AbstractCallStructureBuilder.createElementInSObjectNamespace(var1, var2);
         var4.addType(2097152L);
         CROM_Xml var5 = (CROM_Xml)var4.getExtension();
         var5.setSchemaType(var3);
         var5.setSchemaTypeNamespace("urn:sobject.partner.soap.sforce.com");
         return var4;
      }

      private static String getFullTypeName(String var0) {
         ExpandedName var1 = XmlNameFactory.conforming().newExpandedName("urn:sobject.partner.soap.sforce.com", var0);
         return var1.toString();
      }

      private boolean needsProcessing(String var1) {
         return !this.typeMap.containsKey(getFullTypeName(var1)) && !this.inProcess.contains(var1);
      }

      public Map<String, CROM> getTypeMap() {
         return this.typeMap;
      }

      private class TypeBuilder {
         private final List<String> typesToBuild;
         private final Set<String> nextLevelTypes = Sets.newHashSet();

         public TypeBuilder(List<String> var2) {
            this.typesToBuild = Lists.newArrayList(Iterables.filter(var2, new Predicate<String>() {
               public boolean apply(String var1) {
                  return RelationshipTreeBuilder.this.needsProcessing(var1);
               }
            }));
         }

         public void build() throws MappingTreeStructureException {
            if (!this.typesToBuild.isEmpty()) {
               try {
                  this.buildThisLevel();
                  this.buildNextLevel();
               } catch (SalesforceException var2) {
                  throw new MappingTreeStructureException(var2);
               }
            }
         }

         private void buildThisLevel() throws SalesforceException {
            for (SObjectSchema var3 : RelationshipTreeBuilder.this.schemaProvider.describeObjects(this.typesToBuild)) {
               this.buildTypeDefinition(var3);
            }
         }

         private void buildTypeDefinition(SObjectSchema var1) {
            String var2 = var1.getName();
            RelationshipTreeBuilder.this.inProcess.add(var2);
            CROM var3 = AbstractCallStructureBuilder.createElementInSObjectNamespace(null, var2);
            var3.addType(2097152L);
            var3.addType(134217728L);
            boolean var4 = true;
            boolean var5 = false;

            for (String var8 : RelationshipTreeBuilder.this.populateObjectNode(var1, var3, var4, var5)) {
               if (RelationshipTreeBuilder.this.needsProcessing(var8)) {
                  this.nextLevelTypes.add(var8);
               }
            }

            RelationshipTreeBuilder.this.inProcess.remove(var2);
            RelationshipTreeBuilder.this.typeMap.put(QueryResponseCromBuilder.RelationshipTreeBuilder.getFullTypeName(var2), var3);
         }

         private void buildNextLevel() throws MappingTreeStructureException {
            if (!this.nextLevelTypes.isEmpty()) {
               QueryResponseCromBuilder.RelationshipTreeBuilder.TypeBuilder var1 = RelationshipTreeBuilder.this.new TypeBuilder(
                  Lists.newArrayList(this.nextLevelTypes)
               );
               var1.build();
            }
         }
      }
   }
}
