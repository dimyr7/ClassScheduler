#ifndef SEMESTER_H
#define SEMESTER_H
#include <string>
#include <array>
namespace CourseInfo{
	using std::string;
	using std::array;
	/*
	 * represents a semester with start and end dates along with some meta info
	 */
	class Semester{
		friend class Section;
		private:
		/**
		 * Constructor for a semester filling in meta info
		 */
		Semester(string year="", string season="", string name="");
		/**
		 * Destructor
		 */
		~Semester();
		/*
		 * Copy constructor
		 */
		Semester(const Semester& copy);
		/*
		 * Assignment operator
		 */
		Semester& operator=(const Semester& copy);



		/**
		 *  Returns true on success
		 *  Returns false otherwise (i.e. value is invalid) and does not change info 
		 */
		void setYear(string year);
		void setSeason(string season);
		void setName(string name);


		bool setDate(int day, int month, int year, bool start);
		bool setDay(int day, bool start);
		bool setMonth(int month, bool start);
		bool setYear(int year, bool start);


		string 	getYear() const; 		// 2016
		string 	getSeason() const;	// Fall
		string	getName() const;		// FA161

		/*
		 * if start=true, returns data about the start date
		 * returns data about end date otherwise
		 */
		int 	getDay(bool start) const; // [28, 8, 2016]  => August 28, 2016
		int 	getMonth(bool start) const;   // [14, 12, 2016] => December 14, 2016
		int 	getYear(bool start) const;
		/*
		 * year must between 1990 and 2100
		 */
		static bool validYear(int year);
		/*
		 * month must be between 1 and 12
		 */
		static bool validMonth(int month);
		/*
		 * day must be between 1 and 31
		 */
		static bool validDay(int day);

		array<int, 3> _startDate;
		array<int, 3> _endDate;

		string 	_year;
		string 	_season;
		string 	_name;
	};
}
#endif
