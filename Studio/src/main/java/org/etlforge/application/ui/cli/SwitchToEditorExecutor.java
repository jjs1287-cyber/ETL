package org.etlforge.application.ui.cli;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;

public final class SwitchToEditorExecutor implements CommandExecutor {
   private final EditorService editors;

   public SwitchToEditorExecutor(EditorService var1) {
      this.editors = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      Editor var2 = this.getEditorToSwitchTo(this.getTitle(var1));
      if (var2 != null) {
         this.editors.openEditor(var2);
      }
   }

   private String getTitle(String[] var1) {
      return StringUtils.join(var1, " ");
   }

   private Editor getEditorToSwitchTo(String var1) {
      Editor[] var2 = this.editors.getOpenEditors();

      for (Editor var6 : var2) {
         if (var1.equalsIgnoreCase(var6.getTitle())) {
            return var6;
         }
      }

      String var11 = var1.toLowerCase();

      for (Editor var7 : var2) {
         if (var7.getTitle().toLowerCase().startsWith(var11)) {
            return var7;
         }
      }

      for (Editor var21 : var2) {
         if (var21.getTitle().toLowerCase().contains(var11)) {
            return var21;
         }
      }

      try {
         Pattern var14 = Pattern.compile(var11);

         for (Editor var8 : var2) {
            Matcher var9 = var14.matcher(var8.getTitle().toLowerCase());
            if (var9.find()) {
               return var8;
            }
         }
      } catch (PatternSyntaxException var10) {
      }

      return null;
   }
}
