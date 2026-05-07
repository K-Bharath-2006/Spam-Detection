# Spam Detector Web Application

A professional, full-stack web application for detecting spam messages using a keyword and rule-based heuristic approach. Built with React (Vite + Tailwind CSS) and Spring Boot.

## Features

- **Authentication System**: Secure JWT-based login and registration using BCrypt password encryption.
- **Spam Detection Engine**: Fast, efficient spam analysis using pre-compiled regex and keyword matching. Evaluates URLs, uppercase frequency, and special characters.
- **Analytics Dashboard**: Real-time statistics of total checks, spam vs safe ratio, and recent activity.
- **History Tracking**: Automatically saves all spam checks to a PostgreSQL database for future reference.
- **Modern UI**: Fully responsive, dark-mode compatible interface built with Tailwind CSS and Lucide React icons.

## Tech Stack

- **Frontend**: React 19, React Router DOM v7, Tailwind CSS v4, Vite, Axios, React Hot Toast.
- **Backend**: Java 17, Spring Boot 3.2.x, Spring Security, Spring Data JPA, JWT (io.jsonwebtoken).
- **Database**: PostgreSQL.

