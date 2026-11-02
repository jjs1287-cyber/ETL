package com.etlforge.integration.client.ui.admin.user;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JLabel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerInformationStore;
import org.etlforge.integration.client.server.ui.AvailableServersComboBox;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.option.InputOption;
import org.etlforge.ui.util.option.InputOptionPanel;
import org.etlforge.ui.util.option.OptionSelector;

public final class DestinationServerSelector implements UiProvider, Disposable {
   private final AvailableServersComboBox serverSelector;
   private final ServerInfo currentServer;
   private final TextInputField2 newServerField;
   private final InputOptionPanel<String> optionPanel;
   private final List<InputChangeListener> changeListeners;

   public DestinationServerSelector(ServerInformationStore var1, ServerInfo var2) {
      Preconditions.checkNotNull(var2, "currentServer");
      this.currentServer = var2;
      this.serverSelector = new AvailableServersComboBox(var1.getRegisteredServers(), null);
      this.newServerField = InputFieldFactory.getNonEmptyTextInputField("newServer", (JLabel)null, 20);
      this.newServerField.setSendInputChangeEvents(true);
      this.optionPanel = this.createOptionPanel();
      this.changeListeners = new CopyOnWriteArrayList<>();
      this.listenToKnownServerSelector();
   }

   @Override
   public void dispose() {
      this.optionPanel.dispose();
   }

   public boolean isInputValid() {
      String var1 = this.getDestinationServer();
      return StringUtils.isNotBlank(var1) && !var1.equals(this.currentServer.getName());
   }

   public String getDestinationServer() {
      return this.optionPanel.getValue();
   }

   public void addChangeListener(InputChangeListener var1) {
      this.changeListeners.add(var1);
      this.newServerField.addInputChangeListener(var1);
   }

   public void removeChangeListener(InputChangeListener var1) {
      this.changeListeners.remove(var1);
      this.newServerField.removeInputChangeListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      this.layoutCaption(var2);
      this.layoutOptions(var2);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.optionPanel.requestFocus();
   }

   private InputOptionPanel<String> createOptionPanel() {
      DestinationServerSelector.KnownServerOption var1 = new DestinationServerSelector.KnownServerOption();
      DestinationServerSelector.NewServerOption var2 = new DestinationServerSelector.NewServerOption();
      InputOptionPanel var3 = new InputOptionPanel<>(var1, var2);
      if (this.serverSelector.getServerCount() > 0) {
         var3.setSelectedOption(var1);
         var2.setActive(false);
      } else {
         var3.setSelectedOption(var2);
         var1.setActive(false);
      }

      return var3;
   }

   private void listenToKnownServerSelector() {
      this.serverSelector.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            InputChangeEvent var2 = new InputChangeEvent(DestinationServerSelector.this.serverSelector, "Known Server");

            for (InputChangeListener var4 : DestinationServerSelector.this.changeListeners) {
               var4.inputChanged(var2);
            }
         }
      });
   }

   private void layoutCaption(BorderLayoutBuilder var1) {
      JLabel var2 = new JLabel(EnterpriseAdminResources.getString("User.Upload.CurrentServer"));
      JLabel var3 = new JLabel(this.currentServer.getName(), ClientIcons.SERVER_16, 10);
      var1.north(BoxBuilder.horizontal().addAllWithSpace(5, var2, var3));
   }

   private void layoutOptions(BorderLayoutBuilder var1) {
      TwoColumnPanel var2 = new TwoColumnPanel();
      var2.withRightFill(GridBagLayoutPanel.Fill.BOTH);
      var2.withRightWeights(1.0, 1.0);
      var2.withRightFill(GridBagLayoutPanel.Fill.BOTH);
      var2.withRightWeights(1.0, 1.0);
      var2.withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
      var2.addRow(UiProviderAdapter.label(EnterpriseAdminResources.getString("User.Upload.UploadTo")), this.optionPanel);
      var1.center(var2);
   }

   private final class KnownServerOption implements DestinationServerSelector.ServerOption {
      private KnownServerOption() {
      }

      public String getValue() {
         ServerInfo var1 = DestinationServerSelector.this.serverSelector.getSelectedServer();
         return var1 != null ? var1.getName() : null;
      }

      @Override
      public boolean isActive() {
         return DestinationServerSelector.this.serverSelector.isEnabled();
      }

      @Override
      public void setActive(boolean var1) {
         DestinationServerSelector.this.serverSelector.setEnabled(var1);
         if (var1) {
            DestinationServerSelector.this.serverSelector.requestFocus();
         }
      }

      @Override
      public void decorate(OptionSelector var1) {
         var1.setText(EnterpriseAdminResources.getString("User.Upload.KnownServer"));
      }

      @Override
      public void decorate(UiCanvas var1) {
         DestinationServerSelector.this.serverSelector.decorate(var1);
      }

      @Override
      public void requestFocus() {
         DestinationServerSelector.this.serverSelector.requestFocus();
      }
   }

   private final class NewServerOption implements DestinationServerSelector.ServerOption {
      private NewServerOption() {
      }

      public String getValue() {
         return DestinationServerSelector.this.newServerField.getValue().trim();
      }

      @Override
      public boolean isActive() {
         return DestinationServerSelector.this.newServerField.isActive();
      }

      @Override
      public void setActive(boolean var1) {
         DestinationServerSelector.this.newServerField.setActive(var1);
         DestinationServerSelector.this.newServerField.setEnabled(true);
         if (var1) {
            DestinationServerSelector.this.newServerField.requestFocus();
         }
      }

      @Override
      public void decorate(OptionSelector var1) {
         var1.setText(EnterpriseAdminResources.getString("User.Upload.NewServer"));
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(DestinationServerSelector.this.newServerField.getInputComponent());
      }

      @Override
      public void requestFocus() {
         DestinationServerSelector.this.newServerField.requestFocus();
      }
   }

   private interface ServerOption extends InputOption<String> {
   }
}
