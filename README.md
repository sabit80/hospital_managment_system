# 🏥 Hospital Management System (HMS)

A complete, modern Hospital Management System built with JavaFX that helps manage all aspects of hospital operations including patients, doctors, staff, appointments, ambulances, and finances.

---

## 📋 Table of Contents

1. [What is This?](#-what-is-this)
2. [Main Features](#-main-features)
3. [What You Need (Requirements)](#-what-you-need-requirements)
4. [How to Install](#-how-to-install)
5. [How to Run](#-how-to-run)
6. [How to Use](#-how-to-use)
7. [Understanding the System](#-understanding-the-system)
8. [Database Information](#-database-information)
9. [Troubleshooting](#-troubleshooting)
10. [Technical Details](#-technical-details)

---

## 🎯 What is This?

This Hospital Management System is a desktop application that helps hospitals manage their daily operations. Think of it as a digital assistant that keeps track of:

- **Patients** - Who is in the hospital and their information
- **Doctors & Staff** - All medical and non-medical employees
- **Appointments** - When patients meet with doctors
- **Ambulances** - Vehicle fleet and their status
- **Beds & Wards** - Available beds and room assignments
- **Money** - Income, expenses, and hospital profits
- **Operations** - Surgical procedures and schedules
- **Visitors** - Who visits patients and when

Everything is stored safely in a database, and you interact with it through a beautiful, easy-to-use interface.

---

## ✨ Main Features

### 1. **Three Different User Roles**

The system has three main dashboards for different users:

#### 🔵 **Manager Dashboard** (Full Control)
The manager sees everything and can:
- View all hospital statistics
- See total patients, doctors, ambulances, and employees
- Check financial reports (income, expenses, profit)
- View charts showing financial trends
- See today's appointments
- Export reports to PDF
- Access all management modules

#### 🟢 **Doctor Dashboard** (Clinical View)
Doctors can:
- Select their name from a dropdown
- View their appointments for today
- See scheduled operations
- Check patient details
- Track their daily schedule

#### 🟡 **Receptionist Dashboard** (Front Desk)
Receptionists can:
- Manage patient check-ins
- Assign beds to patients
- View available beds in different wards (General Ward, ICU, CCU)
- Create new appointments
- Manage visitor information

---

### 2. **Patient Management**
- Add new patients with complete information
- Store: Name, age, gender, blood group, phone, email, address
- Track registration dates
- View all patients in a organized table
- Search and filter patients
- Update patient information anytime

---

### 3. **Doctor Management**
- Register doctors with their specialization
- Store: Name, specialty (Cardiologist, Surgeon, etc.), phone, email
- Track medical license numbers
- Record office location and working hours
- Add notes about each doctor
- View all doctors in the system

---

### 4. **Appointment System**
- Book appointments between patients and doctors
- Set appointment date and time
- Track appointment status (Scheduled, Completed, Cancelled)
- Add notes for each appointment
- View appointments by date
- See today's appointments on dashboard
- Automatic linking with patients and doctors

---

### 5. **Nurse Management**
- Register nurses working in the hospital
- Assign nurses to specific floors and rooms
- Track department assignments
- Store nursing license numbers
- Record working hours and shifts
- Manage salary information

---

### 6. **Cleaner Management**
- Manage cleaning staff
- Assign floors and areas to each cleaner
- Set work shifts (Morning, Evening, Night)
- Track cleaning schedule
- Record staff availability
- Monitor performance

---

### 7. **Ambulance Management**
- Track all ambulances in the fleet
- View vehicle numbers and types
- See real-time status:
  - ✅ **Available** - Ready to use
  - 🚑 **On Duty** - Currently on a call
  - 🔧 **In Maintenance** - Being serviced
- Track driver information and phone numbers
- Monitor current location
- Record last service date
- Check fuel status
- View equipment inventory
- Manage capacity (how many patients can travel)

---

### 8. **Financial Management** (Hospital Profits)
- Record all income (money coming in):
  - Patient services
  - Surgery fees
  - Lab tests
  - Medicine sales
  - Consultation fees
- Record all expenses (money going out):
  - Staff salaries
  - Equipment purchases
  - Maintenance costs
  - Utilities (electricity, water)
  - Supplies
- **Automatic profit calculation** = Income - Expenses
- View financial dashboard with:
  - Total Revenue
  - Total Expenses
  - Net Profit
- See trends on a chart
- Export financial reports to PDF
- Track payment methods (Cash, Card, Insurance)

---

### 9. **Ward & Bed Management**
- Three types of wards:
  - **General Ward** - Regular patients (50 beds)
  - **ICU** (Intensive Care Unit) - Critical patients (20 beds)
  - **CCU** (Coronary Care Unit) - Heart patients (15 beds)
- Assign beds to patients
- See available/occupied beds
- Track bed status in real-time
- View which patient is in which bed

---

### 10. **Reception/Visitor Management**
- Record visitors when they come to the hospital
- Check-in and check-out system
- Link visitors to patients they're visiting
- Record purpose of visit
- Track visit duration
- Note receptionist who handled the visitor

---

### 11. **Operation/Surgery Management**
- Schedule surgical operations
- Assign doctor and patient
- Set operation date and time
- Choose operation theater (OT1, OT2, OT3)
- Record expected duration
- Add notes about the procedure
- Track operation status (Scheduled, Completed, Cancelled)

---

## 💻 What You Need (Requirements)

Before you can use this system, your computer needs:

### Required Software:

1. **Java (JDK)** - Version 21 or newer
   - This is the programming language the system runs on
   - **You already have Java 25** ✅ (checked during setup)

2. **Maven** - Version 3.9 or newer
   - This helps build and run the project
   - **Already installed at** `C:\Users\User\apache-maven-3.9.9` ✅

3. **Operating System**
   - ✅ Windows 10 or 11 (You have Windows 11)
   - ✅ macOS 10.14 or newer
   - ✅ Linux (Ubuntu 18.04 or newer)

### Computer Requirements:
- **RAM**: At least 4 GB (8 GB recommended)
- **Storage**: At least 500 MB free space
- **Display**: 1280x720 resolution or higher

---

## 📥 How to Install

The system is **already installed and ready to use!** But here's how it was set up:

### Step 1: Make Sure Java is Installed
Open PowerShell and type:
```powershell
java -version
```
You should see: `java version "25.0.1"` ✅

### Step 2: Make Sure Maven is Installed
Type:
```powershell
mvn -version
```
You should see: `Apache Maven 3.9.9` ✅

### Step 3: Download All Dependencies
This was already done! Maven downloaded all necessary libraries:
- JavaFX (for the user interface)
- SQLite (for the database)
- PDFBox (for PDF export)

---

## 🚀 How to Run

Running the system is super easy!

### Method 1: Using Maven (Recommended)

1. **Open PowerShell** or Command Prompt

2. **Navigate to the project folder**:
   ```powershell
   cd "C:\Users\User\Desktop\hospital managment system\hospital_managment_system"
   ```

3. **Run the application**:
   ```powershell
   mvn javafx:run
   ```

4. **Wait a few seconds** - The application window will open!

### Method 2: Using the JAR file (Faster after first build)

If you've already built the project once:
```powershell
java -jar target/HospitalManagementSystem-1.0-SNAPSHOT.jar
```

### What You'll See:
- A window will open showing the **Role Selection Screen**
- Three large buttons: Manager, Doctor, Receptionist
- Click any button to enter that dashboard

---

## 📖 How to Use

### Starting the Application

1. **Launch the application** using one of the methods above
2. You'll see the **Role Selection Screen** with three buttons

---

### 👔 Using the Manager Dashboard

**Who uses this?**: Hospital administrators, management staff

**How to access**: Click the **"Manager"** button on the main screen

**What you can do**:

#### View Dashboard Statistics
When you first open, you'll see:
- **Top Cards**: Total patients, doctors, ambulances, employees, current date
- **Finance Chart**: Shows income and expenses over the last 7 days
- **Financial Summary**: Total revenue, expenses, and net profit
- **Today's Appointments**: List of all appointments scheduled for today

#### Managing Patients
1. Click **"Manage Patients"** in the left sidebar
2. You'll see a table with all patients
3. **To add a new patient**:
   - Click the **"Add Patient"** button
   - Fill in the form (name, age, gender, blood group, etc.)
   - Click **"Save"**
4. **To edit a patient**:
   - Select the patient from the table
   - Click **"Edit"**
   - Update the information
   - Click **"Save"**

#### Managing Doctors
1. Click **"Manage Doctors"** in the sidebar
2. **To add a doctor**:
   - Click **"Add Doctor"**
   - Fill in: Name, Specialization (Cardiologist, Surgeon, etc.), License number
   - Add phone, email, working hours
   - Click **"Save"**
3. View all doctors in a table with their complete information

#### Managing Appointments
1. Click **"Manage Appointments"**
2. **To create an appointment**:
   - Click **"New Appointment"**
   - Select a **patient** from the dropdown
   - Select a **doctor** from the dropdown
   - Pick a **date** from the calendar
   - Choose a **time**
   - Add notes if needed
   - Click **"Book Appointment"**
3. See all appointments in a table
4. Update status: Scheduled → Completed or Cancelled

#### Managing Nurses
1. Click **"Manage Nurses"**
2. **To add a nurse**:
   - Click **"Add Nurse"**
   - Fill in personal info
   - Assign floor and room number
   - Set department (Emergency, ICU, Pediatrics, etc.)
   - Add license number and working hours
   - Click **"Save"**

#### Managing Cleaners
1. Click **"Manage Cleaners"**
2. **To add a cleaner**:
   - Click **"Add Cleaner"**
   - Enter name and contact info
   - Assign floor and area to clean
   - Set shift (Morning, Evening, Night)
   - Click **"Save"**

#### Managing Ambulances
1. Click **"Manage Ambulances"**
2. **To add an ambulance**:
   - Click **"Add Ambulance"**
   - Enter vehicle number (e.g., "AMB-001")
   - Choose type (Basic Life Support, Advanced Life Support, etc.)
   - Add driver name and phone
   - Set initial status (Usually "Available")
   - Enter location, equipment list, fuel status
   - Click **"Save"**
3. **To update ambulance status**:
   - Select the ambulance
   - Click **"Edit"**
   - Change status to "On Duty" when dispatched
   - Change to "In Maintenance" when being serviced
   - Click **"Update"**

#### Managing Finances
1. Click **"Finance Management"**
2. **To add income**:
   - Click **"Add Transaction"**
   - Select type: **"Income"**
   - Choose category (Patient Service, Lab Test, Surgery, etc.)
   - Enter amount (e.g., 5000)
   - Select payment method (Cash, Card, Insurance)
   - Add description
   - Click **"Save"**
3. **To add expense**:
   - Click **"Add Transaction"**
   - Select type: **"Expense"**
   - Choose category (Salary, Equipment, Maintenance, etc.)
   - Enter amount
   - Click **"Save"**
4. **View the dashboard** to see:
   - Total income
   - Total expenses
   - **Net profit** (automatically calculated)
   - Chart showing trends
5. **Export report**:
   - Click **"Export to PDF"**
   - Choose where to save
   - PDF will contain all financial data

#### Managing Reception/Visitors
1. Click **"Reception Management"**
2. **When a visitor arrives**:
   - Click **"Check In Visitor"**
   - Enter visitor name and phone
   - Select which patient they're visiting
   - Enter purpose (Family visit, Friend, Consultation)
   - Receptionist name is recorded
   - Click **"Check In"**
3. **When a visitor leaves**:
   - Select the visitor from the list
   - Click **"Check Out"**
   - System records the check-out time

---

### 👨‍⚕️ Using the Doctor Dashboard

**Who uses this?**: Medical doctors working in the hospital

**How to access**: Click the **"Doctor"** button on the main screen

**What you can do**:

1. **Select Your Name**
   - At the top, there's a dropdown menu
   - Find and select your name from the list of doctors

2. **View Your Appointments**
   - After selecting your name, you'll see all appointments scheduled with you for **today**
   - Each appointment shows:
     - Patient name
     - Appointment time
     - Status
     - Any notes

3. **View Your Operations**
   - See all surgeries you're scheduled to perform today
   - Shows operation time and patient details

4. **Check Appointment Count**
   - At the top, you'll see total number of appointments and operations for the day

5. **Return to Main Menu**
   - Click the **"Back"** button to go to role selection

---

### 👩‍💼 Using the Receptionist Dashboard

**Who uses this?**: Front desk staff, receptionists

**How to access**: Click the **"Receptionist"** button on the main screen

**What you can do**:

#### Check Bed Availability
- Dashboard shows available beds in each ward:
  - **General Ward**: Shows how many beds are free (out of 50)
  - **ICU**: Shows ICU bed availability (out of 20)
  - **CCU**: Shows CCU bed availability (out of 15)

#### Assign a Bed to a Patient
1. Look at the **"Assign Bed"** section
2. **Select a patient** from the first dropdown
3. **Select an available bed** from the second dropdown (only shows empty beds)
4. Click **"Assign Bed"**
5. Success message appears: "Assigned bed to [patient name]"
6. The bed is now marked as occupied

#### Create New Appointment
1. Click **"New Appointment"** button
2. A form opens where you can:
   - Select patient
   - Select doctor
   - Pick date and time
   - Click **"Book"**

#### View All Appointments
1. Click **"View Appointments"** button
2. See a list of all scheduled appointments
3. Can check or update appointment details

---

## 🧠 Understanding the System

### How Everything Connects

The system works like this:

```
You interact with → The Application (JavaFX UI)
                          ↓
    Application talks to → Controllers (Handle button clicks)
                          ↓
    Controllers talk to → Services (Business logic)
                          ↓
    Services talk to → Database (SQLite - stores everything)
```

### Simple Example: Adding a Patient

1. **You**: Click "Add Patient" button
2. **Application**: Opens a form
3. **You**: Fill in patient name, age, etc., and click "Save"
4. **Controller**: Receives the data
5. **Service**: Validates the data (checks if required fields are filled)
6. **Database**: Stores the patient information
7. **Application**: Shows success message and updates the patient list

Everything happens in milliseconds!

---

## 💾 Database Information

### What is the Database?

The database is where all your data is stored. Think of it like a digital filing cabinet with different drawers:

**Database File**: `hospital.db`
**Location**: In the same folder as the application
**Type**: SQLite (a lightweight database that doesn't need a separate server)

### Tables (drawers) in the Database:

1. **patients** - All patient records
2. **doctors** - All doctor information
3. **nurses** - Nursing staff details
4. **cleaners** - Cleaning staff details
5. **appointments** - All appointment bookings
6. **operations** - Surgical procedures
7. **ambulances** - Ambulance fleet
8. **reception** - Visitor logs
9. **finance** - All financial transactions
10. **wards** - Hospital wards (General, ICU, CCU)
11. **beds** - All beds and their status

### Database is Created Automatically

- **First time you run the app**: The database file is created automatically
- **All tables are created**: No setup needed
- **Default data is added**: 85 beds (50 general, 20 ICU, 15 CCU)

### Backup Your Data

**Important!** To backup your hospital data:
1. Close the application
2. Copy the `hospital.db` file to a safe location (USB drive, cloud storage)
3. To restore: Replace the `hospital.db` file with your backup

---

## 🔧 Troubleshooting

### Problem: "mvn: The term 'mvn' is not recognized"
**Solution**: 
- Maven is not in your system PATH
- Close PowerShell completely
- Open a **new** PowerShell window
- Maven should now work (it was added to PATH during setup)

### Problem: Application window doesn't open
**Solution**:
- Check that no other Java applications are running
- Make sure you're in the correct folder:
  ```powershell
  cd "C:\Users\User\Desktop\hospital managment system\hospital_managment_system"
  ```
- Try rebuilding:
  ```powershell
  mvn clean install
  ```
- Then run again:
  ```powershell
  mvn javafx:run
  ```

### Problem: "Database connection error"
**Solution**:
- The `hospital.db` file might be corrupted
- Delete the `hospital.db` file
- Run the application again
- A fresh database will be created with all tables

### Problem: Application is slow
**Solution**:
- Close other applications to free up memory
- If the database is very large (many records), performance may slow
- Consider archiving old data

### Problem: Can't see any doctors/patients in dropdowns
**Solution**:
- You need to add data first!
- Go to Manager Dashboard
- Add doctors using "Manage Doctors"
- Add patients using "Manage Patients"
- Then they'll appear in dropdowns

### Problem: Warnings appear when running
**Solution**:
- Warnings like "restricted method" or "terminally deprecated" are **normal**
- They appear because we're using Java 25 with JavaFX 21
- **These do NOT affect functionality** - you can ignore them
- The application works perfectly despite these warnings

### Problem: PDF export doesn't work
**Solution**:
- Make sure you have write permissions in the folder where you're saving
- Try saving to Desktop instead
- Check that PDFBox library is downloaded (it's in Maven dependencies)

---

## 🛠️ Technical Details

### Project Structure

```
hospital_managment_system/
│
├── src/main/java/com/hms/              # Java source code
│   ├── App.java                        # Main application entry
│   ├── controller/                     # UI Controllers
│   │   ├── ManagerDashboardController.java
│   │   ├── DoctorDashboardController.java
│   │   ├── ReceptionistDashboardController.java
│   │   ├── PatientController.java
│   │   ├── DoctorController.java
│   │   ├── AmbulanceController.java
│   │   ├── FinanceController.java
│   │   └── ... (more controllers)
│   │
│   ├── model/                          # Data models (entities)
│   │   ├── Patient.java
│   │   ├── Doctor.java
│   │   ├── Nurse.java
│   │   ├── Ambulance.java
│   │   ├── Appointment.java
│   │   ├── Finance.java
│   │   └── ... (more models)
│   │
│   ├── service/                        # Business logic
│   │   ├── PatientService.java
│   │   ├── DoctorService.java
│   │   ├── FinanceService.java
│   │   └── ... (more services)
│   │
│   └── database/
│       └── DatabaseManager.java        # Database connection
│
├── src/main/resources/com/hms/        # Resources
│   ├── views/                          # FXML UI files
│   │   ├── role-selection.fxml
│   │   ├── manager-dashboard.fxml
│   │   ├── doctor-dashboard.fxml
│   │   ├── receptionist-dashboard.fxml
│   │   └── ... (more views)
│   │
│   └── styles.css                      # Application styling
│
├── target/                             # Compiled files (auto-generated)
│   └── HospitalManagementSystem-1.0-SNAPSHOT.jar
│
├── pom.xml                             # Maven configuration
├── hospital.db                         # SQLite database (auto-created)
└── README.md                           # This file!
```

### Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 21+ | Core programming language |
| **JavaFX** | 21.0.6 | User interface framework |
| **Maven** | 3.9+ | Build tool and dependency management |
| **SQLite** | 3.45+ | Lightweight database |
| **PDFBox** | 2.0.30 | PDF generation and export |
| **FXML** | - | UI markup language |

### Architecture Pattern

The application follows **MVC** (Model-View-Controller) pattern:

- **Model**: Data classes (Patient.java, Doctor.java, etc.)
- **View**: FXML files (UI layouts)
- **Controller**: Controller classes (connect View and Model)

Plus a **Service Layer** for business logic and a **Database Layer** for data persistence.

### Maven Commands Reference

```powershell
# Clean and compile the project
mvn clean compile

# Run the application
mvn javafx:run

# Create executable JAR file
mvn clean package

# Clean all compiled files
mvn clean

# Install dependencies
mvn clean install

# Skip tests and build
mvn clean install -DskipTests
```

### Key Java Packages

- `com.hms` - Main application package
- `com.hms.controller` - All UI controllers
- `com.hms.model` - Data models/entities
- `com.hms.service` - Business logic services
- `com.hms.database` - Database management

### Database Schema (Simplified)

**Patients Table**:
- ID, First Name, Last Name, Gender, Date of Birth, Phone, Email, Address, Blood Group, Registration Date

**Doctors Table**:
- ID, First Name, Last Name, Specialization, Phone, Email, License Number, Office Location, Working Hours

**Appointments Table**:
- ID, Patient ID (linked to patient), Doctor ID (linked to doctor), Date, Time, Status, Notes

**Finance Table**:
- ID, Date, Type (Income/Expense), Category, Description, Amount, Payment Method

(And more...)

---

## 📝 Additional Notes

### Default Data

When you first run the application:
- **85 beds are created automatically**:
  - 50 beds in General Ward (Bed #1 to #50)
  - 20 beds in ICU (ICU-1 to ICU-20)
  - 15 beds in CCU (CCU-1 to CCU-15)
- All beds start as "Available"
- 3 wards are created: General Ward, ICU, CCU

### Data Validation

The system automatically validates:
- Required fields (name, etc.) cannot be empty
- Phone numbers are stored as text (can include + and -)
- Email addresses are stored (but not validated in current version)
- Dates must be valid dates
- Numbers (amounts, counts) must be valid numbers

### Best Practices

1. **Regular Backups**: Copy `hospital.db` file weekly
2. **Data Entry**: Train staff properly on data entry
3. **Testing**: Test with sample data before going live
4. **Permissions**: Limit Manager Dashboard access to authorized personnel
5. **Updates**: Keep the application updated
6. **Support**: Document any custom changes you make

### Future Enhancements (Possible)

- User login system with passwords
- Role-based permissions
- Email notifications for appointments
- SMS alerts for ambulance dispatch
- Advanced reports with charts
- Prescription management
- Lab test results
- Medicine inventory
- Billing system
- Patient medical history
- Multi-language support

---

## 📞 Support & Contact

If you need help:
1. Check the **Troubleshooting** section above
2. Review the **How to Use** section
3. Make sure all requirements are met
4. Try rebuilding the project: `mvn clean install`

---

## 📄 License

This project is free to use for educational and commercial purposes.

---

## 👨‍💻 Credits

**Developer**: Hospital Management System Team  
**Built with**: Java, JavaFX, Maven, SQLite  
**Last Updated**: February 28, 2026

---

## 🎉 Final Notes

This Hospital Management System is designed to be:
- ✅ **Easy to use** - Simple, intuitive interface
- ✅ **Complete** - Manages all hospital operations
- ✅ **Reliable** - Data is safely stored in database
- ✅ **Fast** - Lightweight and responsive
- ✅ **Modern** - Clean, professional design

**Thank you for using Hospital Management System!**

Feel free to customize and enhance the system according to your hospital's specific needs.

---

*For technical support or questions, please refer to the documentation or contact your system administrator.*
