package org.jitterbit.integration.client.ui.interchange.component.start;

import java.awt.event.ActionListener;
import javax.swing.Icon;
import org.jitterbit.application.ui.ShowGettingStartedPagePreference;
import org.jitterbit.application.ui.look.TextStyles;
import org.jitterbit.application.ui.resources.CommonIcons;
import org.jitterbit.application.ui.window.ApplicationWindow;
import org.jitterbit.application.ui.window.editor.EditorCategory;
import org.jitterbit.application.ui.window.editor.EditorHistoryPoint;
import org.jitterbit.application.ui.window.editor.GenericEditor;
import org.jitterbit.ui.layout.BorderLayoutBuilder;

public final class GettingStartedPage extends GenericEditor {
   private static boolean hasBeenOpened;
   public static final String ID = "design.getting.started";
   private final GettingStartedUi ui;
   private SloganOpener sloganOpener;
   private final EditorHistoryPoint historyPoint;

   public static boolean hasBeenOpened() {
      return hasBeenOpened;
   }

   public GettingStartedPage(ActionListener var1, ActionListener var2) {
      super(
         "design.getting.started",
         "Getting Started",
         "You can use the Project Explorer to the left to create a new integration. Or use the links below to get started."
      );
      this.ui = new GettingStartedUi(var1, var2, ShowGettingStartedPagePreference.INSTANCE);
      this.historyPoint = new GettingStartedPageHistoryPoint(var1, var2);
      this.setCategory(new EditorCategory("Getting Started", CommonIcons.GETTING_STARTED_16));
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      this.createTitlePanel();
      this.setSubTitleStyle(TextStyles.DefaultText);
      this.setToolBar(this.createToolBar());
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.ui);
      this.setMainPanel(var2.container());
      super.addToWindow(var1);
      hasBeenOpened = true;
      this.sloganOpener = new SloganOpener(var1, this);
      this.addResource(this.sloganOpener);
   }

   @Override
   public EditorHistoryPoint getHistoryPoint() {
      return this.historyPoint;
   }

   @Override
   protected boolean includeMainTitleInTitleBar() {
      return false;
   }

   @Override
   public Icon getSmallIcon() {
      return CommonIcons.GETTING_STARTED_16;
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      if (super.removeFromWindow(var1)) {
         if (this.sloganOpener != null) {
            this.sloganOpener.dispose(this);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }
}
