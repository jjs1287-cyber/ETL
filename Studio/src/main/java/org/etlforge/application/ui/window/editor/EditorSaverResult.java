package org.etlforge.application.ui.window.editor;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

public class EditorSaverResult {
   private final Set<Editor> saved = Sets.newHashSet();
   private final Map<Editor, Exception> failed = Maps.newHashMap();

   public static final EditorSaverResult noOp() {
      return new EditorSaverResult();
   }

   public boolean wasNoOp() {
      return this.saved.isEmpty() && this.failed.isEmpty();
   }

   public boolean nothingFailed() {
      return this.failed.isEmpty();
   }

   public boolean somethingFailed() {
      return !this.failed.isEmpty();
   }

   public boolean wasSaved(Editor var1) {
      return this.saved.contains(var1);
   }

   public Editor[] editorsThatWereSaved() {
      return this.saved.toArray(new Editor[this.saved.size()]);
   }

   public Editor[] editorsThatCouldNotBeSaved() {
      return this.failed.keySet().toArray(new Editor[this.failed.size()]);
   }

   public Exception reasonForError(Editor var1) {
      Exception var2 = this.failed.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("The save request for editor did not fail");
      } else {
         return var2;
      }
   }

   void saved(Editor var1) {
      this.saved.add(var1);
   }

   void failed(Editor var1, Exception var2) {
      this.failed.put(var1, var2);
   }
}
