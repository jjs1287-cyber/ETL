package org.etlforge.integration.client.ui.structure.editor.db.rels;

import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.database.info.DatabaseObject;

class JoinChecker {
   private final Relations relations;

   public JoinChecker(Relations var1) {
      this.relations = var1;
   }

   public boolean isValidJoin(DatabaseObject var1, DatabaseObject var2) {
      String var3 = null;
      if (var2 == var1) {
         var3 = "A table cannot be linked to itself.";
      } else if (this.relations.getParent(var2) != null) {
         var3 = var2.getName() + " is already linked to another parent (" + this.relations.getParent(var2).getName() + ").";
      } else if (var1 != null && this.relations.isAncestor(var2, var1)) {
         var3 = var2.getName() + " is already an ancestor of " + var1.getName();
      }

      if (var3 != null) {
         NonBlockingAlert.error(var3, "Invalid Child Selection");
         return false;
      } else {
         return true;
      }
   }

   public boolean canBeJoinedToParent(DatabaseObject var1, DatabaseObject var2) {
      if (this.relations.getParent(var2) != null) {
         return false;
      } else {
         return var1 == null ? true : var2 != var1 && !this.relations.isAncestor(var2, var1);
      }
   }
}
