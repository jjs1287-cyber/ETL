package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.graph2d.GraphZoomer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;

class GraphZoomButton {
   private final OperationPage page;
   private final JButton button;

   public GraphZoomButton(OperationPage var1) {
      this.page = var1;
      this.button = KongaButton.forToolbar(new GraphZoomButton.ZoomTrigger());
   }

   public JButton getButton() {
      return this.button;
   }

   private class ZoomTrigger extends AbstractAction {
      public ZoomTrigger() {
         super("", ClientIcons.ZOOM_16);
         this.putValue("ShortDescription", Strings.get("Page.Zoom.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         GraphZoomButton.Zoomer var2 = GraphZoomButton.this.new Zoomer();
         var2.open();
      }
   }

   private class Zoomer implements ActionListener {
      private final GraphZoomer zoomer = new GraphZoomer(GraphZoomButton.this.page.getGraph());
      private final OverlayDecorator overlay;

      public Zoomer() {
         this.zoomer.addActionListener(this);
         this.overlay = this.createOverlay();
      }

      private OverlayDecorator createOverlay() {
         JComponent var1 = this.createUi();
         OverlayDecorator var2 = new OverlayDecorator(var1, "Zoom level");
         var2.setBlocking(true);
         var2.useCloseOnly();
         var2.setFadeIn(AnimationPreference.get());
         var2.setAlpha(0.75F);
         return var2;
      }

      private JComponent createUi() {
         JPanel var1 = BorderLayoutBuilder.fillWith(this.zoomer);
         var1.setBorder(Empty.border(10));
         return var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.overlay.hide();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               JFrame var1x = GraphZoomButton.this.page.getWindow().getFrame();
               var1x.getRootPane().validate();
               var1x.getRootPane().repaint();
            }
         });
      }

      public void open() {
         this.overlay.show(GraphZoomButton.this.page.getOverlayContainer(), new OverlayRelativePositioner(GraphZoomButton.this.button, new Point(8, 16)));
         this.zoomer.requestFocus();
      }
   }
}
