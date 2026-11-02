/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#if !defined(AFX_RFC822HEADERSWITHPARAMETERS_H__9837CD89_6BE6_4238_B56B_272A63074C62__INCLUDED_)
#define AFX_RFC822HEADERSWITHPARAMETERS_H__9837CD89_6BE6_4238_B56B_272A63074C62__INCLUDED_

#include <string>
#include <vector>

namespace konga {

	                                                      
	                                                              
	           
	                                                  
	class HeaderValue
	{
	public:
		typedef std::pair<std::string, std::string> Parameter;
		typedef std::vector< Parameter > Parameters;
	
	public:
		HeaderValue() {}
		HeaderValue(const std::string& stHeaderValue);
		virtual ~HeaderValue() {};
	
		std::string Print() const;                          
		std::string GetValue() const;                                                          
		std::string GetParameter(const std::string& stParameterName) const;                                                    
	
		Parameters GetParameters() const;                                           
	
		bool empty();
	
		void Parse(const std::string& stHeadervalue);
	
	protected:
		static void Unfold(std::string& st);
	
	private:
		std::string m_stValue;
		Parameters m_Parameters;
	};
	
	class RFC822HeadersWithParameters
	{
		typedef std::pair<std::string, HeaderValue> Header;
		typedef std::vector< Header > Headers;
	
	public:
		RFC822HeadersWithParameters() {}
		RFC822HeadersWithParameters(const std::string& stHeaders);
		virtual ~RFC822HeadersWithParameters() {};
	
		void PrintHeaders(std::string& stHeaders) const;

		                                                 
		std::string GetHeaderValueString(const std::string& stFieldName) const;
		HeaderValue GetHeaderValue(const std::string& stFieldName) const;
		std::string GetHeader(const std::string& stFieldName) const;

		                                                                             
		std::vector<HeaderValue> GetHeaderValues(const std::string& stFieldName) const;
	
	                          
	                       
		void Parse(const std::string& stHeaders);
		void AddHeader(const std::string& stHeader);
	
	private:
		Headers m_Headers;
	};
}                   

#endif                                                                                                

