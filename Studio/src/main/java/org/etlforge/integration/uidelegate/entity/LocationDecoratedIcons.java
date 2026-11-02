package org.etlforge.integration.uidelegate.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.Icon;
import org.etlforge.collections.Pair;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.ui.util.ImageLoader;

public final class LocationDecoratedIcons {
   private static final LocationDecoratedIcons sourceIcons = new LocationDecoratedIcons(SourceTarget.Source);
   private static final LocationDecoratedIcons targetIcons = new LocationDecoratedIcons(SourceTarget.Target);
   private final Map<LocationDecoratedIcons.Key, Icon> icons = Maps.newHashMap();

   public static LocationDecoratedIcons forLocatable(Locatable var0) {
      return var0 instanceof Source ? sourceIcons : targetIcons;
   }

   public static LocationDecoratedIcons forSource() {
      return sourceIcons;
   }

   public static LocationDecoratedIcons forTarget() {
      return targetIcons;
   }

   private LocationDecoratedIcons(SourceTarget var1) {
      this.loadIcons(var1);
   }

   public Icon getIcon(Locatable var1, EntityIcons.Size var2) {
      DataLocation var3 = var1.getLocation();
      if (var3 != null) {
         Icon var4 = this.getIcon(var3, var2);
         if (var4 != null) {
            return var4;
         }
      }

      return EntityIcons.forType(var1.getID().getEntityType()).getDefaultIcon(var2);
   }

   public Icon getIcon(DataLocation var1, EntityIcons.Size var2) {
      return this.getIcon(var1.getDataLocationType(), var2);
   }

   public Icon getIcon(DataLocationType var1, EntityIcons.Size var2) {
      return this.icons.get(new LocationDecoratedIcons.Key(var1, var2));
   }

   private void loadIcons(SourceTarget var1) {
      this.load(var1, DataLocationType.FTP, "Ftp");
      this.load(var1, DataLocationType.FileShare, "FileShare");
      this.load(var1, DataLocationType.Database, "Database");
      this.load(var1, DataLocationType.HTTP, "Http");
      this.load(var1, DataLocationType.LDAP, "Ldap");
      this.load(var1, DataLocationType.TemporaryStorage, "TemporaryStorage");
   }

   private void load(SourceTarget var1, DataLocationType var2, String var3) {
      this.icons.put(new LocationDecoratedIcons.Key(var2, EntityIcons.Size.SMALL), this.loadIcon(var1.name() + "." + var3 + ".16"));
      this.icons.put(new LocationDecoratedIcons.Key(var2, EntityIcons.Size.LARGE), this.loadIcon(var1.name() + "." + var3 + ".32"));
   }

   private Icon loadIcon(String var1) {
      return ImageLoader.getImageIcon("org.etlforge.integration.uidelegate.entity.icons", var1 + ".png");
   }

   private static final class Key {
      private final Pair<DataLocationType, EntityIcons.Size> data;

      public Key(DataLocationType var1, EntityIcons.Size var2) {
         this.data = Pair.of(var1, var2);
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof LocationDecoratedIcons.Key && this.data.equals(((LocationDecoratedIcons.Key)var1).data);
      }

      @Override
      public int hashCode() {
         return this.data.hashCode();
      }
   }
}
