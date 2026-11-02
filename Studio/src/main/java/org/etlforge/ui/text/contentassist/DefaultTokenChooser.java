package org.etlforge.ui.text.contentassist;

import java.awt.EventQueue;
import javax.swing.text.JTextComponent;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.list.ListFactory;

public final class DefaultTokenChooser implements TokenChooser {
   private ListFactory listFactory;
   private int maxWidth = 50;
   private boolean trackWidthOfTrigger;

   @Override
   public void activate(final CompletionContext var1, JTextComponent var2, final CompletionToken[] var3, Receiver<CompletionToken> var4) {
      final TokenChooserListUi var5 = new TokenChooserListUi(var2, var4);
      var5.setAutoSelectFirstToken(true);
      var5.setListFactory(this.listFactory);
      var5.setTrackWidthOfTrigger(this.trackWidthOfTrigger);
      var5.setMaxWidth(this.maxWidth);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var5.display(var1, var3);
         }
      });
   }

   public void setListFactory(ListFactory var1) {
      this.listFactory = var1;
   }

   public void setMaxWidth(int var1) {
      this.maxWidth = var1 > 0 ? var1 : 50;
   }

   public void setTrackWidthOfTrigger(boolean var1) {
      this.trackWidthOfTrigger = var1;
   }
}
