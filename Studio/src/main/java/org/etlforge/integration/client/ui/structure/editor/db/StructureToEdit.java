package org.etlforge.integration.client.ui.structure.editor.db;

import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.info.DatabaseObject;

public final class StructureToEdit {
   private final Transformation tx;
   private final DatabaseStructure dbStruct;
   private final DatabaseObject[] tables;
   private final SourceTarget use;

   public StructureToEdit(Transformation var1, DatabaseStructure var2, DatabaseObject[] var3, SourceTarget var4) {
      this.tx = var1;
      this.dbStruct = var2.clone();
      this.tables = var3;
      this.use = var4;
   }

   public DatabaseStructure getStructure() {
      return this.dbStruct;
   }

   public DatabaseObject[] getObjects() {
      return (DatabaseObject[])this.tables.clone();
   }

   public Transformation getTransformation() {
      return this.tx;
   }

   public SourceTarget getUse() {
      return this.use;
   }

   public void setIncludeSchema(boolean var1) {
      if (var1 != this.dbStruct.getIncludeSchemaName()) {
         EditedStructureSchemaChange var2 = var1 ? new SchemaNameAdder(this.dbStruct, this.tables) : new SchemaNameRemover(this.dbStruct, this.tables);
         var2.process();
      }
   }
}
