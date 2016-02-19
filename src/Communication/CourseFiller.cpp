#include <iostream>
#include <string>
#include "Communication/CourseFiller.hpp"
bool CourseFiller::fill(CourseStore* store){
	CourseStoreDB db("104.131.42.182", "/lookup?mode=detail", "7819");
	db.getGZip();
	
	store->insert("a", "dad");
	return true;
}
