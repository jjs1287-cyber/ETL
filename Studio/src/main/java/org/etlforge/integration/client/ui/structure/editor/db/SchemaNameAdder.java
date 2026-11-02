package org.etlforge.integration.client.ui.structure.editor.db;

import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;

final class SchemaNameAdder extends EditedStructureSchemaChange {
   public SchemaNameAdder(DatabaseStructure var1, DatabaseObject[] var2) {
      super(var1, var2);
      assert !var1.getIncludeSchemaName();
   }

   @Override
   protected DatabaseObject translate(DatabaseObject var1, BeginEndQuote var2) {
      String var3 = var2.getQualifiedName(var1);
      return var1.createCopy(var3);
   }
}
