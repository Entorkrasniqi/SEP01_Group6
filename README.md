# 📝 Digital Note-taking and Annotation Tool

A **JavaFX desktop application** that enables students to take lecture notes, organize them by categories, and annotate with images, diagrams, and highlights.  
The system uses **MariaDB** for persistent storage of notes and categories, ensuring data is secure and easily retrievable.

---

## 🚀 Features
- **Note Management**
  - Add, edit, and delete notes.
  - Categorize notes into subjects or custom categories.
  - Rich text editing (bold, italic, highlight).
  - **Font customization** – switch between 4 available fonts.

- **Annotations**
  - Attach images, diagrams, and screenshots to notes.
  - Highlight important text or concepts.
  - Inline annotation support.

- **Search & Filter**
  - Search notes by **keywords**.
  - Filter notes by category, topic, or creation date.

- **Productivity Tools**
  - ⏱ **Time Clock** – track how long you've been writing or set a countdown timer for focused writing sessions.
  - Font change support for better readability (4 preloaded fonts).

- **User-Friendly Interface**
  - Modern JavaFX interface.
  - Category sidebar for quick navigation.
  - Preview pane for annotated notes.

---

## 🗂 Database Design (MariaDB)

### `categories`
| category_id | name          |
|-------------|---------------|
| 1           | Computer Sci  |
| 2           | Mathematics   |

### `notes`
| note_id | category_id | title        | content               | created_at          |
|---------|-------------|--------------|-----------------------|---------------------|
| 1       | 1           | Lecture 1    | Intro to Algorithms   | 2025-08-20 10:30:00 |

### `annotations`
| annotation_id | note_id | type   | data                         |
|---------------|---------|--------|------------------------------|
| 1             | 1       | image  | /images/diagram1.png         |
| 2             | 1       | text   | Highlighted: "Big-O notation"|

---

## 🛠 Technologies
- **Frontend:** JavaFX  
- **Backend:** Java (JDBC)  
- **Database:** MariaDB  
- **Storage:** File system (for images/attachments)  
- **Utilities:** Java `Timeline` & `ScheduledExecutorService` (for timer/clock)  

---

## 📸 UI Preview
*(Add screenshots of your JavaFX interface here once built!)*

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
