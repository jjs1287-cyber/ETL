package org.etlforge.integration.client.ui.interchange.entity.transformation.database;

import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.Set;
import org.etlforge.integration.client.database.ui.primarykeys.PrimaryKeys;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.structure.editor.db.PrimaryKeysCreator;
import org.etlforge.integration.client.ui.structure.editor.db.TargetInsertUpdateModeUi;
import org.etlforge.integration.client.ui.structure.editor.db.TransformationDatabaseInfo;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

final class TargetUpdateKeysEditorUi implements UiProvider {
   private final TargetInsertUpdateModeUi ui;

   public TargetUpdateKeysEditorUi(TreeMapper var1) {
      this.ui = this.createUi(var1);
   }

   public TargetUpdateKeysEditorUi.State getMemento() {
      return new TargetUpdateKeysEditorUi.State(this.ui.getInsertMode(), this.ui.getSelectedKeys());
   }

   public void setMemento(TargetUpdateKeysEditorUi.State var1) {
      var1.applyTo(this.ui);
   }

   public void applyTo(Transformation var1) {
      DatabaseStructure var2 = this.getDbStructure(var1);
      var1.setDbInsertMode(this.ui.getInsertMode());
      var2.setDbFields(this.ui.createDbFields());
   }

   private DatabaseStructure getDbStructure(Transformation var1) {
      DataStructure var2 = var1.getOutputStructure();
      assert var2 instanceof DatabaseStructure;
      return (DatabaseStructure)var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   private TargetInsertUpdateModeUi createUi(TreeMapper var1) {
      TransformationDatabaseInfo var2 = this.getDbInfo(var1);
      PrimaryKeys[] var3 = this.getInitialKeys(var2);
      TargetInsertUpdateModeUi var4 = new TargetInsertUpdateModeUi(var2.getDatabaseObjects(), var3);
      var4.setInsertMode(var1.getTransformation().getDbInsertMode());
      var4.setIncludeAutoTruncateChoice(false);
      return var4;
   }

   private TransformationDatabaseInfo getDbInfo(TreeMapper var1) {
      TransformationDatabaseInfoFactory var2 = TransformationDatabaseInfoFactory.fromTargetTree(var1);
      return var2.createInfo();
   }

   private PrimaryKeys[] getInitialKeys(TransformationDatabaseInfo var1) {
      PrimaryKeysCreator var2 = new PrimaryKeysCreator(var1);
      return var2.getPrimaryKeys();
   }

   public static final class State {
      private final DbInsertMode mode;
      private final Set<PrimaryKeys> keys;
      private final Set<TargetUpdateKeysEditorUi.State.KeyCapsule> toCompare;

      private State(DbInsertMode var1, PrimaryKeys[] var2) {
         this.mode = var1;
         this.keys = new LinkedHashSet<>();
         this.toCompare = Sets.newHashSet();
         this.storeKeys(var2);
      }

      private void storeKeys(PrimaryKeys[] var1) {
         if (var1 != null) {
            for (PrimaryKeys var5 : var1) {
               this.keys.add(var5);
               this.toCompare.add(new TargetUpdateKeysEditorUi.State.KeyCapsule(var5));
            }
         }
      }

      private void applyTo(TargetInsertUpdateModeUi var1) {
         var1.setInsertMode(this.mode);
         var1.setUpdateKeys(this.keys.toArray(new PrimaryKeys[this.keys.size()]));
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof TargetUpdateKeysEditorUi.State)) {
            return false;
         }

         TargetUpdateKeysEditorUi.State var2 = (TargetUpdateKeysEditorUi.State)var1;
         return this.mode == var2.mode && this.toCompare.equals(var2.toCompare);
      }

      @Override
      public int hashCode() {
         int var1 = 31;
         var1 = 17 * var1 + this.mode.hashCode();
         return 17 * var1 + this.toCompare.hashCode();
      }

      private static final class KeyCapsule {
         private final DatabaseObject object;
         private final Set<DatabaseColumn> columns;

         public KeyCapsule(PrimaryKeys var1) {
            this.object = var1.getDatabaseObject();
            this.columns = Sets.newHashSet(var1.getPrimaryKeys());
         }

         @Override
         public boolean equals(Object var1) {
            if (var1 == this) {
               return true;
            }

            if (!(var1 instanceof TargetUpdateKeysEditorUi.State.KeyCapsule)) {
               return false;
            }

            TargetUpdateKeysEditorUi.State.KeyCapsule var2 = (TargetUpdateKeysEditorUi.State.KeyCapsule)var1;
            return this.object.equals(var2.object) && this.columns.equals(var2.columns);
         }

         @Override
         public int hashCode() {
            int var1 = 31;
            var1 = 17 * var1 + this.object.hashCode();
            return 17 * var1 + this.columns.hashCode();
         }
      }
   }
}
