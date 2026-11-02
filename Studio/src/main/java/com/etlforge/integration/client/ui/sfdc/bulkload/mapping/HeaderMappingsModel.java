package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.bulkload.HeaderMappings;
import org.etlforge.integration.salesforce.bulkload.Mapping;
import org.etlforge.integration.salesforce.bulkload.TargetFields;
import org.etlforge.integration.salesforce.wsdl.AutoMappableFieldFilter;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;

public final class HeaderMappingsModel extends AbstractPropertyBasedModel {
   public static final String MAPPINGS = "mappings";
   public static final String SOURCE_AND_TARGET_DATA = "sourceAndTargetData";
   private LinkedHashSet<String> sourceHeader = Sets.newLinkedHashSet();
   private final ListProperty<Mapping> mappings = this.newListProperty("mappings");
   private TargetFields targetFields = TargetFields.empty();
   private BulkAction action;
   private final Predicate<Mapping> validMapping = new Predicate<Mapping>() {
      public boolean apply(Mapping var1) {
         return HeaderMappingsModel.this.sourceHeader.contains(var1.source) && HeaderMappingsModel.this.targetFields.contains(var1.target);
      }
   };

   public HeaderMappingsModel(BulkAction var1) {
      this.action = var1;
   }

   public synchronized List<RelatedObject> getRelatedObjects() {
      return Lists.newArrayList(this.targetFields.getRelatedObjects());
   }

   public synchronized HeaderMappings getHeaderMappings() {
      return new HeaderMappings(this.getSourceHeader(), this.targetFields, this.getMapList());
   }

   public synchronized void setHeaderMappings(HeaderMappings var1) {
      boolean var2 = false;
      this.setSourceAndTargetFieldsImpl(var1.getSourceHeader(), var1.getTargetFields(), var2);
      this.setMapList(var1.getMapList());
   }

   private void setSourceAndTargetFieldsImpl(List<String> var1, TargetFields var2, boolean var3) {
      List var4 = this.getMapList();
      this.sourceHeader = new LinkedHashSet<>(var1);
      if (this.action == BulkAction.CREATE) {
         this.targetFields = var2.withoutIdField();
      } else {
         this.targetFields = var2;
      }

      this.sendChange("sourceAndTargetData", null, null);
      this.mappings.set((List<Mapping>)null);
      if (var3) {
         this.setMapList(var4);
      }
   }

   public synchronized boolean hasUnmappedSourceFields() {
      return this.mappings.size() < this.sourceHeader.size();
   }

   public synchronized List<String> getSourceHeader() {
      return Lists.newArrayList(this.sourceHeader);
   }

   public synchronized TargetFields getTargetFields() {
      return this.targetFields;
   }

   public synchronized List<SObjectSchema.Field> getObjectFields() {
      return Lists.newArrayList(this.targetFields.getObjectFields());
   }

   public synchronized List<SObjectSchema.Field> getAutoMappableTargetFields() {
      AutoMappableFieldFilter var1 = new AutoMappableFieldFilter(this.action);
      return KongaListUtils.filter(this.getObjectFields(), var1);
   }

   public synchronized SObjectSchema.Field getField(String var1) {
      return this.targetFields.getField(var1);
   }

   public synchronized void updateAndRetainMappings(List<String> var1, TargetFields var2) {
      this.updateAndRetainMappings(this.action, var1, var2);
   }

   public synchronized void updateAndRetainMappings(BulkAction var1, List<String> var2, TargetFields var3) {
      this.action = var1;
      boolean var4 = true;
      this.setSourceAndTargetFieldsImpl(var2, var3, var4);
   }

   public synchronized List<Mapping> getMapList() {
      return this.mappings.get();
   }

   public synchronized void setMapList(List<Mapping> var1) {
      List var2 = KongaListUtils.filter(var1, this.validMapping);
      this.mappings.set(var2);
   }

   public void addMappings(List<Mapping> var1) {
      List var2 = KongaListUtils.filter(var1, this.validMapping);
      this.mappings.addAll(var2);
   }

   public synchronized void autoMap() {
      BulkLoadAutoMapper var1 = new BulkLoadAutoMapper(this);
      var1.run();
   }
}
