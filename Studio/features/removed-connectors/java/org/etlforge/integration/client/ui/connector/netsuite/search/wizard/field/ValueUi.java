package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import com.google.common.base.Preconditions;
import java.awt.event.ActionListener;
import javax.annotation.Nullable;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldBoolean;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldDate;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldEnumMultiSelect;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldMultiSelect;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldPostingPeriodDate;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldRecordRef;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldString;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.ui.UiProvider;

abstract class ValueUi implements UiProvider {
   public final SearchFieldValue getValue(NetSuiteSearchOperator var1) {
      return this.constructValue(var1);
   }

   protected abstract SearchFieldValue constructValue(NetSuiteSearchOperator var1);

   public abstract void setValue(SearchFieldValue var1);

   public ValueUi.ValidationResult validateInput() {
      return ValueUi.ValidationResult.success();
   }

   public void addActionListener(ActionListener var1) {
   }

   public void removeActionListener(ActionListener var1) {
   }

   public static ValueUi fromField(SearchField var0) {
      if (var0 instanceof SearchFieldBoolean) {
         return new BooleanValueUi();
      } else if (var0 instanceof SearchFieldString) {
         return new SingleStringValueUi();
      } else if (var0 instanceof SearchFieldDate) {
         return new DateValueUi((SearchFieldDate)var0);
      } else if (var0 instanceof SearchFieldEnumMultiSelect) {
         return new StringListValueUi();
      } else if (var0 instanceof SearchFieldMultiSelect) {
         return new RecordRefListValueUi((SearchFieldMultiSelect)var0);
      } else if (var0 instanceof SearchFieldRecordRef) {
         return new RecordRefValueUi((SearchFieldRecordRef)var0);
      } else {
         return var0 instanceof SearchFieldPostingPeriodDate ? new PostingPeriodDateValueUi((SearchFieldPostingPeriodDate)var0) : new TwoStringsValueUi();
      }
   }

   public static ValueUi fromFilter(Filter var0) {
      ValueUi var1 = fromField(var0.getFieldDef());
      var1.setValue(var0.getFieldValue());
      return var1;
   }

   public static final class ValidationResult {
      @Nullable
      private final String error;

      public static ValueUi.ValidationResult success() {
         return new ValueUi.ValidationResult(null);
      }

      public static ValueUi.ValidationResult error(String var0) {
         Preconditions.checkNotNull(var0);
         return new ValueUi.ValidationResult(var0);
      }

      private ValidationResult(String var1) {
         this.error = var1;
      }

      public boolean isValid() {
         return this.error == null;
      }

      public String whyNot() {
         Preconditions.checkState(this.error != null);
         return this.error;
      }

      @Override
      public String toString() {
         return this.isValid() ? "[VALID]" : "[INVALID]: " + this.error;
      }
   }
}
