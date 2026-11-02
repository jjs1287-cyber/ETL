package org.etlforge.integration.client.ui.interchange.component.page.entity;

import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.TextAreaInput;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.label.KongaLabel;

public final class DescriptionEditor {
   private static final TextStyle CAPTION_STYLE = TextStyles.DefaultText;
   private final IntegrationEntityPage page;
   private final TextAreaInput inputWidget;
   private String description;

   public DescriptionEditor(IntegrationEntityPage var1) {
      this.page = var1;
      this.description = var1.getObject().getDescription();
      this.inputWidget = this.createInputWidget();
   }

   public void show() {
      this.inputWidget.show(this.description);
   }

   public void save() {
      this.page.getObject().setDescription(this.description);
   }

   private TextAreaInput createInputWidget() {
      TextAreaInput var1 = new TextAreaInput("Description", new Receiver<String>() {
         public void handle(String var1) {
            if (var1 != null) {
               DescriptionEditor.this.handleNewDescription(DescriptionEditor.this.description, var1);
            }

            DescriptionEditor.this.page.restoreFocus(50);
         }
      });
      var1.setCaption(this.createCaption());
      var1.setOverlayContainer(this.page.getOverlayContainer());
      var1.setSize(8, 50);
      return var1;
   }

   private JComponent createCaption() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      KongaLabel var2 = CAPTION_STYLE.makeLabel(Strings.get("EntityPage.DescriptionUi.Caption"));
      KongaLabel var3 = CAPTION_STYLE.makeLabel("");
      RendererUtils.decorateRenderer((javax.swing.JLabel)var3, this.page.getObject(), false);
      var1.add(var2).strut(5).add(var3).glue();
      return var1.container();
   }

   private void handleNewDescription(String var1, String var2) {
      if (!var2.equals(var1)) {
         this.description = var2;
         this.page.setDirty();
         DescriptionEditor.DescriptionEdit var3 = new DescriptionEditor.DescriptionEdit(var2, var1);
         this.page.undoableEditHappened(new UndoableEditEvent(this, var3));
      }
   }

   private class DescriptionEdit extends AbstractUndoableEdit {
      private final String newValue;
      private final String previousValue;

      public DescriptionEdit(String var2, String var3) {
         this.newValue = var2;
         this.previousValue = var3;
      }

      @Override
      public String getPresentationName() {
         return "description";
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         DescriptionEditor.this.description = this.newValue;
         this.postMessage("Description change was redone");
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         DescriptionEditor.this.description = this.previousValue;
         this.postMessage("Description change was undone");
      }

      private void postMessage(String var1) {
         DescriptionEditor.this.page.getWindow().postApplicationMessageEvent(new ApplicationMessageEvent(DescriptionEditor.this.page, var1));
      }
   }
}

