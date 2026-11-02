/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#if !defined(AFX_POPACCOUNT_H__76A936D5_E2C6_11D1_99D1_006008CE14ED__INCLUDED_)
#define AFX_POPACCOUNT_H__76A936D5_E2C6_11D1_99D1_006008CE14ED__INCLUDED_

                            

class POPAccount  
{
public:
	POPAccount(const char* name=0, const char* password=0, const char* server=0) :
	  m_name(strsave(name)), m_password(strsave(password)), m_server(strsave(server)) 
	  {

	  }
	  ~POPAccount() { 
		  if(m_name) delete []m_name;
		  if(m_password) delete []m_password;
		  if(m_server) delete []m_server;
	  }

	const char* getName() const { return m_name; }
	const char* getPassword() const { return m_password; }
	const char* getServer() const { return m_server; }

private:
	char* m_name;
	char* m_password;
	char* m_server;
};

#endif                                                                               
