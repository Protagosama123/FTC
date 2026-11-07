package com.invex

import com.invex.supplement.SheetStatus
import com.invex.supplement.SupplementViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SupplementViewModelTest {

    @Test
    fun testInitialSummaryAndStudents() {
        val viewModel = SupplementViewModel()
        val state = viewModel.state.value

        assertEquals(1, state.summary.issued)
        assertEquals(15, state.summary.awaitingReturn)
        assertEquals(1, state.summary.returned)
        assertTrue(state.allStudents.isNotEmpty())
    }

    @Test
    fun testIssueSheetUpdatesSummary() {
        val viewModel = SupplementViewModel()
        val initialStock = viewModel.state.value.summary.inStock
        val initialIssued = viewModel.state.value.summary.issued

        viewModel.issueSheet(
            studentName = "Aman Gupta",
            course = "B.tech",
            year = "3rd Year",
            rollNumber = "7099",
            count = 2,
        )

        val updated = viewModel.state.value
        assertEquals(initialIssued + 2, updated.summary.issued)
        assertEquals(initialStock - 2, updated.summary.inStock)
        assertEquals("Aman Gupta", updated.allStudents.first().name)
        assertEquals(2, updated.allStudents.first().sheetCount)
        assertEquals(SheetStatus.PENDING, updated.allStudents.first().status)
    }

    @Test
    fun testQueryFiltering() {
        val viewModel = SupplementViewModel()
        viewModel.onQueryChange("7045")
        val state = viewModel.state.value

        assertEquals(1, state.visibleStudents.size)
        assertEquals("7045", state.visibleStudents.first().rollNumber)

        viewModel.onQueryChange("nonexistent_student_xyz")
        assertTrue(viewModel.state.value.visibleStudents.isEmpty())
    }

    @Test
    fun testMarkResolved() {
        val viewModel = SupplementViewModel()
        val pendingStudent = viewModel.state.value.allStudents.first { it.status == SheetStatus.PENDING }
        val awaitingBefore = viewModel.state.value.summary.awaitingReturn
        val returnedBefore = viewModel.state.value.summary.returned

        viewModel.markResolved(pendingStudent.id)

        val updated = viewModel.state.value
        val resolvedStudent = updated.allStudents.first { it.id == pendingStudent.id }
        assertEquals(SheetStatus.RESOLVED, resolvedStudent.status)
        assertEquals(returnedBefore + pendingStudent.sheetCount, updated.summary.returned)
        assertEquals(awaitingBefore - pendingStudent.sheetCount, updated.summary.awaitingReturn)
    }
}
