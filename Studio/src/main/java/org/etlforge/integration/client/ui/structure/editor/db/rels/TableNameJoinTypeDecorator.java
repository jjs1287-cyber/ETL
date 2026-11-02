package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.base.Function;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseObject;

public final class TableNameJoinTypeDecorator implements Function<DatabaseObject, String> {
   private final Relations relations;

   public TableNameJoinTypeDecorator(Relations var1) {
      this.relations = var1;
   }

   public String apply(DatabaseObject var1) {
      DatabaseObject var2 = this.relations.getParent(var1);
      if (var2 != null) {
         TableRelationship var3 = this.relations.getRelation(var2, var1);
         switch (var3.getRelationshipNature()) {
            case ONE_TO_N:
               return " [1 to N]";
            case ONE_TO_ONE:
               return " [1]";
            case ZERO_TO_N:
               return " [0 to N]";
            case ZERO_OR_ONE:
               return " [0 or 1]";
            default:
               throw new RuntimeException("Unexepected join type: " + var3.getRelationshipNature());
         }
      } else {
         return "";
      }
   }
}
