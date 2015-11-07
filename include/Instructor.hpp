#ifndef INSTRUCTOR_H
#define INSTRUCTOR_H
#include <string>
#include "Section.hpp"


class Section::Instructor{
	friend class Section;
	public:
		std::string getName() const;

	
	private:
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
