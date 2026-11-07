package com.invex.supplement

data class StudentSheet(
    val id: String,
    val name: String,
    val course: String,
    val year: String,
    val rollNumber: String,
    val sheetCount: Int,
    val status: SheetStatus,
    val issuedAtMillis: Long = System.currentTimeMillis()
) {
    val meta: String get() = "$course | $year | $rollNumber"
}

enum class SheetStatus(val label: String) {
    RESOLVED("Resolved"),
    PENDING("Pending"),
    AWAITING("Awaiting");
}

data class SheetSummary(
    val issued: Int = 0,
    val awaitingReturn: Int = 0,
    val returned: Int = 0,
    val inStock: Int = 0,
)
