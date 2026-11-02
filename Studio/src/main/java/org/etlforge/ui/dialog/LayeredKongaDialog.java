package org.etlforge.ui.dialog;

import com.google.common.base.Supplier;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import javax.swing.JComponent;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.wait.WaitIndicator;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayContainerListener;
import org.etlforge.ui.widget.overlay.OverlayPositioner;

public class LayeredKongaDialog extends KongaDialog implements OverlayContainer {
   private final KongaLayeredPane layeredPane = new KongaLayeredPane();
   private final BorderLayoutBuilder defaultLayout = new BorderLayoutBuilder();

   public LayeredKongaDialog(String var1) {
      super(var1);
      this.init();
   }

   public LayeredKongaDialog(Frame var1, String var2) {
      super(var1, var2);
      this.init();
   }

   public LayeredKongaDialog(String var1, boolean var2) {
      super(var1, var2);
      this.init();
   }

   public LayeredKongaDialog(Frame var1, String var2, boolean var3) {
      super(var1, var2, var3);
      this.init();
   }

   public LayeredKongaDialog(Dialog var1, String var2, Image var3, boolean var4) {
      super(var1, var2, var3, var4);
      this.init();
   }

   public LayeredKongaDialog(Window var1, String var2) {
      super(var1, var2);
      this.init();
   }

   public LayeredKongaDialog(Window var1, String var2, ModalityType var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void init() {
      this.layeredPane.setBaseComponent(this.defaultLayout.container());
      super.setContentPane(this.layeredPane);
   }

   public BorderLayoutBuilder getDefaultLayout() {
      return this.defaultLayout;
   }

   @Override
   public void setContentPane(Container var1) {
      if (this.layeredPane != null) {
         this.layeredPane.setBaseComponent(var1);
      } else {
         super.setContentPane(var1);
      }
   }

   @Override
   public void addOverlay(JComponent var1, OverlayPositioner var2) {
      this.layeredPane.addOverlay(var1, var2);
   }

   @Override
   public void removeOverlay(JComponent var1) {
      this.layeredPane.removeOverlay(var1);
   }

   @Override
   public void addListener(OverlayContainerListener var1) {
      this.layeredPane.addListener(var1);
   }

   @Override
   public void removeListener(OverlayContainerListener var1) {
      this.layeredPane.removeListener(var1);
   }

   @Override
   public WaitLock startWait() {
      return this.layeredPane.startWait();
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.layeredPane.startWait(var1);
   }

   public boolean isInWaitState() {
      return this.layeredPane.isInWaitState();
   }

   @Override
   public void pack() {
      this.layeredPane.setPreferredSize(this.defaultLayout.container().getPreferredSize());
      super.pack();
   }

   @Override
   protected final WaitService createWaitService() {
      return super.createWaitService();
   }

   public final void setWaitIndicatorFactory(Supplier<WaitIndicator> var1) {
      this.layeredPane.setWaitIndicatorFactory(var1);
   }

   public final KongaLayeredPane getKongaLayeredPane() {
      return this.layeredPane;
   }
}
