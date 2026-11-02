from pathlib import Path
p=Path('C:/ETL/Server/tests/integration/EndpointEditorOpeningTest.java');s=p.read_text(encoding='utf-8').replace('static DataLocationPanel create(int type,SourceTarget side){','static DataLocationPanel create(int type,SourceTarget side)throws Exception{').replace('case 5:return new LocalFileLocationPanel(new LocalFileLocation(),side);','''case 5:
   LocalFileLocationPanel local=new LocalFileLocationPanel(new LocalFileLocation(),side);
   java.lang.reflect.Field connection=LocalFileLocationPanel.class.getDeclaredField("serverConnectionService");connection.setAccessible(true);
   connection.set(local,java.lang.reflect.Proxy.newProxyInstance(EndpointEditorOpeningTest.class.getClassLoader(),new Class[]{org.etlforge.integration.client.server.ServerConnectionService.class},(proxy,method,args)->method.getName().equals("getConnectionStatus")?org.etlforge.integration.client.server.ConnectionStatus.DISCONNECTED:null));return local;''');p.write_text(s,encoding='utf-8')
