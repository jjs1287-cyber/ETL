package org.etlforge.ui;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.swing.Icon;
import org.etlforge.ui.util.ImageLoader;
import org.etlforge.util.properties.PropertiesReader;

public final class PropertiesBasedIconProvider implements IconProvider {
   private final String iconPackageName;
   private final Properties idToFileName;
   private final Map<String, Icon> idToIcon;

   public PropertiesBasedIconProvider(String var1, String var2) throws IOException {
      this.idToFileName = PropertiesReader.readResource(var1);
      this.iconPackageName = var2;
      this.idToIcon = Maps.newHashMap();
   }

   @Override
   public Icon getIcon(String var1) {
      synchronized (this) {
         Icon var3 = this.idToIcon.get(var1);
         if (var3 == null) {
            String var4 = this.idToFileName.getProperty(var1);
            if (var4 != null) {
               var3 = ImageLoader.getImageIcon(this.iconPackageName, var4);
               this.idToIcon.put(var1, var3);
            }
         }

         return var3;
      }
   }
}
