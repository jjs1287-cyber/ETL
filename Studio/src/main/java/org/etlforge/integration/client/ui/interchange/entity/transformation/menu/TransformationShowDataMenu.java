package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.ToggleActionCheckBoxMenuItem;

public final class TransformationShowDataMenu extends AbstractMenu {
   private final TransformationPage page;

   public TransformationShowDataMenu(TransformationPage var1) {
      super("menu.ic.tx.ShowData");
      this.page = var1;
   }

   @Override
   protected List<? extends JMenuItem> getMenuItems() {
      ArrayList var1 = Lists.newArrayList();

      for (String var5 : new String[]{"action.ic.tx.ShowSourceData", "action.ic.tx.ShowTargetData"}) {
         JMenuItem var6 = this.getMenuItem(var5);
         if (var6 != null) {
            var1.add(var6);
         }
      }

      return var1;
   }

   private JMenuItem getMenuItem(String var1) {
      ApplicationAction var2 = this.page.getPageAction(var1);
      if (var2 == null) {
         return null;
      } else {
         return var2 instanceof ToggleAction ? new ToggleActionCheckBoxMenuItem((ToggleAction)var2) : new KongaMenu(var2);
      }
   }
}
