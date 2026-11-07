package com.invex.supplement

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.invex.ui.theme.Inter
import com.invex.ui.theme.InvexType
import com.invex.ui.theme.MobileBackground
import com.invex.ui.theme.Orange
import com.invex.ui.theme.SectionHeadingColor
import com.invex.ui.theme.SoftWhite
import com.invex.ui.theme.TableRowInteraction

/** Stateful entry point: owns the ViewModel and navigation actions. */
@Composable
fun SupplementSheetRoute(
    onBack: () -> Unit,
    viewModel: SupplementViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.feedbackMessage) {
        val msg = state.feedbackMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedbackMessage()
        }
    }

    SupplementSheetScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onIssueSheetClick = viewModel::openIssueDialog,
        onQueryChange = viewModel::onQueryChange,
        onStudentClick = viewModel::selectStudent,
        onRestockStock = viewModel::restock,
    )

    // Issue Sheet Dialog
    if (state.isIssueSheetDialogOpen) {
        IssueSheetDialog(
            stockCount = state.summary.inStock,
            onDismiss = viewModel::closeIssueDialog,
            onConfirm = { name, course, year, roll, count ->
                viewModel.issueSheet(name, course, year, roll, count)
            },
        )
    }

    // Student Detail Dialog
    state.selectedStudentForDetails?.let { student ->
        StudentDetailDialog(
            student = student,
            onDismiss = { viewModel.selectStudent(null) },
            onMarkResolved = { viewModel.markResolved(student.id) },
            onAddExtraSheet = { viewModel.addSheetToStudent(student.id, 1) },
        )
    }
}

/** Stateless screen matching the Figma frame and SupplementSheet.md specification. */
@Composable
fun SupplementSheetScreen(
    state: SupplementUiState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onIssueSheetClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onStudentClick: (StudentSheet) -> Unit,
    onRestockStock: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    shape = RoundedCornerShape(12.dp),
                    containerColor = Color(0xFF1E1E1E),
                    contentColor = SoftWhite,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(text = data.visuals.message, fontFamily = Inter, fontSize = 13.sp)
                }
            }
        },
        containerColor = MobileBackground,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .testTag("supplement_sheet_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Header: Chevron back + Orange sheet icon + "Supplements"
            item {
                SupplementsHeader(onBack = onBack)
            }

            // Status cards 2x2 grid
            item {
                StatusGrid(
                    summary = state.summary,
                    onStockClick = onRestockStock,
                )
            }

            // Issue a Supplementary Sheet primary CTA
            item {
                IssueSheetButton(onClick = onIssueSheetClick)
            }

            // "Sheets Issued" section heading
            item {
                Text(
                    text = "Sheets Issued",
                    style = InvexType.SectionHeading,
                    color = SectionHeadingColor,
                    modifier = Modifier.testTag("sheets_issued_heading"),
                )
            }

            // Search bar input
            item {
                SearchBar(
                    query = state.query,
                    onQueryChange = onQueryChange,
                )
            }

            // Student rows or empty state
            if (state.visibleStudents.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(TableRowInteraction, RoundedCornerShape(15.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = "No students found",
                                fontFamily = Inter,
                                fontSize = 15.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                            )
                            if (state.query.isNotEmpty()) {
                                Text(
                                    text = "Try searching for a different name, course or roll number",
                                    fontFamily = Inter,
                                    fontSize = 11.sp,
                                    color = Color.Gray.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            } else {
                items(
                    items = state.visibleStudents,
                    key = { it.id },
                ) { student ->
                    StudentRow(
                        student = student,
                        onClick = { onStudentClick(student) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
