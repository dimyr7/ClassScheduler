#ifndef COURSE_STORE_DB
#define COURSE_STORE_DB
#include <string>
class CourseStoreDB{
	public:
		
		/**
		 * Constructor that sets the host URL & path
		 */
		CourseStoreDB(std::string host, std::string path, std::string port);

		/**
		 * Copy constructor
		 * @param the CourseStoreDB to copy
		 */
		CourseStoreDB(const CourseStoreDB &copy);

		/**
		 * Assignment operator
		 * @param copy is CourseStore to copy
		 * @return the copied coursestore
		 */
		CourseStoreDB& operator=(const CourseStoreDB &copy);

		/**
		 * Destructor
		 */
		~CourseStoreDB();
		
		/**
		 * Retrieves that data from the coursestore as a string
		 */
		std::string* getJson();
	private:

		/**
		 * Sends a message to the specigied host
		 * @param sockfd is the socket to write to
		 * @param msg is the message to write
		 * @return true if the send is successful, false otherwise
		 */
		bool sendMessage(int sockfd, std::string msg);		

		/**
		 * Reads a message from the socket and returns it as a string
		 * @param sockfd is the socket to read from
		 * @return a string with the read message
		 */
		std::string recvMessage(int sockfd, int length);

		/**
		 * Verifies that the http response has the appropriate status
		 * @param message is the string http response
		 * @param status is the int that represents the desiderd status
		 * @return true if the message has the appropriate status, false otherwise
		 */
		bool verifyStatus(std::string message, int status);

		/**
		 * A string representing the host
		 */
		std::string _host;

		/**
		 * A string representing the path for the gzipped courseinfo
		 */
		std::string _path;

		/**
		 * A string representing the port
		 */
		std::string _port;
};
#endif
