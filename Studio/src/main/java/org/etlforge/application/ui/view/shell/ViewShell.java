package org.etlforge.application.ui.view.shell;

import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.view.menu.MenuExtensionData;

public final class ViewShell {
   private final String viewId;
   private final String viewName;
   private Action switchAction;
   private List<MenuExtensionData> menuExtensions;

   public ViewShell(String var1, String var2) {
      this.viewId = var1;
      this.viewName = var2;
      this.menuExtensions = Lists.newArrayList();
   }

   public String getID() {
      return this.viewId;
   }

   public String getName() {
      return this.viewName;
   }

   public Action getSwitchAction() {
      return this.switchAction;
   }

   public void setSwitchAction(Action var1) {
      this.switchAction = var1;
   }

   public void setMenuExtensions(List<MenuExtensionData> var1) {
      this.menuExtensions = Lists.newArrayList(var1);
   }

   public List<MenuExtensionData> getMenuExtensions() {
      return Lists.newArrayList(this.menuExtensions);
   }

   @Override
   public String toString() {
      return "[ViewShell: ID = " + this.viewId + ", Name = " + this.viewName + "]";
   }
}
