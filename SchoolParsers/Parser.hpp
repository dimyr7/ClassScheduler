#ifndef PARSE_H
#define PARSE_H

#include <vector>
#include <string>
#include "rapidjson/document.h"
#include "Section.hpp"

class Parser {
    public:
        // Parses example.json 
        Parser();

        // Parses json string (example.json for now)
        Parser(const char *json);

//        ~Parser();
        
        std::vector<Section::Section*>    getAll();
        Section*            getNext();
        int                 getSize();
        int                 getSizeLeft();
        bool                hasNext();

    private:
        rapidjson::Document _dom;        
        int                 _sizeInit;
        int                 _index;

        void                parseJSON();
        void                getClassTime(const char*, const char*, int*);
};
#endif
