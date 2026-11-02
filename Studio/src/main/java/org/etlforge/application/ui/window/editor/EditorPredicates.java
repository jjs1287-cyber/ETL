package org.etlforge.application.ui.window.editor;

import com.google.common.base.Predicate;

public enum EditorPredicates implements Predicate<Editor> {
   DIRTY {
      public boolean apply(Editor var1) {
         return var1.isDirty();
      }
   };

   EditorPredicates() {
   }
}
