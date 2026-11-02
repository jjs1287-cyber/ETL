/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_URL_H
#define KONGA_URL_H

#include <string>

namespace konga {

	class Url {
	public:
		Url(void);
		explicit Url(const std::string& stUrl);
		~Url(void);

		                                                    
		                                                                      
		                                                                         
		                                       
		void Parse(const std::string& stUrl);

		std::string getParsedUrl() const;
		std::string getProtocol() const;
		std::string getHost() const;
		std::string getPath() const;

		                                           
		std::string getQueryString() const;
		bool hasQueryString() const;

		                                            
		std::string getFragment() const;
		bool hasFragment() const;

		long getPort() const;

	private:
		void Clear();

	private:
		long m_lPort;
		bool m_bHasQueryString, m_bHasFragment;
		std::string m_stProtocol;
		std::string m_stHost;
		std::string m_stPath;
		std::string m_stQueryString;
		std::string m_stFragment;
		std::string m_stParsedUrl;
	};

	                                                                 
	                      
	bool IsUrl(const std::string& stUrl);
}
#endif               
