package org.etlforge.integration.client.ui.connector.msax.endpoint.page;

import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeSupport;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.AdditionalParametersPanel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.binding.ui.PropertyValueHistory;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.PropertyChangeSource;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.ListProperty;
import org.etlforge.util.csv.CsvUtils;

final class AdditionalMSAXParamsPanel implements AdditionalParametersPanel {
   private final TextInputField domainName = new TextInputField("Domain Name", new KongaTextField(30), "Domain Name");
   private final KongaLabel wsdlListLabel = new KongaLabel("WSDL Urls");
   private final ListProperty<String> urls = new ListProperty<>("URLs", new PropertyChangeSupport(this));
   private final KongaList urlListWidget = this.createUrlListWidget();
   private final KongaTextField addUrlField = new KongaTextField(0);
   private final AdditionalMSAXParamsPanel.AddUrlAction addUrlAction = new AdditionalMSAXParamsPanel.AddUrlAction();
   private final KongaButton addUrlButton = new KongaButton(this.addUrlAction);
   private final KongaButton removeUrlButton = new KongaButton(new AdditionalMSAXParamsPanel.RemoveUrlAction());
   private final JComponent listLayout;

   public AdditionalMSAXParamsPanel() {
      ListBinding var1 = new ListBinding<>(this.urls, this.urlListWidget);
      var1.syncUi();
      this.addUrlField.addActionListener(this.addUrlAction);
      this.listLayout = this.layoutUrlList();
      this.addUrlButton.setEnabled(false);
      this.removeUrlButton.setEnabled(!this.urlListWidget.getSelection().isEmpty());
      this.addUrlField.getDocument().addDocumentListener(new AdditionalMSAXParamsPanel.AddUrlDocListener());
      this.urlListWidget.addSelectionListener(new AdditionalMSAXParamsPanel.WSDLUrlListSelectionListener());
   }

   private KongaList createUrlListWidget() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      return var1.newList();
   }

   private JComponent layoutUrlList() {
      return new BorderLayoutBuilder(0, 5).center(this.urlListWidget).container();
   }

   @Override
   public Properties getParameters() {
      Properties var1 = new Properties();
      var1.put("domain", this.domainName.getValue());
      if (this.urls.get() != null && this.urls.get().size() > 0) {
         var1.put("wsdl_urls", CsvUtils.toRecord(this.urls.get()));
      } else {
         var1.put("wsdl_urls", "");
      }

      return var1;
   }

   @Override
   public void setParameters(@Nullable Properties var1) {
      if (var1 == null) {
         this.domainName.setValue("");
         this.urls.clear();
      } else {
         this.domainName.setValue(var1.getProperty("domain"));
         if (var1.getProperty("wsdl_urls") != null) {
            String var2 = var1.getProperty("wsdl_urls");
            if (var2 != null && var2.trim().length() != 0) {
               this.urls.set(CsvUtils.toValues(var2));
            }
         }
      }
   }

   @Override
   public void layoutIn(NColumnPanel var1) {
      var1.addRow(this.domainName.getLabel(), this.domainName.getInputComponent(), null);
      var1.addRow(this.wsdlListLabel, this.listLayout, this.removeUrlButton);
      KongaLabel var2 = KongaLabel.withMnemonic("Add &WSDL:");
      this.addUrlField.setLabel(var2);
      var1.addRow(var2, this.addUrlField, this.addUrlButton);
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.domainName);
      var1.addChangeSource(PropertyChangeSource.of(this.urls));
   }

   @Override
   public ImmutableList<BoundProperty> getBoundProperties() {
      return ImmutableList.of(BoundPropertyFactory.textProperty("domain", this.domainName));
   }

   @Override
   public Disposable installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1) {
      DisposableCollection var2 = new DisposableCollection();
      var2.add(PropertyValueHistory.installAutoCompletion(this.domainName, var1, "domain"));
      return var2;
   }

   private class AddUrlAction extends AbstractAction {
      public AddUrlAction() {
         super("Add");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddUrlAction.this.addUrl();
               AdditionalMSAXParamsPanel.this.addUrlField.setText("");
            }
         });
      }

      private void addUrl() {
         String var1 = AdditionalMSAXParamsPanel.this.addUrlField.getText().trim();
         if (this.canBeAdded(var1)) {
            AdditionalMSAXParamsPanel.this.urls.add(var1);
         }
      }

      private boolean canBeAdded(String var1) {
         return !var1.isEmpty() && !AdditionalMSAXParamsPanel.this.urls.contains(var1);
      }
   }

   private class AddUrlDocListener implements DocumentListener {
      private AddUrlDocListener() {
      }

      private void checkFieldEmpty() {
         AdditionalMSAXParamsPanel.this.addUrlButton.setEnabled(!AdditionalMSAXParamsPanel.this.addUrlField.getText().trim().isEmpty());
      }

      @Override
      public void changedUpdate(DocumentEvent var1) {
         this.checkFieldEmpty();
      }

      @Override
      public void insertUpdate(DocumentEvent var1) {
         this.checkFieldEmpty();
      }

      @Override
      public void removeUpdate(DocumentEvent var1) {
         this.checkFieldEmpty();
      }
   }

   private class RemoveUrlAction extends AbstractAction {
      public RemoveUrlAction() {
         super("Remove");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (!AdditionalMSAXParamsPanel.this.urlListWidget.isEmpty()) {
                  int var1x = AdditionalMSAXParamsPanel.this.urlListWidget.getSelection().headIndex();
                  if (var1x >= 0) {
                     AdditionalMSAXParamsPanel.this.urls.remove(AdditionalMSAXParamsPanel.this.urls.itemAt(var1x));
                  }
               }
            }
         });
      }
   }

   private class WSDLUrlListSelectionListener implements SelectionListener {
      private WSDLUrlListSelectionListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         AdditionalMSAXParamsPanel.this.removeUrlButton.setEnabled(!AdditionalMSAXParamsPanel.this.urlListWidget.getSelection().isEmpty());
      }
   }
}
