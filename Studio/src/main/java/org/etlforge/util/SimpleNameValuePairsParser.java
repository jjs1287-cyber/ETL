package org.etlforge.util;

public class SimpleNameValuePairsParser extends NameValuePairsParser {
   public SimpleNameValuePairsParser(boolean var1) {
      super(var1);
   }

   @Override
   protected String convertToString(NameValuePair var1) {
      return var1.getName() + '=' + var1.getValue();
   }

   @Override
   protected void parserNameValuePairImpl(String var1) {
      int var2 = var1.indexOf(61);
      boolean var3 = var2 >= 0 && var2 < var1.length();
      if (var3) {
         this.pairName = var1.substring(0, var2);
         this.pairValue = var1.substring(var2 + 1);
      }
   }

   @Override
   protected String[] splitNameValuePairsImpl(String var1) {
      String var2 = new String(NameValuePairSeparatorSplitter);
      return var1.split(var2);
   }
}
