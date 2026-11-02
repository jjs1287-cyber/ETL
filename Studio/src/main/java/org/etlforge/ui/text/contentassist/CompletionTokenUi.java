package org.etlforge.ui.text.contentassist;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import javax.swing.Icon;
import javax.swing.text.Document;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.util.Interval;

public final class CompletionTokenUi implements CompletionToken, ListDecorator {
   private final CompletionToken token;
   public static final Comparator<CompletionTokenUi> BY_LENGTH = new Comparator<CompletionTokenUi>() {
      public int compare(CompletionTokenUi var1, CompletionTokenUi var2) {
         return KongaStringUtils.compareLength(var1.token.getDisplayValue(), var2.token.getDisplayValue());
      }
   };

   public CompletionTokenUi(CompletionToken var1) {
      Preconditions.checkNotNull(var1, "token");
      this.token = var1;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setIcon(this.token.getIcon());
      var1.setText(this.token.getDisplayValue());
   }

   @Override
   public String toString() {
      return this.token.getDisplayValue();
   }

   @Override
   public void complete(Document var1) {
      this.token.complete(var1);
   }

   @Override
   public String getDisplayValue() {
      return this.token.getDisplayValue();
   }

   @Override
   public Icon getIcon() {
      return this.token.getIcon();
   }

   @Override
   public String getInfo() {
      return this.token.getInfo();
   }

   @Override
   public Interval getInterval(Document var1) {
      return this.token.getInterval(var1);
   }
}
