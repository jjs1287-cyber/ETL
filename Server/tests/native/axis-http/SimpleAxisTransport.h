#ifdef ENABLE_AXISTRACE
/* TRACE ADDED BY THE TRACE INSTRUMENTOR TOOL */
#include "../../common/AxisTrace.h"
#endif
/*
 *   Copyright 2003-2004 The Apache Software Foundation.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 * @author Samisa Abeysinghe (sabeysinghe@virtusa.com)
 */

#if !defined(SIMPLE_AXIS_TRANSPORT_H__OF_AXIS_INCLUDED_)

#define SIMPLE_AXIS_TRANSPORT_H__OF_AXIS_INCLUDED_

#ifdef WIN32

#include <winsock2.h>

#else

#include    <sys/socket.h>

#include <arpa/inet.h>

#include <stdlib.h>

#include <unistd.h>

#include <sys/time.h>

#include    <sys/types.h>

#endif

#include    <string.h>

#include "C:/ETL/Dependencies/axis-cpp-1.6b/axis-c-1.6b-Win32-trace-src/src/transport/SOAPTransport.h"

#include    <string>

AXIS_CPP_NAMESPACE_USE class SimpleAxisTransport:public SOAPTransport
{
  public:
    SimpleAxisTransport (SOCKET iSocket);
    virtual ~ SimpleAxisTransport ();
    int openConnection ()
    {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "openConnection", NULL, 0);	  /* AUTOINSERTED TRACE */
	#endif

		{
		#ifdef ENABLE_AXISTRACE
			int traceRet = (AXIS_SUCCESS);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "openConnection", NULL, 0,
					TRACETYPE_INT, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return AXIS_SUCCESS;
		#endif
	}

    };
    void closeConnection ()
    {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "closeConnection", NULL, 0);	  /* AUTOINSERTED TRACE */
	#endif

    	{
		#ifdef ENABLE_AXISTRACE
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "closeConnection", NULL, 0);	  /* AUTOINSERTED TRACE */
		#endif
		return;
	}
};
    AXIS_TRANSPORT_STATUS sendBytes (const char *pcSendBuffer,
				     const void *pBufferId);
    void registerReleaseBufferCallback
	(AXIS_ENGINE_CALLBACK_RELEASE_SEND_BUFFER pFunct)
    {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "registerReleaseBufferCallback", NULL, 1,
					TRACETYPE_DATA, sizeof(AXIS_ENGINE_CALLBACK_RELEASE_SEND_BUFFER), ((void*)&pFunct));	  /* AUTOINSERTED TRACE */
	#endif

	m_pReleaseBufferCallback = pFunct;
    	{
		#ifdef ENABLE_AXISTRACE
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "registerReleaseBufferCallback", NULL, 0);	  /* AUTOINSERTED TRACE */
		#endif
		return;
	}
};
    AXIS_TRANSPORT_STATUS getBytes (char *pBuffer, int *piSize);
    void releaseBuffer (const char *pBuffer);
    int setTransportProperty (AXIS_TRANSPORT_INFORMATION_TYPE type,
			       const char *value);
    const char *getTransportProperty (AXIS_TRANSPORT_INFORMATION_TYPE eType);
    int setTransportProperty (const char *pcKey, const char *pcValue);
    const char *getTransportProperty (const char *pcKey, bool response=true);
    void setAttachment (const char *pcAttachmentid, const char *pcAttachment)
    {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "setAttachment", NULL, 2,
					TRACETYPE_STRING, 0, ((void*)&pcAttachmentid),
					TRACETYPE_STRING, 0, ((void*)&pcAttachment));	  /* AUTOINSERTED TRACE */
	#endif

    	{
		#ifdef ENABLE_AXISTRACE
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "setAttachment", NULL, 0);	  /* AUTOINSERTED TRACE */
		#endif
		return;
	}
};
    ISoapAttachment* getAttachment (const char *pcAttachmentid)
    {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getAttachment", NULL, 1,
					TRACETYPE_STRING, 0, ((void*)&pcAttachmentid));	  /* AUTOINSERTED TRACE */
	#endif

		{
		#ifdef ENABLE_AXISTRACE
			ISoapAttachment* traceRet = (NULL);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getAttachment", NULL, 0,
					TRACETYPE_POINTER, sizeof(ISoapAttachment), ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return NULL;
		#endif
	}

    };
    ISoapAttachment** getAllAttachments(int *pAttchArraySize)
    {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getAllAttachments", NULL, 1,
					TRACETYPE_POINTER, sizeof(int), ((void*)&pAttchArraySize));	  /* AUTOINSERTED TRACE */
	#endif

		{
		#ifdef ENABLE_AXISTRACE
			ISoapAttachment** traceRet = (NULL);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getAllAttachments", NULL, 0,
					TRACETYPE_POINTER, sizeof(ISoapAttachment*), ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return NULL;
		#endif
	}

    };
    char* getIncomingSOAPMimeHeaders() {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getIncomingSOAPMimeHeaders", NULL, 0);	  /* AUTOINSERTED TRACE */
	#endif

		{
		#ifdef ENABLE_AXISTRACE
			char* traceRet = ("incoming headers");
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getIncomingSOAPMimeHeaders", NULL, 0,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return "incoming headers";
		#endif
	}

    };
    void setEndpointUri (const char *pcEndpointUri)
    {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "setEndpointUri", NULL, 1,
					TRACETYPE_STRING, 0, ((void*)&pcEndpointUri));	  /* AUTOINSERTED TRACE */
	#endif

      m_pcEndpointUri = new char[strlen(pcEndpointUri)+1]; 
      strcpy(m_pcEndpointUri,pcEndpointUri);
    	{
		#ifdef ENABLE_AXISTRACE
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "setEndpointUri", NULL, 0);	  /* AUTOINSERTED TRACE */
		#endif
		return;
	}
};
    void setSessionId (const char *pcSessionId);
    const char *getSessionId ();
    const char *getServiceName ();
    AXIS_PROTOCOL_TYPE getProtocol ();
    int setProtocol(AXIS_PROTOCOL_TYPE eProtocol);
    int getSubProtocol ();
    AXIS_TRANSPORT_STATUS flushOutput ();
  /**
    * Set transport timeout.
    * TODO: need to implement
    *
    * @param lSeconds Timeout in seconds
    */
    void setTimeout (long lSeconds)
    {
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "setTimeout", NULL, 1,
					TRACETYPE_LONG, 0, ((void*)&lSeconds));	  /* AUTOINSERTED TRACE */
	#endif

    	{
		#ifdef ENABLE_AXISTRACE
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "setTimeout", NULL, 0);	  /* AUTOINSERTED TRACE */
		#endif
		return;
	}
};
  private:
    SOCKET m_iSocket;
    BufferInfo *m_pBuffers;
    std::string m_strHTTPHeaders;
    bool m_bReadPastHeaders;
    std::string m_strPayloadToSend;
    std::string m_strServiceURI;
    std::string m_strReceived;
    std::string m_strSOAPAction;
    std::string m_strTransportPropertyURI;
    std::string m_strTransportPropertyOperation;
    void readFromClient ();

};

#endif //SIMPLE_AXIS_TRANSPORT_H__OF_AXIS_INCLUDED_

