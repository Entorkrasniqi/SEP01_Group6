#  Digital Note-taking.

A **JavaFX desktop application** that enables students to take lecture notes, organize them by categories, and highlights.  
The system uses **MariaDB** for persistent storage of notes and categories, ensuring data is secure and easily retrievable.

---
## Team members
- Entor Krasniqi 
- Doni Kojovic
- Omar Al-Dulaimi
- Sarujan Mathyruban

##  Features
- **Note Management**
  - Add, edit, and delete notes.
  - Categorize notes into subjects or custom categories.
  - Rich text editing (bold, italic, highlight).
  - **Font customization** – switch between 4 available fonts.
 
##  Target audience and vision
- The target audience for this system is students who want an easy way to organize their daily schedules and reflect on what they’ve learned. The application allows students to add notes about their day, with a focus on writing them after the class session rather than during it. Research and practice show that reviewing and writing notes after a lesson strengthens memory, encourages active recall, and helps students think critically about what they have been taught.

- **Annotations**
  - Highlight important text or concepts.
  

- **Search & Filter**
  - Search notes by **keywords**.
  - Filter notes by category and topic

- **Productivity Tools**
  - ⏱ **Time Clock** – track how long you've been writing or set a countdown timer for focused writing sessions.
  - Font change support for better readability (4 preloaded fonts).

- **User-Friendly Interface**
  - Modern JavaFX interface.
  - Category sidebar for quick navigation.
  - Preview pane for annotated notes.

---


---

##  Technologies
- **Frontend:** JavaFX  
- **Backend:** Java (JDBC)  
- **Database:** MariaDB  
- **Storage:** File system (for images/attachments)  
- **Utilities:** Java `Timeline` & `ScheduledExecutorService` (for timer/clock)  

---

## Technology Stack and Reasoning  
We chose Java because it is the main language we are studying right now, which helps us stay consistent with our coursework and avoid confusion by sticking to one language. JavaFX was chosen for the user interface because it is a Java-based technology that we have already used before, making it easier to work with while also providing good tools for building interactive applications. For the database, MariaDB was selected because the teacher requires the use of an SQL database, and since our group has already worked with MariaDB in the past, it was the natural choice for us. This combination keeps the project simple, familiar, and aligned with both the course requirements and our previous experience.

---

##  UI Preview
<img width="969" height="674" alt="Screenshot 2025-09-02 at 13 54 40" src="https://github.com/user-attachments/assets/ead1ce5f-238c-4a6f-90e2-628a2f21a045" />


---

# Database Structure

The project uses a simple database with a single table, `notes`, since there is no user authentication. The table is designed to store notes along with their category and status.

## Table: `notes`

| Column       | Type          | Description                                                   |
|--------------|---------------|---------------------------------------------------------------|
| `id`        | INT           | Primary key, auto-incremented note ID                         |
| `title`     | VARCHAR(255)  | The title of the note                                         |
| `content`   | TEXT          | The main content of the note                                  |



This structure allows the application to store and manage notes efficiently, including categorization and tracking of their status.

---

##  Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/Entorkrasniqi/SEP01_Group6.git
cd SEP01_Group6
```

### 2. Database Setup
1. Install MariaDB on your system
2. Create a database named `digital_notes`
3. Update database credentials in `src/main/java/com/group6/digitalnotes/util/DatabaseUtil.java`

### 3. Run the Application
```bash
mvn clean compile
mvn javafx:run
```

---

## 📁 Project Structure
```
SEP01_Group6/
├── src/main/java/
│   └── com/group6/digitalnotes/
│       ├── model/          # Data models
│       ├── dao/            # Database access objects
│       ├── controller/     # Application controllers
│       ├── util/           # Utility classes
│       └── DigitalNotesApp.java  # Main application
├── src/main/resources/     # FXML files and assets
├── pom.xml                 # Maven configuration
└── README.md               # This file
```

---

##  Usage
2. **Add Notes**: Write and format your lecture notes
5. **Timer**: Track your study/writing sessions

---




##  Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request




