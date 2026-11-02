package org.etlforge.integration.client.ui.interchange.entity.transformation.database;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNode;
import org.etlforge.integration.client.ui.structure.editor.db.TransformationDatabaseInfo;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;

public abstract class TransformationDatabaseInfoFactory {
   public abstract TransformationDatabaseInfo createInfo();

   public static TransformationDatabaseInfoFactory fromSourceTree(TreeMapper var0) {
      Preconditions.checkNotNull(var0, "mapper");
      return new TransformationDatabaseInfoFactory.SourceTreeFactory(var0);
   }

   public static TransformationDatabaseInfoFactory fromTargetTree(TreeMapper var0) {
      Preconditions.checkNotNull(var0, "mapper");
      return new TransformationDatabaseInfoFactory.TargetTreeFactory(var0);
   }

   private static final class SourceTreeFactory extends TransformationDatabaseInfoFactory.TreeMapperFactory {
      public SourceTreeFactory(TreeMapper var1) {
         super(var1);
      }

      @Override
      protected TreeMapperNode getRoot(TreeMapper var1) {
         return var1.getSourceRoot();
      }

      @Override
      protected DatabaseStructure getDataStructure(Transformation var1) {
         DataStructure var2 = var1.getInputStructure();
         return var2 instanceof DatabaseStructure ? (DatabaseStructure)var2 : null;
      }
   }

   private static final class StructureImpl implements TransformationDatabaseInfo {
      private final DatabaseStructure structure;
      private final DatabaseObject[] objects;

      public StructureImpl(DatabaseStructure var1, DatabaseObject[] var2) {
         this.structure = var1;
         this.objects = var2;
      }

      @Override
      public DatabaseObject[] getDatabaseObjects() {
         return this.objects;
      }

      @Override
      public DatabaseStructure getStructure() {
         return this.structure;
      }
   }

   private static final class TargetTreeFactory extends TransformationDatabaseInfoFactory.TreeMapperFactory {
      public TargetTreeFactory(TreeMapper var1) {
         super(var1);
      }

      @Override
      protected DatabaseStructure getDataStructure(Transformation var1) {
         DataStructure var2 = var1.getOutputStructure();
         return var2 instanceof DatabaseStructure ? (DatabaseStructure)var2 : null;
      }

      @Override
      protected TreeMapperNode getRoot(TreeMapper var1) {
         return var1.getTargetRoot();
      }
   }

   private abstract static class TreeMapperFactory extends TransformationDatabaseInfoFactory {
      private final TreeMapper treeMapper;

      public TreeMapperFactory(TreeMapper var1) {
         this.treeMapper = var1;
      }

      @Override
      public final TransformationDatabaseInfo createInfo() {
         DatabaseStructure var1 = this.getDataStructure(this.treeMapper.getTransformation());
         return var1 != null ? new TransformationDatabaseInfoFactory.StructureImpl(var1, this.getObjects(var1)) : null;
      }

      protected abstract TreeMapperNode getRoot(TreeMapper var1);

      protected abstract DatabaseStructure getDataStructure(Transformation var1);

      private DatabaseObject[] getObjects(DatabaseStructure var1) {
         DatabaseObjectBuilder var2 = new DatabaseObjectBuilder();
         Collection var3 = var2.buildObjects(this.getRoot(this.treeMapper));
         var3 = this.applySchemaSetting(var1, var3);
         return (DatabaseObject[]) var3.toArray(new DatabaseObject[var3.size()]);
      }

      private Collection<DatabaseObject> applySchemaSetting(DatabaseStructure var1, Collection<DatabaseObject> var2) {
         if (!var1.getIncludeSchemaName()) {
            return var2;
         }

         ArrayList var3 = Lists.newArrayList();
         BeginEndQuote var4 = var1.getBeginEndQuote();

         for (DatabaseObject var6 : var2) {
            String var7 = var6.getName();
            if (var4.isEnclosingName(var7)) {
               String var8 = var4.end + "." + var4.begin;
               if (var7.contains(var8)) {
                  var3.add(var6);
                  continue;
               }
            }

            String var9 = var4.getQualifiedName(var6);
            var3.add(var6.createCopy(var9));
         }

         return var3;
      }
   }
}
