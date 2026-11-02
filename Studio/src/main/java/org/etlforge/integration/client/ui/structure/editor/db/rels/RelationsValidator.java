package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseObject;

final class RelationsValidator {
   private final DatabaseObject[] objects;
   private Relations rels;
   private boolean allowMultipleRoots;

   public RelationsValidator(DatabaseObject[] var1) {
      this.objects = var1;
   }

   public void setAllowMultipleRoots(boolean var1) {
      this.allowMultipleRoots = var1;
   }

   public void validate(Relations var1) throws IllegalRelationException {
      this.rels = var1;
      if (this.allowMultipleRoots) {
         this.checkMissingLinkKeys();
      } else {
         this.checkEmpty();
         this.checkMultipleRoots();
         this.checkMissingLinkKeys();
         this.checkMissingTables();
      }
   }

   private void checkEmpty() {
      if (this.rels.isEmpty()) {
         throw new IllegalRelationException("No relationships are defined.");
      }
   }

   private void checkMultipleRoots() {
      if (!this.rels.hasSingleRoot()) {
         throw new IllegalRelationException("Must define a root table.");
      }
   }

   private void checkMissingLinkKeys() {
      for (TableRelationship var2 : this.rels.getAllRelationships()) {
         if (var2.getFieldRelationships().isEmpty()) {
            throw new IllegalRelationException(
               "No link keys defined for the " + var2.getParentObject().getName() + " - " + var2.getChildObject().getName() + " join."
            );
         }
      }
   }

   private void checkMissingTables() {
      HashSet var1 = Sets.newHashSet(this.objects);

      for (TableRelationship var3 : this.rels.getAllRelationships()) {
         var1.remove(var3.getParentObject());
         var1.remove(var3.getChildObject());
      }

      if (!var1.isEmpty()) {
         String var4 = this.createMissingTablesMessage(var1);
         throw new IllegalRelationException(var4);
      }
   }

   private String createMissingTablesMessage(Set<DatabaseObject> var1) {
      return "The following tables have not been joined: " + Collections2.join(var1, ", ");
   }
}
