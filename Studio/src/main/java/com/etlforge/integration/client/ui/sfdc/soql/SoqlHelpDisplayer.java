package com.etlforge.integration.client.ui.sfdc.soql;

import java.awt.Color;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import org.etlforge.application.ui.widget.HtmlDisplayer;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayPositioner;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;

public final class SoqlHelpDisplayer {
   private static final String URL = "http://www.salesforce.com/us/developer/docs/api/index_Left.htm#StartTopic=Content%2Fsforce_api_calls_soql_relationships.htm";
   private final HtmlDisplayer htmlViewer = this.createHtmlDisplayer();
   private final SoqlHelpDisplayer.DisplayOption displayer;

   public static void open(JComponent var0) {
      new SoqlHelpDisplayer(var0).open();
   }

   private SoqlHelpDisplayer(JComponent var1) {
      this.displayer = this.createDisplayer(var1);
      this.htmlViewer.addHyperlinkListener(new HyperlinkListener() {
         @Override
         public void hyperlinkUpdate(HyperlinkEvent var1) {
            if (var1.getEventType() == EventType.ACTIVATED) {
               SoqlHelpDisplayer.this.displayer.close();
            }
         }
      });
   }

   private HtmlDisplayer createHtmlDisplayer() {
      HtmlDisplayer var1 = new HtmlDisplayer();
      var1.setText(getHtml());
      var1.setCaretPosition(0);
      return var1;
   }

   private SoqlHelpDisplayer.DisplayOption createDisplayer(JComponent var1) {
      OverlayContainer var2 = WidgetUtils.getOverlayContainerAncestor(var1);
      return var2 == null ? new SoqlHelpDisplayer.DialogDisplayer(var1) : new SoqlHelpDisplayer.OverlayDisplayer(var2, var1);
   }

   private void open() {
      this.displayer.open();
   }

   private static String getHtml() {
      return "<html>Here you should enter a Salesforce.com Object Query Language (SOQL) query.<br><br>SOQL can be generated using Data Loader or Apex Explorer. Documentation on SOQL is available at <a href=\"http://www.salesforce.com/us/developer/docs/api/index_Left.htm#StartTopic=Content%2Fsforce_api_calls_soql_relationships.htm\">salesforce.com</a>.</html>";
   }

   private class DialogDisplayer implements SoqlHelpDisplayer.DisplayOption {
      private KongaDialog dialog;

      public DialogDisplayer(JComponent var2) {
         this.dialog = this.createDialog(var2);
      }

      private KongaDialog createDialog(JComponent var1) {
         KongaDialog var2 = new KongaDialog(SwingUtilities.getWindowAncestor(var1), "SOQL Information");
         var2.setUndecorated(true);
         BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 10).center(SoqlHelpDisplayer.this.htmlViewer);
         var2.standardLayout(var3, KongaDialog.CLOSE_ONLY);
         ((JComponent)var2.getContentPane()).setBorder(new LineBorder(Color.BLACK));
         var2.setLocationRelativeTo(var2.getOwner());
         return var2;
      }

      @Override
      public void open() {
         this.dialog.setVisible(true);
         this.dialog = null;
      }

      @Override
      public void close() {
         this.dialog.dispose();
         this.dialog = null;
      }
   }

   private interface DisplayOption {
      void open();

      void close();
   }

   private class OverlayDisplayer implements SoqlHelpDisplayer.DisplayOption {
      private final OverlayContainer container;
      private final OverlayDecorator overlay;
      private final OverlayPositioner positioner;

      public OverlayDisplayer(OverlayContainer var2, JComponent var3) {
         this.container = var2;
         this.overlay = new OverlayDecorator(SoqlHelpDisplayer.this.htmlViewer, null);
         this.overlay.useCloseOnly();
         this.overlay.setBlocking(true);
         this.overlay.setFocusReceiverWhenClosed(var3);
         this.overlay.setAlpha(0.8F);
         this.positioner = new OverlayRelativePositioner(var3, new Point(8, 8));
      }

      @Override
      public void close() {
         this.overlay.hide();
      }

      @Override
      public void open() {
         this.overlay.show(this.container, this.positioner);
      }
   }
}
