package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import com.google.common.base.Optional;
import java.util.Arrays;
import javax.swing.JLabel;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.structure.text.validation.Operator;
import org.etlforge.integration.data.structure.text.validation.Range;
import org.etlforge.integration.data.structure.text.validation.ScriptArgument;
import org.etlforge.integration.data.structure.text.validation.StringArgument;
import org.etlforge.integration.data.structure.text.validation.StringList;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;

abstract class ArgumentUi implements UiProvider, Disposable {
   public static final ArgumentUi forSelectedOperator(FieldValidationModel var0, Optional<EntityExplorerSupport> var1) {
      Optional var2 = var0.getSelectedOperator();
      if (var2.isPresent()) {
         Class var3 = ((Operator)var2.get()).getArgumentClass();
         if (var3.equals(StringArgument.class)) {
            return createStringUi(var0);
         } else if (var3.equals(Range.class)) {
            return new RangeUi(var0);
         } else if (var3.equals(StringList.class)) {
            return new StringListUi(var0);
         } else if (var3.equals(ScriptArgument.class)) {
            return new ScriptSelector(var0, var1);
         } else {
            throw new IllegalStateException("Unexpected argument class: " + var3.getName());
         }
      } else {
         return new ArgumentUi.None();
      }
   }

   private static ArgumentUi createStringUi(FieldValidationModel var0) {
      StringArgumentUi var1 = new StringArgumentUi(var0);
      if (var0.isDateField()) {
         CompletionContext var2 = createDateCompletionContext();
         var1.setAutoCompletionContext(var2);
      }

      return var1;
   }

   private static CompletionContext createDateCompletionContext() {
      TokenListCompletionContext var0 = new TokenListCompletionContext(
         Arrays.asList(
            "YESTERDAY",
            "TODAY",
            "TOMORROW",
            "LAST_WEEK",
            "THIS_WEEK",
            "NEXT_WEEK",
            "LAST_MONTH",
            "THIS_MONTH",
            "NEXT_MONTH",
            "LAST_90_DAYS",
            "NEXT_90_DAYS",
            "LAST_N_DAYS:",
            "NEXT_N_DAYS:",
            "THIS_QUARTER",
            "LAST_QUARTER",
            "NEXT_QUARTER",
            "NEXT_N_QUARTERS:",
            "LAST_N_QUARTERS:",
            "THIS_YEAR",
            "LAST_YEAR",
            "NEXT_YEAR",
            "NEXT_N_YEARS:",
            "LAST_N_YEARS"
         )
      );
      var0.setCaseSensitive(false);
      return var0;
   }

   public abstract void setEnabled(boolean var1);

   public abstract void registerAsChangeSource(InputPanel var1);

   private static class None extends ArgumentUi {
      private None() {
      }

      @Override
      public void decorate(UiCanvas var1) {
         JLabel var2 = new JLabel(" ");
         var2.setBorder(Empty.border(0, 160, 150, 160));
         var1.display(var2);
      }

      @Override
      public void setEnabled(boolean var1) {
      }

      @Override
      public void registerAsChangeSource(InputPanel var1) {
      }

      @Override
      public void requestFocus() {
      }

      @Override
      public void dispose() {
      }
   }
}
