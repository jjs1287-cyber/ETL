/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PROTOCOL_H
#define KONGA_PROTOCOL_H

#include <string>

namespace konga {

    class FileSecurity
    {
    public:
		FileSecurity(void) {}
		virtual ~FileSecurity(void) {}

        virtual bool HasAccess(const std::string& stFileName) const { return true; }
    };

      
                                                                            
       
    class Protocol
    {
    protected:
		Protocol() : m_lLastError(0), m_Type(1                               ),
			m_Conversion(NOCONV), m_pFileSecurity(0), m_bUseUrl(false), m_bAppend(false), m_Port(0) { }

    public:
        virtual ~Protocol()
        {
            if( m_pFileSecurity )
                delete m_pFileSecurity;
        }

		virtual long Connect() = 0;
		virtual long DisConnect() = 0;

        virtual long GetAsFile(const std::string& stLocalFile) = 0;
        virtual long GetAsString(std::string& stResult) = 0;

        virtual long PutAsFile(const std::string& stLocalFile) = 0;
        virtual long PutAsFileGetRespAsString(const std::string& stLocalFile, std::string& stResponse) = 0;	
        virtual long PutAsFileGetRespAsFile(const std::string& LocalFile, const std::string& stResponseFile) = 0;

        virtual long PutAsString(const std::string& stLocalResource) = 0;
        virtual long PutAsStringGetRespAsString(const std::string& stLocalResource, std::string& stResponse) = 0;
        virtual long PutAsStringGetRespAsFile(const std::string& stLocalResource, const std::string& stResponseFile) = 0;

		virtual long Delete(const std::string& stFileName) = 0;

        void Append(bool bAppend) { m_bAppend = bAppend; }

        long SetUrl(const std::string& url){m_stUrl = url; UseUrl(true); return SUCCESS;}
		std::string GetUrl(void) const { return m_stUrl; }

		long UseUrl(bool bUseUrl){m_bUseUrl = bUseUrl; return 0; }
		long UsingUrl() { return m_bUseUrl; }

        std::string GetUserName() const { return m_UserName; }
        void SetUserName(const std::string& UserName){ m_UserName = UserName; }

        std::string GetPassword() const { return m_Password; }
        void SetPassword(const std::string& Password){ m_Password = Password; }

                                                              
                                                                             
                                                 

        long PutResponseType(long);
        long GetResponseType() const {return m_Type;}

                                                                          
                                                                           

        bool HasAccessToFile(const std::string& stFileName) const
        {
            return m_pFileSecurity ? m_pFileSecurity->HasAccess(stFileName) : true;
        }

		enum {
			SUCCESS = 0,
			FAILURE = -1
		};

		std::string GetLastError() const;
		long GetLastErrorNumber() const { return m_lLastError;}

	protected:
		void AddError(const std::string& stError) const;

    protected:
        enum Conversions { UTOC = 1, CTOU, NOCONV};

                                                                                                        

                                            
                                                     

        long m_lLastError;

        long m_Type;
        Conversions m_Conversion;

        FileSecurity *m_pFileSecurity;

	private:
		bool m_bUseUrl;
		bool m_bAppend;
		int m_Port;
		std::string m_stUrl;
		std::string m_UserName, m_Password;
		std::string m_UrlPath, m_HostName;
        mutable std::string m_stLastError;	                                                              
    };
}                   

#endif                    

