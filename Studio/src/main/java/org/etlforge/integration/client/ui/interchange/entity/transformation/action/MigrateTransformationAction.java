package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.entity.selector.EntitySelector;
import org.etlforge.integration.client.ui.entity.selector.EntitySelectorDialog;
import org.etlforge.integration.client.ui.entity.selector.EntityTreeSelector;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.widget.tree.LeafDisplayNameFilterFunction;

public final class MigrateTransformationAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.Migrate";
   private final TransformationPage page;

   public MigrateTransformationAction(TransformationPage var1) {
      super("action.ic.tx.Migrate", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            MigrateTransformationAction.this.startMigration();
         }
      });
   }

   private void startMigration() {
      this.page.saveAndRun(new Runnable() {
         @Override
         public void run() {
            Transformation var1 = MigrateTransformationAction.this.getSecondTransformation(MigrateTransformationAction.this.page.getObject());
            if (var1 != null) {
               MigrateTransformationAction.this.page.getTreeMapper().migration(var1);
            }
         }
      });
   }

   private Transformation getSecondTransformation(IntegrationEntity var1) {
      EntitySelector var2 = this.createSelector(var1);
      return (Transformation)EntitySelectorDialog.selectOne(var2, this.page.getWindow().getFrame(), "Select a Transformation", (String)null);
   }

   private EntitySelector<Transformation> createSelector(final IntegrationEntity var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var1);
      EntityTreeSelector var3 = EntityTreeSelector.newTree(var2, Transformation.class);
      var3.setFilter(new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1x) {
            return var1x != var1;
         }
      });
      var3.setToolbarTitle("Select the transformation to which the mappings will be exported:    ");
      var3.setTreeFilterFunction(new LeafDisplayNameFilterFunction(StringMatchers.CONTAINS_IGNORE_CASE, true));
      var3.setIncludeToolbar(true);
      return var3;
   }
}
