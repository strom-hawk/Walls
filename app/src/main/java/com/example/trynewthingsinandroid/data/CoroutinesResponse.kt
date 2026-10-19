package com.example.trynewthingsinandroid.data

/**
 * Data class representing a coroutines test response.
 *
 * @property status The status message or code.
 * @property message Detailed description of the response.
 * @property data List of employees returned in the response.
 */
data class CoroutinesResponse(
    val status: String,
    val message: String,
    val data: List<Employee>
)

/**
 * Data class representing employee details.
 *
 * @property id Unique identifier for the employee.
 * @property employee_name Name of the employee.
 * @property employee_salary Salary of the employee.
 * @property employee_age Age of the employee.
 * @property profile_age Profile age descriptor.
 */
data class Employee(
    val id: Int,
    val employee_name: String,
    val employee_salary: String,
    val employee_age: Int,
    val profile_age: String
)
