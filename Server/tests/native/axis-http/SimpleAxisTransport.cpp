#include <stdexcept>
#include <sstream>
#include <algorithm>
#include <cctype>
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
 *
 */

#include "SimpleAxisTransport.h"

#define AXIS_URI_EXTENSION "/axis"

/* Following is the character that should be used to separate the method name in
 * the SOAPAction header value. Ex: "http://localhost:80/axis/Calculator#Add"
 */

#define SOAPACTION_METHODNAME_SEPARATOR "#"

SimpleAxisTransport::SimpleAxisTransport( SOCKET iSocket)
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "SimpleAxisTransport", this, 1,
					TRACETYPE_INT, 0, ((void*)&iSocket));	  /* AUTOINSERTED TRACE */
	#endif

    m_eProtocolType = APTHTTP1_1;
    m_iSocket = iSocket;
    m_bReadPastHeaders = false;
    m_strPayloadToSend = "";
    m_strServiceURI = "base";
    m_strReceived = "";
    m_strSOAPAction = "";
	{
		#ifdef ENABLE_AXISTRACE
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "SimpleAxisTransport", this, 0);	  /* AUTOINSERTED TRACE */
		#endif
		return;
	}
}

SimpleAxisTransport::~SimpleAxisTransport()
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "~SimpleAxisTransport", this, 0);	  /* AUTOINSERTED TRACE */
	#endif

#ifdef WIN32
    shutdown( m_iSocket, SD_BOTH);
	closesocket( m_iSocket);
#else //Linux
    shutdown( m_iSocket, SHUT_RDWR);	
    close( m_iSocket);
#endif
	{
		#ifdef ENABLE_AXISTRACE
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "~SimpleAxisTransport", this, 0);	  /* AUTOINSERTED TRACE */
		#endif
		return;
	}
}

AXIS_TRANSPORT_STATUS SimpleAxisTransport::sendBytes( const char *pcSendBuffer, const void *pBufferId)
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "sendBytes", this, 2,
					TRACETYPE_STRING, 0, ((void*)&pcSendBuffer),
					TRACETYPE_POINTER, 1, ((void*)&pBufferId));	  /* AUTOINSERTED TRACE */
	#endif

    m_strPayloadToSend += pcSendBuffer;
    	{
		#ifdef ENABLE_AXISTRACE
			AXIS_TRANSPORT_STATUS traceRet = (TRANSPORT_FINISHED);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "sendBytes", this, 0,
					TRACETYPE_DATA, sizeof(AXIS_TRANSPORT_STATUS), ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return TRANSPORT_FINISHED;
		#endif
	}

/*
    if (!pBufferId)		// temporary buffers should always sent immediately 
    {
	if ((send (m_iSocket, pcSendBuffer, (int) strlen( pcSendBuffer), MSG_DONTROUTE)) == -1)
	    return TRANSPORT_FAILED;
	return TRANSPORT_FINISHED;
    }
    for (int index = 0; index < NO_OF_SERIALIZE_BUFFERS; index++)
    {
	if (!m_pBuffers[index].pcBuffer)
	{
	    m_pBuffers[index].pcBuffer = pcSendBuffer;
	    m_pBuffers[index].pBufferId = pBufferId;
	    break;
	}
    }
    return TRANSPORT_IN_PROGRESS;
*/
}

int SimpleAxisTransport::setTransportProperty( AXIS_TRANSPORT_INFORMATION_TYPE type, const char * value)
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "setTransportProperty", this, 2,
					TRACETYPE_DATA, sizeof(AXIS_TRANSPORT_INFORMATION_TYPE), ((void*)&type),
					TRACETYPE_STRING, 0, ((void*)&value));	  /* AUTOINSERTED TRACE */
	#endif

	const char *	key = NULL;
	int				iSuccess = AXIS_FAIL;
	switch( type)
	{
		case SOAPACTION_HEADER:	/* needed only in the client side ? */
			break;
	
		case SERVICE_URI:		/* need to set ? */
			break;
		case OPERATION_NAME:	/* need to set ? */
			break;
		case SOAP_MESSAGE_LENGTH: /* This is apache module and transport is http so the key */
			key = "Content-Length";
			break;
		default:
			;
	}
	if( key)
	{
		iSuccess = setTransportProperty( key, value);
	}
		{
		#ifdef ENABLE_AXISTRACE
			int traceRet = (AXIS_SUCCESS);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "setTransportProperty", this, 0,
					TRACETYPE_INT, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return AXIS_SUCCESS;
		#endif
	}

}

AXIS_TRANSPORT_STATUS SimpleAxisTransport::flushOutput(){
 std::string response="HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Type: text/xml; charset=utf-8\r\nContent-Length: "+std::to_string(m_strPayloadToSend.size())+"\r\n\r\n"+m_strPayloadToSend;
 size_t offset=0;while(offset<response.size()){int sent=send(m_iSocket,response.data()+offset,static_cast<int>(response.size()-offset),0);if(sent<=0)return TRANSPORT_FAILED;offset+=sent;}return TRANSPORT_FINISHED;
}

AXIS_TRANSPORT_STATUS SimpleAxisTransport::getBytes(char* buffer,int* size){
 if(!buffer || !size || *size<=0)return TRANSPORT_FAILED;
 const size_t count=std::min(static_cast<size_t>(*size),m_strReceived.size());
 if(count)memcpy(buffer,m_strReceived.data(),count);
 m_strReceived.erase(0,count);*size=static_cast<int>(count);
 return m_strReceived.empty()?TRANSPORT_FINISHED:TRANSPORT_IN_PROGRESS;
}

void SimpleAxisTransport::readFromClient() {
    std::string request;char buffer[4096];
    auto receive = [&]() { int count=recv(m_iSocket,buffer,sizeof(buffer),0); if(count<=0)throw std::runtime_error("Incomplete HTTP request");request.append(buffer,count);if(request.size()>2*1024*1024)throw std::runtime_error("HTTP request too large"); };
    size_t end;
    while((end=request.find("\r\n\r\n"))==std::string::npos){receive();if(request.size()>65536)throw std::runtime_error("HTTP headers too large");}
    std::istringstream headers(request.substr(0,end));std::string line;std::getline(headers,line);
    std::istringstream first(line);std::string method,version;first>>method>>m_strServiceURI>>version;
    if(method!="POST")throw std::runtime_error("Only SOAP POST supported by test fixture");
    size_t length=0;bool hasLength=false;
    while(std::getline(headers,line)){
      size_t colon=line.find(':');if(colon==std::string::npos)continue;
      std::string key=line.substr(0,colon),value=line.substr(colon+1);
      std::transform(key.begin(),key.end(),key.begin(),[](unsigned char c){return std::tolower(c);});
      value.erase(0,value.find_first_not_of(" \t"));while(!value.empty()&&(value.back()=='\r'||value.back()==' '))value.pop_back();
      if(key=="content-length"){if(hasLength)throw std::runtime_error("Duplicate length");length=std::stoull(value);hasLength=true;}
      if(key=="transfer-encoding")throw std::runtime_error("Chunked requests unsupported by test fixture");
      if(key=="soapaction"){if(value.size()>=2&&value.front()=='"'&&value.back()=='"')value=value.substr(1,value.size()-2);m_strSOAPAction=value;}
    }
    if(!hasLength || length>1024*1024)throw std::runtime_error("Invalid SOAP length");
    const size_t body=end+4;while(request.size()<body+length)receive();
    m_strReceived=request.substr(body,length);
}

const char * SimpleAxisTransport::getTransportProperty( AXIS_TRANSPORT_INFORMATION_TYPE eType)
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getTransportProperty", this, 1,
					TRACETYPE_DATA, sizeof(AXIS_TRANSPORT_INFORMATION_TYPE), ((void*)&eType));	  /* AUTOINSERTED TRACE */
	#endif

	switch( eType)
	{
		case SOAPACTION_HEADER:
		{
				{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (getTransportProperty( "SOAPAction"));
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getTransportProperty", this, 1,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return getTransportProperty( "SOAPAction");
		#endif
	}

		}
		case SERVICE_URI:
		{
			readFromClient();
			if( m_strServiceURI.find( AXIS_URI_EXTENSION) != std::string::npos)
			{
				unsigned int uiStartPos = (unsigned int) (m_strServiceURI.find( AXIS_URI_EXTENSION) +
														  strlen( AXIS_URI_EXTENSION) +
														  1);
				m_strTransportPropertyURI = m_strServiceURI.substr( uiStartPos,
																	m_strServiceURI.length() - uiStartPos);
					{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (m_strTransportPropertyURI.c_str());
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getTransportProperty", this, 2,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return m_strTransportPropertyURI.c_str();
		#endif
	}

			}
				{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (m_strServiceURI.c_str());
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getTransportProperty", this, 3,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return m_strServiceURI.c_str();
		#endif
	}

		}
		case OPERATION_NAME:
		{
			std::string::size_type uiOpStart = m_strSOAPAction.find( SOAPACTION_METHODNAME_SEPARATOR);
			if( uiOpStart != std::string::npos)
			{
				uiOpStart += (unsigned int) strlen( SOAPACTION_METHODNAME_SEPARATOR);
				m_strTransportPropertyOperation = m_strSOAPAction.substr( uiOpStart,
																		  m_strSOAPAction.length() - uiOpStart);
					{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (m_strTransportPropertyOperation.c_str());
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getTransportProperty", this, 4,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return m_strTransportPropertyOperation.c_str();
		#endif
	}

			}
				{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (m_strSOAPAction.c_str());
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getTransportProperty", this, 5,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return m_strSOAPAction.c_str();
		#endif
	}

		}
		case SOAP_MESSAGE_LENGTH:
		{
				{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (getTransportProperty( "Content-Length"));
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getTransportProperty", this, 6,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return getTransportProperty( "Content-Length");
		#endif
	}

		}
		default:
		{
			;
		}
	}
    
		{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (NULL);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getTransportProperty", this, 7,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return NULL;
		#endif
	}

}

int SimpleAxisTransport::setTransportProperty( const char * pcKey, const char * pcValue)
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "setTransportProperty", this, 2,
					TRACETYPE_STRING, 0, ((void*)&pcKey),
					TRACETYPE_STRING, 0, ((void*)&pcValue));	  /* AUTOINSERTED TRACE */
	#endif

    m_strHTTPHeaders += std::string( pcKey) +
						std::string( ": ") +
						std::string( pcValue) +
						std::string( "\r\n");
    	{
		#ifdef ENABLE_AXISTRACE
			int traceRet = (0);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "setTransportProperty", this, 0,
					TRACETYPE_INT, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return 0;
		#endif
	}

}

const char * SimpleAxisTransport::getTransportProperty( const char * pcKey, bool response)
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getTransportProperty", this, 2,
					TRACETYPE_STRING, 0, ((void*)&pcKey),
					TRACETYPE_BOOL, 0, ((void*)&response));	  /* AUTOINSERTED TRACE */
	#endif

    	{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (0);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getTransportProperty", this, 0,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return 0;
		#endif
	}

}

void SimpleAxisTransport::setSessionId( const char * pcSessionId)
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "setSessionId", this, 1,
					TRACETYPE_STRING, 0, ((void*)&pcSessionId));	  /* AUTOINSERTED TRACE */
	#endif


	{
		#ifdef ENABLE_AXISTRACE
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "setSessionId", this, 0);	  /* AUTOINSERTED TRACE */
		#endif
		return;
	}
}

const char * SimpleAxisTransport::getSessionId()
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getSessionId", this, 0);	  /* AUTOINSERTED TRACE */
	#endif

    	{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = ("this is temporary session id");
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getSessionId", this, 0,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return "this is temporary session id";
		#endif
	}
	//TODO
}

const char * SimpleAxisTransport::getServiceName()
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getServiceName", this, 0);	  /* AUTOINSERTED TRACE */
	#endif

    	{
		#ifdef ENABLE_AXISTRACE
			const char* traceRet = (0);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getServiceName", this, 0,
					TRACETYPE_STRING, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return 0;
		#endif
	}
			//TODO
}

AXIS_PROTOCOL_TYPE SimpleAxisTransport::getProtocol()
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getProtocol", this, 0);	  /* AUTOINSERTED TRACE */
	#endif

    	{
		#ifdef ENABLE_AXISTRACE
			AXIS_PROTOCOL_TYPE traceRet = (APTHTTP1_1);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getProtocol", this, 0,
					TRACETYPE_DATA, sizeof(AXIS_PROTOCOL_TYPE), ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return APTHTTP1_1;
		#endif
	}

}

int SimpleAxisTransport::setProtocol(AXIS_PROTOCOL_TYPE eProtocol)
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "setProtocol", this, 1,
					TRACETYPE_DATA, sizeof(AXIS_PROTOCOL_TYPE), ((void*)&eProtocol));	  /* AUTOINSERTED TRACE */
	#endif

    if( eProtocol == APTHTTP1_1 || eProtocol == APTHTTP1_0)
    {
       m_eProtocolType = eProtocol;
       	{
		#ifdef ENABLE_AXISTRACE
			int traceRet = (AXIS_SUCCESS);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "setProtocol", this, 1,
					TRACETYPE_INT, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return AXIS_SUCCESS;
		#endif
	}

    }
		{
		#ifdef ENABLE_AXISTRACE
			int traceRet = (AXIS_FAIL);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "setProtocol", this, 2,
					TRACETYPE_INT, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return AXIS_FAIL;
		#endif
	}

}

int SimpleAxisTransport::getSubProtocol()
{
	#ifdef ENABLE_AXISTRACE
		if (axiscpp::AxisTrace::isTraceOn())
			axiscpp::AxisTrace::traceEntry("SimpleAxisTransport", "getSubProtocol", this, 0);	  /* AUTOINSERTED TRACE */
	#endif

    	{
		#ifdef ENABLE_AXISTRACE
			int traceRet = (AXIS_HTTP_POST);
			if (axiscpp::AxisTrace::isTraceOn())
				axiscpp::AxisTrace::traceExit("SimpleAxisTransport", "getSubProtocol", this, 0,
					TRACETYPE_INT, 0, ((void*)&traceRet));	  /* AUTOINSERTED TRACE */
			return traceRet;
		#else
			return AXIS_HTTP_POST;
		#endif
	}

}

