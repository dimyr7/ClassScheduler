#ifndef SEMESTER_H
#define SEMESTER_H
#include <string>
#include <array>
using std::string;
using std::array;
/*
 * represents a semester with start and end dates along with some meta info
 */
class Semester{
	public:
		/**
		 * Constructor for a semester filling in meta info
		 */
		Semester(string year, string season, string name);
		~Semester();
		/**
		 *  Returns true on success
		 *  Returns false otherwise (i.e. value is invalid) and does not change info 
		 */
		void setYear(string year);
		void setSeason(string season);
		void setName(string name);


		bool setStartDate(int day, int month, int year);
		bool setStartDay(int day);
		bool setStartMonth(int month);
		bool setStartYear(int year);

		bool setEndDate(int day, int month, int year);
		bool setEndDay(int day);
		bool setEndMonth(int month);
		bool setEndYear(int year);

		string 	getYear(); 		// 2016
		string 	getSeason();	// Fall
		string	getName();		// FA161

		array<int, 3> 	getStartDate(); // [28, 8, 2016]  => August 28, 2016
		array<int, 3> 	getEndDate();   // [14, 12, 2016] => December 14, 2016
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
	private:
		array<int, 3> _startDate;
		array<int, 3> _endDate;

		string 	_year;
		string 	_season;
		string 	_name;
};
#endif
