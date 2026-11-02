package org.etlforge.application.ui.groovy;

import com.google.common.base.Function;
import java.util.Arrays;
import org.etlforge.lang.Arrays2;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;

final class PackageNameCompletionContextBuilder {
   public static CompletionContext buildForField() {
      return new TokenListCompletionContext(getKnownPackageNames());
   }

   private static String[] getKnownPackageNames() {
      Package[] var0 = Package.getPackages();
      String[] var1 = Arrays2.transform(var0, String.class, new Function<Package, String>() {
         public String apply(Package var1) {
            return var1.getName();
         }
      });
      Arrays.sort(var1);
      return var1;
   }

   public static CompletionContext buildForEditor() {
      return new EditorCompletionContext(getKnownPackageNames());
   }

   private PackageNameCompletionContextBuilder() {
   }
}
