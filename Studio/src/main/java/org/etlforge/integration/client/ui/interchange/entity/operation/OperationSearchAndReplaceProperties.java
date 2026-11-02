package org.etlforge.integration.client.ui.interchange.entity.operation;

import org.etlforge.integration.client.entity.search.AbstractSearchableProperty;
import org.etlforge.integration.client.entity.search.KeyValueBooleanProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

final class OperationSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public OperationSearchAndReplaceProperties() {
      super(
         new OperationSearchAndReplaceProperties.OperationTypeProperty(),
         new OperationSearchAndReplaceProperties.SourceTypeProperty(),
         new OperationSearchAndReplaceProperties.TargetTypeProperty(),
         new OperationSearchAndReplaceProperties.RunsOnScheduleProperty(),
         new OperationSearchAndReplaceProperties.DebugModeProperty(),
         new OperationSearchAndReplaceProperties.SourceHistoryProperty(),
         new KeyValueBooleanProperty("Validate source file", "validate_source_text_file", false)
      );
   }

   private abstract static class BooleanOperationOptionProperty implements ReplaceableProperty<Boolean> {
      private final String name;

      public BooleanOperationOptionProperty(String var1) {
         this.name = var1;
      }

      @Override
      public final String getName() {
         return this.name;
      }

      @Override
      public final Class<Boolean> getValueType() {
         return Boolean.class;
      }

      public final Boolean getValue(IntegrationEntity var1) {
         synchronized (var1.getDataLock()) {
            OperationOptions var3 = ((Operation)var1).getOperationOptions();
            return this.getValue(var3);
         }
      }

      protected abstract Boolean getValue(OperationOptions var1);

      public final void setValue(IntegrationEntity var1, Boolean var2) {
         Operation var3 = (Operation)var1;
         synchronized (var1.getDataLock()) {
            OperationOptions var5 = var3.getOperationOptions();
            OperationOptions var6 = this.applyValue(var5, var2);
            var3.setOperationOptions(var6);
         }
      }

      protected abstract OperationOptions applyValue(OperationOptions var1, Boolean var2);
   }

   private static final class DebugModeProperty extends OperationSearchAndReplaceProperties.BooleanOperationOptionProperty {
      public DebugModeProperty() {
         super("Run in debug mode");
      }

      @Override
      protected OperationOptions applyValue(OperationOptions var1, Boolean var2) {
         return var1.withDebugOption(var2);
      }

      @Override
      protected Boolean getValue(OperationOptions var1) {
         return var1.getDebugOption().isDebugModeEnabled();
      }
   }

   private abstract static class LocatableTypeProperty extends AbstractSearchableProperty<DataLocationType> {
      public LocatableTypeProperty(String var1) {
         super(var1, DataLocationType.class);
      }

      public final DataLocationType getValue(IntegrationEntity var1) {
         IntegrationProject var2 = ProjectUtils.getProject(var1);
         if (var2 == null) {
            return null;
         }

         IntegrationEntityId var3 = this.getLocatableId((Operation)var1);
         Locatable var4 = (Locatable)var2.getItemLookup().getEntity(var3);
         return var4 != null ? var4.getLocation().getDataLocationType() : null;
      }

      protected abstract IntegrationEntityId getLocatableId(Operation var1);
   }

   private static final class OperationTypeProperty extends AbstractSearchableProperty<OperationType> {
      public OperationTypeProperty() {
         super("Activity", OperationType.class);
      }

      public OperationType getValue(IntegrationEntity var1) {
         return ((Operation)var1).getOperationType();
      }
   }

   private static final class RunsOnScheduleProperty extends AbstractSearchableProperty<Boolean> {
      public RunsOnScheduleProperty() {
         super("Run on schedule", Boolean.class);
      }

      public Boolean getValue(IntegrationEntity var1) {
         return ((Operation)var1).getScheduleId() != null;
      }
   }

   private static final class SourceHistoryProperty extends OperationSearchAndReplaceProperties.BooleanOperationOptionProperty {
      public SourceHistoryProperty() {
         super("Do not use processed files");
      }

      @Override
      protected OperationOptions applyValue(OperationOptions var1, Boolean var2) {
         return var1.withSourceHistoryOption(var2);
      }

      @Override
      protected Boolean getValue(OperationOptions var1) {
         return var1.getSourceHistoryOption().isHistoryChecked();
      }
   }

   private static final class SourceTypeProperty extends OperationSearchAndReplaceProperties.LocatableTypeProperty {
      public SourceTypeProperty() {
         super("Source type");
      }

      @Override
      protected IntegrationEntityId getLocatableId(Operation var1) {
         return var1.getSourceId();
      }
   }

   private static final class TargetTypeProperty extends OperationSearchAndReplaceProperties.LocatableTypeProperty {
      public TargetTypeProperty() {
         super("Target type");
      }

      @Override
      protected IntegrationEntityId getLocatableId(Operation var1) {
         return var1.getTargetId();
      }
   }
}
