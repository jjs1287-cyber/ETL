/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#if !defined(AFX_ATTACHMENT_H__0E6B4D99_BE2E_11D3_9A22_000000000000__INCLUDED_)
#define AFX_ATTACHMENT_H__0E6B4D99_BE2E_11D3_9A22_000000000000__INCLUDED_

#include <vector>

#include "Mime.h"

namespace konga {
	class Attachment
	{
	public:
		Attachment(const std::string& stFilename) : m_filename(stFilename) { }
		virtual ~Attachment() {}
	
		std::string EncodedData();
	
		std::string ContentType() const
		{
			if( !m_contentType.length() ) 
				return m_contentType = Mime::ContentTypeFromFileName( m_filename );
			return m_contentType;
		}
	
		std::string ContentTransferEncoding() const;
	
		std::string FileName() const { return m_filename; }
	
	private:
		std::string m_filename;
		mutable std::string m_contentType, m_encoding;
	};
	
	
	class AttachmentManager  
	{
	public:
		AttachmentManager() {}
		virtual ~AttachmentManager() {}
	
		void AddAttachment(const std::string filename) { m_attachments.push_back( filename ); }
		void AddAttachment(const Attachment& attachment) { m_attachments.push_back( attachment ); }
	
		std::string GetMimeHeaders() const ;
		std::string GetBody();
		std::string BoundaryString() const ;
	
		int NumberOfAttachments() { return static_cast<int>(m_attachments.size()); }
	
		void clear() { m_attachments.clear(); }
	private:
		std::vector< Attachment > m_attachments;
	};
}

#endif                                                                               

