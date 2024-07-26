<h1>Personalized News Update Aggregator</h1>
<p><strong>Overview</strong><br>
The Personalized News Update Aggregator is a microservices-based application designed to aggregate news articles based on user preferences and deliver them via email. It utilizes modern architectural patterns such as microservices, message queuing, and API integration to achieve its functionality.</p>

<p><strong>Features</strong></p>
<ul>
  <li><strong>Microservices Architecture:</strong> Built using microservices to separate concerns and enable scalability and maintainability.</li>
  <li><strong>Message Queuing with RabbitMQ</strong> Uses RabbitMQ as a message broker for asynchronous communication between services.</li>
  <li><strong>Communication with Dapr:</strong> Utilizes Dapr for service-to-service invocation, pub/sub messaging, and state management.</li>
  <li><strong>API Integration:</strong> Integrates with the Newsdata.io API to fetch the latest news articles based on user preferences.</li>
  <li><strong>Email Notification:</strong> Sends personalized news updates to users via email using RabbitMQ and Gemini AI.</li>
  <li><strong>Docker and Docker Compose:</strong> Containerized application with Docker Compose for orchestrating multiple services.</li>
  <li><strong>Database:</strong> MySQL used as the database management system for persistent storage.</li>
  <li><strong>Programming Language:</strong> Developed primarily using Java and Spring Boot for backend services.</li>
</ul>

<p><strong>Project Structure</strong></p>
<p>The project is structured into three main microservices:</p>
<ul>
  <li><strong>UserPreferencesService:</strong> Manages user profiles and preferences.</li>
  <li><strong>NewsAggregationService:</strong> Fetches news articles based on user preferences.</li>
  <li><strong>CommunicationService:</strong> Sends news updates via email.</li>
</ul>

<p><strong>important API Endpoints</strong></p>
<p><strong>UserPreferencesService</strong></p>
<ul>
  <li>POST /users/register: Registers a new user with preferences+ sends the message to a pub sub queue</li>
  <li>GET /users/{userId}/preferences: Retrieves user preferences by userId.</li>
  <li>PUT users/updatePreferencesByNames?userId={userId}&preferencesNames={Categories}: updates the user`s preferences list by its id, and adds the preferencesNames to the existing preferences list. .</li>
</ul>
<p><strong>NewsAggregationService</strong></p>
<ul>
  <li>*Inner POST Req by Dapr:  /userRegisterDetails: gets the queue message,fetches news,sends forward to CommunicationService.</li>
     <li>GET /userDetails/{userId}: gets user Details by Id and then fetches new by his preferences,then send it to CommunicationService.</li>
</ul>
<p><strong>CommunicationService</strong></p>
<ul>
  <li>*Inner POST Req by Dapr: /newsNotificationDetails: gets the queue message,sends the response message(articles&categories of them)to mail`s user using gmail & smtp.</li>
      <li>GET /newsNotification/byUserId/{userId}: if user is exist, can get its details and activate`s news aggregation fetch news method, gets the articles and sends the response to mail`s user.</li>

</ul>

<p><strong>Preferences (Categories)</strong></p>
<p>Users can choose from the following categories when setting their news preferences,for instance:</p>
<ul>
  <li>Technology</li>
  <li>Arts</li>
  <li>Middle East</li>
  <li>Finance</li>
  <li>Sports</li>
  <li>Politics</li>
</ul>

<p><strong>Testing</strong></p>
<p>Postman collection (postman\FinalProjectZionet.postman_collection.json) is included for testing the API endpoints. Import this collection into Postman to execute requests against the running services.</p>

<p><strong>Deployment</strong></p>
<p>Clone the repository:</p>
<pre><code>git clone https://github.com/guy4213/ZionetFinalProject.git
</code></pre>
<p>Navigate to the project directory:</p>
<pre><code>cd ZionetFinalProject
</code></pre>
<p>Start the application using Docker Compose:</p>
<pre><code>docker-compose up
</code></pre>

<p><strong>Notes</strong></p>
<p>Ensure Docker and Docker Compose are installed and running on your system to deploy and run the application seamlessly.</p>
