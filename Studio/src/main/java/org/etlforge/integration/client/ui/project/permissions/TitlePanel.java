package org.etlforge.integration.client.ui.project.permissions;

import java.awt.Color;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.border.Border;
import org.etlforge.application.ui.help.HelpButtonResource;
import org.etlforge.integration.client.ui.entity.EntityDropHandler;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.KongaButton;

final class TitlePanel implements UiProvider {
   private static final String HELP_ID = "consoles.integration.permissions";
   private final PermissionsViewImpl view;
   private final TitlePanel.PanelImpl ui;
   private final JButton helpButton;

   public TitlePanel(PermissionsViewImpl var1, Color var2, Border var3) {
      this.view = var1;
      this.helpButton = HelpButtonResource.forToolbar(var1, "consoles.integration.permissions");
      this.ui = new TitlePanel.PanelImpl(var2, var3);
   }

   public void setAccessedEntity(IntegrationEntity var1) {
      this.ui.setAccessedEntity(var1);
   }

   public void onEntityRenamed(String var1) {
      this.ui.onEntityRenamed(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui.panel);
   }

   @Override
   public void requestFocus() {
   }

   private JToolBar getToolBar() {
      JToolBar var1 = new JToolBar();
      var1.setFloatable(false);
      var1.setRollover(true);
      var1.setOpaque(false);
      var1.setBorder(Empty.border());
      var1.add(this.helpButton);
      var1.add(this.createCloseButton());
      return var1;
   }

   private JButton createCloseButton() {
      return createToolBarButton(this.view.getCloseAction());
   }

   private static JButton createToolBarButton(Action var0) {
      return KongaButton.forToolbar(var0);
   }

   private class PanelImpl {
      private final JPanel panel = new JPanel();
      private final JLabel accessedEntityLabel = new JLabel(" ");

      public PanelImpl(Color var2, Border var3) {
         this.layoutPanel();
         this.panel.setTransferHandler(new EntityDropHandler() {
            @Override
            protected boolean handleDrop(IntegrationEntity var1, TransferSupport var2x) {
               TitlePanel.this.view.setAccessedEntity(var1);
               return true;
            }
         });
         this.panel.setBorder(var3);
         this.panel.setBackground(var2);
      }

      public void setAccessedEntity(IntegrationEntity var1) {
         if (var1 != null) {
            RendererUtils.decorateRenderer(this.accessedEntityLabel, var1, false);
         } else {
            this.accessedEntityLabel.setText(" ");
            this.accessedEntityLabel.setIcon(null);
         }
      }

      public void onEntityRenamed(String var1) {
         this.accessedEntityLabel.setText(var1);
      }

      private void layoutPanel() {
         BorderLayoutBuilder var1 = BorderLayoutBuilder.from(this.panel);
         var1.west(this.accessedEntityLabel).center(Box.createGlue()).east(TitlePanel.this.getToolBar());
      }
   }
}
