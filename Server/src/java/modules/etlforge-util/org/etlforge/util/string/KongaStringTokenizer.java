package org.etlforge.util.string;

import java.util.Stack;
import java.util.StringTokenizer;

public final class KongaStringTokenizer extends StringTokenizer {
   private final Stack<String> savedTokens = new Stack<>();

   public KongaStringTokenizer(String var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public KongaStringTokenizer(String var1, String var2) {
      super(var1, var2);
   }

   public KongaStringTokenizer(String var1) {
      super(var1);
   }

   @Override
   public String nextToken() {
      return this.savedTokens.isEmpty() ? super.nextToken() : this.savedTokens.pop();
   }

   @Override
   public String nextToken(String var1) {
      return this.savedTokens.isEmpty() ? super.nextToken(var1) : this.savedTokens.pop();
   }

   @Override
   public Object nextElement() {
      return this.savedTokens.isEmpty() ? super.nextElement() : this.savedTokens.pop();
   }

   @Override
   public boolean hasMoreTokens() {
      return this.savedTokens.isEmpty() ? super.hasMoreTokens() : true;
   }

   @Override
   public boolean hasMoreElements() {
      return this.savedTokens.isEmpty() ? super.hasMoreTokens() : true;
   }

   @Override
   public int countTokens() {
      return this.savedTokens.size() + super.countTokens();
   }

   public void push(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.savedTokens.add(var1);
      }
   }

   public String peek() {
      if (this.savedTokens.isEmpty()) {
         if (!super.hasMoreTokens()) {
            return null;
         }

         this.savedTokens.add(super.nextToken());
      }

      return this.savedTokens.peek();
   }
}
