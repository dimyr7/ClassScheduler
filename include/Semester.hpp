#ifndef SEMESTER_H
#define SEMESTER_H

#include <string>
/*
 * This represents a 'Semester' such as Fall 2015, or Spring 2016 A (Only the first 8 weeks)
 */
class Semester{
	public:

		/**
		 * Constructor
		 * A creates a new semester. 
		 * Sets the start dates to Jan 1, 2000 and the end date to Dec 31, 2000 
		 * Start and end dates can only be changed once after this
		 * @param year is a string representing the year (e.g. '2016'), defualts to '2000'
		 * @param season is a string representation of a season (e.g. 'Spring'), defaults to 'Fall'
		 * @param name is a string representation of a semester name (e.g. 'SP16A') defaults to 'FA15'
		 */
		Semester(std::string year="2000", std::string season="Fall", std::string name="FA15");

		/**
		 * Destructor
		 * Releases all the resources held by a Semester Object
		 */
		~Semester();

		/**
		 * Copy constructor
		 * @param copy is a Semester object to be copied into a new object
		 */
		Semester(const Semester& copy);

		/**
		 * Assignment operator
		 * @param copy is a semester object to be copied into a new object
		 * @return is a Semester object that called this function with the new set of attributes
		 */
		Semester& operator=(const Semester& copy);

		/**
		 * Getter function to get the day of the starting date 
		 * @return an integer representing the starting day number
		 */
		int 	getStartDay() const; 

		/**
		 * Getter function to get the month of the starting date 
		 * @return an integer representing the month number (e.g. Jan => 1, Aug => 8)
		 */
		int 	getStartMonth() const;  

		/**
		 * Getter function to get the year of the starting date 
		 * @return an integer representing the starting year
		 */
		int 	getStartYear() const;

		/**
		 * Getter function to get the day of the ending date 
		 * @return an integer representing the ending day number
		 */
		int 	getEndDay() const;

		/**
		 * Getter function to get the month of the ending date 
		 * @return an integer representing the ending month number (e.g. Jan => 1, Aug => 8)
		 */
		int 	getEndMonth() const;
		
		/**
		 * Getter function to get the year of the ending date 
		 * @return an integer representing the ending year
		 */
		int 	getEndYear() const;

		/**
		 * Getter function to get the year to which this semester belongs to
		 * @return string representation of the year of the semester
		 */
		std::string 	getYear() const; 	

		/**
		 * Getter function to get the season to which this semester belongs to
		 * @return string representation of the season (e.g. 'Fall')
		 */
		std::string 	getSeason() const;

		/**
		 * Getter function to get the name of the semester
		 * @return string representation of the name (e.g. 'FA15')
		 */
		std::string		getName() const;

		/**
		 * A setter function to set the starting and ending dates for a semester
		 * Both dates must be provided 
		 * The dates can only be set once for a Semester object.
		 * @param  start is an integer array of length 3 that represents a starting date (e.g. Jan 2, 2000 => [2, 1, 2000] 
		 * @param end is an integer array of length 3 that represents an ending date (e.g. Dec 31, 2000 => [31, 12, 2000]
		 * @return true if the operation was successful, false otherwise (e.g. dates already set, parameters are valid dates, the start date is not actually before the end date
		 */
		bool setDates(const int (&start)[3], const int (&end)[3]);
	
		/**
		 * A static function that takes two integer arrays of length 3 and determines if one is before the other
		 * @param first is an integer array of length 3 that represents a date
		 */
		static bool before(const int (&first)[3], const int (&second)[3]);

	private:
 
		/*
	 	 * A function that determines if a year is valid
		 * @param year is an int representing a year to validate
		 * @return true if the year is valid, false otherwise
		 */
		static bool validYear(int year);

		/*
		 * A function that determines if a month is invalid
		 * @param month is a int representing a month to validate
		 * @return true if a month is valid, false otherwise
		 */
		static bool validMonth(int month);

		/*
		 * A function that determines if a day is valid
		 */
		static bool validDay(int day);
		
		/**
		 * A semestetr can be sent to a stream to be printed in human readable form
		 * @param os is a stream to write to
		 * @param semester is a Semester object that will be converted to a human readable stream and written to stream
		 * @return is the stream os so that commands can be chained
		 */
		friend std::ostream& operator<<(std::ostream& os, const Semester& semester);

		/**
		 * An integer representing the starting date
		 */
		int _startDate[3];

		/**
		 * An integer representing the ending date
		 */
		int _endDate[3];

		/**
		 * A string representing a year to which a semester belongs to
		 */
		std::string 	_year;

		/**
		 * A string representing the season
		 */
		std::string 	_season;

		/**
		 * The name of the semeseter
		 */
		std::string 	_name;
	
		/**
		 * An array of string representations of months
		 */
		const static std::string monthsStr[12];
		

		/**
		 * set to true if the date has already been set, false otherwise
		 */
		bool _isSet;

};
#endif
