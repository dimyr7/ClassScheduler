#ifndef COURSE_FILLER_H
#define COURSE_FILLER_H

#include <Course/CourseStore.hpp>
#include <Communication/CourseStoreDB.hpp>
class CourseFiller{
	public:
		
		/**
		 * Fills the coursestore with all the courses
		 */
		static bool fill(CourseStore* store);
};
#endif
