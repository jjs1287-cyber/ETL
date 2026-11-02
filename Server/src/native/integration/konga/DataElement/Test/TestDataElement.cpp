/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <limits>
#include <string>
#include <iostream>

#include <konga/Utils/util.h>

#include <konga/DataElement/DataElement.h>

#ifdef max
#undef max
#endif
#ifdef min
#undef min
#endif

using namespace konga;
using namespace std;

int generalTest();

int longTest();

int doubleTest();

int convTest();

int testIsInt();

int unitTest();

int main(int argc, char* argv[])
{
	return unitTest();

                      
                        
                     
                       
                     
}

int unitTest() {
	DataElement de1(DataElement::t_string, "Hello");
	de1.appendStringValue(" World");

	DataElement de2(DataElement::t_string, "Hello World");

	if ( !(de1 == de2) ) {
		std::cerr << "*** Append or EQ is broken!" << std::endl;
	}

	for( int i = 0; i < 100; ++i ) {
		const string str1(GetGUIDString());
		const string str2(GetGUIDString());
		de1.setValue(str1.c_str());
		de2.setValue(str2.c_str());

		if( (de1<de2) != (str1<str2) ) {
			std::cerr << "*** setValue or LT is broken!" << std::endl;
		}
	}

	DataElement nullDE(DataElement::t_string);
	nullDE.appendStringValue("append to null");
	if ( !(nullDE == DataElement(DataElement::t_string, "append to null")) ) {
		std::cerr << "*** appending to null is broken!" << std::endl;
	}

	de1.set(&nullDE);

	if ( !(de1==nullDE) ) {
		std::cerr << "*** Set is broken!" << std::endl;
	}

	std::cout << "Size of char*:" << sizeof(char*) << std::endl;
	std::cout << "Size of new stringrep:" << (sizeof(std::string*) + sizeof(std::string)) << std::endl; 
	std::cout << "Size of DataElement: " << sizeof(konga::DataElement) << std::endl;
	std::cout << "Size of KString: " << sizeof(KString) << std::endl;
	std::cout << "Size of std::string: " << sizeof(std::string) << std::endl;
	std::cout << "Size of konga::xml::DOMNodePtr: " <<sizeof(konga::xml::DOMNodePtr) << std::endl;

	return 0;
}

int testIsInt() {
	DataElement de(DataElement::t_float, 1.23);
	cout << konga::RepresentsAnInteger(de) << endl;

	de.setValue("1.0");
	cout << konga::RepresentsAnInteger(de) << endl;

	de.setType(DataElement::t_string);
	de.setValue("1.23");
	cout << konga::RepresentsAnInteger(de) << endl;

	de.setValue("1.0");
	cout << konga::RepresentsAnInteger(de) << endl;

	de.setValue("1a");
	cout << konga::RepresentsAnInteger(de) << endl;

	de.setValue("1");
	cout << konga::RepresentsAnInteger(de) << endl;

	return 0;
}

int convTest() {

                                                       
	DataElement de1(DataElement::t_float, "2.38564");
	double epsilon = std::numeric_limits<float>::epsilon();
	int err = 0;
	cout << de1.GetFloat(err) << endl;
	double d = de1.GetDouble(err);
	d += epsilon;
	cout << d << endl;

	KString str = de1.GetString(err);
	DataElement de2(DataElement::t_double, (const char*)str);
	cout << de2.GetDouble(err) << endl;

	str = de1.GetString(err);
	cout << (const char*)str << endl;

	return 0;
}

int doubleTest() {

	const double max = std::numeric_limits<double>::max();

	DataElement de1(DataElement::t_double, max);
	int err = 0;
	cout << max << endl;
	cout << de1.GetString(err) << endl;

	return 0;
}

int longTest() {
	const long long max = std::numeric_limits<long long>::max();

	DataElement de1(DataElement::t_long, max);
	int err = 0;
	KString str = de1.GetString(err);
	cout << max << endl;
	cout << (const char*)str << endl;

	DataElement de2(DataElement::t_long, (const char*)str);
	cout << de2.GetLong(err) << endl;
	cout << (const char*)de2.GetString(err) << endl;

	const long long min = std::numeric_limits<long long>::min();
	DataElement de3(DataElement::t_long, min);
	cout << min << endl;
	cout << de3.GetString(err) << endl;

	return 0;
}

int generalTest() {
	const string sValue("123.45");
	double bValue = 123.45;
	float fValue = 123.45f;
	int err = 0;

	DataElement de1(DataElement::t_string, sValue.c_str());

	DataElement de2(DataElement::t_double, bValue);

	de1.set(&de2);

	DataElement de3(DataElement::t_string, sValue.c_str());

	de1.set(&de3);

	DataElement de4(DataElement::t_float, fValue);

	KString str = de4.GetString(err);
	cout << (const char*)str << endl;

	DataElement de5(DataElement::t_string, sValue.c_str());
	DataElement de6(de5);

	de6.set(&de5);

	DataElement de7(DataElement::t_string, sValue.c_str(), 0);

	DataElement de8;
	de8 = de7;

	return 0;
}
