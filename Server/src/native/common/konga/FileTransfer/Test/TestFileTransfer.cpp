/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <iostream>
#include <vector>
#include <string>

#include <stdio.h>

#include <curl/curl.h>

#include <boost/scoped_array.hpp>
#include <boost/filesystem/path.hpp>
#include <boost/filesystem/exception.hpp>
#include <boost/filesystem/operations.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/DateUtils.h>

#include <konga/FileTransfer/Http.h>
#include <konga/FileTransfer/Https.h>
#include <konga/FileTransfer/Ftp.h>
#include <konga/FileTransfer/ftpparse.h>

#include <konga/FileTransfer/CurlTransferLibWrapper.h>
#include <konga/FileTransfer/Samba.h>

#include <konga/FileTransfer/FileRetriever.h>
#include <konga/FileTransfer/HttpOptions.h>

#include <konga/Utils/FileUtils.h>

using namespace std;
using namespace konga;

typedef vector<string> vstrings;

int testHttpGet();
int testFtpGet();
int testFtpPut();
int testCurlWrapper();
int testFtpDirectoryListing();
int testFtpRename();
int testSamba();
int test();
int testHeaders();
int testWebServiceCall();
int testCurl();
int testHttpHead();

int testFileShareRename();

int testHttpWithQuery();

int testFileRetriever();

int testCookies();

int testFtps();

int testFtpDelete();

int testHttpDigestGet(const string& pwd);

int testHttpDigestPut(const string& pwd);

int testHttpDigestPost(const string& pwd);

int testHttpDigestPutFromFile(const string& pwd);

int testRFC1867();

int testHttpProxySettings();

int testVMSFtpDirectoryListing();

int testMTHTTP();

int testHttpOptions();


BOOST_FILESYSTEM_DECL bool no_check( const std::string & name ) {
	return true;
}

namespace {
	string stPassword;
}

int main (int argc, char *argv[])
{
	if ( argc > 1 ) {
		stPassword = argv[1];
	}

	return testMTHTTP();

                                       

                            

                                       

                                  
                                                                          
                 
                        
                      
                          
                                    
                       
                       
                        
                            
                               
                     
                         
                                
                              
                              
                        
                     
                          
                                        
                                        
                                         
                                                
                        
}

int testHttpOptions() {
	HttpOptions ho;
	ho.setCustomRequestHeaders("User: Emanuel\n Norrbin\n Jr\nPassword: sdfsdfsdfa");

	return 0;
}

int testVMSFtpDirectoryListing() {
	vector<Ftp::FtpDirectoryListEntry> filelist;
	try {
		filelist.clear();

		string stResult;
		read_file_to_string("vms_dir_list.txt", stResult);

		string::size_type iPos = 0;
		string::size_type iBegin = 0;
		const char feed_char[]="\r\n";
		while ( (iBegin!=string::npos) &&
			    (iPos=stResult.find_first_of(feed_char, iBegin))!=string::npos )
		{
			const string& stEntry = stResult.substr(iBegin, iPos-iBegin);
			if ( !stEntry.empty() ) {
				Ftp::addEntry(filelist, stEntry);
			}
			iBegin = stResult.find_first_not_of(feed_char, iPos);
		}
		int i = 0;
		for( vector<Ftp::FtpDirectoryListEntry>::const_iterator cit = filelist.begin(); cit != filelist.end(); ++cit, ++i ) {
			const Ftp::FtpDirectoryListEntry& entry = *cit;
			cout << i << ": " << entry.name << "\t" << entry.size << "\t" << entry.seconds << " "
				<< entry.day << " " << entry.month << " " << entry.year << endl;
		}
		return 0;
	}
	catch ( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return Protocol::FAILURE;
	}
}

class TestHttpProxyConfiguration : public HttpProxyConfiguration {
public:
	TestHttpProxyConfiguration(void) {}
	~TestHttpProxyConfiguration(void) {}

	bool isProxyConfigured(std::string& sProxyHostName, long& lProxyPort, std::string& sProxyUserName, std::string& sProxyPassword) const {
		sProxyHostName = "linuxdoc";
		lProxyPort = 80;
		sProxyUserName = "puser";
		sProxyPassword = "ppass";
		return true;
	}

};

int testHttpProxySettings() {

	Http http( new TestHttpProxyConfiguration() );
	http.SetUrl("http://linuxdoc/~user1/index.html");
	http.SetHttpVerb(HttpGet);
	string stResult;
	if ( http.GetAsString(stResult) ) {
		cout << http.GetLastError() << endl;
	}
	cout << stResult << endl;

	return 0;
}

int testRFC1867() {

	Http http(0);
	http.SetUrl("http://linuxdoc/~user1/post.php");
	http.SetHttpVerb(HttpPost);
                                                                                  

	try {
		string stResponse;
		if ( http.PutAsFileGetRespAsString("C:\\tmp\\file.txt", stResponse) != Protocol::SUCCESS ) {
			cerr << http.GetLastError() << endl;
			return 1;
		}
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 2;
	}
	return 0;
}

int testHttpDigestPutFromFile(const string& pwd) {
	Http http(0);
	http.SetUrl("http://linuxdoc/digest_auth/put/test_put.txt");

	try {
		http.SetUserName("user1");
		http.SetPassword(pwd);
		http.SetHttpVerb(konga::HttpPut);

		const string stLocalFile("data");
		string stResponse;
                                                                                        
                                            
     
                               

		const string stResponseFile("Response.html");
		if ( http.PutAsFileGetRespAsFile(stLocalFile, stResponseFile) != Protocol::SUCCESS ) {
			throw CrException(http.GetLastError());
		}
		cout << stResponse << endl;

	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testHttpDigestPost(const string& pwd) {

	Http http(0);
	http.SetUrl("http://linuxdoc/digest_auth/post/post.php");
	try {
		http.SetUserName("user1");
		http.SetPassword(pwd);
		http.SetHttpVerb(konga::HttpPost);
		string stResponse;
		if ( http.PutAsStringGetRespAsString("Some text to post\n", stResponse) != Protocol::SUCCESS ) {
			throw CrException(http.GetLastError());
		}
		cout << stResponse << endl;
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}


int testHttpDigestPut(const string& pwd) {
	Http http(0);
	http.SetUrl("http://linuxdoc/digest_auth/put/test_put.txt");

	try {
		http.SetUserName("user1");
		http.SetPassword(pwd);
		http.SetHttpVerb(konga::HttpPut);
		string stResponse;
		if ( http.PutAsStringGetRespAsString("More text to put\n", stResponse) != Protocol::SUCCESS ) {
			throw CrException(http.GetLastError());
		}
		cout << stResponse << endl;
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testHttpDigestGet(const string& pwd) {
	Http http(0);
	http.SetUrl("http://linuxdoc/digest_auth/test.txt");


	try {
		string stResult;
		http.SetUserName("user1");
		http.SetPassword(pwd);
		if ( http.GetAsString(stResult) != Protocol::SUCCESS ) {
			throw CrException(http.GetLastError());
		}
		cout << stResult << endl;
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}

int testFtpDelete() {
	try {
		Ftp ftp;
		ftp.SetUrl("ftp://linuxdoc/source/emanuel");
		ftp.SetUserName("user1");
		ftp.SetPassword(stPassword);

		if ( ftp.Delete("deleteme.txt") ) {
			cerr << ftp.GetLastError() << endl;
		}
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testFtps() {

	try {
		Ftp ftp;
		ftp.SetUrl("ftp://emanuel/test1.txt");
		ftp.SetUserName("user1");
		ftp.SetPassword(stPassword);
		ftp.setSecurityOption(FTP_SECURITY_SSL_CONTROL);
		ftp.setVerifyPeer(false);

		string stResult;
		if ( ftp.GetAsString(stResult) != Protocol::SUCCESS ) {
			cerr << ftp.GetLastError() << endl;
			return static_cast<int>(ftp.GetLastErrorNumber());
		}
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testCookies() {
	Http http(0);

	string stCookiejar("c:\\tmp\\cookiejar.txt");

	                                                          
	http.SetUrl("http://linuxdoc/~user1/setcookie.php?uname=lussebulle");
	http.SetOutputCookieFile(stCookiejar);

	string stResponse;
	http.GetAsString(stResponse);
	cout << stResponse << endl;

	                                                     
	http.SetUrl("http://linuxdoc/~user1/printcookie.php");
	http.SetInputCookieFile(stCookiejar);
	http.GetAsString(stResponse);

	{
		                                                                      
		Http http2(0);
		http2.SetUrl("http://linuxdoc/~user1/printcookie.php");
		http2.SetInputCookieFile(stCookiejar);
		string stResponse2;
		http2.GetAsString(stResponse2);
		if ( stResponse != stResponse2 ) {
			cout << "Got different result!" << endl;
			cout << stResponse2;
		}
	}
	return 0;
}

int testFileRetriever() {
	FileRetriever fr;
	const string stUrl("http://greg.froh.ca/fun/random_bushism/soap/?wsdl");
                                                        
	string stFile("/tmp/gotten");
	if ( fr.GetAsFile(stUrl, stFile) != Protocol::SUCCESS ) {
		cout << fr.LastError() << endl;
		return 1;
	}
	return 0;
}

int testHttpWithQuery() {
	try {
		Https https(0);
		const string stUrl("http://greg.froh.ca/fun/random_bushism/soap/?wsdl");
		https.SetUrl(stUrl);
		https.GetAsFile("/tmp/gotten");
		return 0;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
}

int testGetFileShareListingTwice() {
	try {
		                                                                             
		Samba smb;
		const string stUserName( "user1" );
		const string stPassword("****");
		smb.setUserName(stUserName);
		smb.setPassword(stPassword);
		std::vector<Samba::SmbDirectoryEntry> dirList;
		smb.GetUncDirectoryListing("\\\\windoc\\\\source", dirList, 0);

		dirList.clear();
		smb.setUserName("MONEY\\emanueln");
		smb.setPassword("***");
		smb.GetUncDirectoryListing("\\\\windoc\\\\source", dirList, 0);
		return 0;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
}


int testFileShareRename() {
	Samba smb;
	const string stUserName( "user1" );
	if ( !stUserName.empty() ) {
		smb.setUserName(stUserName);
		smb.setPassword("****");
	}
	string stUncPath( "\\\\linuxdev\\source\\emanuel\\archive\\" );


	return 0;
}


int testHttpHead() {
	try {
		Http http(0);
		http.setHost("linuxdoc");
		http.setPath("index.html");
		http.SetHttpVerb(HttpHead);
		string stResult;
		if ( http.GetAsString(stResult) != InetBase::SUCCESS ) {
			cout << http.GetLastError() << endl;
			return -1;
		}

		cout << "GOT:" << endl << stResult << endl;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return -1;
	}

	return 0;
}

int testCurl() {
	CURL *curl = curl_easy_init();
	if(curl) {
		curl_easy_setopt(curl, CURLOPT_URL, "http://192.168.0.65/crhttptest.txt");

		FILE* f = fopen("data/gotten.txt", "wb");
		if ( f == 0 )
			return -1;
		CURLcode res = CURLE_OK;
		res = curl_easy_setopt(curl, CURLOPT_WRITEDATA, f);
		if ( res != CURLE_OK )
			return res;

		res = curl_easy_perform(curl);

		curl_easy_cleanup(curl);
    }
	return 0;
}

int testWebServiceCall() {
	try {

		Http http(0);
		http.SetUrl("http://greg.froh.ca/fun/random_bushism/soap/index.php");
		http.addRequestHeader("Content-Type: text/xml; charset=\"utf-8\"");
		http.addRequestHeader("SOAPAction: \"urn:RandomBushism#bushism#getRandomBushism\"");

		if ( http.PutAsFileGetRespAsFile("data/_empty_input_req.xml", "data/response.xml") != Protocol::SUCCESS ) {
			cout << http.GetLastError() << endl;
			return -1;
		}
		return 0;
	}
	catch(...) {
		cout << "Unexpected exception caught..." << endl;
		return -1;
	}
}

int test() {

	try {
		string stSource("C:\\tmp\\README.txt");
		boost::filesystem::path ph(stSource);

		cout << ph.root_path().native_file_string() << endl;
		cout << ph.root_name() << endl;

		string stTarget( "C:\\tmp\\DONTREADME.txt" );
		boost::filesystem::copy_file(stSource, stTarget);
		boost::filesystem::remove(stTarget);

		boost::filesystem::copy_file("\\\\linuxdev\\emanuel\\source.txt", "C:\\tmp\\mellan slag\\target.txt");

		cout << ph.native_file_string() << endl;
	}
	catch( const boost::filesystem::filesystem_error& err ) {
		cout << err.what() << endl;
	}
	return 0;
}

int testHeaders() {
	Http http(0);
	string crlf("\r\n");
	stringstream sHeaders;
	sHeaders << "header 1" << crlf << "header 2" << crlf;
	http.setRequestHeaders(sHeaders.str());

	return 0;
}

int testSamba() {

	try {
		string stUser("emanuel");
		string stPwd("****");
		Samba smb;
		smb.setServer("linuxdev");
		smb.setWorkGroup("LinuxDev");
		smb.setShare(stUser);
		smb.setUserName(stUser);
		smb.setPassword(stPwd);

		smb.CopyFromShareToLocalFile("smbtest.txt", "data/smbtext.txt");
		smb.CopyFromLocalFileToShare("data/smbtext.txt", "smbtext.txt");

		Samba smb2;
		smb2.setUNCPath("\\\\linuxdev\\emanuel");
		smb2.setUNCPath("\\\\linuxdev\\emanuel\\");
		smb2.setUNCPath("//linuxdev/emanuel/file");
		smb2.setUserName(stUser);
		smb2.setPassword(stPwd);
                                                                  

		smb2.DeleteFileOnShare("target.txt");
		CopyFromUncToUnc("\\\\linuxdev\\emanuel\\source.txt", smb, "\\\\linuxdev\\emanuel\\target.txt", smb2);

		smb.CopyFromUncToLocalFile("//linuxdev/emanuel/source.txt", "data/source.txt");
		smb2.CopyFromLocalFileToUnc("data/source.txt", "//linuxdev/emanuel/target.txt");

		std::vector<Samba::SmbDirectoryEntry> list;
		smb.GetShareDirectoryListing("\\dev\\", list, 0);
		cout << "Dirlist:" << endl;
		for( std::vector<Samba::SmbDirectoryEntry>::const_iterator it = list.begin(); it != list.end(); ++it ) {
			cout << it->type << " " << it->size << " " << it->day() << "" << it->month() << " " << it->name << endl; 
		}

		list.clear();
		smb2.GetUncDirectoryListing("\\\\linuxdev\\emanuel\\software", list, 0);
		cout << "Dirlist:" << endl;
		for( std::vector<Samba::SmbDirectoryEntry>::const_iterator it = list.begin(); it != list.end(); ++it ) {
			cout << it->type << " " << it->size << " " << it->day() << " " << it->name << endl; 
		}

		                                 
		string stResult;
		smb.CopyFromShareToLocalString("source.txt", stResult);
		cout << "Result (size=" << stResult.size() << "):" << endl << stResult;

		                                                
		stResult.append("Then add some!\n");
		smb.CopyFromLocalStringToUnc(stResult, "\\\\linuxdev\\emanuel\\stringtarget.txt");

		smb.MoveFileOnShare("stringtarget.txt", "stringtarget-moved.txt");

		smb.MoveUnc("\\\\linuxdev\\emanuel\\stringtarget-moved.txt", "//linuxdev/emanuel/stringtarget-moved-again.txt");

		smb.DeleteFileOnShare("stringtarget-moved-again.txt");

		try {
			smb.CopyFromUncToLocalString("\\\\linuxdev\\emanuel\\stringtarget.txt", stResult);
			cout << "Hey!! this should fail. That file does not exist as far as I know!" << endl;
			return -1;
		}
		catch( const CrException& ex ) {
			                         
			cout << "OK, exception thrown: " << ex.GetErrorString() << endl;
		}

		smb.DeleteFileOnShare("stringtarget.txt");

		try {
			smb.DeleteUnc("\\\\linuxdev\\emanuel\\stringtarget.txt", true);
			cout << "Hey!! this should fail. Because the flag says so!" << endl;
			return -1;
		}
		catch( const CrException& ex ) {
			                         
			cout << "OK, exception thrown: " << ex.GetErrorString() << endl;
		}
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return -1;
	}
	return 0;
}

void printHeaders(const vstrings& v) {
	if ( !v.empty() ) {
		cout << "Headers: " << endl;
		for( vstrings::const_iterator it = v.begin(); it != v.end(); ++it ) {
			cout << *it << endl;
		}
	}
}

void printResponseHeaders(const CurlTransferLibWrapper& c) {
	vstrings headers = c.getResponseHeaders();
	printHeaders(headers);
}

int testFtpRename() {
	CurlTransferLibWrapper curl;
	try {

		string stUrl("ftp://localhost/pub/");
		string stOld("uploaded.txt");
		string stNew("uploaded-renamed.txt");

		curl.setUserAndPassword("ftp", "emanuel@thep.lu.se");
		curl.setUrl(stUrl);
		curl.ftpRename(stOld, stNew);
		printResponseHeaders(curl);

		cout << curl.getFtpDirectoryListing(false) << endl;
		printResponseHeaders(curl);

		curl.ftpRename(stNew, stOld);
		cout << curl.getFtpDirectoryListing(false) << endl;

		Ftp ftp;
		ftp.SetUserName("ftp");
		ftp.SetPassword("emanuel@thep.lu.se");
		ftp.SetUrl("ftp://localhost/pub/");
		if ( ftp.RenameFile(stOld, stNew) != Protocol::SUCCESS ) {
			cout << ftp.GetLastError() << endl;
		}

		ftp.UseUrl(false);
		ftp.setHost("localhost");
		ftp.setPath("/pub/");

		if ( ftp.RenameFile(stNew, stOld) != Protocol::SUCCESS ) {
			cout << ftp.GetLastError() << endl;
		}
		
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		printResponseHeaders(curl);
		return -1;
	}
	return 0;
}

int testHttpGet() {
    string stUrl("http://192.168.0.65/crhttptest.txt");
                                                
    string stLocalFile("data/crhttptest.txt");
	string stLocalString;

	Http http(0);
	http.SetUrl(stUrl);
	if ( http.GetAsFile(stLocalFile) != Protocol::SUCCESS  ){
		cout << http.GetLastError().c_str() << endl;
		return -1;
	}
	printHeaders( http.GetResponseHeaders() );

	if ( http.GetAsString(stLocalString) != Protocol::SUCCESS ) {
		cout << http.GetLastError().c_str() << endl;
		return -1;
	}
	cout << "Received data: " << endl;
	cout << stLocalString << endl;
  
             
                                               
                     
                                                              
                                                                 
                                               
            
  
                                            

                                                               
                                       
            
  
                                   
                               
  
	return 0;
}

int testFtpGet() {
	string stUrl("ftp://linuxdev:21/source/emanuel/products.txt");
	string stLocalFile("data/test.txt");

	Ftp ftp;
	ftp.SetUrl(stUrl);
	ftp.SetUserName("user1");
	ftp.SetPassword(stPassword);
	if ( ftp.GetAsFile(stLocalFile) != Protocol::SUCCESS ){
		cout << ftp.GetLastError() << endl;
		return -1;
	}

	string stResult;
	if ( ftp.GetAsString(stResult) != Protocol::SUCCESS ){
		cout << ftp.GetLastError() << endl;
		return -1;
	}
	cout << "Result:" << endl;
	cout << stResult << endl;

	ftp.UseUrl(false);
	ftp.setHost("localhost");
	ftp.setPath("pub/test2.txt");
	if ( ftp.GetAsFile("data/ftp-test2.txt") != Protocol::SUCCESS ){
		cout << ftp.GetLastError() << endl;
		return -1;
	}

	stResult.clear();
	if ( ftp.GetAsString(stResult) != Protocol::SUCCESS ){
		cout << ftp.GetLastError() << endl;
		return -1;
	}
	cout << "Result:" << endl;
	cout << stResult << endl;

	return 0;
}

int testFtpPut() {
	Ftp ftp;
	ftp.SetUrl("ftp://localhost/pub/uploaded.txt");
	ftp.SetUserName("ftp");
	ftp.SetPassword("emanuel@thep.lu.se");
	string stResult;

	if ( ftp.PutAsFileGetRespAsString("data/test.txt", stResult) != Protocol::SUCCESS ) {
		cout << ftp.GetLastError() << endl;
		return -1;
	}
	printHeaders(ftp.GetResponseHeaders());

	string stStringToPut("Put This!!");
	string stResponseFile("data/response.txt");
	if ( ftp.PutAsStringGetRespAsFile(stStringToPut, stResponseFile) != Protocol::SUCCESS ) {
		cout << ftp.GetLastError() << endl;
		return -1;
	}
	printHeaders(ftp.GetResponseHeaders());

	return 0;
}

int testFtpDirectoryListing() {
	CurlTransferLibWrapper curl;
	string stUrl("ftp://localhost/pub/");
	try {
		                                            
		curl.setUrl(stUrl);
		string  stResult = curl.getFtpDirectoryListing(false);
		cout << stResult << endl << endl;
		printResponseHeaders(curl);
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		printResponseHeaders(curl);
		return -1;
	}

	Ftp ftp;
	ftp.SetUrl(stUrl);
	vector<Ftp::FtpDirectoryListEntry> listing;
	if ( ftp.GetDirectoryList(listing, false) != Protocol::SUCCESS ) {
		cout << ftp.GetLastError() << endl;
		return -1;
	}
	for( vector<Ftp::FtpDirectoryListEntry>::const_iterator it = listing.begin(); it != listing.end(); ++it ) {
		cout << "<file>" << it->name << "</file>" << endl;
	}

	stUrl = "ftp://ftp.sunet.se/pub/";
	ftp.SetUserName("ftp");
	ftp.SetPassword("emanuel@thep.lu.se");
	ftp.SetUrl(stUrl);

	if ( ftp.GetDirectoryList(listing, false) != Protocol::SUCCESS ) {
		cout << ftp.GetLastError() << endl;
		return -1;
	}
	for( vector<Ftp::FtpDirectoryListEntry>::const_iterator it = listing.begin(); it != listing.end(); ++it ) {
		cout << it->size << ' ' << it->month << ' ';
		cout << it->day << ' ' << it->seconds << ' ' << it->name << endl;
	}

	return 0;
}

int testCurlWrapper() {
	  
                             
      
                                              
                                       
                                                              
                             

                           
                                                            
                                  
                               
                                                  
                  
                                                                              
                                      
                     
                               
                
                                      
     
    
   
  
                                 
                                      
                             
            
  

      
               
                    
                                                          
                                  
                                     
             

                                                        
                                                            
                                   
                                     
             

                              
                                                     
   
  
                                 
                                      
            
  
   

	return 0;
}

