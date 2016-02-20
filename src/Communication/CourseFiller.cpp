#include <iostream>
#include <string>
#include "Communication/CourseFiller.hpp"

bool CourseFiller::fill(CourseStore* store){
	CourseStoreDB db("104.236.4.226", "/lookup", "7819");
	std::string* jsonString = db.getJson();
	
	store->insert("a", "dad");
	return true;
}
