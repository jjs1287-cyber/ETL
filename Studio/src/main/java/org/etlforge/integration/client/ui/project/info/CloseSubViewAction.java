package org.etlforge.integration.client.ui.project.info;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;

public final class CloseSubViewAction extends AbstractAction {
   private final ProjectConsoleView view;
   private final ProjectConsoleSubView subView;

   public CloseSubViewAction(ProjectConsoleView var1, ProjectConsoleSubView var2) {
      Preconditions.checkNotNull(var1, "view");
      Preconditions.checkNotNull(var2, "subView");
      this.view = var1;
      this.subView = var2;
      ActionDecorators.CLOSE.decorate(this);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.view.closeSubView(this.subView.getId());
   }
}
