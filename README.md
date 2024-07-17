Personalized News Update Aggregator
Overview
The Personalized News Update Aggregator is a microservices-based application designed to aggregate news articles based on user preferences and deliver them via email. It utilizes modern architectural patterns such as microservices, message queuing, and API integration to achieve its functionality.

Features
Microservices Architecture: The application is built using microservices to separate concerns and enable scalability and maintainability.

Message Queuing with RabbitMQ: RabbitMQ is used as a message broker to handle asynchronous communication between services, ensuring reliable delivery of news updates.

API Integration: Integrates with the Newsdata.io API to fetch the latest news articles based on user preferences.

Email Notification: Utilizes RabbitMQ to send personalized news updates to users via email using Gemini AI.

Docker and Docker Compose: The application is containerized using Docker, and Docker Compose is used for orchestrating multiple services.

Database: MySQL is used as the database management system for persistent storage.

Programming Language: Developed primarily using Java and Spring Boot for the backend services.

Project Structure
The project is structured into three main microservices:

UserPreferencesService: Manages user profiles and preferences, allowing users to select categories of interest for news updates.

NewsAggregationService: Fetches news articles from Newsdata.io based on user preferences and prepares them for delivery.

CommunicationService: Handles communication tasks such as sending news updates via email using RabbitMQ and Gemini AI.

API Endpoints
UserPreferencesService
POST /users/register: Registers a new user with preferences.
GET /users/{userId}/preferences: Retrieves user preferences by userId.
NewsAggregationService
GET /news/{category}: Fetches the latest news articles for a specific category.
CommunicationService
POST /email/send: Sends personalized news updates to the user's email.
Preferences (Categories)
Users can choose from the following categories when setting their news preferences:

Technology
Arts
Middle East
Finance
Sports
Politics
Testing
Postman collection (FinalProjectZionet.postman_collection.json) is included for testing the API endpoints. Import this collection into Postman to execute requests against the running services.

Deployment
Clone the repository
bash
Copy code
git clone https://github.com/guy4213/ZionetFinalProject.git
Navigate to the project directory
bash
Copy code
cd ZionetFinalProject
Start the application using Docker Compose
bash
Copy code
docker-compose up
Notes
Ensure Docker and Docker Compose are installed and running on your system to deploy and run the application seamlessly.




 
