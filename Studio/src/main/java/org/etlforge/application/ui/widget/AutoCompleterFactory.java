package org.etlforge.application.ui.widget;

import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.CodeEditorAutoCompleter;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.DefaultTokenChooser;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.widget.list.ListFactory;

public abstract class AutoCompleterFactory {
   private ListFactory listFactory = ComponentFactories.listFactory();
   private boolean grabFocus = true;
   private int maxCharsWidth = -1;

   public static AutoCompleterFactory codeEditorFactory() {
      return new AutoCompleterFactory.CodeEditorFactory();
   }

   public static AutoCompleterFactory dropDownFactory() {
      return new AutoCompleterFactory.DropDownCompleterFactory();
   }

   public static AutoCompleterFactory defaultFactory() {
      return AutoCompletionPreference.isAutomatic() ? dropDownFactory() : codeEditorFactory();
   }

   public abstract AutoCompleter createAutoCompleter(CompletionContext var1);

   public final void setListFactory(ListFactory var1) {
      this.listFactory = var1;
   }

   protected final ListFactory getListFactory() {
      return this.listFactory;
   }

   public final void setGrabFocus(boolean var1) {
      this.grabFocus = var1;
   }

   protected final boolean getGrabFocus() {
      return this.grabFocus;
   }

   public final void setMaxCharsWidth(int var1) {
      this.maxCharsWidth = var1;
   }

   protected final int getMaxCharsWidth() {
      return this.maxCharsWidth;
   }

   private static class CodeEditorFactory extends AutoCompleterFactory {
      private CodeEditorFactory() {
      }

      @Override
      public AutoCompleter createAutoCompleter(CompletionContext var1) {
         CodeEditorAutoCompleter var2 = new CodeEditorAutoCompleter();
         var2.setGrabFocusAfterCompletion(this.getGrabFocus());
         DefaultTokenChooser var3 = new DefaultTokenChooser();
         var3.setListFactory(this.getListFactory());
         var3.setMaxWidth(this.getMaxCharsWidth());
         var2.setTokenChooser(var3);
         var2.setCompletionContext(var1);
         return var2;
      }
   }

   private static class DropDownCompleterFactory extends AutoCompleterFactory {
      private DropDownCompleterFactory() {
      }

      @Override
      public AutoCompleter createAutoCompleter(CompletionContext var1) {
         DropDownAutoCompleter var2 = new DropDownAutoCompleter(var1);
         var2.setGrabFocusAfterCompletion(this.getGrabFocus());
         var2.setListFactory(this.getListFactory());
         var2.setMaxWidth(this.getMaxCharsWidth());
         return var2;
      }
   }
}
