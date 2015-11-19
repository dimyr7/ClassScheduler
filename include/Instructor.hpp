#ifndef INSTRUCTOR_H
#define INSTRUCTOR_H
#include <string>
class Instructor{
	public:

		/**
		 * Construtor
		 * @param name is the full name of the instructor. Defaults to empty string
		 */
		Instructor(std::string name = "");

		/**
		 * Destructor
		 */
		~Instructor();

		/**
		 * Copy constructor
		 * @param copy is the Instructor object to be copied
		 */
		Instructor(const Instructor& copy);

		/**
		 * Assignment operator
		 * @param copy is the instructor object to be copied
		 * @return an instructor object with the values changed
		 */
		Instructor& operator=(const Instructor& copy);

		/**
		 * Getter for the isntructor's name
		 * @return the instructor's name
		 */
		std::string getName() const;

	
	private:
		
		/**
		 * A string that represents the instructor
		 */
		std::string _name;

		/**
		 * Prints the instructor's name to stream
		 * @param os the stream to write instructor information to
		 * @param insstructor is the object to write to stream
		 * @return the stream passed in paramater with after the information is written
		 */
		friend std::ostream& operator<<(std::ostream& os, const Instructor& instructor);
};
#endif
