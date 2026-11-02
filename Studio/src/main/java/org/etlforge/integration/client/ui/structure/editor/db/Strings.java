package org.etlforge.integration.client.ui.structure.editor.db;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;

final class Strings {
   static String get(String var0) {
      return StructureEditorResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
   }
}
