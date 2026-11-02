package org.etlforge.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetToStringListConverter implements ResultSetConverter {
   public final List<String> convert(ResultSet var1) throws SQLException {
      ArrayList var2 = null;
      if (var1.next()) {
         var2 = new ArrayList();
         int var3 = var1.getMetaData().getColumnCount();

         for (int var4 = 1; var4 <= var3; var4++) {
            var2.add(this.readValueAsString(var1, var4));
         }
      }

      return var2;
   }

   protected String readValueAsString(ResultSet var1, int var2) throws SQLException {
      return var1.getString(var2);
   }
}
