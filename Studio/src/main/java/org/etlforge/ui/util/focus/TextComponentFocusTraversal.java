package org.etlforge.ui.util.focus;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.Container;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.FocusTraversalPolicy;
import javax.swing.text.JTextComponent;

public final class TextComponentFocusTraversal extends FocusTraversalPolicy {
   private final FocusTraversalPolicy impl;

   public TextComponentFocusTraversal(FocusTraversalPolicy var1) {
      Preconditions.checkNotNull(var1, "impl");
      this.impl = var1;
   }

   public TextComponentFocusTraversal() {
      this(new ContainerOrderFocusTraversalPolicy());
   }

   @Override
   public Component getComponentAfter(Container var1, Component var2) {
      Component var3 = this.impl.getComponentAfter(var1, var2);
      return this.moveToNextTextComponent(var1, var3);
   }

   private Component moveToNextTextComponent(Container var1, Component var2) {
      while (var2 != null && !(var2 instanceof JTextComponent)) {
         var2 = this.impl.getComponentAfter(var1, var2);
      }

      return var2;
   }

   @Override
   public Component getComponentBefore(Container var1, Component var2) {
      Component var3 = this.impl.getComponentBefore(var1, var2);
      return this.moveToPreviousTextComponent(var1, var3);
   }

   private Component moveToPreviousTextComponent(Container var1, Component var2) {
      while (var2 != null && !(var2 instanceof JTextComponent)) {
         var2 = this.impl.getComponentBefore(var1, var2);
      }

      return var2;
   }

   @Override
   public Component getFirstComponent(Container var1) {
      Component var2 = this.impl.getFirstComponent(var1);
      return this.moveToNextTextComponent(var1, var2);
   }

   @Override
   public Component getLastComponent(Container var1) {
      Component var2 = this.impl.getLastComponent(var1);
      return this.moveToPreviousTextComponent(var1, var2);
   }

   @Override
   public Component getDefaultComponent(Container var1) {
      return this.impl.getDefaultComponent(var1);
   }
}
