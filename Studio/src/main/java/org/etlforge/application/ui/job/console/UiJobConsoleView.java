package org.etlforge.application.ui.job.console;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.ui.job.ProcessConsoleIcons;
import org.etlforge.application.ui.job.UiJobConsole;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.ui.canvas.ComponentCanvas;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;

public class UiJobConsoleView extends AbstractWindowSectionContent {
   public static final String CONTENT_ID = "ui_job_console";
   private final UiJobConsole console;

   public UiJobConsoleView(UiJobConsole var1, WindowSection var2) {
      super("ui_job_console", var2);
      this.console = var1;
   }

   @Override
   public String getTitle() {
      return Strings.get("JobConsole.View.Title");
   }

   @Override
   public Icon getIcon() {
      return ProcessConsoleIcons.PROCESS_OFF_16;
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = BorderLayoutBuilder.from(var1);
      var2.north(this.getTitlePanel());
      ComponentCanvas var3 = ComponentCanvasFactory.northBorder(this.console.getUi());
      JComponent var4 = var3.getUi();
      KongaScrollPane var5 = new KongaScrollPane(var4);
      var2.center(var5);
      UiUtils.setOpaqueRecursively(var5, false);
      var1.setBackground(Colors.NEUTRAL_BACKGROUND);
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      var1.add(this.createRemoveCompletedButton());
      super.populateToolBar(var1);
   }

   private JButton createRemoveCompletedButton() {
      return createToolBarButton(new UiJobConsoleView.RemoveCompletedAction());
   }

   private class RemoveCompletedAction extends AbstractAction {
      public RemoveCompletedAction() {
         super("", ProcessConsoleIcons.DELETE);
         this.putValue("ShortDescription", Strings.get("JobConsole.View.Remove.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         UiJobConsoleView.this.console.removeCompletedJobs();
      }
   }
}
