#ifndef INSTRUCTOR_H
#define INSTRUCTOR_H
#include <string>
class Instructor{
	public:

		/*
		 * Construtor
		 */
		Instructor(std::string name = "");
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

		std::string getName() const;

	
	private:
		

		/*
		 * setters
		 */
		void setName(std::string name);
		
		/*
		 * member data
		 */
		std::string _name;

		friend std::ostream& operator<<(std::ostream& os, const Instructor& instructor);
};
#endif
