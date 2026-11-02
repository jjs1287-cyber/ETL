/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <string.h>		// memset

#include <algorithm>	// std::find
#include <iostream>
#include <map>

// To compile on VC 8.0 you need to change the following in c-client\osdep.h:
// From:
// #include <windows.h>
// To:
// #undef T
// #include <windows.h>
// #define T 1
//
// or else code containing template<T ... stuff will not compile... after all c-client was written in -88...

#include <c-client/c-client.h>
#undef T	// Same reason as above... but on all platforms...

#include <boost/scoped_array.hpp>
#include <boost/noncopyable.hpp>

#include <konga/SysInfo/SysInfo.h>		// GetMachineName()

#include <konga/Utils/StringUtils.h>	// strsave
#include <konga/Utils/util.h>	// strsave

#include <konga/Utils/ProcessUtils.h>	// GetCurrentThreadId()
#include <konga/Utils/CrException.h>
#include <konga/Utils/AutoPtrCleanup.h>	// AutoArrayOfArrayDelete

#include "rfc822Date.h"
#include "SmtpClient.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

namespace {
#ifdef _DEBUG
	const long smtpOptions = SOP_DEBUG|SOP_TRYSSL;
#else
	const long smtpOptions = 0;
#endif
}

//
// The mm_*** callback routines have to be defined even if they are not used
// or the application will not compile
//

namespace {
	class MailSession {
	public:
		MailSession(void) {}
		~MailSession(void) {}

		void Reset() {
			m_sAccount.clear();
			m_sAccountPassword.clear();
			m_sDebug.clear();
			m_sErrors.clear();
			m_sWarnings.clear();
		}

		void setAuthenticationInfo(const std::string& sAccount, const std::string& sPassword) {
			m_sAccount = sAccount;
			m_sAccountPassword = sPassword;
		}

		void AddError(const char* const string) {
			AddString(string, m_sErrors);
		}

		void AddWarning(const char* const string) {
			AddString(string, m_sWarnings);
		}

		void AddDebug(const char* const string) {
			AddString(string, m_sDebug);
		}

		string GetWarnings() const {
			return m_sWarnings;
		}

		string GetErrors() const {
			return m_sErrors;
		}

		string GetDebug() const {
			return m_sDebug;
		}

		const char* GetAccount() const {
			return m_sAccount.c_str();
		}

		const char* GetAccountPassword() const {
			return m_sAccountPassword.c_str();
		}

	private:
		static void AddString(const char* const pch, string& str) {
			if ( !pch || !*pch )
				return;
			if ( !str.empty() )
				str += "\n";
			str += pch;
		}

	private:
		string m_sErrors, m_sWarnings, m_sDebug;
		string m_sAccount, m_sAccountPassword;
	};

	class MailSessionTracker {
	public:
		MailSessionTracker(void) {}
		~MailSessionTracker(void) {}

		MailSession& GetSession() {
			ThreadIdT tid = konga::GetCurrentThreadId();
			TidSessionMapT::iterator it = m_ResultMap.find(tid);
			if ( it != m_ResultMap.end() )
				return it->second;
			m_ResultMap.insert( TidSessionMapT::value_type(tid, MailSession()) ); 
			return GetSession();
		}
	private:
		typedef map<ThreadIdT, MailSession> TidSessionMapT;
		TidSessionMapT m_ResultMap;
	};

	// Global instance of this class keeps track of
	// the session for each thread.
	MailSessionTracker g_mailSessionTracker;
}

// errflg = NIL, PARSE, WARN, ERROR.
void mm_log (char *string, long errflg) {
	if ( !string )
		return;
	switch(errflg) {
		case NIL:
			#ifdef _DEBUG
			std::cout << "BABBBLE: " << string << std::endl;
			#endif
			break;
		case PARSE:
			#ifdef _DEBUG
			std::cout << "PARSE: " << string << std::endl;
			#endif
			g_mailSessionTracker.GetSession().AddWarning(string);
			break;
		case WARN:
			#ifdef _DEBUG
			std::cout << "WARN: " << string << std::endl;
			#endif
			g_mailSessionTracker.GetSession().AddWarning(string);
			break;
		case ERROR:
			#ifdef _DEBUG
			std::cout << "ERROR: " << string << std::endl;
			#endif
			g_mailSessionTracker.GetSession().AddError(string);
			break;
		default:
			std::cout <<"Unknown error flag: " << errflg << "  " << string << std::endl;
	};
}

void mm_dlog (char *string) {
	if ( !string )
		return;
#ifdef _DEBUG
	std::cout << "DEBUG: " << string << std::endl;
#endif
	g_mailSessionTracker.GetSession().AddDebug(string);
}

void mm_status (MAILSTREAM *stream,char *mailbox,MAILSTATUS *status) {
}

void mm_flags (MAILSTREAM *stream,unsigned long number) {
}

void mm_searched (MAILSTREAM *stream,unsigned long number) {
}

void mm_notify (MAILSTREAM *stream,char *string,long errflg) {
}

void mm_exists (MAILSTREAM *stream,unsigned long number) {
}

void mm_expunged (MAILSTREAM *stream,unsigned long number) {
}

void mm_fatal (char *string) {
	mm_log(string, ERROR);
}

void mm_login (NETMBX *mb,char *user,char *pwd,long trial) {
	#ifdef _DEBUG
	std::cout << "Login information requested" << std::endl;
	#endif
	const MailSession& session = g_mailSessionTracker.GetSession();
	if ( user ) strcpy(user, session.GetAccount());
	if ( pwd ) strcpy(pwd, session.GetAccountPassword());
	#ifdef _DEBUG
	if ( user ) std::cout << "Sending login information for user " << user << std::endl;
	#endif
}

void mm_nocritical (MAILSTREAM *stream) {
}

void mm_critical (MAILSTREAM *stream) {
}

long mm_diskerror (MAILSTREAM *stream,long errcode,long serious) {
	std::cout << "Disk error " << errcode << " " << serious << std::endl;
	string sErr("Disk error occurred!. OS Error Code: " + toString(errcode) + ".");
	g_mailSessionTracker.GetSession().AddError(sErr.c_str());
	return 1;
}

void mm_list (MAILSTREAM *stream,int delimiter,char *name,long attributes) {
}

void mm_lsub (MAILSTREAM *stream,int delimiter,char *name,long attributes) {
}

namespace {

	char** allocate_host_list(const vector<string>& v, size_t& n) {
		if ( v.empty() )
			return 0;

		const vector<string>::size_type size = v.size();
		char** retval = new char*[size+1];
		
		vector<string>::size_type i = 0;
		for( ; i < size; ++i ) {
			retval[i] = konga::strsave(v.at(i).c_str());
		}
		retval[i] = 0;
		n = size + 1;
		return retval;
	}

	// Recursively deallocates a linked list of ADDRESSes.
	void DeallocateAddress(ADDRESS* addr) {
		if ( !addr )
			return;
		DeallocateAddress(addr->next);
		delete addr;
	}
}

void SmtpClient::addSmtpServer(const std::string& stSmtpServer) {
	if ( !stSmtpServer.empty() ) {
		if ( std::find(m_vSmtpServers.begin(), m_vSmtpServers.end(), stSmtpServer) == m_vSmtpServers.end() ) {
			m_vSmtpServers.push_back(stSmtpServer);
		}
	}
}

void SmtpClient::addSmtpServers(const std::vector<std::string>& vSmtpServers) {
	for( vector<string>::const_iterator cit = vSmtpServers.begin(); cit != vSmtpServers.end(); ++cit ) {
		addSmtpServer(*cit);
	}
}

namespace {
	class AddressWrapper {
	public:
		AddressWrapper(const EmailAddress& email) : m_addr(0) {
			if ( !email.empty() ) {
				m_addr = new ADDRESS;
				FillAddress(email, *m_addr);
			}
		}

		AddressWrapper(void) : m_addr(0) {}

		AddressWrapper(const EmailProperties::vAddrT& addresses) : m_addr(0) {
			set(addresses);
		}

		~AddressWrapper(void) {
			DeallocateAddress(m_addr);
			for( vector<char*>::const_iterator cit = m_alloc_str.begin();
				cit != m_alloc_str.end(); ++cit)
			{
				if ( *cit ) delete[] *cit;
			}
			m_alloc_str.clear();
		}

		ADDRESS* GetAddress() {
			return m_addr;
		}

	protected:
		void set(const EmailProperties::vAddrT& addresses) {
			ADDRESS *addr = 0, *last_addr = 0;
			EmailProperties::vAddrT::const_iterator cit = addresses.begin();
			for(; cit != addresses.end(); ++cit ) {
				addr = new ADDRESS;
				if ( cit == addresses.begin() ) {
					m_addr = addr;
				}
				else if ( last_addr ) {
					last_addr->next = addr;
				}

				const EmailAddress& email = *cit;
				FillAddress(email, *addr);
				last_addr = addr;
			}
		}

		// Fills the ADDRESS structure with the information in the EmailAddress instance.
		void FillAddress(const EmailAddress& email, ADDRESS& addr) {
			memset(&addr, 0, sizeof(ADDRESS));
			if ( email.getEmailAddress().empty() )
				throw CrException("Address is empty");

			char* pc = konga::strsave(email.getMailBox().c_str());
			m_alloc_str.push_back(pc);
			addr.mailbox = pc;
			m_alloc_str.push_back( pc = konga::strsave(email.getHost().c_str()) );
			addr.host = pc;
			
			const string& stPersonalName = email.getPersonalName();
			if ( !stPersonalName.empty() ) {
				m_alloc_str.push_back( pc = konga::strsave(stPersonalName.c_str()) );
				addr.personal = pc;
			}
		}

	private:
		ADDRESS* m_addr;
		vector<char*> m_alloc_str;
	};

	class AutoSmtpClose : boost::noncopyable {
	public:
		AutoSmtpClose(SENDSTREAM* strm) : m_stream(strm) {}
		~AutoSmtpClose(void) {
			if ( m_stream ) {
				smtp_close(m_stream);
				m_stream = 0;
			}
		}
	private:
		SENDSTREAM* m_stream;
	};

	// Similar to AutoArrayDelete but uses free to release the memory that has to be allocated with malloc, realloc...
	template<typename T>
	class AutoArrayFree : boost::noncopyable {
	public:
		explicit AutoArrayFree(T*p) : m_p(p) {}
		~AutoArrayFree(void) {
			Reset(0);
		}

		void Reset(T*p) {
			if ( p != m_p )
				free(m_p);
			m_p=p;
		}

		T* Release() {
			T*p = m_p;
			m_p = 0;
			return p;
		}

		T* Get() {
			return m_p;
		}

	private:
		T*m_p;

	};

	void CollectAddressErrors(const ADDRESS* const addr, string& sErr) {
		if ( addr && addr->error && *(addr->error) ) {
			if ( !sErr.empty() )
				sErr += "\n";
			sErr += "Error in address: ";
			sErr += addr->error;
			CollectAddressErrors(addr->next, sErr);
		}
	}
}

void SmtpClient::sendMessage(const EmailProperties& props, const string& sSubject, const string& stMsg) {

	// Don't include all of this for now, just the parts we need/use.
//	#include <c-client/linkage.c>
	#ifdef WIN32	// Only available when compiled on windows, dont know why or what it is...
	auth_link (&auth_gss);		/* link in the gss authenticator */ 
	#endif
	auth_link (&auth_md5);		/* link in the md5 authenticator */ 
	auth_link (&auth_pla);		/* link in the pla authenticator */ 
	auth_link (&auth_log);		/* link in the log authenticator */ 

	if ( m_vSmtpServers.empty() )
		throw CrException("No smtp servers were specified.");

	size_t nHosts = 0;
	char** hostlst = allocate_host_list(m_vSmtpServers, nHosts);
	AutoArrayOfArrayDelete<char> adopt_hostlst(hostlst, nHosts);

	g_mailSessionTracker.GetSession().Reset();
	if ( !m_sAccount.empty() ) {
		if ( m_sAccount.size() > MAILTMPLEN-1 )
			throw CrException("The account user name is too long, the maximum is " + toString(MAILTMPLEN-1) + ".");
		if ( m_sAccountPassword.size() > MAILTMPLEN-1 )
			throw CrException("The account password is too long, the maximum is " + toString(MAILTMPLEN-1) + ".");

		g_mailSessionTracker.GetSession().setAuthenticationInfo(m_sAccount, m_sAccountPassword);
	}

	SENDSTREAM* stream = smtp_open(hostlst, smtpOptions);
	if ( stream == NIL ) {
		const MailSession& res = g_mailSessionTracker.GetSession();
		string sErr(res.GetErrors());
		string sWarn(res.GetWarnings());
		if ( !sErr.empty() && !sWarn.empty() )
			sErr += "\n";
		sErr += sWarn;
		if ( sErr.empty() )
			sErr = "smtp_open failed. No reason was given.";
		throw CrException(sErr);
	}
	AutoSmtpClose adopt_stream(stream);

	// Construct the envelope (from, to, subject, ...)
	ENVELOPE msg;
	memset(&msg, 0, sizeof(ENVELOPE));

	// Validate and allocate addresses.
	if ( props.getFromByRef().empty() )
		throw CrException("From address is empty.");
	AddressWrapper from(props.getFromByRef());
	msg.from = from.GetAddress();
//	msg.sender = from.GetAddress();	// Not needes since we require a From field (RFC822)

	if ( props.getToAddressesByRef().empty() )
		throw CrException("No message recipient was specified.");
	AddressWrapper to(props.getToAddressesByRef());
	msg.to = to.GetAddress();

	// This is optional.
	AddressWrapper replyTo(props.getReplyToByRef());
	msg.reply_to = replyTo.GetAddress();

	// If no Return-Path is specified we use the From address.
	AddressWrapper returnPath(props.getReturnPathByRef());
	msg.return_path = returnPath.GetAddress();
	if ( !msg.return_path ) {
		msg.return_path = from.GetAddress();
	}

	// CC and BCC are optional.
	AddressWrapper cc(props.getCCAddressesByRef());
	msg.cc = cc.GetAddress();

	AddressWrapper bcc(props.getBCCAddressesByRef());
	msg.bcc = bcc.GetAddress();

	// Generate a message id of format < GUID @ hostname > which seems to be a semi-standard.
	const string sMsgId("<" + GetGUIDString() + "@" + konga::getMachineName() + ">");
	boost::scoped_array<char> msgId( konga::strsave(sMsgId.c_str()) );
	msg.message_id = msgId.get();

	boost::scoped_array<char> subject( konga::strsave(sSubject.c_str()) );
	msg.subject = subject.get();

	boost::scoped_array<char> now( konga::strsave(konga::rfc822Date().c_str()) );
	msg.date = reinterpret_cast<unsigned char*>(now.get());

	// Allocate the body of the message.
	BODY body;
	memset(&body, 0, sizeof(BODY));

	body.type = TYPETEXT;
	body.encoding = ENC8BIT;

	// Allocate memory using malloc for the data since this data may be free'd by call to smtp_mail.
	AutoArrayFree<unsigned char> pMsg( (unsigned char*)malloc(stMsg.size()+1) );
	memcpy(pMsg.Get(), stMsg.c_str(), stMsg.size()+1);

	body.contents.offset = 0;
	body.contents.text.size = stMsg.size();
	body.contents.text.data = pMsg.Get();
	body.size.bytes = stMsg.size();

	char* type = "MAIL";
	if ( smtp_mail(stream, type, &msg, &body) == NIL ) {
		// Get warnings reported by the library
		string sErr;
		if ( stream->reply && *(stream->reply) ) {
			sErr = stream->reply;
		}
		CollectAddressErrors(msg.from, sErr);
		CollectAddressErrors(msg.to, sErr);

		// Add the errors, warnings and session debug that has been collected.
		const string& sResErr = g_mailSessionTracker.GetSession().GetErrors();
		if ( !sErr.empty() && !sResErr.empty() ) {
			sErr += "\n";
		}
		sErr += sResErr;

		const string& sWarnings = g_mailSessionTracker.GetSession().GetWarnings();
		if ( !sErr.empty() && !sWarnings.empty() ) {
			sErr += "\n";
		}
		if ( !sWarnings.empty() ) {
			sErr += "Warning: ";
			sErr += sWarnings;
		}

		const string& sDebug = g_mailSessionTracker.GetSession().GetDebug();
		if ( !sErr.empty() && !sDebug.empty() ) {
			sErr += "\n";
		}
		if ( !sDebug.empty() ) {
			sErr += "The following is a trace of the conversation with the mail server:\n";
			sErr += sDebug;
		}

		throw CrException("Failed to send email. Reason: " + sErr);
	}

	// If smtp_mail changed the text.data we should not delete it.
	if ( (void*)body.contents.text.data != (void*)pMsg.Get() ) {
		pMsg.Release();
		pMsg.Reset(body.contents.text.data);
	}
}



