#ifndef PARSE_H
#define PARSE_H

#include <vector>
#include <string>
#include "../lib/rapidjson/document.h"
#include "Section.hpp"

/* 
 * Parser is a class that reads and parses json containing UIUC scheduling information for a class.
 * From the parsed information, it uses SectionBuilder to creates Sections, which can then be returned.
 */
class Parser {
    public:
        /*
         * Creates a Parser object that parses the json in the file example.json by default.
         */
        Parser();

        /*
         * Creates a Parser object that parses the json from a file.  jsonFileName
         * is the name of the file to read from.
         */
        Parser(std::string jsonFileName);
        
        /*
         * Returns a vector of pointers to all sections in the json file.
         */
        std::vector<Section::Section*> getAll();

        /*
         * Returns a pointer to a single section
         */
        Section*            getNext();
        
        /*
         * Returns the number of sections contained the json file
         */
        int                 getSize();

        // Returns if there is a section remaining
        bool                hasNext();

    private:
        rapidjson::Document               _dom;
        std::vector<Section::Section*>    _sections;     
        rapidjson::SizeType               _sizeInit;
        int                               _index;
        std::string                       _description; 

        /*
         * Builds all sections contained in json file and adds to _sections vector
         */
        void                    buildAllSections();
        
        /*
         * Converts string of time to integers hour and minutes in 24 hour time
         * vector[0] is the hour, vector of [1] is the minutes.
         * E.g. "3:50 PM" -> [15, 50]
         */
        std::vector<int>        convertTime(std::string time); 
        
        /* 
         * Opens the file fileName and parses the json
         */ 
       void                    parseJSON(std::string fileName);
};
#endif
