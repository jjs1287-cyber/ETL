package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.integration.data.structure.text.validation.InvalidRecordAction;
import org.etlforge.integration.data.structure.text.validation.Operator;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.Range;
import org.etlforge.integration.data.structure.text.validation.StringArgument;
import org.etlforge.integration.data.structure.text.validation.StringList;
import org.etlforge.integration.data.structure.text.validation.StringOptions;
import org.etlforge.integration.data.structure.text.validation.ValidationRule;
import org.etlforge.ui.InvalidInputException;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class FieldValidationModel extends AbstractPropertyBasedModel {
   public static final String ENABLED = "enabled";
   public static final String REQUIRED = "required";
   public static final String LENGTH = "length";
   public static final String STRING_OPTIONS = "stringOptions";
   public static final String DATE_FORMAT = "dateFormat";
   public static final String ACTION = "action";
   public static final String OPERATORS = "operators";
   public static final String SELECTED_OPERATOR = "operator";
   public static final String ARGUMENT = "argument";
   public static final String ARGUMENT_TYPE = "argumentType";
   public static final String DATA_TYPE = "dataType";
   private final IntegrationProject project;
   private final Property<Boolean> enabled = this.newProperty("enabled", false);
   private final Property<Boolean> required = this.newProperty("required", false);
   private final Property<String> length = this.newProperty("length", "");
   private final Property<StringOptions> stringOptions = this.newProperty("stringOptions", StringOptions.DEFAULT);
   private final Property<String> dateFormat = this.newProperty("dateFormat", "");
   private final Property<InvalidRecordAction> action = this.newProperty("action", InvalidRecordAction.SKIP_ROW);
   private final ListProperty<Operator> operators = this.newListProperty("operators");
   private final Property<Operator> operator = this.newProperty("operator");
   private final Property<OperatorArgument> argument = this.newProperty("argument");
   private final Property<Class> argumentType = this.newProperty("argumentType");
   private final Property<FieldDataType> dataType = this.newProperty("dataType");
   private final Map<Class, OperatorArgument> argumentCache = Maps.newHashMap();
   private boolean fixedWidth;

   public FieldValidationModel(IntegrationProject var1, Field var2) {
      this(var1, var2.getDataType());
      this.fixedWidth = var2.isFixedWidth();
      this.setFieldValidation(var2.getValidation());
      if (var2.isDate()) {
         this.dateFormat.set(var2.getFormat());
      }
   }

   public FieldValidationModel(IntegrationProject var1, FieldDataType var2) {
      this.project = var1;
      this.dataType.set(var2);
      this.dataType.setAllowsNull(false);
      this.length.setAllowsNull(false);
      this.stringOptions.setAllowsNull(false);
      this.dateFormat.setAllowsNull(false);
      this.action.setAllowsNull(false);
      this.setApplicableOperators();
      FieldValidationModel.InternalListener var3 = new FieldValidationModel.InternalListener();
      this.dataType.addListener(var3);
      this.operators.addListener(var3);
      this.operator.addListener(var3);
   }

   private void setApplicableOperators() {
      ImmutableList var1 = Operator.listApplicable(this.getDataType());
      ArrayList var2 = Lists.newArrayListWithExpectedSize(var1.size() + 1);
      var2.add(null);
      var2.addAll(var1);
      this.operators.set(var2);
   }

   public IntegrationProject getProject() {
      return this.project;
   }

   public boolean isEnabled() {
      return this.enabled.get();
   }

   public boolean isFixedWidth() {
      return this.fixedWidth;
   }

   public boolean isDateField() {
      return this.dataType.get() == FieldDataType.Date;
   }

   public FieldDataType getDataType() {
      return this.dataType.get();
   }

   public void setDataType(FieldDataType var1) {
      this.dataType.set(var1);
   }

   public String getDateFormat() {
      return this.dateFormat.get();
   }

   public Optional<FieldValidation> getFieldValidation() {
      if (!this.enabled.get()) {
         return Optional.absent();
      }

      FieldValidation.Builder var1 = FieldValidation.builder();
      var1.setRequired(this.required.get());
      var1.setAction(this.action.get());
      var1.setStringOptions(this.stringOptions.get());
      String var2 = this.length.get();
      if (!var2.isEmpty()) {
         int var3 = Integer.parseInt(var2);
         var1.setMaxLength(var3);
      }

      Operator var5 = this.operator.get();
      if (var5 != null && this.hasArgument(var5)) {
         ValidationRule var4 = new ValidationRule(var5, this.argument.get());
         var1.setRule(var4);
      }

      return Optional.of(var1.build());
   }

   private boolean hasArgument(Operator var1) {
      Class var2 = var1.getArgumentClass();
      if (var2.equals(StringArgument.class)) {
         StringArgument var3 = (StringArgument)this.argument.get();
         return !var3.isBlank();
      } else {
         return !this.argument.isNull();
      }
   }

   public void setFieldValidation(FieldValidation var1) {
      this.setFieldValidation(Optional.of(var1));
   }

   public void setFieldValidation(Optional<FieldValidation> var1) {
      if (var1.isPresent()) {
         this.enabled.set(true);
         this.updateProperties((FieldValidation)var1.get());
      } else {
         this.enabled.set(false);
      }
   }

   private void updateProperties(FieldValidation var1) {
      this.required.set(var1.isRequired());
      Optional var2 = var1.getMaxLength();
      if (var2.isPresent()) {
         this.length.set(((Integer)var2.get()).toString());
      } else {
         this.length.set("");
      }

      StringOptions var3 = var1.getStringOptions().or(StringOptions.DEFAULT);
      this.stringOptions.set(var3);
      InvalidRecordAction var4 = var1.getAction();
      this.action.set(var4);
      Optional var5 = var1.getRule();
      if (var5.isPresent() && ((ValidationRule)var5.get()).isApplicableToField(this.getDataType())) {
         this.operator.set(((ValidationRule)var5.get()).getOperator());
         this.argument.set(((ValidationRule)var5.get()).getArgument());
      } else {
         this.operator.set(null);
         this.argument.set(null);
      }
   }

   public Optional<Operator> getSelectedOperator() {
      return Optional.fromNullable(this.operator.get());
   }

   public void setSelectedOperator(Operator var1) {
      this.operator.set(var1);
   }

   public void validate() throws InvalidInputException {
      if (this.isEnabled()) {
         Operator var1 = this.operator.get();
         if (var1 != null) {
            OperatorArgument var2 = this.argument.get();
            if (var2 == null) {
               throw new InvalidInputException("Missing value for the " + var1.getName() + " validation.");
            }

            if (var2 instanceof StringArgument) {
               if (((StringArgument)var2).isBlank()) {
                  throw new InvalidInputException("Missing value for the " + var1.getName() + " validation.");
               }
            } else if (var2 instanceof StringList) {
               if (((StringList)var2).isEmpty()) {
                  throw new InvalidInputException("Must provide at least one value for the " + var1.getName() + " validation.");
               }
            } else if (var2 instanceof Range && ((Range)var2).isIncomplete()) {
               throw new InvalidInputException("Incomplete input to the " + var1.getName() + " validation.");
            }
         }
      }
   }

   private class InternalListener implements PropertyChangeListener {
      private Operator lastOp;

      private InternalListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if ("dataType".equals(var2)) {
            FieldValidationModel.this.setApplicableOperators();
         } else if ("operators".equals(var2)) {
            Operator var3 = FieldValidationModel.this.operator.get();
            if (var3 != null && !var3.isApplicableToField(FieldValidationModel.this.getDataType())) {
               FieldValidationModel.this.operator.set(null);
            }
         } else if ("operator".equals(var2)) {
            this.handleNewOperatorSelection();
         }
      }

      private void handleNewOperatorSelection() {
         this.updateArgumentCache();
         Operator var1 = FieldValidationModel.this.operator.get();
         this.lastOp = var1;
         if (var1 == null) {
            FieldValidationModel.this.argumentType.set(null);
            FieldValidationModel.this.argument.set(null);
         } else {
            Class var2 = var1.getArgumentClass();
            if (var2.equals(FieldValidationModel.this.argumentType.get())) {
               return;
            }

            FieldValidationModel.this.argumentType.set(var2);
            OperatorArgument var3 = FieldValidationModel.this.argumentCache.get(var2);
            FieldValidationModel.this.argument.set(var3);
         }
      }

      private void updateArgumentCache() {
         if (this.lastOp != null) {
            FieldValidationModel.this.argumentCache.put(this.lastOp.getArgumentClass(), FieldValidationModel.this.argument.get());
         }
      }
   }
}
