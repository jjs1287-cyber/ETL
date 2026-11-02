package org.etlforge.ui.wait;

import com.google.common.base.Supplier;
import javax.swing.JComponent;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.CircleProgressBarWithMessage;
import org.etlforge.ui.widget.overlay.DefaultWaitIndicatorFactory;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;

public final class DefaultMessageWaitService implements MessageWaitService {
   private final KongaLayeredPane layeredPane;
   private final TextStyle messageStyle;
   private final DefaultWaitIndicatorFactory defaultFactory = new DefaultWaitIndicatorFactory();

   public DefaultMessageWaitService(KongaLayeredPane var1, TextStyle var2) {
      this.layeredPane = var1;
      this.messageStyle = var2;
   }

   @Override
   public WaitLock startWait() {
      return this.layeredPane.startWait();
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.layeredPane.startWait(var1);
   }

   @Override
   public WaitLock startWait(final String var1) {
      return UiThreadSupplier.call(new Supplier<WaitLock>() {
         public WaitLock get() {
            if (!DefaultMessageWaitService.this.layeredPane.isInWaitState()) {
               DefaultMessageWaitService.this.layeredPane.setWaitIndicatorFactory(DefaultMessageWaitService.this.new MessageWaitIndicatorFactory(var1));
            }

            return DefaultMessageWaitService.this.startWait();
         }
      });
   }

   private class MessageWaitIndicatorFactory implements Supplier<WaitIndicator> {
      private final String message;

      public MessageWaitIndicatorFactory(String var2) {
         this.message = var2;
      }

      public WaitIndicator get() {
         CircleProgressBarWithMessage var1 = new CircleProgressBarWithMessage(DefaultMessageWaitService.this.defaultFactory.get(), this.message);
         var1.setMessageStyle(DefaultMessageWaitService.this.messageStyle);
         var1.setOpaque(false);
         var1.setBorder(Empty.border());
         return DefaultMessageWaitService.this.new WaitIndicatorImpl(var1);
      }
   }

   private class WaitIndicatorImpl implements WaitIndicator {
      private final WaitIndicator impl;

      public WaitIndicatorImpl(WaitIndicator var2) {
         this.impl = var2;
      }

      @Override
      public void start() {
         this.impl.start();
      }

      @Override
      public void stop() {
         this.impl.stop();
         this.restoreDefaultWaitIndicatorFactory();
      }

      private void restoreDefaultWaitIndicatorFactory() {
         DefaultMessageWaitService.this.layeredPane.setWaitIndicatorFactory(DefaultMessageWaitService.this.defaultFactory);
      }

      @Override
      public JComponent getUi() {
         return this.impl.getUi();
      }
   }
}
