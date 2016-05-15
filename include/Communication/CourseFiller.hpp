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
		 * @param store is the store to fill
		 * @return true if the operaion succeded
		 */
		static bool fill(CourseStore* store);

	private:
		/**
		 * Given a json document, this will build a new course from the information
		 * @param courseJson is the json of the course
		 * @return a pointer to a new course, NULL if operation failed
		 */
		static Course* buildCourse(rapidjson::Value& courseJson);

		/**
		 * Given a json document, this will build a section 
		 * @param sectionJson is teh json of the section
		 * @return a pointer to a new section, NULL if operation failed
		 */
		static Section* buildSection(rapidjson::Value& sectionJson);

		/**
		 * Given a 12-hour time, this will convert into a 24 hour time 
		 * @param time is time in 12-hour
		 * @return an array of a time in 24-hour format where [hour, minute]
		 */
		static std::array<int, 2> convertTime(std::string time);
};
#endif
