package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Preconditions;
import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.Orientation;
import org.etlforge.ui.widget.button.KongaButton;

public final class CreateIntegrationControlPanel implements UiProvider {
   private final CreateIntegrationControlPanel.Button createOperation;
   private final CreateIntegrationControlPanel.Button createRequest;
   private final CreateIntegrationControlPanel.Button createResponse;
   private Orientation orientation = Orientation.HORIZONTAL;

   public CreateIntegrationControlPanel(Action var1, Action var2, Action var3) {
      this.createOperation = new CreateIntegrationControlPanel.Button(Preconditions.checkNotNull(var1, "createOperation"));
      this.createRequest = new CreateIntegrationControlPanel.Button(Preconditions.checkNotNull(var2, "createRequest"));
      this.createResponse = new CreateIntegrationControlPanel.Button(Preconditions.checkNotNull(var3, "createResponse"));
      forceSameSize(this.createOperation, this.createRequest, this.createResponse);
   }

   private static void forceSameSize(CreateIntegrationControlPanel.Button... var0) {
      Dimension var1 = new Dimension(0, 0);

      for (CreateIntegrationControlPanel.Button var5 : var0) {
         Dimension var6 = var5.getDefaultPreferredSize();
         var1.width = Math.max(var1.width, var6.width);
         var1.height = Math.max(var1.height, var6.height);
      }

      for (CreateIntegrationControlPanel.Button var10 : var0) {
         var10.overridePreferredSize(var1);
      }
   }

   public void setOrientation(Orientation var1) {
      this.orientation = Preconditions.checkNotNull(var1, "orientation");
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.doLayout();
      var1.display(var2);
   }

   private JComponent doLayout() {
      Border var1 = StyleSheet.getDefaultTitleBorder("Use in an Integration");
      return this.orientation == Orientation.VERTICAL
         ? Vertical.column()
            .stretchHorizontally()
            .withInsets(4, 0, 4, 0)
            .of(this.createOperation, new JSeparator(), this.createRequest, this.createResponse)
            .withBorder(var1)
            .nonOpaque()
            .invisible()
         : Horizontal.box()
            .addAllWithSpace(10, this.createOperation, new JSeparator(1), this.createRequest, this.createResponse)
            .withBorder(var1)
            .nonOpaque()
            .invisible();
   }

   @Override
   public void requestFocus() {
      this.createOperation.requestFocusInWindow();
   }

   private static class Button extends KongaButton {
      private Dimension prefSize;

      public Button(Action var1) {
         super(var1);
         this.withMediumActionIcon();
      }

      public void overridePreferredSize(Dimension var1) {
         this.prefSize = new Dimension(var1);
      }

      @Override
      public Dimension getPreferredSize() {
         return this.prefSize == null ? super.getPreferredSize() : new Dimension(this.prefSize);
      }

      public Dimension getDefaultPreferredSize() {
         return super.getPreferredSize();
      }
   }
}
