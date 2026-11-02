/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.runtime;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.etlforge.integration.server.authentication.AuthenticationHandler;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.integration.server.jms.JmsServerException;
import org.etlforge.integration.server.jms.messaging.ConnectionTester;
import org.etlforge.integration.server.jms.messaging.JmsOperationQueuer;
import org.etlforge.integration.server.jms.messaging.LocationProperties;
import org.etlforge.integration.server.jms.messaging.MessageRouter;
import org.etlforge.util.kongaprotocol.KongaCommandSocketListener;
import org.etlforge.util.kongaprotocol.KongaCommandResult;
import org.etlforge.util.kongaprotocol.KongaModuleStatusBitFields;
import org.etlforge.util.net.Command;
import org.etlforge.util.net.CommandHandler;
import org.etlforge.util.net.CommandResult;

   
                                                                          
   
                          
                          
   
               
   
public class JmsRuntime {

    private static final String JMS_ENGINE_COMPONENT_NAME = "JmsEngine";

    private static final int DEFAULT_COMMAND_LISTENER_PORT = 46910;

    private int commandListenerPort = DEFAULT_COMMAND_LISTENER_PORT;

    private KongaCommandSocketListener commandListener = null;

    private MessageRouter messageRouter = null;

    private final CountDownLatch countdownLatch = new CountDownLatch(1);

    public void init(String[] args) throws ServerConfigurationException {
        if (commandListener != null)
            throw new IllegalStateException("The JMS runtime can only be initialized once.");

        ServerConfig.loadOptions();

        commandListenerPort = ServerConfig.getOption(JMS_ENGINE_COMPONENT_NAME, "CommandPort",
                        DEFAULT_COMMAND_LISTENER_PORT);

        try {
        commandListener = new KongaCommandSocketListener(new java.net.ServerSocket(commandListenerPort, 50, java.net.InetAddress.getByName("127.0.0.1")), JMS_ENGINE_COMPONENT_NAME);

        } catch (IOException e) { throw new ServerConfigurationException("Cannot bind loopback JMS listener", e); }
        commandListener.registerCommandAuthenticator(new AuthenticationHandler());
        commandListener.registerCommandHandler("GetStatus", new GetStatusHandler());
        commandListener.registerCommandHandler("Start", new StartHandler());
        commandListener.registerCommandHandler("Stop", new StopHandler());
        commandListener.registerCommandHandler("Shutdown", new ShutdownHandler());
        commandListener.registerCommandHandler("Update", new UpdateHandler());
        commandListener.registerCommandHandler("CheckOutgoing", new CheckOutgoingHandler());
        commandListener.registerCommandHandler("TestConnection", new TestConnectionHandler());

        try {
            commandListener.startListening();
        } catch (IOException e) {
            throw new ServerConfigurationException("Failed to start command listener. " + e.getMessage(), e);
        }

                                 
        messageRouter = new MessageRouter(new JmsOperationQueuer(false));
        try {
            if (ServerConfig.getOption(JMS_ENGINE_COMPONENT_NAME, "AutoStart", false)) messageRouter.start();
        } catch (JmsServerException e) {
            e.printStackTrace();
        }
    }

    public void waitUntilShutdown() {
        try {
            countdownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    class GetStatusHandler implements CommandHandler {

        @Override
        public CommandResult handleCommand(Command command) {
            KongaCommandResult result = new KongaCommandResult(command.getCommandName());

            int nArgs = command.getArguments().size();
            if (nArgs != 1) {
                result.addReturnValue(Integer.toString(KongaModuleStatusBitFields.rsERROR));
                result.addReturnValue("Internal Error: Too many arguments for the GetStatus command.");
                return result;
            }

            String cmd = command.getArguments().iterator().next();
            int responseLevel = 0;
            try {
                responseLevel = Integer.parseInt(cmd);
                if (responseLevel < -1 || responseLevel > 1)
                    responseLevel = 0;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                result.addReturnValue(Integer.toString(KongaModuleStatusBitFields.rsERROR));
                result.addReturnValue("Internal Error: Invalid status level " + cmd);
                return result;
            }

            long status = KongaModuleStatusBitFields.rsSTOPPED;
            if (messageRouter != null) {
                status = messageRouter.getStatus();
            }

            String msg = "The JMS Engine is ";
            if ((status & KongaModuleStatusBitFields.rsSTARTING) != 0)
                msg += "starting";
            else if ((status & KongaModuleStatusBitFields.rsSTOPPING) != 0)
                msg += "stopping";
            else if ((status & KongaModuleStatusBitFields.rsRUNNING) != 0)
                msg += "running";
            else if ((status & KongaModuleStatusBitFields.rsSTOPPED) != 0)
                msg += "stopped";
            else if ((status & KongaModuleStatusBitFields.rsUNREACHABLE) != 0)
                msg += "unreachable";
            msg += ".";

            if (responseLevel == 1) {
                                                           
            }
            if (responseLevel == -1) {
                                                    
                                               
                                                      
                                      
            }

            result.addReturnValue(Long.toString(status));
            result.addReturnValue(msg);
            return result;
        }
    }


    private class StartHandler implements CommandHandler {

        @Override
        public CommandResult handleCommand(Command command) {
            KongaCommandResult result = new KongaCommandResult(command.getCommandName());

            if (messageRouter == null) {
                messageRouter = new MessageRouter(new JmsOperationQueuer(false));
            }
            try {
                messageRouter.start();
            } catch (JmsServerException e) {
                                                               
                e.printStackTrace();
            }
            return result;
        }
    }


    private class StopHandler implements CommandHandler {

        @Override
        public CommandResult handleCommand(Command command) {
            KongaCommandResult result = new KongaCommandResult(command.getCommandName());
            if (messageRouter != null) {
                messageRouter.stop();
            }
            return result;
        }
    }


    private class ShutdownHandler implements CommandHandler {

        @Override
        public CommandResult handleCommand(Command command) {
            KongaCommandResult result = new KongaCommandResult(command.getCommandName());
            try {
                if (commandListener != null) {
                    commandListener.stopListening();
                }
            } catch (IOException e) {
                                 
                e.printStackTrace();
            }
            if (messageRouter != null) {
                messageRouter.stop();
            }
            countdownLatch.countDown();
            return result;
        }
    }


    private class UpdateHandler implements CommandHandler {

        @Override
        public CommandResult handleCommand(Command command) {
            KongaCommandResult result = new KongaCommandResult(command.getCommandName());
            try {
                if (messageRouter != null) {
                    messageRouter.update();
                }
            } catch (JmsServerException e) {
                                                                
                e.printStackTrace();
            }
            return result;
        }
    }


    private class CheckOutgoingHandler implements CommandHandler {

        @Override
        public CommandResult handleCommand(Command command) {
            KongaCommandResult result = new KongaCommandResult(command.getCommandName());
            if (messageRouter != null) {
                messageRouter.checkOutgoing();
            }
            return result;
        }
    }


    private static class TestConnectionHandler implements CommandHandler {

        @Override
        public CommandResult handleCommand(Command command) {
            KongaCommandResult result = new KongaCommandResult(command.getCommandName());
            try {
                LocationProperties connectivityProps = new LocationProperties();
                boolean isKey = true;
                String key = "";
                for (String s : command.getArguments()) {
                    if (isKey) {
                        key = s;
                    } else {
                        connectivityProps.addProperty(key, s);
                    }
                    isKey = !isKey;
                }
                ConnectionTester.testConnection(connectivityProps);
                result.addReturnValue("true");
            } catch (Throwable e) {
                result.setException(e);
                e.printStackTrace();
            }
            return result;
        }
    }
    
}
