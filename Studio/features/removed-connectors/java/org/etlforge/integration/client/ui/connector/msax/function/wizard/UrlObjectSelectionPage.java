package org.etlforge.integration.client.ui.connector.msax.function.wizard;

import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.ValidStateBinding;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.util.ListProperty;
import org.etlforge.util.csv.CsvUtils;

public class UrlObjectSelectionPage extends AbstractWizardPage {
   private final ListProperty<String> urls = new ListProperty<>("URLs", new PropertyChangeSupport(this));
   private final KongaList urlListWidget = this.createUrlListWidget();
   private final ObjectSelectionModel model;

   public UrlObjectSelectionPage(Wizard var1, String var2, ObjectSelectionModel var3) {
      super(var1, var2);
      this.model = var3;
      ListBinding var4 = new ListBinding<>(this.urls, this.urlListWidget);
      var4.syncUi();
      Binding var5 = ValidStateBinding.notNull(this, var3, "selected_object");
      var5.syncUi();
      this.urlListWidget.addSelectionListener(new UrlObjectSelectionPage.WSDLUrlListSelectionListener());
   }

   private KongaList createUrlListWidget() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      return var1.newList();
   }

   public final ObjectSelectionModel getModel() {
      return this.model;
   }

   public final void populateList() {
      String var1 = this.getModel().getEndpoint().getConnectionParameters().getAdditionalParam("wsdl_urls");
      if (var1 != null && var1.length() != 0) {
         this.urls.set(CsvUtils.toValues(var1));
      }

      LinkedList var2 = new LinkedList();

      for (String var4 : this.urls.get()) {
         var2.add(new SimpleConnectorObject(var4));
      }

      this.getModel().setObjects(var2);
   }

   @Override
   protected final JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      JPanel var2 = BorderLayoutBuilder.fillWith(this.urlListWidget);
      var1.add(var2, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public final void requestFocus() {
      this.urlListWidget.requestFocus();
   }

   @Override
   public final boolean prepareForBackwardNavigation() {
      return true;
   }

   @Override
   public boolean canContinue() {
      String var1 = this.getModel().getEndpoint().getConnectionParameters().getAdditionalParam("wsdl_urls");
      return var1 != null && var1.length() != 0;
   }

   private class WSDLUrlListSelectionListener implements SelectionListener {
      private WSDLUrlListSelectionListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         int var2 = UrlObjectSelectionPage.this.urlListWidget.getSelection().headIndex();
         if (var2 >= 0) {
            UrlObjectSelectionPage.this.model.getSelectedObjectProperty().set(UrlObjectSelectionPage.this.model.getObjectsProperty().itemAt(var2));
         } else {
            UrlObjectSelectionPage.this.model.getSelectedObjectProperty().set(null);
         }
      }
   }
}
