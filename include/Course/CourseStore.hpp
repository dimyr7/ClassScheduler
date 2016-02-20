#ifndef COURSE_STORE
#define COURSE_STORE
#include "Course/Course.hpp"
#include  "boost/unordered/unordered_map.hpp"
class CourseStore{
	public:
		/**
		 * Default constructor will create an empty CourseStore
		 */
		CourseStore();

		/**
		 * Copy constuctor will
		 */
		CourseStore(const CourseStore &copy);
		
		/**
		 * Destructor
		 */
		~CourseStore();
		
		/**
		 * Operator=
		 */
		CourseStore& operator=(const CourseStore &copy);

		/**
		 * Inserts a new course and its information into the course
		 * @param courseName is a string that represents the course string
		 * @param courseData is a string that is the data of the course
		 * @return true if the insertion is successful, false otherwise
		 */
		bool insert(std::string courseName, Course* courseData);

		/**
		 * Gets the data assoociated data of a course
		 * @param courseName the name of the course
		 * @return the data of the course, returns empty string if no entry exists
		 */
		Course* get(std::string courseName);
	private:

		/**
		 * A hashtable that maps the course name to the course data
		 */
		boost::unordered_map<std::string, Course*>  _courses;
};
#endif
