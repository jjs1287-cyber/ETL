package org.etlforge.integration.client.ui.project.info;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.system.callback.Acceptor;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.ComponentTraverser;
import org.etlforge.ui.widget.button.KongaButton;

final class CachedInfoStatusPanel implements UiProvider {
   private final JComponent ui;
   private final JButton refreshButton = new KongaButton(CommonIcons.REFRESH_24).makeToolbarButton();

   public CachedInfoStatusPanel() {
      this.ui = BoxBuilder.horizontal()
         .glue()
         .addAllWithSpace(5, CommonIcons.WARNING_16, "The project list may be out-of-date.", this.refreshButton)
         .container();
   }

   public void setRefreshAction(Action var1) {
      this.refreshButton.addActionListener(var1);
   }

   public void setVisible(final boolean var1) {
      ComponentTraverser.traverse(this.ui, new Acceptor<Component>() {
         public boolean accept(Component var1x) {
            var1x.setVisible(var1);
            return true;
         }
      });
      UiUtils.revalidate(this.ui);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
   }
}
