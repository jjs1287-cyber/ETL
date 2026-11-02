package org.etlforge.integration.client.ui.structure.editor.ldap;

import java.awt.Color;
import java.awt.Font;
import org.etlforge.integration.client.ldap.ui.ClassDefinitionToolTip;
import org.etlforge.integration.ldap.structure.LdapUtils;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassType;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

final class ClassListItem implements ListDecorator {
   private static final Color NORMAL = Color.BLACK;
   private static final Color AUXILIARY = Color.RED;
   private final ObjectClassDefinition objectClass;
   private final String display;
   private Font auxFont;
   private final String tooltip;

   public ClassListItem(ObjectClassDefinition var1) {
      this.objectClass = var1;
      this.display = createDisplayString(var1);
      this.tooltip = this.createToolTip();
   }

   private String createToolTip() {
      String var1 = this.isAuxiliary() ? PackageResources.Wizard.ClassSelector.getAuxiliaryToolTip(this.objectClass) : null;
      return new ClassDefinitionToolTip(this.objectClass, var1).get();
   }

   public ObjectClassDefinition getClassDefinition() {
      return this.objectClass;
   }

   public boolean isAuxiliary() {
      return this.objectClass.getType() == ObjectClassType.AUXILIARY;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.display);
      var1.setToolTip(this.tooltip);
      if (this.isAuxiliary()) {
         if (this.auxFont == null) {
            this.auxFont = Fonts.italic(var1.getListFont());
         }

         var1.setFont(this.auxFont);
         if (!var2) {
            var1.setTextColor(AUXILIARY);
         }
      } else {
         var1.setFont(var1.getListFont());
         if (!var2) {
            var1.setTextColor(NORMAL);
         }
      }
   }

   @Override
   public String toString() {
      return this.display;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ClassListItem ? this.objectClass.equals(((ClassListItem)var1).objectClass) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.objectClass.hashCode();
   }

   private static String createDisplayString(ObjectClassDefinition var0) {
      String var1 = LdapUtils.getRepresentation(var0);
      if (var0.getType() == ObjectClassType.AUXILIARY) {
         var1 = var1 + " (auxiliary)";
      }

      return var1;
   }
}
