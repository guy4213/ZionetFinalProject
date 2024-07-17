<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Personalized News Update Aggregator</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            max-width: 800px;
            margin: auto;
            padding: 20px;
        }
        h1, h2, h3 {
            color: #333;
        }
        pre {
            background-color: #f4f4f4;
            padding: 10px;
            border: 1px solid #ccc;
            overflow-x: auto;
        }
        code {
            font-family: Consolas, monospace;
            font-size: 1em;
        }
    </style>
</head>
<body>

    <h1>Personalized News Update Aggregator</h1>

    <h2>Overview</h2>
    <p>The Personalized News Update Aggregator is a microservices-based application designed to aggregate news articles based on user preferences and deliver them via email. It utilizes modern architectural patterns such as microservices, message queuing, and API integration to achieve its functionality.</p>

    <h2>Features</h2>
    <ul>
        <li><strong>Microservices Architecture:</strong> The application is built using microservices to separate concerns and enable scalability and maintainability.</li>
        <li><strong>Message Queuing with RabbitMQ:</strong> RabbitMQ is used as a message broker to handle asynchronous communication between services, ensuring reliable delivery of news updates.</li>
        <li><strong>API Integration:</strong> Integrates with the Newsdata.io API to fetch the latest news articles based on user preferences.</li>
        <li><strong>Email Notification:</strong> Utilizes RabbitMQ to send personalized news updates to users via email using Gemini AI.</li>
        <li><strong>Docker and Docker Compose:</strong> The application is containerized using Docker, and Docker Compose is used for orchestrating multiple services.</li>
        <li><strong>Database:</strong> MySQL is used as the database management system for persistent storage.</li>
        <li><strong>Programming Language:</strong> Developed primarily using Java and Spring Boot for the backend services.</li>
    </ul>

    <h2>Project Structure</h2>
    <p>The project is structured into three main microservices:</p>
    <ul>
        <li><strong>UserPreferencesService:</strong> Manages user profiles and preferences, allowing users to select categories of interest for news updates.</li>
        <li><strong>NewsAggregationService:</strong> Fetches news articles from Newsdata.io based on user preferences and prepares them for delivery.</li>
        <li><strong>CommunicationService:</strong> Handles communication tasks such as sending news updates via email using RabbitMQ and Gemini AI.</li>
    </ul>

    <h2>API Endpoints</h2>
    <h3>UserPreferencesService</h3>
    <ul>
        <li><code>POST /users/register</code>: Registers a new user with preferences.</li>
        <li><code>GET /users/{userId}/preferences</code>: Retrieves user preferences by userId.</li>
    </ul>

    <h3>NewsAggregationService</h3>
    <ul>
        <li><code>GET /news/{category}</code>: Fetches the latest news articles for a specific category.</li>
    </ul>

    <h3>CommunicationService</h3>
    <ul>
        <li><code>POST /email/send</code>: Sends personalized news updates to the user's email.</li>
    </ul>

    <h2>Preferences (Categories)</h2>
    <p>Users can choose from the following categories when setting their news preferences:</p>
    <ul>
        <li>Technology</li>
        <li>Arts</li>
        <li>Middle East</li>
        <li>Finance</li>
        <li>Sports</li>
        <li>Politics</li>
    </ul>

    <h2>Testing</h2>
    <p>Postman collection (<code>FinalProjectZionet.postman_collection.json</code>) is included for testing the API endpoints. Import this collection into Postman to execute requests against the running services.</p>

    <h2>Deployment</h2>
    <h3>Clone the repository</h3>
    <pre><code>git clone https://github.com/guy4213/ZionetFinalProject.git</code></pre>

    <h3>Navigate to the project directory</h3>
    <pre><code>cd ZionetFinalProject</code></pre>

    <h3>Start the application using Docker Compose</h3>
    <pre><code>docker-compose up</code></pre>

    <h2>Notes</h2>
    <p>Ensure Docker and Docker Compose are installed and running on your system to deploy and run the application seamlessly.</p>

</body>
</html>
