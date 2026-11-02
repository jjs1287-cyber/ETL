package org.etlforge.application.ui.text.editor;

import java.io.File;
import java.nio.charset.Charset;

public final class TextEditorFactory {
   public static TextEditor fromString(String var0, String var1, String var2) {
      return new DefaultTextEditor(var0, var1, var2);
   }

   public static TextEditor openFile(String var0, File var1, Charset var2) {
      return new DefaultTextEditor(var0, var1, var2);
   }

   public static ChunkedTextFileEditor openFileInChunks(String var0, File var1, Charset var2, int var3) {
      return new ChunkedTextFileEditor(var0, var1, var2, var3);
   }

   private TextEditorFactory() {
   }
}
