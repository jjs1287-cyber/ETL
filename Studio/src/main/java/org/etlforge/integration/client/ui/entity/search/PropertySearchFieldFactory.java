package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Preconditions;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.client.entity.search.StringList;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.ui.UiCanvas;

public final class PropertySearchFieldFactory {
   public static PropertySearchField createField(SearchableProperty var0) {
      Class var1 = var0.getValueType();
      if (var1.equals(String.class)) {
         return new StringSearchField(var0);
      }

      if (var1.equals(Boolean.class)) {
         return new BooleanPropertySearchField(var0);
      }

      if (var1.equals(Integer.class)) {
         return new IntegerSearchField(var0);
      }

      if (var1.isEnum()) {
         return new EnumPropertySearchField(var0);
      }

      if (var1.equals(OperationType.class)) {
         return new OperationTypeSearchField(var0);
      }

      if (var1.equals(StringList.class)) {
         return new StringListSearchField(var0);
      }

      if (var1.equals(Password.class)) {
         return new PasswordSearchField(var0);
      }

      System.err.println("Unsupported value type: " + var1);
      return null;
   }

   public static ReplaceField createReplaceField(SearchableProperty var0) {
      Preconditions.checkArgument(var0 instanceof ReplaceableProperty, "p must implement ReplacableProperty");
      PropertySearchField var1 = createFieldForReplace(var0);
      return var1 instanceof TwoRowSearchField
         ? new PropertySearchFieldFactory.TwoRowReplaceField(var1)
         : new PropertySearchFieldFactory.DefaultReplaceField(var1);
   }

   private static PropertySearchField createFieldForReplace(SearchableProperty var0) {
      Class var1 = var0.getValueType();
      return var1.equals(Password.class) ? new PasswordSearchField(var0).forReplace() : createField(var0);
   }

   private PropertySearchFieldFactory() {
   }

   private static class DefaultReplaceField implements ReplaceField {
      protected final PropertySearchField impl;

      public DefaultReplaceField(PropertySearchField var1) {
         this.impl = var1;
      }

      @Override
      public final ReplaceableProperty getProperty() {
         return (ReplaceableProperty)this.impl.getProperty();
      }

      @Override
      public final void decorate(UiCanvas var1) {
         this.impl.decorate(var1);
      }

      @Override
      public final void requestFocus() {
         this.impl.requestFocus();
      }

      @Override
      public final Object getValue() {
         return this.impl.getValue();
      }

      @Override
      public final void setValue(Object var1) {
         if (this.impl instanceof BooleanPropertySearchField && var1 instanceof Boolean) {
            ((BooleanPropertySearchField)this.impl).setValue((Boolean)var1);
         }
      }

      @Override
      public final void setReplaceAction(Action var1) {
         if (this.impl instanceof AbstractStringBasedSearchField) {
            ((AbstractStringBasedSearchField)this.impl).addActionListener(var1);
         }
      }
   }

   private static final class TwoRowReplaceField extends PropertySearchFieldFactory.DefaultReplaceField implements TwoRowSearchField {
      public TwoRowReplaceField(PropertySearchField var1) {
         super(var1);
         assert var1 instanceof TwoRowSearchField;
      }

      @Override
      public JComponent getTopRow() {
         return ((TwoRowSearchField)this.impl).getTopRow();
      }

      @Override
      public JComponent getBottomRow() {
         return ((TwoRowSearchField)this.impl).getBottomRow();
      }
   }
}
