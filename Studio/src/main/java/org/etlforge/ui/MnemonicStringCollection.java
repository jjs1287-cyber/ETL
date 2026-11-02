package org.etlforge.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class MnemonicStringCollection {
   private final MnemonicStringFactory factory = MnemonicStringFactory.newCharMarkerFactory('&');
   private Set<Character> used = Sets.newHashSet();

   public void markCharactersAsUsed(Character... var1) {
      for (Character var5 : var1) {
         this.used.add(var5);
      }
   }

   public MnemonicString autoTranslate(String var1) {
      String var2 = this.insertMnemonicMarker(this.factory.escape(var1));
      return this.factory.fromString(var2);
   }

   public List<MnemonicString> autoTranslateAll(Collection<String> var1) {
      this.used = Sets.newHashSet();
      ArrayList var2 = Lists.newArrayList();

      for (String var4 : var1) {
         MnemonicString var5 = this.autoTranslate(var4);
         var2.add(var5);
      }

      return var2;
   }

   private String insertMnemonicMarker(String var1) {
      int var2 = this.findLocationForMarker(var1);
      return var2 == -1 ? var1 : var1.substring(0, var2) + "&" + var1.substring(var2);
   }

   private int findLocationForMarker(String var1) {
      char[] var2 = var1.toCharArray();

      for (int var3 = 0; var3 < var2.length; var3++) {
         char var4 = var2[var3];
         if (Character.isLetter(var4) && this.used.add(Character.toUpperCase(var4))) {
            return var3;
         }
      }

      return -1;
   }
}
