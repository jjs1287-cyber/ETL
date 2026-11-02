package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNode;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class LaunchTreeMapperToTextStructureWizardAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.TextWizard";
   private final TransformationPage page;
   private final UiEntityFactory entityFactory;
   private final ProjectPersistor persistor;

   public LaunchTreeMapperToTextStructureWizardAction(TransformationPage var1, UiEntityFactory var2, ProjectPersistor var3) {
      super("action.ic.tx.TextWizard", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
      this.entityFactory = var2;
      this.persistor = var3;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LaunchTreeMapperToTextStructureWizardAction.this.startWizard();
         }
      });
   }

   private void startWizard() {
      try {
         Wizard var1 = this.createWizard(this.page.getTreeMapper());
         var1.start();
      } catch (Exception var2) {
         ErrorLog.attention(LaunchTreeMapperToTextStructureWizardAction.class, "An unexpected error occurred", var2);
      }
   }

   private Wizard createWizard(TreeMapper var1) throws Exception {
      Class var2 = Class.forName("org.etlforge.integration.client.ui.interchange.entity.document.wizard.TreeMapperToTextStructureWizard");
      Class var3 = var2.asSubclass(Wizard.class);
      Constructor var4 = var3.getConstructor(UiEntityFactory.class, ProjectPersistor.class, Transformation.class, TreeMapperNode.class, TreeMapperNode.class);
      return (Wizard)var4.newInstance(this.entityFactory, this.persistor, var1.getTransformation(), var1.getSourceRoot(), var1.getTargetRoot());
   }
}
