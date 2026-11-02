package com.etlforge.integration.client.ui.script.fcn;

import java.awt.Color;
import javax.swing.Action;
import javax.swing.JButton;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.client.ui.script.builder.dataelement.DataElementViewer;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class TestConsole implements UiProvider, Disposable {
   private final JButton startButton;
   private final DataElementViewer deViewer;

   public TestConsole(ScriptDataElementsModel var1, Action var2) {
      this.startButton = new KongaButton(var2);
      this.deViewer = new DataElementViewer(var1);
   }

   @Override
   public void dispose() {
      this.deViewer.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      TestConsole.Layout var2 = new TestConsole.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.startButton.requestFocusInWindow();
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 5)
         .withBorder(new BorderBuilder().line(Color.GRAY).empty(5))
         .withBackground(StyleSheet.GenericEditorPanelClass.getBackground());

      public Layout() {
         this.layoutDataElementViewer();
         this.layoutStartButton();
      }

      private void layoutDataElementViewer() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 2).nonOpaque();
         var1.north("Data Elements").center(TestConsole.this.deViewer);
         this.layout.center(var1);
      }

      private void layoutStartButton() {
         this.layout.south(InvisiblePanel.of(TestConsole.this.startButton));
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }
}
