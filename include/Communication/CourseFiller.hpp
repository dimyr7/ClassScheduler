#ifndef COURSE_FILLER_H
#define COURSE_FILLER_H

#include "Communication/CourseStoreDB.hpp"
#include "Course/CourseStore.hpp"
#include "Course/Course.hpp"
#include "Course/SectionBuilder.hpp"
#include "rapidjson/document.h"

class CourseFiller{
	public:
		
		/**
		 * Fills the coursestore with all the courses
		 */
		static bool fill(CourseStore* store);

	private:
		static Course* buildCourse(rapidjson::Value& courseJson);
		static Section* buildSection(rapidjson::Value& sectionJson);
		static std::vector<int> convertTime(std::string time);
};
#endif
