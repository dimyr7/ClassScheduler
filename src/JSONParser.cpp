#include "JSONParser.hpp"
#include <fstream>
#include <boost/property_tree/json_parser.hpp>

boost::property_tree::ptree* JSONParser::openJSON(std::string file){
	// Creating a new stream for a file
	std::ifstream json(file, std::ios::in);

	// init a property tree
	boost::property_tree::ptree *propTree = new boost::property_tree::ptree();

	// read json from stream & put into property tree
	boost::property_tree::json_parser::read_json(json, *propTree);

	return propTree;
}

