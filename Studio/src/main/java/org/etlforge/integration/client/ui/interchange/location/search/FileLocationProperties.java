package org.etlforge.integration.client.ui.interchange.location.search;

import java.util.List;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueBooleanProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueIntegerProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.FileLocation;

final class FileLocationProperties {
   public static final SearchableProperty<Integer> IGNORE_LAST = intProperty("Ignore first lines", "ignore_last", 0);
   public static final SearchableProperty<Integer> IGNORE_FIRST = intProperty("Ignore first lines", "ignore_first", 0);

   public static void collect(SourceTarget var0, List<SearchableProperty> var1) {
      collect(var0, var1, true);
   }

   public static void collect(SourceTarget var0, List<SearchableProperty> var1, boolean var2) {
      if (var2) {
         var1.add(stringProperty("Login", "login"));
      }

      var1.add(stringProperty("File Name", "file_name"));
      if (var0 == SourceTarget.Source) {
         var1.add(booleanProperty("Filter - ignore case", "filterOption.ignoreCase", false));
         var1.add(booleanProperty("Filter - use regex", "filterOption.regularExpression", false));
         var1.add(new FileLocationProperties.DeleteAfterProcessingProperty());
         var1.add(stringProperty("Rename file", "renamefileto"));
         var1.add(IGNORE_FIRST);
         var1.add(IGNORE_LAST);
      } else {
         var1.add(booleanProperty("Don't create 0K files", "skip_empty_files", false));
         var1.add(booleanProperty("Auto Create Directories", "file_share_create_directories", true));
         var1.add(stringProperty("Character Encoding", "character_encoding"));
      }
   }

   private static final SearchableProperty<String> stringProperty(String var0, String var1) {
      return new ReplaceableKeyValueStringProperty(var0, var1, TargetFunctions.TO_LOCATION);
   }

   private static final SearchableProperty<Integer> intProperty(String var0, String var1, int var2) {
      return new ReplaceableKeyValueIntegerProperty(var0, var1, var2, TargetFunctions.TO_LOCATION);
   }

   private static final SearchableProperty<Boolean> booleanProperty(String var0, String var1, boolean var2) {
      return new ReplaceableKeyValueBooleanProperty(var0, var1, var2, TargetFunctions.TO_LOCATION);
   }

   private FileLocationProperties() {
   }

   private static final class DeleteAfterProcessingProperty implements ReplaceableProperty<Boolean> {
      private DeleteAfterProcessingProperty() {
      }

      @Override
      public String getName() {
         return "Delete after processing";
      }

      @Override
      public Class<Boolean> getValueType() {
         return Boolean.class;
      }

      public Boolean getValue(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         return ((FileLocation)var2).willBeDeletedOnSuccess();
      }

      public void setValue(IntegrationEntity var1, Boolean var2) {
         DataLocation var3 = ((Locatable)var1).getLocation();
         if (var2) {
            ((FileLocation)var3).setDeleteSourceFile();
         } else {
            ((FileLocation)var3).setRenameFileTo("");
         }
      }
   }
}
