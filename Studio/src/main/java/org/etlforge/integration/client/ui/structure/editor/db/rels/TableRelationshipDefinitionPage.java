package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPage;
import org.etlforge.integration.client.ui.structure.editor.db.MultipleTableWhereClausePage;
import org.etlforge.integration.client.ui.structure.editor.db.StructureToEdit;
import org.etlforge.integration.client.ui.structure.editor.db.TargetInsertUpdateModePage;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbTranRelations;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseObject;

public final class TableRelationshipDefinitionPage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final TableRelationshipDefinitionPageUi ui;
   private final DatabaseObject[] tables;
   private final SourceTarget sourceOrTarget;
   private final Relations relations;
   private Map<TableRelationshipDefinitionPage.CacheKey, WizardPage> nextPageCache;

   public TableRelationshipDefinitionPage(Wizard var1, DatabaseObject[] var2, SourceTarget var3) {
      super(var1, lookupSubTitle(var3));
      Preconditions.checkArgument(var2.length >= 2, "tables must contain at least two objects");
      this.tables = var2;
      this.sourceOrTarget = var3;
      this.relations = new Relations();
      this.ui = new TableRelationshipDefinitionPageUi(var2, this.relations, var3);
      this.setCanContinue(true);
   }

   public SourceTarget getSourceTarget() {
      return this.sourceOrTarget;
   }

   public DatabaseObject[] getDatabaseObjects() {
      return this.tables;
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      DbTranRelations var2 = new DbTranRelations();
      this.setRelationships(var2);
      var1.setRelations(var2);
   }

   private void setRelationships(DbTranRelations var1) {
      boolean var2 = this.sourceOrTarget == SourceTarget.Source;

      for (TableRelationship var4 : this.relations.getAllRelationships()) {
         var1.addTableRelationship(var4, var2);
      }
   }

   @Override
   public boolean prepareForForwardNavigation() {
      try {
         RelationsValidator var1 = new RelationsValidator(this.tables);
         var1.setAllowMultipleRoots(this.sourceOrTarget == SourceTarget.Target);
         var1.validate(this.relations);
         return true;
      } catch (IllegalRelationException var2) {
         NonBlockingAlert.error(var2.getMessage(), "Invalid Table Relations");
         return false;
      }
   }

   @Override
   public WizardPage getNextPage() {
      TableRelationshipDefinitionPage.CacheKey var1 = new TableRelationshipDefinitionPage.CacheKey(this.relations);
      WizardPage var2 = this.getCachedNextPage(var1);
      if (var2 == null) {
         var2 = this.createNewNextPage(var1);
      }

      return var2;
   }

   private WizardPage getCachedNextPage(TableRelationshipDefinitionPage.CacheKey var1) {
      WizardPage var2 = null;
      if (this.nextPageCache == null) {
         this.nextPageCache = Maps.newHashMap();
      } else {
         var2 = this.nextPageCache.get(var1);
      }

      return var2;
   }

   private WizardPage createNewNextPage(TableRelationshipDefinitionPage.CacheKey var1) {
      AbstractWizardPage var2;
      if (this.sourceOrTarget == SourceTarget.Source) {
         var2 = new MultipleTableWhereClausePage(this.getWizard(), this.tables);
      } else {
         var2 = new TargetInsertUpdateModePage(this.getWizard(), this.tables);
      }

      this.nextPageCache.put(var1, var2);
      return var2;
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      DatabaseStructure var2 = var1.getStructure();

      for (TableRelationship var5 : var2.getRelations().getTableRelationships(this.tables)) {
         this.relations.add(var5);
      }

      this.ui.relationsChanged();
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         StructureEditorResources.getString("Db.RelationshipDefinitionPage.MainCaption"),
         StructureEditorResources.getString("Db.RelationshipDefinitionPage.SubCaption")
      );
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }

   private static String lookupSubTitle(SourceTarget var0) {
      assert var0 != null;
      String var1 = "Db.RelationshipDefinitionPage." + var0 + ".SubTitle";
      return StructureEditorResources.getString(var1);
   }

   private static final class CacheKey {
      private final Set<TableRelationship> data;

      public CacheKey(Relations var1) {
         this.data = Sets.newHashSet(var1.getAllRelationships());
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this
            ? true
            : var1 instanceof TableRelationshipDefinitionPage.CacheKey && ((TableRelationshipDefinitionPage.CacheKey)var1).data.equals(this.data);
      }

      @Override
      public int hashCode() {
         return this.data.hashCode();
      }
   }
}
