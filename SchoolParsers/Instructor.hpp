#ifndef INSTRUCTOR_H
#define INSTRUCTOR_H
#include <string>
using std::string;
class Instructor{
	public:
		Instructor(string name);
		~Instructor();
		string getName();
		void setName(string name);
	private:
		string _name;
};
#endif
