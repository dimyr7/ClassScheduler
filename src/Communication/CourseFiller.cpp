#include <iostream>
#include <string>
#include "Communication/CourseFiller.hpp"
bool CourseFiller::fill(CourseStore* store){
	CourseStoreDB db("104.236.4.226", "/lookup", "7819");
	db.getGZip();
	
	store->insert("a", "dad");
	return true;
}
