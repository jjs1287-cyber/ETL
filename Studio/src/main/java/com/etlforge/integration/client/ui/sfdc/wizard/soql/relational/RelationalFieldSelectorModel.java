package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.collect.Maps;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderModel;
import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class RelationalFieldSelectorModel extends AbstractPropertyBasedModel {
   public static final String SELECTED_OBJECT = "selectedObject";
   private final Property<FieldOwner> selectedObject = this.newProperty("selectedObject");
   private final ConcurrentMap<String, SObjectSchema> knownObjectTypes = Maps.newConcurrentMap();
   private final QueryBuilderModel queryBuilderModel;

   public RelationalFieldSelectorModel(QueryBuilderModel var1) {
      this.queryBuilderModel = var1;
      var1.addPropertyChangeListener("object", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            RelationalFieldSelectorModel.this.updateSelectedObject();
         }
      });
      this.updateSelectedObject();
   }

   private void updateSelectedObject() {
      SObjectSchema var1 = this.queryBuilderModel.getSObject();
      if (var1 == null) {
         throw new IllegalStateException("No object selected");
      }

      this.selectedObject.set(new RootObject(var1));
      this.selectedObject.setAllowsNull(false);
      this.storeSchema(var1);
   }

   public QueryBuilderModel getQueryBuilderModel() {
      return this.queryBuilderModel;
   }

   public NameDisplayMode getObjectNameDisplayMode() {
      return this.queryBuilderModel.getObjectNameDisplayMode();
   }

   public NameDisplayMode getFieldNameDisplayMode() {
      return this.queryBuilderModel.getFieldNameDisplayMode();
   }

   public FieldOwner getSelectedObject() {
      return this.selectedObject.get();
   }

   public void setSelectedObject(FieldOwner var1) {
      this.selectedObject.set(var1);
   }

   public SObjectSchema getSchema(String var1) {
      return this.knownObjectTypes.get(var1);
   }

   public void storeSchema(SObjectSchema var1) {
      this.knownObjectTypes.put(var1.getName(), var1);
   }

   public List<SObjectSchema.Field> alphabeticalOrder(Iterable<SObjectSchema.Field> var1) {
      NameDisplayMode var2 = this.queryBuilderModel.getFieldNameDisplayMode();
      return var2.sortedCopy(var1);
   }
}
