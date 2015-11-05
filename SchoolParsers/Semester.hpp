#ifndef SEMESTER_H
#define SEMESTER_H
#include <string>
#include "Section.hpp"

/*
 * represents a semester with start and end dates along with some meta info
 */
class Section::Semester{
	friend class Section;
	public:
		/*
		 * if start=true, returns data about the start date
		 * returns data about end date otherwise
		 */
		int 	getDay(bool start) const; // [28, 8, 2016]  => August 28, 2016
		int 	getMonth(bool start) const;   // [14, 12, 2016] => December 14, 2016
		int 	getYear(bool start) const;
		
		std::string 	getYear() const; 		// 2016
		std::string 	getSeason() const;	// Fall
		std::string	getName() const;		// FA161
		
		 const static std::string monthsStr[12];
	private:
		/*
		 * Constructor for a semester filling in meta info
		 */
		Semester(std::string year="", std::string season="", std::string name="");
		/*
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



		/*
		 *  Returns true on success
		 *  Returns false otherwise (i.e. value is invalid) and does not change info 
		 */
		void setYear(std::string year);
		void setSeason(std::string season);
		void setName(std::string name);


		bool setDates(const int (&start)[3], const int (&end)[3]);


 
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
		static bool before(const int (&first)[3], const int (&second)[3]);


		friend std::ostream& operator<<(std::ostream& os, const Semester& semester);

		const static int  NUMOFDATESPECIFIER = 3;
		int _startDate[ NUMOFDATESPECIFIER ];
		int _endDate[ NUMOFDATESPECIFIER ];

		std::string 	_year;
		std::string 	_season;
		std::string 	_name;
};
#endif
