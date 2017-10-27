A simple Attendance management System (with Android Frontend and CakePhp backend)

This is a simple Attendance Management System and is comprised of

a) An Android App

The Android Mobile (with AMS app installed), therotically, will be installed in the customer premises

The APP first scans a QR code based Employee ID card and then clicks a Photo (from the Selfie Cam) and uploads it to the Server.
The settings for the server's URL can be changed at android/ams/app/src/main/java/adiractech/ams/utils/Constants.java. 
It uses REST with JSON to update the Server

b) CakePhp Backend
  - The database schema is available at sql/ams.sql
  - The Rest Controller is available at app/Controller/RestController.php
  
  
