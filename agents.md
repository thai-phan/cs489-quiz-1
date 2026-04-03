For this quiz, you will perform Requirements, analysis, domain modeling and implement a basic Java Command-Line Interface (CLI) application to keep track of Contacts. Each Contact has a first name, last name, company, job title, zero or more Phone Numbers, and zero or more Email Addresses. Each Email Address has an actual address field and a label so that users can write something to indicate how it is related to the Contact (like home, mobile, work, etc.). Similarly, each Phone Number also has a field for the number and a field for a label.

The user of the application should be able to search through the contacts. Add new contacts, update existing contacts, and delete old contacts. In addition to this, it is important to be able to merge duplicate contacts.

@startuml

skinparam classAttributeIconSize 0

class Contact {
- String firstName
- String lastName
- String company
- String jobTitle
- List<PhoneNumber> phoneNumbers
- List<EmailAddress> emailAddresses

    + create()
    + update(firstName, lastName, company, jobTitle)
    + delete()
    + addPhoneNumber(number: String, label: String)
    + addEmailAddress(address: String, label: String)
    + merge(other: Contact)
    + toString(): String
}

class PhoneNumber {
- String number
- String label
+ getNumber(): String
+ getLabel(): String
+ setNumber(number: String)
+ setLabel(label: String)
}

class EmailAddress {
- String address
- String label
+ getAddress(): String
+ getLabel(): String
+ setAddress(address: String)
+ setLabel(label: String)
}

' Relationships
Contact "1" *-- "0..*" PhoneNumber : contains
Contact "1" *-- "0..*" EmailAddress : contains

@enduml


