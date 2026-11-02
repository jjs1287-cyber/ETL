package org.etlforge.integration.database.info.retriever;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.EnumSet;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.info.DatabaseObjectType;

public final class TableSearchFilter {
   public static final TableSearchFilter ALL_OBJECTS = new TableSearchFilter("%", allTypes());
   public static final TableSearchFilter TABLE_AND_VIEWS = new TableSearchFilter("%", EnumSet.of(DatabaseObjectType.TABLE, DatabaseObjectType.VIEW));
   private final String searchString;
   private final EnumSet<DatabaseObjectType> types;

   private static EnumSet<DatabaseObjectType> allTypes() {
      return EnumSet.allOf(DatabaseObjectType.class);
   }

   public TableSearchFilter(String var1) {
      this(var1, allTypes());
   }

   public TableSearchFilter(String var1, EnumSet<DatabaseObjectType> var2) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "searchString cannot be null or empty");
      this.searchString = var1;
      this.types = EnumSet.copyOf(var2);
   }

   public String getSearchString() {
      return this.searchString;
   }

   public EnumSet<DatabaseObjectType> getObjectTypes() {
      return EnumSet.copyOf(this.types);
   }

   String[] getObjectTypeNames() {
      ArrayList var1 = Lists.newArrayList();

      for (DatabaseObjectType var3 : this.types) {
         switch (var3) {
            case TABLE:
               var1.add("TABLE");
               break;
            case VIEW:
               var1.add("VIEW");
               break;
            case SYSTEM_TABLE:
               var1.add("SYSTEM TABLE");
         }
      }

      return (String[]) var1.toArray(new String[var1.size()]);
   }
}
