package org.etlforge.ui.text.partition;

public interface TokenScanner {
   void setWhitespaceAware(boolean var1);

   void setPosition(int var1);

   void setText(String var1);

   Token next();
}
