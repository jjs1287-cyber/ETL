package org.etlforge.integration.data.script;

import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;

public final class ScriptConstants {
   public static final String BEGIN_TRANS = "<trans>";
   public static final int BEGIN_TRANS_LENGTH = "<trans>".length();
   public static final String END_TRANS = "</trans>";
   public static final int END_TRANS_LENGTH = "</trans>".length();
   private static final String EMPTY_EXPRESSION_PATTERN = "<trans>\\s*</trans>";
   public static final String EMPTY_SCRIPT = "<trans>\n\n</trans>";
   public static final String ARGUMENT = "<arg>";
   public static final int ARGUMENT_LENGTH = "<arg>".length();
   public static final String BEGIN_TAG = "\"<TAG>";
   public static final String END_TAG = "</TAG>\"";

   public static String getProjectItemPath(IntegrationEntity var0) {
      return new EntityPath(var0, false).toString();
   }

   public static String getProjectItemTagString(IntegrationEntity var0) {
      return "\"<TAG>" + getProjectItemPath(var0) + "</TAG>\"";
   }

   public static String getPluginTagString(PipelinePluginManifest var0) {
      return "\"<TAG>plugin:" + var0.getName() + "</TAG>\"";
   }

   public static String wrapInTags(String var0) {
      return wrapInTags(var0, true);
   }

   public static String wrapInTags(String var0, boolean var1) {
      return var1 ? "<trans>\n" + var0 + "\n" + "</trans>" : "<trans>" + var0 + "</trans>";
   }

   public static boolean isEmptyExpression(String var0) {
      if (var0.isEmpty()) {
         return true;
      }

      String var1 = var0.toLowerCase();
      return var1.matches("<trans>\\s*</trans>");
   }

   private ScriptConstants() {
   }
}
