# 🏁 Sprint 6 Report

## 🎯 Sprint Objective
- Extend localization support into the **database layer** while improving code quality and maintainability.  
- Perform **static code review**, refactoring, and code cleanup.  
- Prepare **acceptance test plans** for the system.  
- Implement a complete **authentication system** including Login and Signup pages.  
- Build login and signup UIs using Scene Builder with full controller logic.  
- Redesign and expand the database schema (Users table + Localization Strings table).  
- Remove all static text and alerts from the codebase.  
- Maintain consistent UI styling and component positioning across all languages.  
- Update architecture documentation (ER diagram & UML models).  

---

## ⚙️ Challenges
- Keeping all UI elements **consistent across all supported languages**, including RTL and languages with varying text lengths.

---

## 🚀 Improvements
- Full **authentication system** implemented (login + signup pages + controllers).  
- Codebase fully cleaned and refactored, removing all static text.  
- Transition from resource bundles to **database-based localization**.  
- Unified and consistent UI styling & layout across English, Arabic, and Japanese.  
- Database redesigned for better localization support and scalability.

---

## 🧱 Database Schema (Visual Representation)

### Users Table
| Column        | Type        | Notes               |
|---------------|-------------|----------------------|
| user_id       | INT         | PK                  |
| username      | VARCHAR     | UNIQUE, NOT NULL    |
| password      | VARCHAR     | NOT NULL            |
| nickname      | VARCHAR     |                      |
| created_at    | TIMESTAMP   | DEFAULT CURRENT_TIMESTAMP |
| updated_at    | TIMESTAMP   | Auto-updated        |

### Notes Table
| Column        | Type        | Notes               |
|---------------|-------------|----------------------|
| note_id       | INT         | PK                  |
| user_id       | INT         | FK → users.user_id  |
| title         | VARCHAR     | NOT NULL            |
| content       | TEXT        | NOT NULL            |
| created_at    | TIMESTAMP   | DEFAULT CURRENT_TIMESTAMP |
| updated_at    | TIMESTAMP   | Auto-updated        |

**Relationship:**  
users (1) —— (∞) notes

### Localization Strings Table
| Column  | Type        | Notes      |
|---------|-------------|------------|
| id      | INT         | PK         |
| key     | VARCHAR     | NOT NULL   |
| value   | VARCHAR     | NOT NULL   |
| language| VARCHAR(10) | NOT NULL   |

---

## 👥 Individual Contributions (Omar)

| Task | Time Spent |
|------|------------|
| Implemented Auth System (Login & Signup) | |
| Designed Login/Signup UI using Scene Builder | |
| Implemented Login/Signup Controllers | |
| Created UserDAO and User Model | |
| Performed Database Redesign (Users + Localization Tables) | |
| Implemented Database Localization | |
| Ensured Consistent UI Styling & Positioning Across All Languages | |
| Performed Full Code Clean-Up (Removed Static Text & Alerts) | |
| **Total Time Spent** | **45h** |

---
## Static code review

PMD report: https://github.com/Entorkrasniqi/SEP01_Group6/blob/main/PMD_Code_Review_Report.pdf

PMD report evidence: https://github.com/Entorkrasniqi/SEP01_Group6/blob/main/PMD_report_evidence.png

---
---

## 🗂️ Sprint 6 Resources


---

## 📄 Summary
Sprint 6 significantly improved the structure, readability, and maintainability of the project. It introduced database-powered localization, strengthened backend architecture through schema redesign and DAO improvements, and established a functional authentication workflow. The UI now maintains consistent styling across all languages, and the codebase is fully cleaned and ready for acceptance test planning.
