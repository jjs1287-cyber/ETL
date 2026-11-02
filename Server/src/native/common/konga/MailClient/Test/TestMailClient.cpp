/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <iostream>

#include <konga/Utils/CrException.h>

#include <konga/MailClient/EmailAddress.h>
#include <konga/MailClient/EmailProperties.h>

#include <konga/MailClient/SmtpClient.h>
#include <konga/MailClient/rfc822Date.h>

using namespace konga;
using namespace std;

int testEmailAddress();

int testSendMail(int argc, char* argv[]);

int main(int argc, char* argv[])
{
	return testEmailAddress();
                                   
}

int testSendMail(int argc, char* argv[]) {
	if ( argc < 6 ) {
		cerr << "Usage: TestMailClient from to subject password message server1 [server2 server3...] " << endl;
		return -1;
	}

	try {
		const string stFrom(argv[1]);
		const string stTo(argv[2]);
		const string stSubj(argv[3]);
		const string stPwd(argv[4]);
		const string stMsg(argv[5]);

		SmtpClient smtp;
		EmailAddress from(stFrom);
		smtp.setAuthenticationInfo(from.getMailBoxAtHost(), stPwd);

		for( int i = 6; i < argc; ++i ) {
			smtp.addSmtpServer(argv[i]);
		}

		EmailProperties props(stFrom);
		props.addToAddress(stTo);

                                                                                                            
                                                                      

		smtp.sendMessage(props, stSubj, stMsg);
	}
	catch ( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testEmailAddress() {

	try {
		EmailAddress e1("\"Emanuel Norrbin\" <emanuel@etlforge.invalid>");
		cout << e1.getEmailAddress() << endl;

		EmailAddress e2("emanuel@etlforge.invalid");
		cout << e2.getEmailAddress() << endl;

		EmailAddress e3("Emanuel Norrbin <emanuel@etlforge.invalid>");
		cout << e3.getEmailAddress() << endl;

		EmailAddress e4("Emanuel Norrbin @ home, but not gone <emanuel@etlforge.invalid>");
		cout << e4.getEmailAddress() << endl;

		EmailAddress e5("\"Emanuel Norrbin <> nice \"<emanuel@etlforge.invalid>");
		cout << e5.getEmailAddress() << endl;

		EmailAddress e6("\"Emanuel Norrbin, Engineer@etlforge\" <emanuel@etlforge.invalid>");
		cout << e6.getEmailAddress() << endl;

		EmailAddress e7("<emanuel@etlforge.invalid>");
		cout << e7.getEmailAddress() << endl;

		try {
			EmailAddress bad("<emanuel@etlforge.invalid");
			cout << bad.getEmailAddress() << endl;
		}
		catch( const CrException& ex ) {
			cout << "This error is OK: " << ex.GetErrorString() << endl;
		}

		try {
			EmailAddress bad("emanuel@etlforge.invalid>");
			cout << bad.getEmailAddress() << endl;
		}
		catch( const CrException& ex ) {
			cout << "This error is OK: " << ex.GetErrorString() << endl;
		}

		try {
			EmailAddress bad("\"\" emanuel@etlforge.invalid");
			cout << bad.getEmailAddress() << endl;
		}
		catch( const CrException& ex ) {
			cout << "This error is OK: " << ex.GetErrorString() << endl;
		}

		try {
			EmailAddress bad("@etlforge.invalid");
			cout << bad.getEmailAddress() << endl;
		}
		catch( const CrException& ex ) {
			cout << "This error is OK: " << ex.GetErrorString() << endl;
		}

		try {
			EmailAddress bad("emanuel@");
			cout << bad.getEmailAddress() << endl;
		}
		catch( const CrException& ex ) {
			cout << "This error is OK: " << ex.GetErrorString() << endl;
		}

		try {
			                                       
			EmailAddress bad("\"Emanuel Norrbin <emanuel@etlforge.invalid>");
			cout << bad.getEmailAddress() << endl;
		}
		catch( const CrException& ex ) {
			cout << "This error is OK: " << ex.GetErrorString() << endl;
		}

		EmailProperties prop(e1);
		prop.addToAddress(e1.getEmailAddress());
		prop.addToAddress(e2.getEmailAddress());
		prop.addToAddress(e1.getEmailAddress());

		prop.addToAddress("emanuel_norrbin@yahoo.se");

		prop.addCCAddress(e2.getEmailAddress());

		prop.addCommaSeparatedCCAddresses("emanuel.norrbin@etlforge.invalid, \"Emanuel Norrbin, engineer@etlforge\" <emanuel_norrbin@yahoo.se>, ,torgil.zethson@etlforge.invalid,  ");
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}
