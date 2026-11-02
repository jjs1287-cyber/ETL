package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.FieldNameDisplayPreference;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.ObjectCategoryPreference;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.ObjectNameDisplayPreference;
import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import com.etlforge.integration.salesforce.ui.NameDisplayModes;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.wsdl.ExternalIdFieldFilter;
import org.etlforge.integration.salesforce.wsdl.ObjectCategory;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsFinder;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;
import org.etlforge.util.name.Nameables;

public final class ObjectSelectionModel extends AbstractPropertyBasedModel {
   public static final String AVAILABLE_OBJECTS = "availableObjects";
   public static final String SELECTED_OBJECT = "selectedObject";
   public static final String AVAILABLE_FIELDS = "availableFields";
   public static final String SELECTED_FIELD = "selectedField";
   public static final String REQUIRES_EXTERNAL_ID = "requiresExternalIdField";
   public static final String NAME_DISPLAY_MODE = "nameDisplayMode";
   public static final String CATEGORY = "category";
   public static final String VALID = "valid";
   private final ListProperty<SObjectSchema> availableObjects;
   private final Property<SObjectSchema> selectedObject;
   private ImmutableMap<String, SObjectSchema> objectNameMap = ImmutableMap.of();
   private final ListProperty<SObjectSchema.Field> availableFields;
   private final Property<SObjectSchema.Field> selectedField;
   private final Property<Boolean> requiresFieldSelection;
   private final Property<Boolean> valid;
   private final Property<ObjectCategory> category;
   private final ObjectCategoryPreference categoryPreference;
   private final NameDisplayModes nameDisplayModes;
   private Predicate<SObjectSchema> objectFilter;
   private final CommonlyUsedObjects commonlyUsedObjects = CommonlyUsedObjects.NONE;

   public ObjectSelectionModel() {
      this(ObjectCategoryPreference.INSTANCE, NameDisplayModes.of(ObjectNameDisplayPreference.INSTANCE.get(), FieldNameDisplayPreference.INSTANCE.get()));
   }

   public ObjectSelectionModel(ObjectCategoryPreference var1, NameDisplayModes var2) {
      this.categoryPreference = var1;
      this.nameDisplayModes = var2;
      this.availableObjects = this.newListProperty("availableObjects");
      this.category = this.newProperty("category", var1.get());
      this.category.setAllowsNull(false);
      this.objectFilter = this.category.get();
      this.selectedObject = this.newProperty("selectedObject");
      this.availableFields = this.newListProperty("availableFields");
      this.selectedField = this.newProperty("selectedField");
      this.requiresFieldSelection = this.newProperty("requiresExternalIdField");
      this.requiresFieldSelection.set(Boolean.FALSE);
      this.valid = this.newProperty("valid");
      this.valid.set(Boolean.FALSE);
      this.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            if (!"valid".equals(var1.getPropertyName())) {
               ObjectSelectionModel.this.updateValidState();
            }
         }
      });
      this.addPropertyChangeListener("selectedObject", new ObjectSelectionModel.SelectedObjectListener());
      this.addPropertyChangeListener("category", new ObjectSelectionModel.CategoryListener());
   }

   public void setObjectFilter(Predicate<SObjectSchema> var1) {
      this.objectFilter = var1;
   }

   public ObjectCategory getCategory() {
      return this.category.get();
   }

   public void setCategory(ObjectCategory var1) {
      this.category.set(var1);
   }

   public List<SObjectSchema> getAvailableObjects() {
      return this.availableObjects.get();
   }

   public ListProperty<SObjectSchema> getAvailableObjectsProperty() {
      return this.availableObjects;
   }

   public void setAvailableObjects(List<SObjectSchema> var1) {
      this.objectNameMap = Nameables.toImmutableMap(var1);
      List var2 = KongaListUtils.filter(var1, this.objectFilter);
      Collections.sort(var2, new ObjectPresentationOrder(this.commonlyUsedObjects));
      this.availableObjects.set(var2);
      SObjectSchema var3 = var2.isEmpty() ? null : (SObjectSchema)var2.get(0);
      this.selectedObject.set(var3);
   }

   public SObjectSchema getSelectedObject() {
      return this.selectedObject.get();
   }

   public void setSelectedObject(SObjectSchema var1) {
      if (var1 != this.getSelectedObject()) {
         this.selectedObject.set(var1);
      }
   }

   public void setFieldSelectionRequired(boolean var1) {
      this.requiresFieldSelection.set(var1);
   }

   public boolean isFieldSelectionRequired() {
      return this.requiresFieldSelection.get();
   }

   public List<SObjectSchema.Field> getExternalIdFieldList() {
      return this.availableFields.get();
   }

   public SObjectSchema.Field getSelectedField() {
      return this.selectedField.get();
   }

   public String getExternalIdFieldName() {
      if (this.isFieldSelectionRequired()) {
         SObjectSchema.Field var1 = this.getSelectedField();
         if (var1 != null) {
            return var1.getName();
         }
      }

      return null;
   }

   public void setSelectedField(SObjectSchema.Field var1) {
      this.selectedField.set(var1);
   }

   private void updateValidState() {
      boolean var1 = this.validate();
      this.valid.set(var1);
   }

   private boolean validate() {
      SObjectSchema var1 = this.getSelectedObject();
      if (var1 == null) {
         return false;
      } else {
         return this.isFieldSelectionRequired() ? this.getSelectedField() != null : true;
      }
   }

   public boolean isValid() {
      return this.valid.get();
   }

   public void setBulkAction(BulkAction var1) {
      this.setFieldSelectionRequired(var1.requiresExternalId());
   }

   public CommonlyUsedObjects getCommonlyUsedObjects() {
      return this.commonlyUsedObjects;
   }

   public NameDisplayMode getObjectNameDisplayMode() {
      return this.nameDisplayModes.object;
   }

   public NameDisplayMode getFieldNameDisplayMode() {
      return this.nameDisplayModes.field;
   }

   public List<RelatedObject> getRelatedObjects() {
      SObjectSchema var1 = this.getSelectedObject();
      return var1 == null ? Collections.emptyList() : RelatedObjectsFinder.find(var1, Functions.forMap(this.objectNameMap));
   }

   public List<SObjectSchema> alphabeticalCopy(Iterable<SObjectSchema> var1) {
      return this.objectNameOrdering().sortedCopy(var1);
   }

   public Ordering<? super SObjectSchema> objectNameOrdering() {
      return this.nameDisplayModes.object.toOrdering();
   }

   private final class CategoryListener implements PropertyChangeListener {
      private CategoryListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         ObjectCategory var2 = (ObjectCategory)var1.getNewValue();
         ObjectSelectionModel.this.categoryPreference.set(var2);
      }
   }

   private final class SelectedObjectListener implements PropertyChangeListener {
      private SelectedObjectListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         SObjectSchema.Field var2 = this.updateListOfAvailableFields((SObjectSchema)var1.getNewValue());
         ObjectSelectionModel.this.selectedField.set(var2);
      }

      private SObjectSchema.Field updateListOfAvailableFields(SObjectSchema var1) {
         if (var1 == null) {
            ObjectSelectionModel.this.availableFields.set((List<SObjectSchema.Field>)null);
         } else {
            List var2 = KongaListUtils.filter(var1.getFields(), new ExternalIdFieldFilter());
            ObjectSelectionModel.this.availableFields.set(var2);
            if (var2.size() == 1) {
               return (SObjectSchema.Field)var2.get(0);
            }
         }

         return null;
      }
   }
}
