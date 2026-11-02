package org.etlforge.integration.client.ui.interchange.location.http;

import com.google.common.base.Preconditions;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import org.etlforge.integration.client.datalocation.cache.HttpLocationCache;
import org.etlforge.integration.client.ui.http.HttpPropertiesEditor;
import org.etlforge.integration.client.ui.http.LaunchPropertiesEditorAction;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelUtils;
import org.etlforge.integration.client.ui.interchange.location.panel.CacheBasedLoginNameHistory;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.http.HttpConnector;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.OverlayPositioner;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class HttpLocationPanel extends DataLocationPanel {
   private final HttpUrlDefiner urlDefiner = new HttpUrlDefiner(this);
   private final HttpLoginDefiner loginDefiner = new HttpLoginDefiner(this);
   private final UseBasicHttpAuthenticationPart useBasicAuthSelector = new UseBasicHttpAuthenticationPart(this);
   private HttpLocationPanelPart responseTargetSelector;
   private final HttpVerbSelectionPanel verbSelector;
   private final HttpLocationPanelPart contentTypeDefiner;
   private final HttpPropertiesEditor httpPropertiesEditor;
   private final HttpLocationPanelPart ignoreLinesDefiner;
   private final LaunchPropertiesEditorAction launchPropertiesEditorAction;
   private JButton httpPropertiesButton;
   private HttpLocationCache cache;

   public HttpLocationPanel(HttpLocation var1, SourceTarget var2) {
      super(var1, var2);
      this.verbSelector = new HttpVerbSelectionPanel(this, var1, var2);
      this.ignoreLinesDefiner = var2 == SourceTarget.Source ? new IgnoreLinesDefiner(this) : new NullHttpLocationPanelPart();
      this.responseTargetSelector = new NullHttpLocationPanelPart();
      this.contentTypeDefiner = var2 == SourceTarget.Target ? new ContentTypeDefiner(this) : new NullHttpLocationPanelPart();
      this.httpPropertiesEditor = new HttpLocationPanel.PropertiesEditor(var1);
      this.httpPropertiesEditor.addInputChangeListener(this);
      this.httpPropertiesEditor.addUndoRedoListener(this);
      this.launchPropertiesEditorAction = this.createLaunchHttpEditorsAction();
      this.urlDefiner.addChangeListener(new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            HttpLocationPanel.this.updateUseBasicAuthenticationSecurityWarning();
         }
      });
   }

   private void updateUseBasicAuthenticationSecurityWarning() {
      boolean var1 = this.urlDefiner.isHttps();
      this.useBasicAuthSelector.setHttps(var1);
   }

   @Override
   public void display() {
      this.updateFields(this.getDisplayedObject());
      this.doLayout();
      super.display();
   }

   @Override
   public void setInitialFocus() {
      this.urlDefiner.requestFocus();
   }

   @Override
   public void dispose() {
      super.dispose();
      this.loginDefiner.dispose();
      this.responseTargetSelector.dispose();
      this.useBasicAuthSelector.dispose();
   }

   public void setCache(HttpLocationCache var1) {
      Preconditions.checkNotNull(var1, "cache");
      this.cache = var1;
      this.loginDefiner.installAutoCompletionInLoginField(new CacheBasedLoginNameHistory(var1));
      this.urlDefiner.setHistory(new CacheBasedUrlHistory(var1));
   }

   public void setResponseTargetSelector(ResponseTargetSelector var1) {
      if (this.getSourceTarget() == SourceTarget.Target) {
         HttpResponseTargetDefiner var2 = new HttpResponseTargetDefiner(this, this.getDisplayedObject());
         var2.setSelector(var1);
         this.responseTargetSelector = var2;
      } else {
         throw new UnsupportedOperationException("Supported only for Target locations");
      }
   }

   private void doLayout() {
      DataLocationPanelLayout var1 = new DataLocationPanelLayout();
      this.urlDefiner.addToLayout(var1);
      this.loginDefiner.addToLayout(var1);
      this.useBasicAuthSelector.addToLayout(var1);
      this.verbSelector.addToLayout(var1);
      JComponent var2 = this.createOptionsLayout();
      var1.add(null, var2, false);
      this.wrap(var1);
   }

   private JComponent createOptionsLayout() {
      DataLocationPanelLayout var1 = new DataLocationPanelLayout();
      this.contentTypeDefiner.addToLayout(var1);
      this.responseTargetSelector.addToLayout(var1);
      this.ignoreLinesDefiner.addToLayout(var1);
      this.httpPropertiesButton = new KongaButton(this.launchPropertiesEditorAction);
      this.launchPropertiesEditorAction.setTrigger(this.httpPropertiesButton);
      var1.add(null, InvisiblePanel.of(this.httpPropertiesButton), false);
      DropDownPanel var2 = DataLocationPanelUtils.createOptionsDropDown(InvisiblePanel.of(var1), false);
      return var2.getDisplayer();
   }

   private LaunchPropertiesEditorAction createLaunchHttpEditorsAction() {
      LaunchPropertiesEditorAction var1 = new LaunchPropertiesEditorAction(this.httpPropertiesEditor, Strings.get("Http.Advanced.Name"), null);
      var1.putValue("ShortDescription", Strings.format("Http.Advanced.ToolTip", this.getSourceTarget()));
      return var1;
   }

   private void updateFields(HttpLocation var1) {
      this.urlDefiner.reset(var1);
      this.loginDefiner.reset(var1);
      this.useBasicAuthSelector.reset(var1);
      this.ignoreLinesDefiner.reset(var1);
      this.responseTargetSelector.reset(var1);
      this.verbSelector.reset(var1);
      this.contentTypeDefiner.reset(var1);
      this.httpPropertiesEditor.getValuesFrom(var1);
      this.updateUseBasicAuthenticationSecurityWarning();
   }

   public HttpLocation getDisplayedObject() {
      return (HttpLocation)super.getDisplayedObject();
   }

   @Override
   public void apply() throws IntegrationDataPanelException {
      HttpLocation var1 = this.getDisplayedObject();
      this.applyParts(var1);
      this.httpPropertiesEditor.apply();
      this.updateCache(var1);
   }

   private void applyParts(HttpLocation var1) throws IntegrationDataPanelException {
      this.urlDefiner.applyTo(var1);
      this.loginDefiner.applyTo(var1);
      this.useBasicAuthSelector.applyTo(var1);
      this.ignoreLinesDefiner.applyTo(var1);
      this.responseTargetSelector.applyTo(var1);
      this.verbSelector.applyTo(var1);
      this.contentTypeDefiner.applyTo(var1);
   }

   private void updateCache(HttpLocation var1) {
      if (this.cache != null) {
         this.cache.update(var1);
      }
   }

   @Override
   public void reset() {
      this.updateFields(this.getDisplayedObject());
   }

   private class PropertiesEditor extends HttpPropertiesEditor {
      public PropertiesEditor(HttpConnector var2) {
         super(var2);
      }

      @Override
      protected OverlayPositioner getOverlayPositioner() {
         return new OverlayRelativePositioner(HttpLocationPanel.this.httpPropertiesButton, new Point(8, 8));
      }

      @Override
      protected void overlayRemoved() {
         HttpLocationPanel.this.httpPropertiesButton.requestFocusInWindow();
      }
   }
}
