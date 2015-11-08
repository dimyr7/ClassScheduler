#ifndef PARSE_H
#define PARSE_H
#include "Section.hpp"
#include "../lib/rapidjson/document.h"
#include <vector>
class Parser {
    public:
        // Parses example.json 
        Parser();

        // Parses json from a file
        Parser(std::string jsonFile);
        
        // Returns a vector of pointers to all sections in file
        std::vector<Section*> getAll();

        // Returns a pointer to a single section
        Section*            getNext();
        ;
        // Returns number of sections in the file
        int                 getSize();

        // Returns number of sections remaining
        // Counts number of times getNext is called
        int                 getSizeLeft();
        
        // Returns if there is a section remaining
        bool                hasNext();

    private:
        rapidjson::Document _dom;        
        int                 _sizeInit;
        int                 _index;
        std::string         _description; 

        void                parseJSON(std::string fileName);
        void                getClassTime(const char*, const char*, int*);
};
#endif
