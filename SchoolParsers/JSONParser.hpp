#ifndef JSONPARSER_H
#define JSONPARSER_H
#include <boost/property_tree/ptree.hpp>
#include <string>

class JSONParser{

	public:
		static boost::property_tree::ptree* openJSON(std::string file);
	private:

};

#endif
