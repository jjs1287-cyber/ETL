package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public class TransformationPageActionsMenu extends AbstractMenu {
   private final TransformationPage page;
   private final String[] actionIds;

   public TransformationPageActionsMenu(String var1, TransformationPage var2, String... var3) {
      super(var1);
      this.page = var2;
      this.actionIds = var3;
   }

   @Override
   protected final List<? extends JMenuItem> getMenuItems() {
      ArrayList var1 = Lists.newArrayList();

      for (String var5 : this.actionIds) {
         if (var5 == null) {
            var1.add(null);
         } else {
            ApplicationAction var6 = this.page.getPageAction(var5);
            if (var6 != null) {
               var1.add(new KongaMenuItem(var6));
            }
         }
      }

      return var1;
   }
}
