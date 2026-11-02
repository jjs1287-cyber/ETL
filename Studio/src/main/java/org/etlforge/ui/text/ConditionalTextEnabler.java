package org.etlforge.ui.text;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.util.Map;
import javax.swing.Action;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.Enablabes;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.widget.DocumentContentChangeListener;

public final class ConditionalTextEnabler {
   private final JTextComponent textComponent;
   private final Map<Enablable, ConditionalTextEnabler.Dependency> dependencies;
   private final DocumentListener docListener;

   private ConditionalTextEnabler(JTextComponent var1) {
      this.textComponent = var1;
      this.docListener = new ConditionalTextEnabler.ChangeHandler();
      this.dependencies = Maps.newHashMap();
   }

   public static ConditionalTextEnabler install(JTextComponent var0) {
      ConditionalTextEnabler var1 = new ConditionalTextEnabler(var0);
      var0.getDocument().addDocumentListener(var1.docListener);
      return var1;
   }

   public void uninstall() {
      this.textComponent.getDocument().removeDocumentListener(this.docListener);
      this.dependencies.clear();
   }

   public void addDependency(Enablable var1, TextEnablerCondition var2) {
      Preconditions.checkNotNull(var1, "e");
      Preconditions.checkNotNull(var2, "c");
      ConditionalTextEnabler.Dependency var3 = new ConditionalTextEnabler.Dependency(var1, var2);
      this.dependencies.put(var1, var3);
      var3.update(new ConditionalTextEnabler.Content(this.textComponent));
   }

   public void addDependency(Action var1, TextEnablerCondition var2) {
      this.addDependency(Enablabes.fromAction(var1), var2);
   }

   public void addDependency(Component var1, TextEnablerCondition var2) {
      this.addDependency(Enablabes.fromComponent(var1), var2);
   }

   public void removeDependency(Enablable var1) {
      this.dependencies.remove(var1);
   }

   public void removeDependency(Action var1) {
      this.removeDependency(Enablabes.fromAction(var1));
   }

   public void removeDependency(Component var1) {
      this.removeDependency(Enablabes.fromComponent(var1));
   }

   private class ChangeHandler extends DocumentContentChangeListener {
      private ChangeHandler() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         if (!ConditionalTextEnabler.this.dependencies.isEmpty()) {
            ConditionalTextEnabler.Content var2 = new ConditionalTextEnabler.Content(ConditionalTextEnabler.this.textComponent);

            for (ConditionalTextEnabler.Dependency var4 : ConditionalTextEnabler.this.dependencies.values()) {
               var4.update(var2);
            }
         }
      }
   }

   private static class Content implements TextContent {
      private final JTextComponent component;
      private String string;

      public Content(JTextComponent var1) {
         this.component = var1;
      }

      @Override
      public int getLength() {
         return this.component.getDocument().getLength();
      }

      @Override
      public String getText() {
         if (this.string == null) {
            this.string = this.component.getText();
         }

         return this.string;
      }
   }

   private static class Dependency {
      private final Enablable enablable;
      private final TextEnablerCondition condition;

      public Dependency(Enablable var1, TextEnablerCondition var2) {
         this.enablable = var1;
         this.condition = var2;
      }

      public void update(TextContent var1) {
         this.enablable.setEnabled(this.condition.isEnabled(var1));
      }
   }
}
