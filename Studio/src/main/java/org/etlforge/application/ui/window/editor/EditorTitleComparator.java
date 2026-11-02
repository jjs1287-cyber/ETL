package org.etlforge.application.ui.window.editor;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Comparator;

public class EditorTitleComparator implements Comparator<Editor>, Serializable {
   private final Ordering<Editor> impl;

   public EditorTitleComparator() {
      this(false);
   }

   public EditorTitleComparator(boolean var1) {
      Ordering var2 = var1 ? Ordering.natural() : Ordering.from(String.CASE_INSENSITIVE_ORDER);
      this.impl = var2.onResultOf(new EditorTitleComparator.ValueFunction());
   }

   public int compare(Editor var1, Editor var2) {
      return this.impl.compare(var1, var2);
   }

   private static class ValueFunction implements Function<Editor, String>, Serializable {
      private ValueFunction() {
      }

      public String apply(Editor var1) {
         return var1.getTitle();
      }
   }
}
