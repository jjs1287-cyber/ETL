package org.etlforge.integration.client.ui.interchange.entity.script.page;

import com.google.common.base.Preconditions;
import java.awt.Color;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.border.LineBorder;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.script.ScriptVariables;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.system.Disposable;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.KongaButton;

public final class ScriptPanel extends IntegrationEntityPanel implements Disposable {
   private final IntegrationProject project;
   private final ScriptBuilderUi builder;
   private CaretPositionMemento caretPositionMemento = CaretPositionMemento.NONE;
   private final JLabel inlineJavaScriptNote = new JLabel("NOTE javascript is slower");
   private final JLabel belowJavaScriptNote = new JLabel("NOTE javascript is slower");
   private KongaButton testButton;

   public ScriptPanel(ApplicationWindow var1, Script var2, ScriptBuilderUi var3) {
      super(var1, var2);
      this.project = ProjectUtils.getProject(var2);
      Preconditions.checkState(this.project != null, "The Script must belong to a project");
      this.builder = var3;
      this.addExpressionAreaInputField();
      this.addTestButton();
      this.doLayout();
      this.getUi().addComponentListener(new ComponentAdapter() {
         @Override public void componentResized(ComponentEvent event) { updateJavaScriptNotePosition(); }
      });
      this.updateJavaScriptNotePosition();
   }

   private void addExpressionAreaInputField() {
      TextInputField2 var1 = this.builder.getExpressionArea().asInputField("Expression");
      this.addInputField(var1, var1, var1);
   }

   private void addTestButton() {
      Action var1 = this.builder.getTestAction();
      KongaButton var2 = new KongaButton(var1).setUseSmallActionIcon(true);
      this.testButton = var2;
      this.addComponentAfterNameField(var2);
      this.addComponentAfterNameField(this.inlineJavaScriptNote);
      ActionKeyBinding.install(var1, this.getUi(), 1);
   }

   private void doLayout() {
      JPanel var1 = BorderLayoutBuilder.fillWith(this.builder);
      var1.setBorder(new LineBorder(Color.DARK_GRAY));
      IntegrationDataPanel.Layout var2 = this.getDefaultLayout();
      var2.setBottomGap(0);
      var2.add(this.belowJavaScriptNote, false);
      var2.add(var1, true);
   }

   private void updateJavaScriptNotePosition() {
      int neededWidth = getNameField().getInputComponent().getPreferredSize().width
         + testButton.getPreferredSize().width + inlineJavaScriptNote.getPreferredSize().width + 120;
      boolean inline = getUi().getWidth() >= neededWidth;
      if (inlineJavaScriptNote.isVisible() != inline || belowJavaScriptNote.isVisible() == inline) {
         inlineJavaScriptNote.setVisible(inline);
         belowJavaScriptNote.setVisible(!inline);
         getUi().revalidate();
         getUi().repaint();
      }
   }

   @Override
   public void dispose() {
      this.removeUncommittedDataElements();
      this.builder.dispose();
      super.dispose();
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      this.getDisplayedObject().setScript(this.builder.getTranslatedExpression());
   }

   public Script getDisplayedObject() {
      return (Script)super.getDisplayedObject();
   }

   public void setSettings(EntitySettings var1) {
      this.caretPositionMemento = new CaretPositionMemento(this.getDisplayedObject(), var1);
   }

   @Override
   public void setInitialFocus() {
      this.builder.requestFocus();
      int var1 = this.caretPositionMemento.restore();
      if (var1 >= 0) {
         this.builder.setCaretPosition(var1);
      } else {
         this.builder.setDefaultCaretPosition();
      }
   }

   public void storeCurrentCaretPosition() {
      this.caretPositionMemento.store(this.builder.getCaretPosition());
   }

   private void removeUncommittedDataElements() {
      Script var1 = this.getDisplayedObject();
      if (!var1.isDeleted()) {
         GlobalDataElements var2 = this.project.getGlobalDataElements();
         ScriptVariables var3 = new ScriptVariables(var2);
         var3.scanScript(var1);
      }
   }
}
