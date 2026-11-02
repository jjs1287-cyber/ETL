package org.etlforge.integration.client.ui.connector.action;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.function.ConnectorCallPage;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.ui.action.Action2;
import org.etlforge.ui.util.KeyUtils;

public final class RefreshFunctionAction<F extends AbstractConnectorCall<E, ?>, E extends ConnectorEndpoint> extends AbstractAction implements Action2 {
   private final ConnectorCallPage<F, E> page;
   private final RefreshFunctionJobFactory jobFactory;

   public RefreshFunctionAction(ConnectorCallPage<F, E> var1, RefreshFunctionJobFactory var2) {
      super("Refresh", CommonIcons.REFRESH_16);
      this.page = Preconditions.checkNotNull(var1, "page");
      this.jobFactory = Preconditions.checkNotNull(var2, "jobFactory");
      this.putValue("AcceleratorKey", KeyUtils.REFRESH);
      this.putValue("KongaMediumIconKey", CommonIcons.REFRESH_24);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RefreshFunctionAction.this.saveAndRefresh();
         }
      });
   }

   private void saveAndRefresh() {
      try {
         this.page.save();
         this.refresh();
      } catch (SaveEditorException var2) {
         Alert.error(var2.getMessage(), "Cannot Save");
      }
   }

   private void refresh() {
      AbstractConnectorCall var1 = this.page.getObject();
      Runnable var2 = this.jobFactory.createRefreshJob(var1);
      var2.run();
   }
}
