package org.etlforge.integration.data.binding.ui;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.entity.valuecache.History;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.text.contentassist.StringValueHistory;

public final class PropertyValueHistory implements StringValueHistory {
   private final History history;

   public static StringValueHistory of(ValueCache<?> var0, String var1) {
      return of(var0.getValues(var1));
   }

   public static StringValueHistory of(Optional<? extends History> var0) {
      return var0.isPresent() ? new PropertyValueHistory((History)var0.get()) : StringValueHistory.EMPTY;
   }

   public static Disposable installAutoCompletion(TextInputField var0, ValueCache<?> var1, String var2) {
      StringValueHistory var3 = of(var1, var2);
      return new TextFieldCompletionSupport(var0.getInputComponent(), var3);
   }

   public PropertyValueHistory(History var1) {
      this.history = Preconditions.checkNotNull(var1, "history");
   }

   @Override
   public String[] values() {
      String[] var1 = Iterables.toArray(this.history.getValues(), String.class);
      Arrays.sort(var1, String.CASE_INSENSITIVE_ORDER);
      return var1;
   }

   @Override
   public boolean delete(String var1) {
      return this.history.delete(var1);
   }
}
