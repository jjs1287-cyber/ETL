package org.etlforge.ui.util;

import com.google.common.base.Preconditions;
import java.awt.Image;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.lang.StringUtils;

public final class ImageLoader {
   private ImageLoader() {
   }

   public static ImageIcon getImageIcon(String var0, String var1) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "You must provide the name of the image when calling ImageLoader.getImageIcon(String, String)");
      if (var0 == null) {
         var0 = "";
      } else if (var0.indexOf(46) >= 0) {
         var0 = convertPackageString(var0);
      }

      if (var0.length() > 0 && !var0.endsWith("/")) {
         var0 = var0 + "/";
      }

      URL var2 = ImageLoader.class.getResource(var0 + var1);
      return var2 != null ? new ImageIcon(var2) : null;
   }

   public static ImageIcon getImageIcon(String var0) {
      return getImageIcon(null, var0);
   }

   public static Image getImage(String var0) {
      return getImage(null, var0);
   }

   public static Image getImage(String var0, String var1) {
      ImageIcon var2 = getImageIcon(var0, var1);
      return var2 != null ? var2.getImage() : null;
   }

   public static void loadIcons(Icon[] var0, String var1, String[] var2, CountDownLatch var3, boolean var4) {
      Preconditions.checkArgument(var0.length == var2.length, "iconsToLoad and iconFileNames must have the same dimension.");
      if (var3 == null || var3.getCount() > 0L) {
         ImageLoader.IconLoader var5 = new ImageLoader.IconLoader(var0, var1, var2, var3);
         if (var4) {
            new Thread(var5).start();
         } else {
            var5.run();
         }
      }
   }

   private static String convertPackageString(String var0) {
      String var1 = var0.replaceAll("\\.", "/");
      return "/" + var1;
   }

   private static final class IconLoader implements Runnable {
      private final Icon[] iconsToLoad;
      private final String packageName;
      private final String[] iconFileNames;
      private final CountDownLatch loadingLatch;

      public IconLoader(Icon[] var1, String var2, String[] var3, CountDownLatch var4) {
         this.iconsToLoad = var1;
         this.packageName = var2;
         this.iconFileNames = var3;
         this.loadingLatch = var4;
      }

      @Override
      public void run() {
         for (int var1 = 0; var1 < this.iconsToLoad.length; var1++) {
            this.iconsToLoad[var1] = ImageLoader.getImageIcon(this.packageName, this.iconFileNames[var1]);
         }

         if (this.loadingLatch != null) {
            this.loadingLatch.countDown();
         }
      }
   }
}
