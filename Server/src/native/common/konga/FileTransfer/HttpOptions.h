/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_FILE_TRANSFER_HTTP_OPTIONS_H
#define KONGA_FILE_TRANSFER_HTTP_OPTIONS_H

#include <string>
#include <vector>

namespace konga {

	class HttpOptions {
	public:
		HttpOptions(void) : m_bEnableContentEncoding(false),
			m_bEnableChunkedTransferEncoding(false),
			m_bEnableExpect100Continue(false),
			m_bOverrideContentType(false) {}
		virtual ~HttpOptions(void) {}

		bool isContentEncodingEnabled() const { return m_bEnableContentEncoding; }
		bool isChunkedTransferEncodingEnabled() const { return m_bEnableChunkedTransferEncoding; }
		bool isExpect100ContinueHeaderEnabled() const { return m_bEnableExpect100Continue; }
		std::vector<std::string> getCustomRequestHeaders() const { return m_vCustomRequestHeaders; }

		                                                           
		                                                                    
		bool isContentTypeSet(std::string& sContentType) const {
			if ( m_bOverrideContentType ) {
				sContentType = m_sContentType;
			}
			return m_bOverrideContentType;
		}

		bool enableContentEncoding(bool b) { return m_bEnableContentEncoding = b; }
		bool enableChunkedTransferEncoding(bool b) { return m_bEnableChunkedTransferEncoding = b; }
		bool enableExpect100ContinueHeader(bool b) { return m_bEnableExpect100Continue = b; }

		void setContentType(const std::string& sContentType) {
			m_sContentType = sContentType;
			m_bOverrideContentType = true;
		}

		                                                                              
		void setCustomRequestHeaders(const std::string& sCustomRequestHeaders);

	private:
		bool m_bEnableContentEncoding;
		bool m_bEnableChunkedTransferEncoding;
		bool m_bEnableExpect100Continue;
		bool m_bOverrideContentType;

		std::string m_sContentType;
		std::vector<std::string> m_vCustomRequestHeaders;
	};
}

#endif                                      

