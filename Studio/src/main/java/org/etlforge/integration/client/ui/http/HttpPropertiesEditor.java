package org.etlforge.integration.client.ui.http;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.http.HttpConnector;
import org.etlforge.integration.http.HttpProperties;
import org.etlforge.integration.http.SslVersion;
import org.etlforge.lang.Objects;
import org.etlforge.ui.Empty;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanelDialog;
import org.etlforge.ui.input.InputPanelOverlay;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayPositioner;

public class HttpPropertiesEditor {
   private final HttpConnector connector;
   private HttpPropertiesEditor.Data data;
   private final List<UndoableEditListener> undoRedoListeners;
   private final List<InputChangeListener> inputChangeListeners;
   private OverlayContainer overlayContainer;
   private OverlayDecorator overlay;

   public HttpPropertiesEditor(HttpConnector var1) {
      this.connector = var1;
      this.getValuesFrom(var1);
      this.undoRedoListeners = new CopyOnWriteArrayList<>();
      this.inputChangeListeners = new CopyOnWriteArrayList<>();
   }

   public final void setOverlayContainer(OverlayContainer var1) {
      this.overlayContainer = var1;
   }

   public final void getValuesFrom(HttpConnector var1) {
      this.data = new HttpPropertiesEditor.Data(var1.getHttpProperties(), var1.getSslVersion(), var1.getRequestHeaders());
   }

   public final void display() {
      HttpPropertiesPanel var1 = new HttpPropertiesPanel(this.data.props, this.data.sslVersion, this.data.requestHeaders);
      this.presentUi(var1);
   }

   protected final void prepareUi(HttpPropertiesPanel var1) {
      if (this.overlayContainer != null) {
         var1.setBorder(Empty.border(10));
      }
   }

   protected final void presentUi(HttpPropertiesPanel var1) {
      if (this.overlayContainer != null) {
         this.presentInOverlay(var1);
      } else {
         this.presentInDialog(var1);
      }
   }

   private void presentInOverlay(HttpPropertiesPanel var1) {
      this.overlay = this.createOverlay(var1);
      this.overlay.show(this.overlayContainer, this.getOverlayPositioner());
      var1.requestFocus();
   }

   private OverlayDecorator createOverlay(final HttpPropertiesPanel var1) {
      InputPanelOverlay var2 = new InputPanelOverlay(var1, getTitle()) {
         @Override
         protected void onOk() {
            HttpPropertiesEditor.this.updateProperties(var1);
         }

         @Override
         protected void onHidden() {
            HttpPropertiesEditor.this.overlayRemoved();
         }
      };
      var2.setFadeIn(AnimationPreference.get());
      var2.setAlpha(0.8F);
      var2.setBlocking(true);
      var2.useOkAndCancel();
      return var2;
   }

   protected OverlayPositioner getOverlayPositioner() {
      return new OverlayCenterPositioner();
   }

   protected void overlayRemoved() {
   }

   private void presentInDialog(HttpPropertiesPanel var1) {
      InputPanelDialog var2 = new InputPanelDialog(UiUtils.getActiveWindow(), var1, getTitle()) {
         @Override
         protected boolean useScroll() {
            return false;
         }
      };
      var2.pack();
      var2.setOkButtonAsDefault();
      var2.setResizable(false);
      var2.manageLocation();
      var2.setVisible(true);
      if (var2.wasOkPressed()) {
         this.updateProperties(var1);
      }
   }

   private void updateProperties(HttpPropertiesPanel var1) {
      this.updateProperties(new HttpPropertiesEditor.Data(var1.getDefinedProperties(), var1.getSslVersion(), var1.getRequestHeaders()));
   }

   public final void apply() {
      this.connector.setHttpProperties(this.data.props);
      this.connector.setSslVersion(this.data.sslVersion);
      this.connector.setRequestHeaders(this.data.requestHeaders);
   }

   public final void addUndoRedoListener(UndoableEditListener var1) {
      this.undoRedoListeners.add(var1);
   }

   public final void removeUndoRedoListener(UndoableEditListener var1) {
      this.undoRedoListeners.remove(var1);
   }

   public final void addInputChangeListener(InputChangeListener var1) {
      this.inputChangeListeners.add(var1);
   }

   public final void removeInputChangeListener(InputChangeListener var1) {
      this.inputChangeListeners.remove(var1);
   }

   private void updateProperties(HttpPropertiesEditor.Data var1) {
      this.updateProperties(var1, true);
   }

   private void updateProperties(HttpPropertiesEditor.Data var1, boolean var2) {
      HttpPropertiesEditor.Data var3 = this.data;
      this.data = var1;
      if (!this.data.equals(var3)) {
         this.fireInputChanged(var3, var1);
         if (var2) {
            this.fireUndoableEdit(var3, var1);
         }
      }
   }

   private void fireInputChanged(HttpPropertiesEditor.Data var1, HttpPropertiesEditor.Data var2) {
      InputChangeEvent var3 = new InputChangeEvent(this, this, "HttpProperties", var1, var2);

      for (InputChangeListener var5 : this.inputChangeListeners) {
         var5.inputChanged(var3);
      }
   }

   private void fireUndoableEdit(HttpPropertiesEditor.Data var1, HttpPropertiesEditor.Data var2) {
      UndoableEditEvent var3 = new UndoableEditEvent(this, new HttpPropertiesEditor.UndoableEditImpl(var1, var2));

      for (UndoableEditListener var5 : this.undoRedoListeners) {
         var5.undoableEditHappened(var3);
      }
   }

   private static String getTitle() {
      return Strings.get("HttpPropertiesEditor.Title");
   }

   private static final class Data {
      public final HttpProperties props;
      public final SslVersion sslVersion;
      public final String requestHeaders;

      public Data(HttpProperties var1, SslVersion var2, String var3) {
         this.props = var1;
         this.sslVersion = var2;
         this.requestHeaders = var3 != null ? var3 : "";
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof HttpPropertiesEditor.Data)) {
            return false;
         }

         HttpPropertiesEditor.Data var2 = (HttpPropertiesEditor.Data)var1;
         return this.props.equals(var2.props) && this.sslVersion == var2.sslVersion && this.requestHeaders.equals(var2.requestHeaders);
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.props, this.sslVersion, this.requestHeaders);
      }
   }

   private final class UndoableEditImpl extends AbstractUndoableEdit {
      private final HttpPropertiesEditor.Data oldProps;
      private final HttpPropertiesEditor.Data newProps;

      public UndoableEditImpl(HttpPropertiesEditor.Data var2, HttpPropertiesEditor.Data var3) {
         this.oldProps = var2;
         this.newProps = var3;
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.setProperties(this.newProps);
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.setProperties(this.oldProps);
      }

      @Override
      public String getPresentationName() {
         return Strings.get("HttpPropertiesEditor.Edit");
      }

      private void setProperties(HttpPropertiesEditor.Data var1) {
         HttpPropertiesEditor.this.updateProperties(var1, false);
      }
   }
}
