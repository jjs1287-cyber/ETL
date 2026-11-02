package org.etlforge.integration.client.plugin.ui.pipeline;

import java.awt.Dimension;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.text.KongaTextPane;
import org.etlforge.ui.xml.XmlTextViewer;

public final class PipelinePluginManifestViewer implements UiProvider {
   private final XmlTextViewer xmlViewer = this.createXmlViewer();

   @Override
   public void decorate(UiCanvas var1) {
      this.xmlViewer.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.xmlViewer.requestFocus();
   }

   public void view(PipelinePluginManifest var1) {
      this.xmlViewer.view(var1.getDefinition());
   }

   private XmlTextViewer createXmlViewer() {
      KongaTextPane var1 = new KongaTextPane("").disableTabs();
      TextStyles.DefaultMonoSpaced.makeOver(var1);
      XmlTextViewer var2 = new XmlTextViewer(var1);
      var2.setPreferredSize(new Dimension(400, 400));
      return var2;
   }
}
