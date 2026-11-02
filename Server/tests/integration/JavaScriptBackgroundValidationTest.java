import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import javax.swing.*;
import org.etlforge.integration.data.script.*;
import org.etlforge.integration.data.script.de.*;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.client.ui.script.builder.ExpressionScanner;
public final class JavaScriptBackgroundValidationTest {
 public static void main(String[] args)throws Exception{
  ExpressionSource source=new ExpressionSource(){public boolean originatesIn(IntegrationEntity e){return false;}public boolean matches(EntityFilter filter){return false;}};
  ExpressionScanner scanner=new ExpressionScanner(source,GlobalDataElements.EMPTY,new ExpressionParser(source));
  CountDownLatch complete=new CountDownLatch(1);AtomicReference<Throwable> failure=new AtomicReference<>();AtomicLong elapsed=new AtomicLong();
  scanner.addResultListener(result->{try{if(!SwingUtilities.isEventDispatchThread())throw new AssertionError("Publisher is not on EDT");if(result.getStatus().isInvalid())throw new AssertionError("Current script rejected");complete.countDown();}catch(Throwable problem){failure.set(problem);complete.countDown();}});
  SwingUtilities.invokeAndWait(()->{JTextArea editor=new JTextArea("<javascript>var = ;</javascript>");scanner.install(editor);editor.setText("<javascript>SetScriptResult('new content');</javascript>");long start=System.nanoTime();scanner.doScan();elapsed.set(System.nanoTime()-start);});
  if(elapsed.get()>TimeUnit.MILLISECONDS.toNanos(150))throw new AssertionError("Validation blocked EDT: "+elapsed.get());
  if(!complete.await(15,TimeUnit.SECONDS))throw new AssertionError("No asynchronous validation result");if(failure.get()!=null)throw new AssertionError(failure.get());
  SwingUtilities.invokeAndWait(()->scanner.uninstall());
  System.out.println("PASS editor validation schedules without blocking EDT");System.out.println("PASS current valid document replaces stale invalid content");System.out.println("PASS validation result publishes on EDT");
 }
}
