package org.etlforge.integration.client.ui.script.builder;

import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.HtmlDisplayer;
import org.etlforge.integration.data.script.Function;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Flow;

public class FunctionHelpLayout implements UiProvider {
   private final JEditorPane displayer;
   private final JLabel header;

   public FunctionHelpLayout(Function var1) {
      this.header = TextStyles.DefaultBoldLarger.makeLabel(var1.getName());
      this.displayer = new HtmlDisplayer();
      this.setPreferredSize(new Dimension(400, 400));
      this.displayer.setText(new FunctionHelpHtml(var1).getFullDocumentation());
      this.displayer.setCaretPosition(0);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(Flow.leading().add(this.header).container());
      JScrollPane var3 = new JScrollPane(this.displayer);
      var3.getVerticalScrollBar().setUnitIncrement(10);
      var3.getHorizontalScrollBar().setUnitIncrement(10);
      var2.center(var3);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.displayer.requestFocusInWindow();
   }

   public void setPreferredSize(Dimension var1) {
      this.displayer.setPreferredSize(var1);
   }
}
