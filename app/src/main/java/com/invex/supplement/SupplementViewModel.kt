package com.invex.supplement

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class SupplementUiState(
    val summary: SheetSummary = SheetSummary(),
    val query: String = "",
    val allStudents: List<StudentSheet> = emptyList(),
    val isIssueSheetDialogOpen: Boolean = false,
    val selectedStudentForDetails: StudentSheet? = null,
    val feedbackMessage: String? = null,
) {
    val visibleStudents: List<StudentSheet>
        get() = if (query.isBlank()) allStudents else allStudents.filter {
            it.name.contains(query, ignoreCase = true) ||
                it.rollNumber.contains(query, ignoreCase = true) ||
                it.course.contains(query, ignoreCase = true) ||
                it.year.contains(query, ignoreCase = true)
        }
}

class SupplementViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        SupplementUiState(
            summary = SheetSummary(
                issued = 1,
                awaitingReturn = 15,
                returned = 1,
                inStock = 30
            ),
            allStudents = listOf(
                StudentSheet(
                    id = "s1",
                    name = "Lorem Ipsum Dorem",
                    course = "B.tech",
                    year = "3rd Year",
                    rollNumber = "7045",
                    sheetCount = 1,
                    status = SheetStatus.RESOLVED,
                ),
                StudentSheet(
                    id = "s2",
                    name = "Aarav Sharma",
                    course = "B.tech",
                    year = "3rd Year",
                    rollNumber = "7042",
                    sheetCount = 2,
                    status = SheetStatus.PENDING,
                ),
                StudentSheet(
                    id = "s3",
                    name = "Priya Patel",
                    course = "BCA",
                    year = "2nd Year",
                    rollNumber = "7089",
                    sheetCount = 1,
                    status = SheetStatus.RESOLVED,
                ),
                StudentSheet(
                    id = "s4",
                    name = "Rohan Das",
                    course = "B.tech",
                    year = "4th Year",
                    rollNumber = "7011",
                    sheetCount = 1,
                    status = SheetStatus.PENDING,
                )
            ),
        )
    )
    val state: StateFlow<SupplementUiState> = _state.asStateFlow()

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun openIssueDialog() {
        _state.update { it.copy(isIssueSheetDialogOpen = true) }
    }

    fun closeIssueDialog() {
        _state.update { it.copy(isIssueSheetDialogOpen = false) }
    }

    fun selectStudent(student: StudentSheet?) {
        _state.update { it.copy(selectedStudentForDetails = student) }
    }

    fun clearFeedbackMessage() {
        _state.update { it.copy(feedbackMessage = null) }
    }

    fun issueSheet(
        studentName: String,
        course: String,
        year: String,
        rollNumber: String,
        count: Int,
    ) {
        val trimmedName = studentName.trim().ifBlank { "Student ${rollNumber.trim()}" }
        val trimmedCourse = course.trim().ifBlank { "B.tech" }
        val trimmedYear = year.trim().ifBlank { "3rd Year" }
        val trimmedRoll = rollNumber.trim().ifBlank { "%04d".format((1000..9999).random()) }
        val validCount = count.coerceAtLeast(1)

        val newStudent = StudentSheet(
            id = UUID.randomUUID().toString(),
            name = trimmedName,
            course = trimmedCourse,
            year = trimmedYear,
            rollNumber = trimmedRoll,
            sheetCount = validCount,
            status = SheetStatus.PENDING,
        )

        _state.update { current ->
            val updatedSummary = current.summary.copy(
                issued = current.summary.issued + validCount,
                awaitingReturn = current.summary.awaitingReturn + validCount,
                inStock = (current.summary.inStock - validCount).coerceAtLeast(0),
            )
            current.copy(
                summary = updatedSummary,
                allStudents = listOf(newStudent) + current.allStudents,
                isIssueSheetDialogOpen = false,
                feedbackMessage = "Issued $validCount sheet(s) to $trimmedName (Roll: $trimmedRoll)"
            )
        }
    }

    fun markResolved(studentId: String) {
        _state.update { current ->
            val target = current.allStudents.find { it.id == studentId } ?: return@update current
            if (target.status == SheetStatus.RESOLVED) return@update current

            val updatedList = current.allStudents.map { student ->
                if (student.id == studentId) {
                    student.copy(status = SheetStatus.RESOLVED)
                } else student
            }

            val updatedSummary = current.summary.copy(
                awaitingReturn = (current.summary.awaitingReturn - target.sheetCount).coerceAtLeast(0),
                returned = current.summary.returned + target.sheetCount,
            )

            current.copy(
                summary = updatedSummary,
                allStudents = updatedList,
                selectedStudentForDetails = null,
                feedbackMessage = "Marked ${target.name}'s sheet as Resolved"
            )
        }
    }

    fun addSheetToStudent(studentId: String, additionalCount: Int = 1) {
        _state.update { current ->
            val target = current.allStudents.find { it.id == studentId } ?: return@update current
            val updatedList = current.allStudents.map { student ->
                if (student.id == studentId) {
                    student.copy(sheetCount = student.sheetCount + additionalCount)
                } else student
            }

            val updatedSummary = current.summary.copy(
                issued = current.summary.issued + additionalCount,
                awaitingReturn = if (target.status == SheetStatus.PENDING) {
                    current.summary.awaitingReturn + additionalCount
                } else current.summary.awaitingReturn,
                inStock = (current.summary.inStock - additionalCount).coerceAtLeast(0),
            )

            current.copy(
                summary = updatedSummary,
                allStudents = updatedList,
                selectedStudentForDetails = updatedList.find { it.id == studentId },
                feedbackMessage = "Added +$additionalCount sheet to ${target.name}"
            )
        }
    }

    fun restock(amount: Int = 20) {
        _state.update { current ->
            current.copy(
                summary = current.summary.copy(inStock = current.summary.inStock + amount),
                feedbackMessage = "Restocked $amount supplementary sheets into inventory"
            )
        }
    }
}
