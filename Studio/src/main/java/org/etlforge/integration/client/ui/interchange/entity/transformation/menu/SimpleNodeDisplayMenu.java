package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.ui.widget.menu.KongaCheckBoxMenuItem;

public class SimpleNodeDisplayMenu extends AbstractMenu {
   private final TransformationPage page;
   private final SimpleNodeDisplayAction sourceAction;
   private final SimpleNodeDisplayAction targetAction;

   public SimpleNodeDisplayMenu(TransformationPage var1) {
      super("menu.ic.tx.SimpleNodeDisplay");
      this.page = var1;
      this.sourceAction = new SimpleNodeDisplayAction("&Source Tree");
      this.targetAction = new SimpleNodeDisplayAction("&Target Tree");
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            SimpleNodeDisplayMenu.this.initializeActions();
         }
      });
   }

   private void initializeActions() {
      this.sourceAction.setTree(this.page.getTreeMapper().m_sourceTreeComponent);
      this.targetAction.setTree(this.page.getTreeMapper().m_targetTreeComponent);
   }

   @Override
   protected List<? extends JMenuItem> getMenuItems() {
      ArrayList var1 = Lists.newArrayList();
      var1.add(new KongaCheckBoxMenuItem(this.sourceAction));
      var1.add(new KongaCheckBoxMenuItem(this.targetAction));
      return var1;
   }
}
