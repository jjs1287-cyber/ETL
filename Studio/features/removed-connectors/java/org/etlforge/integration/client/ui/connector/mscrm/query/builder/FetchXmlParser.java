package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.integration.mscrm.metadata.ManyToManyRelationship;
import org.etlforge.integration.mscrm.metadata.Relationship;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.util.SortOrder;
import org.etlforge.util.name.Nameables;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.StartElementHandler;
import org.etlforge.xml.sax.StartOfElement;
import org.xml.sax.SAXException;

public final class FetchXmlParser {
   private final MSCrmObjectLookup objectLookup;

   public FetchXmlParser(MSCrmObjectLookup var1) {
      this.objectLookup = var1;
   }

   public QueryBuilderRootModel parse(String var1) throws FetchXmlParseException {
      try {
         FetchXmlParser.Handler var2 = new FetchXmlParser.Handler();
         KongaSaxParser var5 = new KongaSaxParser(var2);
         var5.parseString(var1);
         return var2.getResult();
      } catch (SAXException var4) {
         Throwable var3 = var4.getCause();
         if (var3 instanceof FetchXmlParseException) {
            throw (FetchXmlParseException)var3;
         } else {
            throw new FetchXmlParseException("Error message here", var4);
         }
      }
   }

   private class Handler {
      private QueryBuilderRootModel rootModel;
      private final Stack<QueryBuilderModel> modelStack = new Stack<>();
      private final Stack<Map<String, MSCrmAttribute>> attributeLookupStack = new Stack<>();
      private final Stack<Filter.FilterBuilder> filterBuilderStack = new Stack<>();
      private MSCrmAttribute conditionAttribute;
      private ConditionOperator conditionOperator;
      private List<String> conditionValues;

      private Handler() {
      }

      public QueryBuilderRootModel getResult() {
         return this.rootModel;
      }

      @StartElementHandler("entity")
      public void startEntity(StartOfElement var1) throws FetchXmlParseException {
         try {
            String var2 = var1.getAttribute("name");
            MSCrmObject var3 = FetchXmlParser.this.objectLookup.describeObject(var2);
            this.pushAttributeLookupMapToStack(var3);
            this.rootModel = new QueryBuilderRootModel(var3);
            this.modelStack.push(this.rootModel);
         } catch (MSCrmMetaDataException var4) {
            throw new FetchXmlParseException(var4);
         }
      }

      private void pushAttributeLookupMapToStack(MSCrmObject var1) {
         this.attributeLookupStack.push(Maps.uniqueIndex(var1.getAttributes(), Nameables.toNameFunction()));
      }

      @StartElementHandler("all-attributes")
      public void allAttributes() {
         this.modelStack.peek().getAttributeSelectionModel().setUseAllAttributes(true);
      }

      @StartElementHandler("attribute")
      public void attribute(StartOfElement var1) throws FetchXmlParseException {
         String var2 = var1.getAttribute("name");
         MSCrmAttribute var3 = this.lookupAttribute(var2);
         this.modelStack.peek().getAttributeSelectionModel().addAttribute(var3);
      }

      private MSCrmAttribute lookupAttribute(String var1) throws FetchXmlParseException {
         MSCrmAttribute var2 = this.attributeLookupStack.peek().get(var1);
         if (var2 == null) {
            throw new FetchXmlParseException(String.format("Unknown attribute %s in object %s", var1, this.currentObject().getName()));
         } else {
            return var2;
         }
      }

      private MSCrmObject currentObject() {
         return this.modelStack.peek().getObject();
      }

      @StartElementHandler("order")
      public void order(StartOfElement var1) {
         String var2 = var1.getAttribute("attribute");
         boolean var3 = KongaXmlUtils.isTrue(var1.attributes(), "descending");
         Order var4 = new Order(var2, var3 ? SortOrder.DESCENDING : SortOrder.ASCENDING);
         this.modelStack.peek().getAttributeOrderModel().add(var4);
      }

      @StartElementHandler("link-entity")
      public void startLinkEntity(StartOfElement var1) throws FetchXmlParseException {
         try {
            String var2 = var1.getAttribute("name");
            String var3 = var1.getAttribute("from");
            String var4 = var1.getAttribute("to");
            boolean var5 = KongaXmlUtils.isTrue(var1.attributes(), "intersect");
            MSCrmObject var6 = FetchXmlParser.this.objectLookup.describeObject(var2);
            QueryBuilderModel var7 = this.modelStack.peek();
            ImmutableList var8 = KongaListUtils.concatenateToImmutableList(var7.getPath(), Collections.singleton(var6));
            Relationship var9 = this.findRelationship(this.currentObject(), var2, var3, var4, var5);
            LinkEntityModel var10 = new LinkEntityModel(var8, var9);
            String var11 = var1.getAttribute("alias");
            boolean var12 = KongaXmlUtils.isTrue(var1.attributes(), "visible");
            String var13 = var1.getAttribute("link-type");
            LinkType var14 = var13 == null ? LinkType.OUTER : LinkType.fromXml(var13);
            var10.setAlias(var11);
            var10.setLinkType(var14);
            var10.setVisible(var12);
            var7.getLinkEntitiesModel().add(var10);
            this.modelStack.push(var10);
            this.pushAttributeLookupMapToStack(var6);
         } catch (MSCrmMetaDataException var15) {
            throw new FetchXmlParseException(var15);
         }
      }

      private Relationship findRelationship(MSCrmObject var1, String var2, String var3, String var4, boolean var5) throws FetchXmlParseException {
         Set var6 = this.findMatchingRelationships(var1, var2, var3, var4, var5);
         switch (var6.size()) {
            case 0:
               throw new FetchXmlParseException(
                  String.format("Cannot find a matching relationship between %s and %s. (from = \"%s\", to = \"%s\"", var1.getName(), var2, var3, var4)
               );
            case 1:
               return (Relationship)var6.iterator().next();
            default:
               throw new NotImplementedYetException("TODO: Pick one of the candidates, but make sure the model does not already contain that relationship.");
         }
      }

      private Set<Relationship> findMatchingRelationships(MSCrmObject var1, String var2, String var3, String var4, boolean var5) {
         HashSet var6 = Sets.newHashSet();
         if (var5) {
            for (ManyToManyRelationship var8 : var1.getManyToManyRelationships()) {
               if (var8.getLinkEntityName().equals(var2)) {
                  var6.add(var8);
               }
            }
         } else {
            for (Relationship var10 : Iterables.concat(var1.getOneToManyRelationships(), var1.getManyToOneRelationships())) {
               if (var10.getLinkEntityName().equals(var2) && var10.getFromAttribute().equals(var3) && var10.getToAttribute().equals(var4)) {
                  var6.add(var10);
               }
            }
         }

         return var6;
      }

      @EndElementHandler("link-entity")
      public void endLinkEntity() {
         this.attributeLookupStack.pop();
         this.modelStack.pop();
      }

      @StartElementHandler("filter")
      public void startFilter(StartOfElement var1) {
         String var2 = var1.getAttribute("type");
         FilterOperator var3 = FilterOperator.fromXml(var2);
         this.filterBuilderStack.push(Filter.builder(var3));
      }

      @EndElementHandler("filter")
      public void endFilter() {
         Filter.FilterBuilder var1 = this.filterBuilderStack.pop();
         Filter var2 = var1.build();
         if (this.filterBuilderStack.isEmpty()) {
            FilterDefinitionModel var3 = this.modelStack.peek().getFilterDefinitionModel();
            var3.setFilterOperator(var2.getOperator());
            var3.setEntries(var2.getEntries());
         } else {
            this.filterBuilderStack.peek().addEntry(var2);
         }
      }

      @StartElementHandler("condition")
      public void startCondition(StartOfElement var1) throws FetchXmlParseException {
         String var2 = var1.getAttribute("attribute");
         this.conditionAttribute = this.lookupAttribute(var2);
         String var3 = var1.getAttribute("operator");
         this.conditionOperator = ConditionOperator.fromXml(var3).orNull();
         if (this.conditionOperator == null) {
            throw new FetchXmlParseException("Unknown condition operator: " + this.conditionOperator);
         }

         this.conditionValues = Lists.newArrayList();
         String var4 = var1.getAttribute("value");
         if (var4 != null) {
            this.conditionValues.add(var4);
         }
      }

      @EndElementHandler("value")
      public void conditionValue(String var1) {
         this.conditionValues.add(var1);
      }

      @EndElementHandler("condition")
      public void endCondition() {
         ConditionValue var1 = this.getConditionValue();
         Condition var2 = new Condition(this.conditionAttribute, this.conditionOperator, var1);
         this.filterBuilderStack.peek().addEntry(var2);
         this.conditionAttribute = null;
         this.conditionOperator = null;
         this.conditionValues = null;
      }

      private ConditionValue getConditionValue() {
         switch (this.conditionOperator.getValueType()) {
            case NO_VALUE:
               return ConditionValue.NONE;
            case SINGLE_VALUE:
               return new SingleConditionValue(this.conditionValues.isEmpty() ? "" : this.conditionValues.get(0));
            case RANGE:
               String var1 = this.conditionValues.isEmpty() ? "" : this.conditionValues.get(0);
               String var2 = this.conditionValues.size() < 2 ? "" : this.conditionValues.get(1);
               return new RangeConditionValue(var1, var2);
            case MULTIPLE_VALUES:
               return new MultiConditionValue(this.conditionValues);
            default:
               throw new RuntimeException("Unexpected condition value type: " + this.conditionOperator.getValueType());
         }
      }
   }
}
