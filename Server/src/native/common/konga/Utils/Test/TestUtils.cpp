/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stdio.h>

#include <iostream>
#include <fstream>	           
#include <string>
#include <limits>
#include <exception>
#include <cassert>
#include <limits>

#include <boost/date_time/posix_time/posix_time.hpp>
#include <boost/date_time/gregorian/gregorian.hpp>

#include <boost/filesystem/path.hpp>

#include <boost/thread/thread.hpp>

#include <konga/Utils/Url.h>
#include <konga/Utils/util.h>
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/FileLineReadWriter.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/Version.h>
#include <konga/Utils/BinaryData.h>
#include <konga/Utils/BinaryDataFileIo.h>
#include <konga/Utils/KString.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/MapUtils.h>
#include <konga/Utils/FileAction.h>
#include <konga/Utils/FilesTraverser.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/PointerCache.h>
#include <konga/Utils/Random.h>
#include <konga/Utils/util.h>
#include <konga/Utils/CsvUtils.h>

using namespace konga;
using namespace std;

                                
int GeneratePIESXml();

int testVersion();
int testSettings();
int testDateString();
int testUrl();
int testRandom();
int testTimeUtils();
int testToString();
int testFileUtils();
int testGUID();
int testReadFile();
int testWriteFile();
int testUpperLower();
int testPathQueries();

                           
int testRCStringTable();
int testCString();
int testGetPot();

                                    
int testKeyValues();

                                  
int testNullable();

int testTmpDir();

int testReplaceStringWithString();

int testFileLineReadWriter();

int testreplace_in_file();

int testkCopyFile();

int testtoLong();

int testBinaryData();

int testGetLevelInfo();

int testConnectString();

int testStringUtils();

int testMapUtils();

int testDateUtils();

int testFileTraversion();

int testKString();                        

int testStuff();

int testAutoFileCleanup();

int testKStringRemove();

int writeAllAscii();

int testReplaceCharWithChar();

int test1();
int test2();

int testGetTmpFileName();

int generateDates();

int testTokenizeString();

int testAsciiNull();

int testDeleteDir(const string&);

int testRunProc();

int testRemoveLines();

int testPadding();

int testUrlEscape();

int stringTests();

int testClock();
int testThreads(int iThreads);

int createBunchOfFiles(int argc, char *argv[]);

int testCsvUtils();

int testTrim();

int testProper();

int testStringSize();

int createHugeFile(int argc, char *argv[]);

int test_getSequenceFileName();

int testRound();

int testHexStringRep();

int testLargeNumerics();

int test_translate_html();

int test_isalnum();

int generateBigCSV();

int testEncodeDecodeQueryString();

int test_write_string_to_file();

int createXMLBenchmarkSource();

int createHugeXml();

int createAccountBulkLoadFile(int argc, char *argv[]);

int main (int argc, char *argv[])
{
	return createAccountBulkLoadFile(argc, argv);

                          

                                     

                            
                                      

                                        

                           

                         

                                
                           

                              

                             

                      

                                     

                                     

                           
                     

                       

                         
                                         
                      
                                       
                        

                  
                          

                        

                            

                           

                        

                                 
                          

                          

                  
                               

                                    

                            

                          
                                

                              


                      
                        
                               

                          
                         

                            

                              

                             
                        

                       

                         

                           
                          
                    
                        
                       
                               
                          
                         
                        
                          
                     
                         
                          
                           
                       
                         
                          
                       
                            
                                        
                                   
                                

                               

                           

}

namespace {
	struct BulkLoadColumn {
		BulkLoadColumn(const string& _header) : header(_header) {}

		const string header;
		string value;
	};

	typedef vector<BulkLoadColumn*> BulkLoadColumnT;

	void AddCol(BulkLoadColumnT& columns, const string& header) {
		columns.push_back( new BulkLoadColumn(header) );
	}

	void SetVal(BulkLoadColumnT& columns, int i, const string& value) {
		columns.at(i)->value = value;
	}

	const char char_pool[] = "abcdefghijklmnopqrstuvxyz���1234567890ABCDEFGHIJKLMNOPQRSTUVXYZ";
	const char char_pool_[] = "abcdefghijklmnopqrstuvxyz1234567890ABCDEFGHIJKLMNOPQRSTUVXYZ";

	string getRandomString(int minlen, int maxlen) {
		return konga::getRandomString(minlen, maxlen, char_pool_);
	}

	string getRandomUtf8String(int minlen, int maxlen) {
		string str1( konga::getRandomString(minlen, maxlen, char_pool) ), str2;

		konga::ReplaceCharWithString(str1, str2, '�', "å");
		konga::ReplaceCharWithString(str2, str1, '�', "ä");
		konga::ReplaceCharWithString(str1, str2, '�', "ö");
		return str2;
	}

	void WriteHeaders(std::ofstream& of, BulkLoadColumnT& columns) {
		for( BulkLoadColumnT::const_iterator cit = columns.begin(); cit != columns.end(); ++cit ) {
			if ( cit != columns.begin() ) of << ",";
			const BulkLoadColumn* p = *cit;
			of << p->header;
		}
		of << std::endl;
	}

	void WriteRecord(std::ofstream& of, const BulkLoadColumnT& columns, long long& sizeBytes) {
		for( BulkLoadColumnT::const_iterator cit = columns.begin(); cit != columns.end(); ++cit ) {
			if ( cit != columns.begin() ) {
				of << ",";
				++sizeBytes;
			}
			const BulkLoadColumn* p = *cit;
			of << p->value;
			sizeBytes += p->value.size();
		}
		of << std::endl;
		of.flush();
		sizeBytes += 2;
	}
}

namespace {
void createAccountBulkLoadFileImpl(const string& fileName, const int sizeMb) {

	BulkLoadColumnT columns;

	AddCol(columns, "AccountName");	         
	AddCol(columns, "AccountNumber");
	AddCol(columns, "Site");
	AddCol(columns, "AnnualRevenue");
	AddCol(columns, "BillingCity");

	AddCol(columns, "BillingCountry");
	AddCol(columns, "BillingPostalCode");
	AddCol(columns, "BillingState");
	AddCol(columns, "BillingStreet");
	AddCol(columns, "Description");

	AddCol(columns, "Fax");
	AddCol(columns, "Industry");
	AddCol(columns, "Phone");
	AddCol(columns, "Type");
	AddCol(columns, "Website");

	AddCol(columns, "Active__c");
	AddCol(columns, "CustomerPriority__c");
	AddCol(columns, "ExternalId__c");
	AddCol(columns, "IsBulkLoaded__c");
	AddCol(columns, "SLA__c");

	AddCol(columns, "SLAExpirationDate__c");
	AddCol(columns, "SLASerialNumber__c");

	std::ofstream of(fileName.c_str(), ofstream::out | ofstream::binary);
	WriteHeaders(of, columns);

	long long maxSizeBytes = sizeMb;
	maxSizeBytes *= 1024 * 1024;
	long long sizeBytes = 0;
	while( true ) {
		SetVal(columns, 0, konga::GetGUIDString());
		SetVal(columns, 1, konga::GetGUIDString());
		SetVal(columns, 2, getRandomUtf8String(2,80));
		SetVal(columns, 3, toString(getRandomNumber(10000,10000000)));
		SetVal(columns, 4, konga::GetGUIDString());

		SetVal(columns, 5, konga::GetGUIDString());
		SetVal(columns, 6, "94501");
		SetVal(columns, 7, "CA");
		SetVal(columns, 8, konga::GetGUIDString());
		SetVal(columns, 9, "Bulkloaded");

		SetVal(columns, 10, "(555) 111 2222");
		SetVal(columns, 11, "Technology");
		SetVal(columns, 12, "(555) 333 4444");
		SetVal(columns, 13, "Prospect");
		SetVal(columns, 14, "http://" + konga::GetGUIDString());

		SetVal(columns, 15, "Yes");
		SetVal(columns, 16, "Medium");
		SetVal(columns, 17, konga::GetGUIDString());
		SetVal(columns, 18, "1");
		SetVal(columns, 19, "Silver");

		SetVal(columns, 20, "10/29/2012");	             
		SetVal(columns, 21, getRandomString(10, 10));

		WriteRecord(of, columns, sizeBytes);
		if ( sizeBytes > maxSizeBytes ) break;
	}

	of.close();
}
}

int createAccountBulkLoadFile(int argc, char *argv[]) {
	if ( argc < 3 ) {
		std::cerr << "Invalid arguments. Usage: fileName maxSizeMb" << std::endl;
		return 1;
	}
	
	if ( argv[1] == 0 ) {
		std::cerr << "Invalid arguments: No file name specified." << std::endl;
		return 1;
	}
	const string fileName(argv[1]);

	if ( argv[2] == 0 ) {
		std::cerr << "Invalid arguments: No file name specified." << std::endl;
		return 1;
	}
	const int sizeMb = atoi(argv[2]);
	if ( sizeMb < 1 ) {
		std::cerr << "Invalid size: " << sizeMb << std::endl;
	}

	try {
		createAccountBulkLoadFileImpl(fileName, sizeMb);
	}
	catch( const CrException& ex ) {
		std::cerr << ex.GetErrorString() << std::endl;
		return 2;
	}
	catch ( const std::exception& ex ) {
		std::cerr << ex.what() << std::endl;
		return 2;
	}
	catch( ... ) {
		std::cerr << "Unknown exception." << std::endl;
		return 2;
	}

	return 0;
}

int createHugeXml() {
	try {
		const string fileName("huge.xml");
		std::ofstream out(fileName.c_str(), ofstream::trunc | ofstream::out | ofstream::binary);
		if ( out.bad() ) return 1;

		out << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
		out << "<records xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"world-check.xsd\">\r\n";

		string snippet_fn("record.txt"), contents;
		konga::read_file_to_string(snippet_fn, contents, true);

		                    
		                  
		                    
		string tmp1, tmp2, ist;
		const int N = 400000;
		for(int i = 1; i <= N; ++i ) {
			ist = konga::toString(i);
			konga::ReplaceStringWithString(contents, tmp1, "Mohammed Sani", "Person Name " + ist);
			konga::ReplaceStringWithString(tmp1, tmp2, "ABACHA", "NAME " + ist);

			out << tmp2;
			tmp1.clear();
			tmp2.clear();
		}
		out << "</records>";

	} catch( const std::exception& ex ) {
		std::cout << ex.what() << std::endl;
		return 2;
	}
	catch( const CrException& ex ) {
		std::cout << ex.GetErrorString() << std::endl;
		return 3;
	}

	return 0;
}

namespace {
	void appendXMLElementStart(string& xml, const string& elementName) {
		xml.append("<").append(elementName).append(">\n");
	}

	void appendXMLElementEnd(string& xml, const string& elementName) {
		xml.append("</").append(elementName).append(">\n");
	}

	void appendXMLElementWithValue(string& xml, const string& elementName, const string& value) {
		xml.append("<").append(elementName).append(">").append(value).append("</").append(elementName).append(">\n");
	}

	void appendRecord(std::ofstream& out, int i) {
		string record;
		const string ist( konga::toString(i) );
		appendXMLElementStart(record, "record");

		appendXMLElementWithValue(record, "Id", GetGUIDString());
		appendXMLElementWithValue(record, "AccountId", GetGUIDString());
		appendXMLElementWithValue(record, "BirthDate", "1972-10-29");
		appendXMLElementWithValue(record, "Department", "Department" + ist);
		appendXMLElementWithValue(record, "Description", "Description" + ist);
		appendXMLElementWithValue(record, "Email", "email." + ist + "@etlforge.invalid");
		appendXMLElementWithValue(record, "External__id", GetGUIDString());
		const string firstName("FirstName-" + ist);
		appendXMLElementWithValue(record, "FirstName", firstName);
		appendXMLElementWithValue(record, "HomePhone", "(555) 555 5555");

		appendXMLElementWithValue(record, "IsDeleted", "false");
		const string lastName("LastName-" + ist);
		appendXMLElementWithValue(record, "LastName", lastName);
		appendXMLElementWithValue(record, "LeadSource", "Standard Test");
		appendXMLElementWithValue(record, "MailingCity", "Oakland");
		appendXMLElementWithValue(record, "MailingCountry", "USA");
		appendXMLElementWithValue(record, "MailingPostalCode", "54321");
		appendXMLElementWithValue(record, "MailingState", "California");
		appendXMLElementWithValue(record, "MailingStreet", ist + " West Main Street");

		appendXMLElementWithValue(record, "MobilePhone", "(555) 555 6666");
		appendXMLElementWithValue(record, "Name", firstName + " " + lastName);
		appendXMLElementWithValue(record, "OwnerId", GetGUIDString());
		appendXMLElementWithValue(record, "Phone", "(555) 555 7777");

		appendXMLElementEnd(record, "record");

		out << record;
	}

	void appendReference(std::ofstream& out, int i) {
		string record;
		const string ist( konga::toString(i) );
		appendXMLElementStart(record, "reference");

		double d = i, frac;
		d /= 2.0;
		const bool b = modf(d, &frac) == 0;
		appendXMLElementWithValue(record, "success", b ? "true" : "false");

		appendXMLElementEnd(record, "reference");

		out << record;
	}
}

int createXMLBenchmarkSource() {
	                
	                  
	                   
	const int nRecords = 1000;
	const string fileName("jb_benchmark-" + konga::toString(nRecords) + ".xml");

	std::ofstream out(fileName.c_str(), ofstream::trunc | ofstream::out);
	if ( out.bad() ) return 1;

	{
	string header("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
	appendXMLElementStart(header, "benchmark");
	appendXMLElementStart(header, "transaction");

	appendXMLElementStart(header, "response");
	
	header.append("<header version=\"1.1\" user=\"user1\" locator=\"GURKA\"><done>true</done></header>\n");
	appendXMLElementStart(header, "records");
	out << header;
	}

	for( int i = 0; i < nRecords; ++i ) {
		appendRecord(out, i);
	}

	string tmp;
	appendXMLElementEnd(tmp, "records");
	tmp.append("<footer time=\"1234\"><size>" + konga::toString(nRecords) + "</size></footer>\n");

	appendXMLElementEnd(tmp, "response");

	appendXMLElementStart(tmp, "request");
	out << tmp;
	tmp.clear();

	for( int i = 0; i < nRecords; ++i ) {
		appendReference(out, i);
	}

	appendXMLElementEnd(tmp, "request");

	appendXMLElementEnd(tmp, "transaction");
	appendXMLElementEnd(tmp, "benchmark");
	out << tmp;

	out.flush();
	out.close();

	return 0;
}

int test_write_string_to_file() {

	string st("\r\n");
	write_string_to_file("test_write_string_to_file.txt", st);

	return 0;
}

void testEncodeDecodeQueryString(const string& str_in) {
	string str(str_in);
	cout << str << endl;
	EncodeQueryString(str, false);
	cout << str << endl;
	DecodeQueryString(str);
	assert(str_in == str);

	str = str_in;
	EncodeQueryString(str, true);
	cout << str << endl;
	DecodeQueryString(str);
	assert(str_in == str);
}

int testEncodeDecodeQueryString() {
	testEncodeDecodeQueryString("AB-_.~");
	testEncodeDecodeQueryString("$-_.+!*'(),");
	testEncodeDecodeQueryString("$-_ .+!*'(),");

	return 0;
}

int generateBigCSV() {

	const string inFile("C:\\tmp\\Asset2.txt");
	const string outFile("C:\\tmp\\AssetBig.txt");

	FILE* fin = fopen(inFile.c_str(), "r");
	if(fin==0) return 1;
	FILE* fout = fopen(outFile.c_str(), "w+");
	if(fout==0) return 1;

	                     
	char buf[257];
	size_t count = 0;
	while( feof(fin) == 0 ) {
		count = fread(buf, 1, 256, fin);
		buf[count] = 0;
		fwrite(buf, 1, count, fout);
	}
	fclose(fin);

	const string a = "\"Tenant_";
	const string b = "\",\"Model_";
	const string c = "\",\"\",\"Hardware\",\"Hardware.Server\",\"Linux\",\"Company_10\",\"\",\"GL_Code_10\",\"Cost_Center_10\",\"Department_10\",\"Site_10\",\"Asset_10\",\"asset_alias_10\",\"alt_asset_id_10\",\"serial_number_10\",\"host_name_10\",\"dns_name_10\",\"mac_address_10\",\"asset_description_10\",\"1\",\"Spare\",\"8/20/2010\",\"\",\"\",\"01/01/2010\",\"01/01/2010\",\"01/01/2012\",\"01/01/2012\",\"01/01/2010\",\"01/01/2012\",\"01/01/2010\",\"01/01/2010\",\"Company_10\",\"Company_10\",\"Company_10\",\"Company_10\",\"Company_10\",\"Organization_10\",\"Organization_10\",\"Organization_10\",\"Location_10\",\"Floor_10\",\"Room_10\",\"CAB_10\",\"Shelf_10\",\"Slot_10\",\"ReqID_10\",\"PO_10\",\"\",\"\",\"\",\"\",\"\",\"2\",\"4\",\"GHz\",\"800\",\"GB\",\"8\",\"GB\",\"ip_address_10\",\"userid\",\"userid_10\",\"\",\"\",\"\",\"userid_10\",\"\",\"\",\"\",\"userid_10\",\"\",\"\",\"\",\"userid_10\",\"\",\"\",\"\",\"userid_10\",\"\",\"\",\"\",\"userid_10\",\"\",\"\",\"\",\"userid_10\",\"\",\"\",\"\",\"userid_10\",\"\",\"\",\"\",\"userid_10\",\"\",\"\",\"\",\"0\",\"1\",\"0\",\"0\"\n";

	for( int i = 11; i < 100000; i++ ) {
		fwrite(a.c_str(), 1, a.size(), fout);
		const string si = toString(i);
		fwrite(si.c_str(), 1, si.size(), fout);

		fwrite(b.c_str(), 1, b.size(), fout);
		fwrite(si.c_str(), 1, si.size(), fout);

		fwrite(c.c_str(), 1, c.size(), fout);
	}

	fclose(fout);

	return 0;
}

int test_isalnum() {

	for(int i = -1; i < 260; ++i ) {
		char c = (char)i;
		cout << i << " " << c << " "  << isalnum_safe(c) << endl;
	}

	isalnum_safe('�');

	return 0;
}

int test_file_size() {
	const string path("Z:\\Virtual Machines\\Winqa31 - Windows XP Pro SP3\\Windows XP Professional-cl1-000001-cl2.vmdk");
	try {
		long lErr;
		long long lSize = konga::GetFileSize(path, lErr);
		if ( FileExists(path) ) {
			cout << "yes" << endl;
		}
	}
	catch( const CrException& e ) {
		cout << e.GetErrorString() << endl;
	}

	return 0;
}

int test_translate_html() {

	string input("&#xFC;&lt;tag&gt;&#134;&#132;&#148;&lt;/tag&gt;#");

	cout << translate_html(input.c_str()) << endl;

	input = html_string("<>&\"'");
	if( "<>&\"'" != translate_html(input.c_str()) ) throw CrException("Error!");

	cout << translate_html(input.c_str()) << endl;

	cout << translate_html("J&#xFC;rgen") << endl;
	cout << translate_html("J&#xFC;") << endl;

	assert(require_translate_html("J&#xFC;rgen"));

                                                                                                                
	input = "Sex laxar i en laxask och Jorgen simmade till en �de � och �t pannkakor med lingonsylt.";

	Clock c1;
	const int N = 15000000;
	for( int i = 0; i < N; ++i ) {
		translate_html(input.c_str());
	}
	cout <<	c1.getSeconds() << endl;

            
                                 
                                     
    
                                   

	return 0;
}

int testLargeNumerics() {
                                             
                                          

	long l = std::numeric_limits<long>::max();
	string s = konga::toString(l);
	cout << s << endl;
	cout << s.size() << endl;
	cout << std::numeric_limits<long>::digits10 << endl;

	long long ll = std::numeric_limits<long long>::max();
	s = konga::toString(ll);
	cout << s << endl;
	cout << s.size() << endl;
	cout << std::numeric_limits<long long>::digits10 << endl;

	double d = std::numeric_limits<double>::max();
	s = konga::toString(d);
	cout << s << endl;
	cout << s.size() << endl;
	cout << std::numeric_limits<double>::digits10 << endl;

	return 0;
}

int testHexStringRep() {

	                                                      
	char* binary_in = new char[256];
	for( int i = 0; i < 256; ++i ) {
		binary_in[i] = i;
	}

	string s1 = getHexString(binary_in, 256);
	cout << s1 << endl;

	size_t len = 0;
	unsigned char* binary_out = fromHexString(s1, len);

	                                           
	BinaryData b1((const char*)binary_in, 256, false);
	BinaryData b2((const char*)binary_out, 256, false);
	assert(b1==b2);

	string s2 = getHexString((const char*)binary_out, 256);
	assert(s1==s2);
	cout << s2 << endl;

	return 0;
}

int testRound() {

	double d = 1234.5456;
	int r = roundDouble(d);

	return r;
}

int test_getSequenceFileName() {

	const string fileName("C:/Users/etlforge.SMO/AppData/Local/Temp/etlforge/OpId_107_67e71eca-e4d1-43b6-913b-c098e9f2bbec/b80f7ec7-21aa-4255-acc9-b851d8713071_target");
	cout << konga::GetSequenceFileName(fileName, "_", 0) << endl;

	cout << konga::RemoveFileExtension(fileName) << endl;

	cout << konga::GetFileNameWithTagInserted(fileName, "TAG");

	return 0;
}

int createHugeFile(int argc, char *argv[]) {
	if ( argc < 3 ) {
		cout << "Usage: " << argv[0] << " " << "template-file-name target-file-name multiplier" << endl;
		cout << "The template file will be repeated <multiplier> times and writtne to the target file name." << endl;
		return 1;
	}
	const string sTemplateFileName(argv[1]), sTargetFileName(argv[2]);
	string sTemplate;

	const long N = atol(argv[3]);

	read_file_to_string(sTemplateFileName, sTemplate, true);

	FILE* out = fopen(sTargetFileName.c_str(), "wb+");
	if ( out == 0 ) {
		cout << "Failed to open the file '" + sTargetFileName + "'." << endl;
		return 1;
	}

	size_t read = 0;
	for( int i = 0; i < N; ++i ) {
		if ( fwrite(sTemplate.c_str(), sTemplate.size(), 1, out) != 1 ) {
			cout << "Write error writing chunk number " << i << "!!" << endl;
			fclose(out);
			return 2;
		}
	}

	fclose(out);

	return 0;
}

int testStringSize() {
	string tmp1("a"), tmp2("a"), tmp3("a");

	string::size_type i = 0;
	try {
		while(true) {
			tmp1 += tmp1;
                  
                  
			cout << (i=tmp1.size()) << endl;
		}
	}
	catch( const std::exception& ex ) {
		cout << ex.what() << endl;
		cout << i << endl;
	}

	return 0;
}

void qprint(const string& s) {
	cout << "\"" << s << "\"" << endl;
}

int testProper() {
	string s = "bob dylan";
	ToProper(s);
	qprint(s);

	s = "arthur c. clarke";
	ToProper(s);
	qprint(s);

	s = "Peter o'toole";
	ToProper(s);
	qprint(s);

	s = "paul mccartney";
	ToProper(s);
	qprint(s);

	s = "My MC";
	ToProper(s);
	qprint(s);

	s = "My MCM";
	ToProper(s);
	qprint(s);

	return 0;
}

int testTrim() {
	string s1 = " \t string \t";
	string s2 = trim(s1);
	qprint(s2);

	s2 = ltrim(s1);
	qprint(s2);

	s2 = rtrim(s1);
	qprint(s2);

	trim(rtrim(ltrim("")));	                                 

	s1 = "/* This is a comment  */";
	s2 = trim_chars(s1, "/* ");
	qprint(s2);

	s1 = "// This is a comment";
	s2 = trim_chars(s1, "/ ");
	qprint(s2);

	s1 = "// This is a comment / this is not";
	s2 = trim_chars(s1, "not ");
	qprint(s2);

	s1 = "// This is a comment/";
	s2 = ltrim_chars(s1, "/ ");
	qprint(s2);

	return 0;
}

int testCsvUtils() {
	string s("\"one\"two\"");
                     
                         
                     

	s = "\r\r";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="one, two,three";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="one\"two,three";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="one\"two,three\r\n";
	qualifyCsvString(s, ',');
	cout << s << endl;

	KString k="one\"two,three\r\n";
	qualifyCsvStringK(k, ',');
	cout << k << endl;
	assert(k==s);

	s="one\"two,three\r\n\r";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="\"quoted string\"";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="\\r";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="\"";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="abc\"";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="\"abc";
	qualifyCsvString(s, ',');
	cout << s << endl;

	s="";
	qualifyCsvString(s, ',');
	cout << s << endl;

	return 0;
}

namespace {
	void CreateFileWithSize(const string& fileName, int size) {
		string s;
		for( int i = 0; i < size; ++i ) {
			s += toString(i%10);
		}
		write_string_to_file(fileName, s);
	}
}

                                                     
                                                    
int createBunchOfFiles(int argc, char *argv[]) {
	if ( argc < 3 )
		return 1;

	string base_dir(argv[1]);
	AppendPathSeparator(base_dir);

	const int nFiles = atoi(argv[2]);

	for( int i = 0; i < nFiles; i+=3 ) {
		CreateFileWithSize(base_dir + "file_" + toString(i) + ".txt", 1000);
		CreateFileWithSize(base_dir + "file_" + toString(i+1) + ".txt", 2000);
		CreateFileWithSize(base_dir + "file_" + toString(i+2) + ".txt", 5000);
	}

	return 0;
}

int testClock() {
	Clock c;

	MilliSleep(3000);

	cout << c.getSeconds() << endl;
	return 0;
}

namespace {

	void rw_mem() {
		const int size = 10000;
		int v[10000];
		for(int i = 0; i < size; ++i) {
			v[i] = i;
		}

		for( int j = 0; j < 1000000; ++j ) {
			for( int i = 0; i < size; ++i ) {
				if( v[i] < 1 ) {
					v[i] = size;
				}
				else {
					v[i] = v[i] - 1;
				}
			}
		}
	}

	void alloc_dealloc() {
		for( int i = 0; i < 5000000; ++i ) {
			string tmp("some string");
			tmp += toString(i);
			string other = tmp + "other";
		}
	}

	class ThreadJob {
	public:
		ThreadJob(int i) : m_i(i) {}
		void operator()() {
			cout << "Starting thread " << m_i << endl;

			konga::Clock c;

			alloc_dealloc();
              

			cout << "End thread " << m_i << ", time = " << c.getSeconds() << endl;
		}

	private:
		int m_i;
	};
}

int testThreads(int iThreads) {
	if ( iThreads < 1 )
		return 1;

	konga::Clock c;
	vector<boost::thread*> threads;
	for( int i = 0; i < iThreads; ++i ) {
		boost::thread* pThread = new boost::thread(ThreadJob(i));
		threads.push_back(pThread);
	}

	for( vector<boost::thread*>::const_iterator cit = threads.begin(); cit != threads.end(); ++cit ) {
		boost::thread* pThread = *cit;
		pThread->join();
	}

	cout << "All done, time = " << c.getSeconds() << endl;
	return 0;
}

int stringTests() {
	string s;
	const char* p = s.c_str();

	s.erase();
	p = s.c_str();

	return 0;
}

int testUrlEscape() {

	string url("http://unsafe- <>\"%{}|\\^~[]`#/safe-$-_.+!*'(),/reserved-;/?:@=&");

	UrlEscape(url);
	cout << url << endl;

	string url2("http://unsafe- <>\"%{}|\\^~[]`#/safe-$-_.+!*'(),/reserved-;/?:@=&");
	UrlEscape(url2, true);
	cout << url2 << endl;

	const string url_("http://unsafe- <>\"%{}|\\^~[]`#/safe-$-_.+!*'(),/reserved-;/?:@=&");
	url = url_;
	UrlEncode(url);
	cout << url << endl;

	url = url_;
	UrlEncode(url, true, false);
	cout << url << endl;

	url = url_;
	UrlEncode(url, false, false);
	cout << url << endl;

	url = url_;
	UrlEncode(url, false, true);
	cout << url << endl;

	return 0;
}


class C {
public:
	C(const string& str = "" ) { m_str=str; }
	~C() {}

	string str() const { return m_str; }

private:
	string m_str;
};


int testPadding() {

	cout << LPadChar("1234567", '0', 10) << endl;
	cout << LPadChar("1234567", '0', 7) << endl;
	cout << LPadChar("1234567", '0', 6) << endl;

	cout << RPadChar("1234567", '0', 10) << endl;
	cout << RPadChar("1234567", '0', 7) << endl;
	cout << RPadChar("1234567", '0', 6) << endl;

	return 0;
}

int testRemoveLines() {

                                               
                                           
                                                
	const string fileName("/tmp/70332214-937f-4531-bbdd-7a920e567b7c");

	RemoveFirstAndLastLines(fileName, 4, 6);

	return 0;
}

int testRunProc() {
	try {
		ProcessIdT pid = konga::RunProc("/bin/bash", "/home/emanuel/tmp/forkme.sh",
											"/home/emanuel/tmp", "/home/emanuel/tmp/out",
											"/hhome/emanuel/tmp/err");
			cout << "called RunProc" << endl;
			WaitForProc(pid);
			cout << "Finished waiting for proc" << endl;
	} catch ( const CrException& ex  ) {
		cout << "Got exception" << endl;
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testDeleteDir(const string& sDir) {
                               
                           
                                                             
	try {
		AutoCleanupTempDir adopt_sDir(sDir);
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}

int testTokenizeString() {
	const string st("s1;s2;s3");
	vector<string> v = SplitString(st, ";");
	cout << v.size() << endl;
	return 0;
}

int testAsciiNull() {

	char* ch = "String1\001string2";

	cout << ch << endl;

	return 0;
}


namespace {
	const char illegal[] = {
		'\001', '\002','\003','\004','\005','\006', '\007','\010',
		'\013','\016','\017','\020','\021','\022','\023','\024',
		'\025','\026','\027','\030','\031','\032','\033','\034',
		'\035','\036', '\037'
	};
}

int generateDates() {
	               
	CrDateTime date(2004, 1, 1, 0, 0, 0);

	try {
		ofstream f("C:\\tmp\\dates.txt", ios_base::out);

		CrTimeDuration oneDayAndSecs(24, 0, 237);
		for( int i=0; i < 365; ++i ) {
			f << "\"" << date.toString("yyyy-mm-dd HH:MM:SS") << "\"" << "," << endl;
			date += oneDayAndSecs;
		}
		f.close();
	}
	catch( const std::exception& ex ) {
		cout << "Failed to write to file. " << ex.what() << endl;
		return 1;
	}

	return 0;
}

int testGetTmpFileName() {
	string stTmp = getTmpFileName();
	stTmp = getTmpFileName("prefix");
	cout << stTmp << endl;
	stTmp = getTmpFileName("", getSystemTmpDir());
	cout << stTmp << endl;
	stTmp = getTmpFileName("prefix", getSystemTmpDir());
	cout << stTmp << endl;

	return 0;
}

int test1() {
	const char num[] = "idjj";

	char* pChar = 0;
	double d = strtod(num, &pChar);
	if ( d == 0 && pChar == num ) {
		cout << "no conversion was performed" << endl;
	}
	else if ( *pChar != 0 ) {
		cout << "This part of the string could not be converted: " << pChar << endl;
	}
	return 0;
}

int test2() {
	cout << sizeof(long*) << endl;
	cout << sizeof(int) << endl;
	cout << sizeof(long) << endl;
	cout << sizeof(long long) << endl;

	long* lp = (long*)0xFFFFFFFF;
	unsigned int i = (unsigned int)lp;
	cout << lp << endl;
	cout << i << endl;
	cout << (long*)i << endl;
	return 0;
}

int testReplaceCharWithChar() {
	const string orig("ABCDEFABCDEFA");
	string st("ABCDEFABCDEFA");

	cout << st << endl;
	                         
	if ( ReplaceCharWithChar(st, 'A', 'A') != 3 ) {
		cerr << "Should return 3" << endl;
	}
	else if ( st != orig ) {
		cerr << "Operation should have no effect!" << endl;
	}
	cout << st << endl;

	                       
	if ( ReplaceCharWithChar(st, 'X', 'A') != 0 ) {
		cerr << "Should return 0" << endl;
	}
	else if ( st != orig ) {
		cerr << "Operation should have no effect!" << endl;
	}
	cout << st << endl;

	                              
	if ( ReplaceCharWithChar(st, 'A', 'X') != 3 ) {
		cerr << "Should change 3 characters" << endl;
	}
	else if ( st == orig ) {
		cerr << "String should be changed!!" << endl;
	}
	cout << st << endl;
	return 0;
}

int writeAllAscii() {

	FILE* f = fopen("C:\\tmp\\allAscii.txt", "w+");
	if ( fopen == 0 )
		return 1;
	for( unsigned char c = 1; c < 0xff; ++c ) {
		fwrite(&c, 1, 1, f);
	}
	fclose(f);

	f = fopen("C:\\tmp\\allAscii.txt", "r");
	if ( f == 0 )
		return 2;

	char c = 0;
	while( fread(&c, 1, 1, f) ) {
		cout << (int)c << " ";
	}
	return 0;
}

int testKStringRemove() {

	KString s("\"abc\".\"edf\"");
	cout << s << endl;
	cout << s.Remove('"') << endl;
	cout << s << endl;

	return 0;
}

int testAutoFileCleanup() {
	string stFile1("C:\\tmp\\files\\file1");
	string stFile2("C:\\tmp\\files\\file2");

	AutoDeleteFileSet set;
	set.AddFile(stFile1);
	set.AddFile(stFile1);	                                           

	set.AddFile(stFile2);

	set.RemoveFile(stFile1);
	set.AddFile(stFile1);

                                                            
	return 0;
}

int testStuff() {
	string str("bcd");
	str.insert(str.begin(), 'a');
                      

	return 0;
}


class FileWriterAndCounter : public konga::FileAction {
public:
	FileWriterAndCounter(void) : m_iCount(0) {}
	~FileWriterAndCounter(void) {}

	void doAction(const std::string& stFilePath) {
		cout << stFilePath << endl;
		++m_iCount;
	}

	long GetCount() const { return m_iCount; }

private:
	long m_iCount;
};

int testFileTraversion() {

	FileWriterAndCounter action;
	AllFilesMatcher matchAll;
	FilesTraverser traverser(action, matchAll);

	boost::filesystem::path::default_name_check( boost::filesystem::no_check );
	try {
		traverser.traverseDirectory("C:\\tmp", true);
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	cout << "Found " << action.GetCount() << " files." << endl;

	return 0;
}

int testDateUtils() {

	CrDateTime now;
	cout << now.toString() << endl;
	cout << now.getDateString() << endl;
	cout << now.getTimeString() << endl;

	time_t now_t = time(0);
	cout << ctime(&now_t) << endl;

	now.SetDateTime(now_t);
	cout << now.toString() << endl;

	cout << CrDateTime::GetCurrentUtcTime().toString() << endl;


	CrDateTime then(2006, 2,12,12,1,30);
	cout << then.toString() << endl;

	CrDateTime utc(CrDateTime::GetCurrentUtcTime());
	cout << utc.toString() << endl;
	cout << CrDateTime::UtcToLocal(utc).toString() << endl;

	return 0;
}

int testMapUtils() {

	typedef std::map<long, std::string> LongStringMapT;
	LongStringMapT m;
	string str1("String 1");
	string str2("String 2");

	AddOrUpdate<LongStringMapT, long, string>(m, 10, str1);
	AddOrUpdate<LongStringMapT, long, string>(m, 10, str2);
	AddOrUpdate<LongStringMapT, long, string>(m, 9, str1);
	AddOrUpdate<LongStringMapT, long, string>(m, 10, str2);
	AddOrUpdate<LongStringMapT, long, string>(m, 11, str2);
	AddOrUpdate<LongStringMapT, long, string>(m,9, str1);

	return 0;
}


int testStringUtils() {
	const string str("Some String");
	char* copy = strsave(str.c_str());
	delete[] copy;
                               

	copy = strsave(0, 2);	         

	return 0;
}


bool GetLevelInfo(KString& sEndPath, KString &sLevelName, int &iInstanceNumber)
{
	iInstanceNumber = 0;
	StringTokenizer tokCur(sEndPath,"$.");
	const char * CurTok;

	if (false == tokCur.NextToken())
		false;

	CurTok = tokCur.token();

	switch(CurTok[0])
	{
		case '$':
		case '.':
			break;
		default:
			KString sLevelStr = CurTok;
			iInstanceNumber = 0;
			int InstPos = sLevelStr.Find('#');
			if (-1 != InstPos)
			{
				sLevelName = sLevelStr.Left(InstPos);
				KString sInstNum = sLevelStr.Right(sLevelStr.GetLength() - InstPos - 1);
				sInstNum.Replace('_', '-');
				if (0 != sInstNum.GetLength())
				{
					iInstanceNumber = atoi(sInstNum);
				}
			}
			else
				sLevelName = sLevelStr;

			KString sMsg;
			sMsg.Format("%s %d", (const char*)sLevelName, iInstanceNumber);
			               

			                                                                                            
			                                       
			sEndPath.Delete(0, sLevelStr.GetLength()+1);
			break;
	}

	return true;
}

int testGetLevelInfo() {
	KString sEndPath("");
	KString sLevelName("a");
	int i = 0;
	GetLevelInfo(sEndPath, sLevelName, i);
	return 0;
}

int testBinaryData() {
	string stData("Some data for testing");
	BinaryData d1(stData);

	unsigned long lSize = 20000;
	char* pData = new char[lSize];
	for( int i = 0; i< lSize; ++i ) {
		pData[i] = 0;
	}
	for( int i = 0; i < 10; ++i ) {
		pData[i] = i+128;
	}
	BinaryData d2(pData, lSize, false);

	BinaryData d3 = d1 + d2;

	d1 += d2;

	d1.OptimizeMemory();

	d1.Size(10);
	d1.Size(30);

	                 
	d1 = d1;

	                 
	d1 += d1;


	BinaryData d4("Hubba");
	d4 += 0;
	d4 += 123456789;
	unsigned long pos = 9;
	const char* peeking = d4.GetPointerToData();
	cout << peeking << endl;
	cout << d4.GetUL(5) << endl;
	cout << d4.GetUL(pos) << endl;

	cout << d4[4] << endl;

	char buf[sizeof(unsigned long)];
	memcpy(buf, peeking+5, sizeof(unsigned long));
	cout << *reinterpret_cast<const unsigned long*>(buf);
	cout << *reinterpret_cast<const unsigned long*>(peeking+5);

                                                   
                                                            

                     

	BinaryData fileContents;
	konga::ReadBinaryDataFromFile(fileContents, "C:\\Emanuel\\ETLForge\\Documents\\ETLForge SAP Integration.doc");

	return 0;
}

int testVersion() {
	Version v1(0,9,0);
	Version v2(0,9,1);
	if ( v1 == v2 ) {
		cout << "error 1!" << endl;
		return 1;
	}

	if ( v1 != v1 || v2 != v2 ) {
		cout << "error 2" << endl;
		return 2;
	}

	if ( v1 != v2 ) {
		               
	}
	else {
		cout << "error 3!" << endl;
		return 3;
	}

	if ( v1 < v2 ) {
		               
	}
	else {
		cout << "error 4!" << endl;
		return 4;

	}

	if ( v1 > v2 ) {
		cout << "error 5!" << endl;
		return 5;
	}

	if ( v1 < v1 || v1 > v1 ) {
		cout << "error 6!" << endl;
		return 6;
	}

	if ( v1 <= v2 && v2 >= v1 ) {
		     
	}
	else {
		cout << "error 7!" << endl;
		return 7;
	}

	if ( v1 <= v1 && v2 >= v2) {
		     
	}
	else {
		cout << "error 8!" << endl;
		return 8;
	}

	cout << v1.GetLongVersionString() << endl;
	cout << v2.GetLongVersionString() << endl;

	Version v3("0.9.0");
	if ( v1 != v3 ) {
		cout << "error 9!" << v3.GetLongVersionString() << endl;
		return 9;
	}

	Version v4("12.23");
	Version v5("123");
	Version v7("12.23.");
	cout << v7.GetLongVersionString() << endl;

	Version v10("01.09.00001");
	cout << v10.GetLongVersionString() << endl;

	if ( v7 < v10 ) {
		cout << "error 10!" << endl;
		return 10;
	}
	else if ( v4 == v5 ) {
		cout << "error 11!" << endl;
		return 11;
	}

	Version v0(0);
	cout << v0.GetLongVersionString() << endl;

	Version vv1(1,1,1,23,"-beta1");
	Version vv2(1,1,1,26,"-beta2");
	if ( vv1 == vv2 ) {
		cout << "Versions are not equal!" << endl;
		return 100;
	}
	else if ( vv1 > vv2 || vv1 >= vv2 || vv2 < vv1 || vv2 <= vv1 ) {
		cout << "Some conversion operator is incorrect!! << endl" << endl;
	}

	cout << vv1.GetVersionString(1) << endl;
	cout << vv1.GetVersionString(2) << endl;
	cout << vv1.GetVersionString(3) << endl;
	cout << vv1.GetVersionString(4) << endl;

	Version vv3("1.2.3.123-beta2");
	Version vv4("1.2.3.134-rc2");
	if ( vv3 == vv4 ) {
		cout << "Versions are not equal!" << endl;
		return 100;
	}
	else if ( vv3 > vv4 || vv3 >= vv4 || vv4 < vv3 || vv4 <= vv3 ) {
		cout << "Some conversion operator is incorrect!! << endl" << endl;
	}

	cout << vv4.GetVersionString(1) << endl;
	cout << vv4.GetVersionString(2) << endl;
	cout << vv4.GetVersionString(3) << endl;
	cout << vv4.GetVersionString(4) << endl;

	try {
		Version vv5("1.2.3.4.5-beta12");	                          
		cout << "Should be an error!!" << endl;
		return -1;
	}
	catch ( const CrException& ex ) {
		cout << "This error should happen: " << ex.GetErrorString() << endl;
	}

	                                               
	try {
		Version pgsqlVersion("08.01.0200 PostgreSQL 8.0.3 on i686-pc-mingw32, compiled by GCC gcc.exe (GCC) 3.4.2 (min");
		cout << pgsqlVersion.GetLongVersionString() << endl;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
	}

	                                                                            
	for( int iMajor = 0; iMajor <= 10; ++iMajor ) {
		for( int iMinor = 0; iMinor <= 9; ++iMinor ) {
			for( int iPatch = 0; iPatch <= 99; ++iPatch ) {
				for ( int iBuild = 0; iBuild <= 999; ++iBuild ) {

					Version v(iMajor, iMinor, iPatch, iBuild, "-beta");
					if ( v.GetMajor() != iMajor ||
						v.GetMinor() != iMinor ||
						v.GetPatchLevel() != iPatch ) {
							cout << "Error 12: This version should work!! " << v.GetLongVersionString() << " "
								<< v.GetVersionNumber() << endl;
							return 12;
					}

					if ( iBuild == 0 ) {
						Version v2(v.GetVersionNumber());
						if ( v2.GetMajor() != iMajor ||
							v2.GetMinor() != iMinor ||
							v2.GetPatchLevel() != iPatch ) {
								cout << "Error 14: This version should work!! " << v2.GetLongVersionString() << " "
									<< v3.GetVersionNumber() << endl;
								return 14;
						}
						if ( v != v2 || v < v2 || v > v2 ) {
							cout << "Error 15: This version should work! " << v2.GetLongVersionString() << " "
								<< v2.GetVersionNumber() << endl;
							return 15;
						}
					}

					Version v3(v.GetLongVersionString());
					if ( v3.GetMajor() != iMajor ||
						v3.GetMinor() != iMinor ||
						v3.GetPatchLevel() != iPatch ) {
							cout << "Error 16: This version should work!! " << v3.GetLongVersionString() << " "
								<< v3.GetVersionNumber() << endl;
							return 16;
					}
					if ( v != v3 || v < v3 || v > v3 ) {
						cout << "Error 17: This version should work! " << v3.GetLongVersionString() << " "
							<< v3.GetVersionNumber() << endl;
						return 17;
					}
				}
			}
		}
	}

	return 0;
}

int testtoLong() {
	try {
		string st("0090");
		long l = toLong(st);
		cout << st << " -> " << l << endl;
	}
	catch ( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
	}

	return 0;
}

int testSettings() {
	long long l = 1000;
	double d = GetOptionAsDouble("konga/Debug", "Double", 1);
	cout << d << endl;
	return 0;
}

int testDateString() {
	using namespace boost::posix_time;
	using namespace boost::gregorian;

	ptime now = second_clock::local_time();
	stringstream sDate;
	date d = now.date();
	sDate << d.year() << '-' << d.month().as_short_string() << '-' << d.day().as_number();
	cout << sDate.str() << endl;

	time_duration t = now.time_of_day();
	stringstream sTime;
	sTime << setfill('0') << setw(2) << t.hours();
	sTime << setfill('0') << setw(2) << t.minutes();
	sTime << setfill('0') << setw(2) << t.seconds();
	cout << sTime.str() << endl;
	return 0;
}

int testkCopyFile() {
	try {
		kCreateDirectory("dir");
		string stFileName("dir/file");
		string stTmpFileName = getTmpFileName();
		ofstream outfile;
		outfile.open(stTmpFileName.c_str());
		string st("to output");
		int len = st.size();
		for( int i = 0; i < len; ++i ) {
			outfile << st[i];
		}
		if ( outfile.fail() )
			cout << "Failed to write contents to file \"" << stFileName
				<< "\". Path does not exist or permission is denied.";
		outfile.close();

		kCopyFile(stTmpFileName, stFileName);
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}

int testreplace_in_file() {
	const string stFile("C:\\tmp\\insert_data.sql");
	replace_in_file(stFile.c_str(), "[KONGA_HOME]", "C:\\\\Program Files\\\\Konga\\\\");
	replace_in_file(stFile.c_str(), "\\\\", "/");
	return 0;
}


                                                        
                                                               
class DummyFileLineReadWriter : public FileLineReadWriter {
public:
	DummyFileLineReadWriter(void) : FileLineReadWriter(getSystemTmpDir()),
		m_iCount(0), m_eof("End of file reached!!") {}
	virtual ~DummyFileLineReadWriter(void) {}

protected:
	const char* ChangeLine(const char* line) {
		if ( line ) {
			cout << line;
			++m_iCount;
		}
		else {
			cout << "EOF reached!!" << endl;
			return m_eof;
		}
		return line;
	}

private:
	int m_iCount;
	const char* m_eof;
};

int testFileLineReadWriter() {
	try {
		const string stFile("/tmp/konga.conf");
		DummyFileLineReadWriter dummy;
		dummy.Read(stFile.c_str());
		return 0;
	} catch ( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
}

int testReplaceStringWithString() {
	const string stFind("[KONGA_HOME]"), stReplace("\\\\home\\\\emanuel\\\\konga");
	const string stOrig(stFind + " should be replaced with " + stReplace + " in this string. "
						+ stFind + " in the middle and then at the end: " + stFind);
	string stOut;
	ReplaceStringWithString(stOrig, stOut, stFind, stReplace);
	cout << stOut << endl;
	return 0;
}

int testPathQueries() {
	string stPath("/usr/local/test.txt");
	cout << "File: " << stPath << endl;
	cout << GetBranchPath(stPath) << endl;
	cout << GetFileExtension(stPath) << endl;

	stPath = "test.txt";
	cout << "File: " << stPath << endl;
	cout << GetBranchPath(stPath) << endl;
	cout << GetFileExtension(stPath) << endl;

	stPath = "/usr/local/test.";
	cout << "File: " << stPath << endl;
	cout << GetBranchPath(stPath) << endl;
	cout << GetFileExtension(stPath) << endl;

	return 0;
}

int testTmpDir() {

	string stTmpDir = getTmpDir();
	cout << stTmpDir << endl;

	return 0;
}

int testUpperLower() {
	string st("MiXeDcAse%$&!");
	string copy(st);
	ToUpper(copy);
	cout << copy << endl;

	copy = st;
	ToLower(copy);
	cout << copy << endl;

	return 0;
}

int testWriteFile() {
	string stInFile("/tmp/readfile");
	string stOutFile("/tmp/writtenfile");
	string stContents;
	try {
		read_file_to_string(stInFile, stContents);
		write_string_to_file(stOutFile, stContents);
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
	}

	return 0;
}

int testReadFile() {

	string stFile("/tmp/readfile");
	string stContents;
	try {
		read_file_to_string(stFile, stContents);
	}
	catch( const CrException& e ) {
		cout << e.GetErrorString() << endl;
		return -1;
	}
	cout << stContents;
	return 0;
}

int testGUID() {
	int N = 100;
	for ( int i = 0; i < N; ++i) {
		string guid = GetGUIDString();
		cout << guid << endl;
		assert(IsGUIDString(guid));
	}

	string guid(GetGUIDString());
	guid.at(10)='g';
	assert(!IsGUIDString(guid));
	guid.at(10)='0';
	guid.at(8)='0';
	assert(!IsGUIDString(guid));
	guid.at(8)='-';
	guid += "0";
	assert(!IsGUIDString(guid));
	guid.erase(guid.end()-1);
	assert(IsGUIDString(guid));
	return 0;
}

int testFileUtils() {

	string stDir("/tmp");
	string stFile("/tmp/test.txt");
	string stLink("/tmp/linktest.txt");

	if ( !IsDirectory(stDir) ) {
		cout << "Oooops, this IS a directory, promise!" << endl;
		return -1;
	}

	if ( IsDirectory(stFile) ) {
		cout << "Oooops, this IS a file, promise!" << endl;
		return -1;
	}

	if ( IsDirectory(stFile) ) {
		cout << "Oooops, this is NOT a directory, promise!" << endl;
		return -1;
	}

	string stFileCopy(stFile+"copy");
	kMoveFile(stFile, stFileCopy);
	kMoveFile(stFileCopy, stFile);
	kMoveFile(stFile, stFile);	                            
	kCopyFile(stFile, stFileCopy);
	kMoveFile(stFile, stFileCopy);	                         
	kDeleteFile(stFileCopy);

	cout << "testFileUtils succeeded." << endl;
	return 0;
}

int testToString() {

	int i = std::numeric_limits<int>::max();
	cout << toString(i) << endl;

	i = 123;
	cout << toString(i) << endl;

	long l = std::numeric_limits<long>::max();
	cout << toString(l) << endl;

	l = 123;
	cout << toString(l) << endl;

	long long ll = std::numeric_limits<long long>::max();
	cout << toString(ll) << endl;

	double d = 1.234567890123456789;
	cout << toString(d) << endl;

	d = std::numeric_limits<double>::max();
	cout << toString(d) << endl;

	d = 12.34;
	cout << toString(d) << endl;

	float f = (float)1.23456753;
	cout << toString(f) << endl;

	f = std::numeric_limits<float>::max();
	cout << toString(f) << endl;

	f = (float)12.34;
	cout << toString(f) << endl;


	double d1 = 1.0;
	  
                     
                                               
                                 
                                
                
                              
  
   

	d1 = 1999;
	double d2 = 100.0;

	double d3 = d1/d2;

	cout << d3 << endl;
	cout << toString<double>(d3, std::numeric_limits<double>::digits10+1) << endl;

	string str("just a string");
	str = toString(str);
	str = toString(str.c_str());

	cout << toString(0) << endl;
	const char* chp = 0;
	cout << toString(chp) << endl;

	return 0;
}


void printDate(const CrDateTime& crdate) {
	cout << crdate.GetMonth() << "/" << crdate.GetDay() << "/" << crdate.GetYear() << " " << crdate.GetHour() << ":" << crdate.GetMinute() << ":" << crdate.GetSecond() << endl;
}

int testTimeUtils() {
	CrDateTime crdate = CrDateTime::GetCurrentTime();
	printDate(crdate);

	CrDateTime crdate2(crdate);
	if ( crdate != crdate2 ) {
		cout << "Error 1" << endl;
	}
	printDate(crdate2);

	time_t t_local = crdate2.GetTime();
	cout << t_local << " Local = " << ctime(&t_local);

	CrDateTime crdate3(t_local);
	if ( crdate2 != crdate3 ) {
		cout << "Error 2: " << crdate3.toString();
	}
	printDate(crdate3);

	printDate(CrDateTime::GetCurrentUtcTime());

	return 0;
}


int testUrl() {
	try {
		Url url;

		string stUrl("http://www.cplusplus.com:8080/wsdl.php?myWSDL");
		url.Parse(stUrl);

		cout << "Protocol: " << url.getProtocol() << endl;
		cout << "Host: " << url.getHost() << endl;
		cout << "Path: " << url.getPath() << endl;
		cout << "Port: " << url.getPort() << endl;
		cout << "Query String: " << url.getQueryString() << endl;
		cout << "Fragment: " << url.getFragment() << endl;
		cout << "Url: " << url.getParsedUrl() << endl;

		if ( !IsUrl(stUrl) ) {
			cout << "This is a URL!!" << endl;
			return 1;
		}

		stUrl = "\\\\linuxdoc\\target\\emanuel\\SimpleText";
		if ( IsUrl(stUrl) ) {
			cout << stUrl << " is is not a URL!!" << endl;
			return 2;
		}

		stUrl = "C:\\dev\\cpp\\Boost";
		if ( IsUrl(stUrl) ) {
			cout << stUrl << " is is not a URL!!" << endl;
			return 3;
		}

		stUrl = "C:\\dev\\cpp\\Boost Does Not Exits";
		if ( IsUrl(stUrl) ) {
			cout << stUrl << " is is not a URL!!" << endl;
			return 4;
		}

		stUrl = "/usr/local/konga/file.txt";
		if ( IsUrl(stUrl) ) {
			cout << stUrl << " is is not a URL!!" << endl;
			return 4;
		}

		stUrl = "somefile.xsd";
		if ( IsUrl(stUrl) ) {
			cout << stUrl << " is is not a URL!!" << endl;
			return 4;
		}

	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString().c_str() << endl;
		return -1;
	}
	return 0;
}

int testAutoPtr() {

	std::string* pStr1 = new std::string("Hello");
	AutoPtrDelete<std::string> auto1(pStr1);

	return 0;
}
