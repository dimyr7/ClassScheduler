#ifndef COURSE_GROUP_H
#define COURSE_GROUP_H
#include "Course/Course.hpp"
#include "Course/Schedule.hpp"
#include <valarray>
#include <string>
/**
 * A CourseGroup is a group of courses that are planning on being scheduled together
 */
class CourseGroup{
	public:
		/**
		 * Constructor
		 * @param numCourses is the number of courses that will eventually be schedulde
		 */
		CourseGroup(size_t numCourses);

		/**
		 * Copy constructor
		 * @param copy the CourseGroup to copy
		 */
		CourseGroup(const CourseGroup& copy);

		/**
		 * Assignment operator
		 * @param copy the CourseGroup to copy
		 * @return this CourseGroup with the new values
		 */
		CourseGroup& operator=(const CourseGroup& copy);

		/**
		 * Destructor
		 */
		~CourseGroup();

		/**
		 * Adds a new course to the course group
		 * @param course is the new course to add
		 * @return true if the course was added successfully, false otherwise
		 */
		bool addCourse(Course* course);

		/**
		 * Generates all possible schedules for this group of sections
		 * @return a vector of all possible schedules
		 */
		std::vector<Schedule*> genSchedules();
	private:

		/**
		 * Given a set of indecies assiciated with choosing different SectionCombinations, this will modify them to get the next combinations
		 * @param index is a set of indecies that will be modified to the next possible valid combinations
		 * @return true if the operation succesfully moved forward, false if the indecies were reset to the initial value of all 0s
		 */
		bool nextIteration(std::valarray<size_t> &index);

		/**
		 * Checks if any of the SecvtionCombos overlap
		 * @param potCombo is the vector of SectionCombos to check for overlap
		 * @return true if any 2 of the sections overlap, false otherwise
		 */
		static bool overlap(std::vector<SectionCombo*> potCombo);
		/**
		 * The number of courses to eventually be scheduled
		 */
		size_t _numCourses;

		/**
		 * The list of courses to be scheduled together
		 */
		std::valarray<Course*> _courses;
};
#endif
