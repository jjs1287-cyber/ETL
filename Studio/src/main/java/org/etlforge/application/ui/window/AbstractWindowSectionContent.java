package org.etlforge.application.ui.window;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.SimpleTabPane;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.NoTopEtchedBorder;
import org.etlforge.ui.widget.button.KongaButton;

public abstract class AbstractWindowSectionContent extends AbstractWindowElement implements WindowSectionContent {
   private final WindowSection section;
   private final AbstractWindowSectionContent.CloseAction closeAction;

   protected AbstractWindowSectionContent(String var1, WindowSection var2) {
      super(var1);
      Preconditions.checkNotNull(var2);
      this.section = var2;
      this.closeAction = new AbstractWindowSectionContent.CloseAction();
   }

   @Override
   public final WindowSection getWindowSection() {
      return this.section;
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public boolean isDetachable() {
      return true;
   }

   public final Action getCloseAction() {
      return this.closeAction;
   }

   protected final void doDefaultDecoration(JComponent var1, final UiProvider var2) {
      BorderLayoutBuilder.from(var1).north(this.getTitlePanel()).center(var2).withBackground(StyleSheet.GenericEditorPanelClass.getBackground());
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var2.requestFocus();
         }
      });
   }

   protected String getTitlePanelTitle() {
      return null;
   }

   protected final JComponent getTitlePanel() {
      String var1 = this.getTitlePanelTitle();
      JLabel var2 = var1 == null ? null : new JLabel(var1);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      JComponent var4 = this.getAdditionalTitleComponent();
      if (var4 == null) {
         var3.west(var2);
      } else {
         BoxBuilder var5 = Horizontal.box().of(var2, var4);
         var3.west(var5);
      }

      var3.center(Box.createGlue()).east(this.createToolBar());
      var3.withBorder(getTitlePanelBorder());
      UiUtils.setPanelBackgrounds(var3.container(), getTitlePanelColor());
      return var3.container();
   }

   protected JComponent getAdditionalTitleComponent() {
      return null;
   }

   protected static final Color getTitlePanelColor() {
      return SimpleTabPane.SUPPORTED ? Colors.LIGHT_BLUE_BACKGROUND : Colors.NEUTRAL_BACKGROUND;
   }

   protected static final Border getTitlePanelBorder() {
      Border var0 = SimpleTabPane.SUPPORTED ? new NoTopEtchedBorder() : new EtchedBorder();
      return new CompoundBorder(var0, Empty.border(0, 2, 0, 2));
   }

   protected JToolBar createToolBar() {
      JToolBar var1 = createEmptyToolBar();
      this.populateToolBar(var1);
      return var1;
   }

   protected void populateToolBar(JToolBar var1) {
      JButton var2 = createToolBarButton(this.closeAction);
      var1.add(var2);
   }

   private static JToolBar createEmptyToolBar() {
      JToolBar var0 = new JToolBar();
      var0.setBorder(Empty.border());
      var0.setFloatable(false);
      var0.setRollover(true);
      var0.setOpaque(false);
      return var0;
   }

   protected static final JButton createToolBarButton(Action var0) {
      return KongaButton.forToolbar(var0);
   }

   protected static JButton createToolBarButton(Icon var0, String var1) {
      KongaButton var2 = new KongaButton(var0).makeToolbarButton();
      var2.setToolTipText(var1);
      return var2;
   }

   private class CloseAction extends AbstractAction {
      public CloseAction() {
         ActionDecorators.CLOSE.decorate(this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         BusyWorker var2 = new BusyWorker(AbstractWindowSectionContent.this.getWindow()) {
            @Override
            protected void doWork() {
               AbstractWindowSectionContent.this.section.removeContent(AbstractWindowSectionContent.this);
            }
         };
         var2.setDelay(75);
         var2.run();
      }
   }
}
