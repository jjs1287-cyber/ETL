package org.etlforge.integration.client.plugin.ui.pipeline;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.etlforge.application.ui.widget.UrlTextFieldDecorator;
import org.etlforge.integration.client.plugin.ui.PluginDataElementTable;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.text.KongaTextField;

public final class PipelinePluginInfoViewer implements UiProvider {
   private PipelinePluginManifest plugin;
   private PipelinePluginManifestViewer manifestViewer;
   private JComponent getsFocus;

   public void view(PipelinePluginManifest var1) {
      Preconditions.checkNotNull(var1, "plugin");
      this.plugin = var1;
      if (this.manifestViewer != null) {
         this.manifestViewer.view(var1);
      }
   }

   public void setPluginManifestViewer(PipelinePluginManifestViewer var1) {
      this.manifestViewer = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.createDisplayer();
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      if (this.getsFocus != null) {
         this.getsFocus.requestFocusInWindow();
      }
   }

   private JComponent createDisplayer() {
      JComponent var1 = null;
      if (this.manifestViewer == null) {
         var1 = this.createDetailsFormCapsule();
      } else {
         var1 = this.createTabbedPane();
      }

      return var1;
   }

   private JComponent createTabbedPane() {
      KongaTabbedPane var1 = new KongaTabbedPane();
      var1.setInterpretTitlesAsMnemonicStrings(true);
      var1.addTab(Strings.get("PipelinePlugin.InfoViewer.Tab.Details"), this.createDetailsFormCapsule());
      var1.addTab(Strings.get("PipelinePlugin.InfoViewer.Tab.PluginXml"), this.createManifestXmlCapsule());
      this.getsFocus = var1.asComponent();
      return var1.asComponent();
   }

   private JComponent createDetailsFormCapsule() {
      JPanel var1 = InvisiblePanel.newPanel();
      TwoColumnPanel var2 = this.createDetailsForm();
      var2.addToParent(var1, null);
      return var1;
   }

   private JComponent createManifestXmlCapsule() {
      return BorderLayoutBuilder.fillWith(this.manifestViewer);
   }

   private TwoColumnPanel createDetailsForm() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withInsets(5, 5, 5, 5);
      var1.withRightFill(TwoColumnPanel.HORIZONTAL);
      var1.withRightWeights(1.0, 0.0);
      this.addOneLineEntry(var1, Strings.get("PipelinePlugin.Label.Name"), this.plugin.getName());
      this.addOneLineEntry(var1, Strings.get("PipelinePlugin.Label.Version"), this.plugin.getVersion());
      this.addOneLineEntry(var1, Strings.get("PipelinePlugin.Label.Author"), this.plugin.getAuthor());
      this.addOneLineEntry(var1, Strings.get("PipelinePlugin.Label.Command"), this.plugin.getCommand());
      this.addOneLineEntry(var1, Strings.get("PipelinePlugin.Label.Arguments"), this.plugin.getArguments());
      this.addMultiLineEntry(var1, Strings.get("PipelinePlugin.Label.Description"), this.plugin.getDescription());
      this.addUrlEntry(var1, Strings.get("PipelinePlugin.Label.DocumentationUrl"), this.plugin.getDocumentationUrl());
      this.addUrlEntry(var1, Strings.get("PipelinePlugin.Label.UpdateUrl"), this.plugin.getUpdateUrl());
      this.addDataElementsTable(var1);
      var1.setOpaque(false, true);
      return var1;
   }

   private void addDataElementsTable(TwoColumnPanel var1) {
      JLabel var2 = new JLabel(Strings.get("PipelinePlugin.Label.DataElements"));
      PluginDataElementTable var3 = new PluginDataElementTable(this.plugin.getDataElements());
      JPanel var4 = BorderLayoutBuilder.fillWith(var3);
      var1.withAnchor(TwoColumnPanel.NORTHWEST);
      var1.addRow(var2, var4);
   }

   private void addOneLineEntry(TwoColumnPanel var1, String var2, Object var3) {
      this.addOneLineEntry(var1, var2, var3 == null ? "" : var3.toString());
   }

   private void addOneLineEntry(TwoColumnPanel var1, String var2, String var3) {
      var1.addRow(getLabel(var2), getTextField(var3));
   }

   private void addUrlEntry(TwoColumnPanel var1, String var2, URL var3) {
      var1.addRow(getLabel(var2), getUrlField(var3));
   }

   private void addMultiLineEntry(TwoColumnPanel var1, String var2, String var3) {
      var1.withAnchor(TwoColumnPanel.NORTHWEST);
      var1.addRow(getLabel(var2), getTextArea(var3));
      var1.withAnchor(TwoColumnPanel.WEST);
   }

   private static JComponent getLabel(String var0) {
      return new JLabel(var0);
   }

   private static JTextField getTextField(String var0) {
      KongaTextField var1 = new KongaTextField(var0);
      var1.setEditable(false);
      var1.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      return var1;
   }

   private static JComponent getUrlField(URL var0) {
      String var1 = var0 != null ? var0.toString() : "";
      JTextField var2 = getTextField(var1);
      UrlTextFieldDecorator.install(var2);
      return var2;
   }

   private static JComponent getTextArea(String var0) {
      KongaTextArea var1 = KongaTextArea.viewer(var0, Size.rows(5), Size.columns(40));
      var1.setCaretPosition(0);
      return new JScrollPane(var1);
   }
}

