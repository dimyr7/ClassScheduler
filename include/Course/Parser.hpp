#ifndef PARSE_H
#define PARSE_H
#include "Section.hpp"
#include "../lib/rapidjson/document.h"
#include <vector>

/** 
 * Parser is a class that reads and parses json containing UIUC scheduling information for a class.
 * From the parsed information, it uses SectionBuilder to creates Sections, which can then be returned.
 * See the example.json for the example formating
 */
class Parser {
    public:

        /**
         * Creates a Parser object that parses the json from a file.  
		 * @jsonFileName is a string of the file name to read
         */
        Parser(std::string jsonFileName);
        
        /**
         * Returns a vector of pointers to all sections in the json file.
		 * @return a vector of Section pointers parsed from the specified file
         */
        std::vector<Section*> getAll();

        /**
         * Returns the number of sections contained the json file
		 * @return an integer the number of sections in a a course
         */
        int getSize();


    private:
		/**
		 * Object representing the json document
		 */
        rapidjson::Document _dom;

		/**
		 * The sections belonging to the course in the document
		 */
        std::vector<Section*> _sections;     

		/**
		 * The numberof sections that belong to this course
		 */
        rapidjson::SizeType _sizeInit;

		/**
		 * Describtion of this course
		 */
        std::string _description; 

        /**
         * Builds all sections contained in json file and adds to _sections vector
         */
        void buildAllSections();
        
        /**
         * Converts string of time to integers hour and minutes in 24 hour time
         * @param time is a string representation of time in the form "3:50 PM"
		 * @return a vector of two ints representing the hour and minute on a 24-hour system: [15, 50]
         */
        std::vector<int> convertTime(std::string time); 
        
        /** 
         * Opens the file fileName and parses the json
		 * @string fileName is the name of the file to parse
         */ 
       void parseJSON(std::string fileName);
};
#endif
