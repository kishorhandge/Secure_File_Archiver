# File Packer and Unpacker (Java)

## Overview

This project is a **File Packer and Unpacker system developed in Java** that combines multiple `.txt` files into a single archive file and restores them back to their original form.

The system stores **file metadata (name and size)** and uses **XOR encryption** to provide basic data security during packing and unpacking.

Both **Command-Line** and **GUI (Java Swing)** versions are implemented.

---

## Features

* Pack multiple `.txt` files into a single file
* Unpack files back to their original form
* Metadata header for file name and size
* XOR-based encryption and decryption
* Command-Line version
* GUI version using Java Swing

---

## Technologies Used

* **Java**
* **Java Swing**
* **FileInputStream & FileOutputStream**
* **ArrayList**
* **XOR Encryption (Key: 0x11)**

---

## Challenges

* Converting the Command-Line project into a GUI application
* Implementing encryption and decryption using XOR
* Correctly separating multiple files during unpacking

---

## Future Improvements

* Support for all file types
* Stronger encryption (AES / DES)
* File compression
* Password protection
* Improved GUI features

