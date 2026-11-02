package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

public final class TreeMapperSearchAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.Search";
   private final TransformationPage page;

   public TreeMapperSearchAction(TransformationPage var1) {
      super("action.ic.tx.Search", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TreeMapperSearchAction.this.page.getTreeMapper().searchAndReplace();
         }
      });
   }
}
