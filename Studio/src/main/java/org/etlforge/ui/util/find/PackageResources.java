package org.etlforge.ui.util.find;

import org.etlforge.ui.resources.UiResourceBundle;

final class PackageResources {
   private static String getString(String var0) {
      return UiResourceBundle.getString(var0);
   }

   private PackageResources() {
   }

   static final class TextSearch {
      static final String DIRECTION = PackageResources.getString("Direction");
      static final String OPTIONS = PackageResources.getString("Options");
      static final String WHAT = PackageResources.getString("TextSearch.FindWhat");
      static final String REPLACE = PackageResources.getString("TextSearch.Replace");
      static final String WHOLE_WORDS = PackageResources.getString("TextSearch.WholeWords");
      static final String WRAP = PackageResources.getString("TextSearch.Wrap");
      static final String MATCH_CASE = PackageResources.getString("TextSearch.MatchCase");

      static final class Actions {
         static final String FIND_NEXT = PackageResources.getString("TextSearch.Action.FindNext");
         static final String REPLACE = PackageResources.getString("TextSearch.Action.Replace");
         static final String REPLACE_FIND = PackageResources.getString("TextSearch.Action.ReplaceFind");
         static final String REPLACE_ALL = PackageResources.getString("TextSearch.Action.ReplaceAll");
      }
   }
}
