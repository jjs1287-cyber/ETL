package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.ui.widget.menu.KongaCheckBoxMenuItem;

public final class SimpleXmlElementDisplaySettingsMenu extends AbstractMenu {
   private final TransformationPage page;
   private final SimpleXmlElementDisplayAction sourceAction;
   private final SimpleXmlElementDisplayAction targetAction;

   public SimpleXmlElementDisplaySettingsMenu(TransformationPage var1) {
      super("menu.ic.tx.SimpleXmlElementDisplay");
      this.page = var1;
      this.sourceAction = new SimpleXmlElementDisplayAction("&Source Tree");
      this.targetAction = new SimpleXmlElementDisplayAction("&Target Tree");
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            SimpleXmlElementDisplaySettingsMenu.this.initializeActions();
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
