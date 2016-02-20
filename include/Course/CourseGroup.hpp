#ifndef COURSE_GROUP_H
#define COURSE_GROUP_H

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
		 * #param copy the CourseGroup to copy
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
		std::vector<Schedule*> genSchedule();
	private:
		/**
		 * The number of courses to eventually be scheduled
		 */
		size_t _numCourses;
		
		/**
		 * The list of courses
		 */
		std::valarray<Course*> _courses;
	
};
#endif
