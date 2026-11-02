import java.util.*;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/** Checks the packaged Spring registrations without starting or closing the GUI. */
public final class StudioRemovedConnectorsSmoke {
 public static void main(String[] args) throws Exception {
  DefaultListableBeanFactory factory=new DefaultListableBeanFactory();
  XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(factory);
  reader.loadBeanDefinitions(new ClassPathResource("ApplicationContext.Core.Common.xml"));
  reader.loadBeanDefinitions(new ClassPathResource("ApplicationContext.Interchange.Common.xml"));
  int count=factory.getBeanDefinitionCount();
  reader.loadBeanDefinitions(new ClassPathResource("ApplicationContext.Connectors.xml"));
  if(factory.getBeanDefinitionCount()!=count)throw new AssertionError("Connector registrations remain in the packaged context");
  for(String name:new String[]{"EntityTypes.Edition","OperationTypes.Edition","ic.EntityTypeCategories.Edition"}){
   if(!((Collection<?>)factory.getBean(name)).isEmpty())throw new AssertionError("Removed connectors remain registered in "+name);
  }
  for(String name:new String[]{
    "org.etlforge.integration.client.ui.connector.ConnectorTopLevelCategory",
    "com.etlforge.integration.server.implementation.webservice.connector.ConnectorCallInfoProviderWsImpl"}){
   try{Class.forName(name);throw new AssertionError("Removed connector implementation remains packaged: "+name);}
   catch(ClassNotFoundException expected){}
  }
  System.out.println("PASS: packaged Studio has no vendor connector entities, operations, sidebar categories or exclusive UI implementations");
 }
}
