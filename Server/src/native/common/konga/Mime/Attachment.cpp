/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
                                                          
  
                                                                      

#include "Attachment.h"

#include <konga/Utils/PortDefs.h>	             


using namespace konga;


                                                                      
                          
                                                                      


std::string Attachment::EncodedData()
{
	if( !m_contentType.length() )
		ContentType();

	if( m_contentType.length() && strncasecmp(m_contentType.c_str(),"text/",5)==0 )
	{
		m_encoding = "quoted-printable";
		return Mime::EncodeQuotedPrintable( m_filename );
	}
	else
	{
		m_encoding = "base64";
		return Mime::EncodeBase64( m_filename );
	}
}

std::string Attachment::ContentTransferEncoding() const
{
	if( m_encoding.length() ) return m_encoding; 

	if( !m_contentType.length() )
		ContentType();

	if( m_contentType.length() && strncasecmp(m_contentType.c_str(),"text/",5)==0 )
		m_encoding = "quoted-printable";
	else
		m_encoding = "base64";
	return m_encoding; 
}

std::string AttachmentManager::GetMimeHeaders() const
{
	if( !m_attachments.size() ) return "";
	return "MIME-Version: 1.0\r\nContent-Type: multipart/mixed;\r\n\tboundary=\"----=_NextPart_0\"\r\n";
}

std::string AttachmentManager::BoundaryString() const
{
	if( !m_attachments.size() ) return "";
	return "----=_NextPart_0";
}

std::string AttachmentManager::GetBody()
{
	if( !m_attachments.size() ) return "";

	                                                                            

	std::string res;
	unsigned int i=0;
                 

	try
	{
		for(std::vector< Attachment >::iterator it=m_attachments.begin(); it!=m_attachments.end(); ++it, ++i)
		{
			                                            
			              
			res += "------=_NextPart_0\r\n";
			res += "Content-Type: ";
			res += it->ContentType();
			res += "\r\n";
			res += "Content-Transfer-Encoding: ";
			res += it->ContentTransferEncoding();
			res += "\r\nContent-Disposition: attachment;\r\n\tfilename=";
			std::string fn = it->FileName();
			if( fn.length() )
			{
				int n = fn.find_last_of('/');
				int m = fn.find_last_of('\\');
				if( (n=n>m?n:m) != 0 )
					fn.erase(0,n+1);
				res += fn;
			}
			else
			{
				char tmp[24];
				sprintf(tmp,"Att%4.4d",i);
				res += tmp;
			}
			res += "\r\n\r\n";
			res += it->EncodedData();
			res += "\r\n\r\n";
			if( i+1 == m_attachments.size() )
			{
				res += "------=_NextPart_0--\r\n";
				             
				                
			}
		}
	}
	catch(const Mime::EncodingException& e)
	{
		return e.Reason();
	}
	
	return res;
}

