# 📝 Digital Note-taking.

A **JavaFX desktop application** that enables students to take lecture notes, organize them by categories, and highlights.  
The system uses **MariaDB** for persistent storage of notes and categories, ensuring data is secure and easily retrievable.

---
## Team members
- Entor Krasniqi 
- Doni Kojovic
- Omar Al-Dulaimi
- Sarujan Mathyruban

## 🚀 Features
- **Note Management**
  - Add, edit, and delete notes.
  - Categorize notes into subjects or custom categories.
  - Rich text editing (bold, italic, highlight).
  - **Font customization** – switch between 4 available fonts.

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

## 🛠 Technologies
- **Frontend:** JavaFX  
- **Backend:** Java (JDBC)  
- **Database:** MariaDB  
- **Storage:** File system (for images/attachments)  
- **Utilities:** Java `Timeline` & `ScheduledExecutorService` (for timer/clock)  

---

## Technology Stack and Reasoning  
We chose Java because it is the main language we are studying right now, which helps us stay consistent with our coursework and avoid confusion by sticking to one language. JavaFX was chosen for the user interface because it is a Java-based technology that we have already used before, making it easier to work with while also providing good tools for building interactive applications. For the database, MariaDB was selected because the teacher requires the use of an SQL database, and since our group has already worked with MariaDB in the past, it was the natural choice for us. This combination keeps the project simple, familiar, and aligned with both the course requirements and our previous experience.

---

## 📸 UI Preview
<img width="927" height="655" alt="Screenshot 2025-08-26 at 16 18 36" src="https://github.com/user-attachments/assets/daed67fa-0e09-41de-bb9a-a6c3ae5229cf" />


---

## ⚡ Getting Started

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

## 🎯 Usage
1. **Create Categories**: Organize your notes by subject or topic
2. **Add Notes**: Write and format your lecture notes
3. **Annotate**: Add images, highlights, and inline comments
4. **Search**: Find specific notes using keywords
5. **Timer**: Track your study/writing sessions

---

## 👨‍💻 Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

---

## 📄 License
This project is licensed under the MIT License.

---

## 📧 Contact
**Group 6 - Software Engineering Project 1**  
- Repository: [SEP01_Group6](https://github.com/Entorkrasniqi/SEP01_Group6)
