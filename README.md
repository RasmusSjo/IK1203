<h1>IK1203 - Networks and Communication</h1>

<p>Repository for the course Networks and Communication - IK1203</p>

<h2>Course summary</h2>

<p>
This introductory networking course covered Internet structure, layered models, communication models, Internet applications, and protocols. Emphasis was on the TCP/IP stack, addressing transport protocols, flow control, congestion control, error handling, interconnected networks, and local area networks. After completion, I can describe networking concepts, analyze communication scenarios, implement simple applications using socket programming, and configure basic networks. The course consisted of three different examination moments, labwork, a project and two written exams. The project was divided into four different tasks, which are contained in this repository.
</p>

<a href="https://www.kth.se/student/kurser/kurs/IK1203?periods=6&startterm=20241&l=en">Course information</a>

<h2>Folder structure</h2>

<p>Each folder in the "src" folder corresponds to their respective task in the project. Below is a brief explanation of each of the tasks:</p>

<ul>
  <li>Task 1 - the assignment was to implement a Java class called TCPClient that facilitated TCP communication with a server by opening a connection, sending data, and receiving a response. To help us with development and testing, we were provided with an application program called TCPAsk that used the TCPClient class.</li>
  <li>Task 2 - in this assignment, the objective was to enhance the TCPClient class introduced in Task 1 to handle various server communication scenarios, including different ways of closing connections, timeouts, and data limits.</li>
  <li>Task 3 - this task involved implementing an HTTP server called HTTPAsk, which utilized the TCPClient from previous tasks to handle HTTP GET requests containing parameters for TCPClient's askServer method. The server parsed the request and extracted parameters such as hostname, port, and optional arguments. It returned the output of askServer as an HTTP response, ensuring proper handling of error scenarios with appropriate HTTP status codes.</li>
  <li>Task 4 - the last task involved transforming the HTTPAsk server from Task 3 into a concurrent server named ConcHTTPAsk, which could handle multiple clients simultaneously using Java multithreading. Each client connection triggered the creation of a new thread, enabling parallel processing of client requests without waiting for the server to finish serving the current client.</li>
</ul>
