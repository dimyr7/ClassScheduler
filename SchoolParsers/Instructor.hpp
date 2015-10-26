#ifndef INSTRUCTOR_H
#define INSTRUCTOR_H
#include <string>
namespace CourseInfo{
	using std::string;
	class Instructor{
		friend class Section;
		public:
			string getName() const;

		private:
			/*
			 * Construtor
			 */
			Instructor(string name = "");
			/*
			 * Destructor
			 */
			~Instructor();
			/*
			 * Copy constructor
			 */
			Instructor(const Instructor& copy);
			/*
			 * Assignment operator
			 */
			Instructor& operator=(const Instructor& copy);


			/*
			 * setters
			 */
			void setName(string name);
			
			/*
			 * member data
			 */
			string _name;
	};
}
#endif
