package com.invex.supplement

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.invex.ui.theme.IconColor
import com.invex.ui.theme.Inter
import com.invex.ui.theme.Orange
import com.invex.ui.theme.OutlineShadow
import com.invex.ui.theme.PillGrey
import com.invex.ui.theme.SoftWhite
import com.invex.ui.theme.StatusGreen
import com.invex.ui.theme.StatusYellow
import com.invex.ui.theme.TableRowInteraction

@Composable
fun StudentDetailDialog(
    student: StudentSheet,
    onDismiss: () -> Unit,
    onMarkResolved: () -> Unit,
    onAddExtraSheet: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("student_detail_dialog"),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Sheet Record",
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = IconColor,
                    )
                    val statusColor = when (student.status) {
                        SheetStatus.RESOLVED -> StatusGreen
                        SheetStatus.PENDING -> StatusYellow
                        SheetStatus.AWAITING -> Orange
                    }
                    Box(
                        modifier = Modifier
                            .background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .border(1.dp, statusColor, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = student.status.label,
                            fontFamily = Inter,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                        )
                    }
                }

                // Student info container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TableRowInteraction, RoundedCornerShape(12.dp))
                        .border(1.dp, OutlineShadow, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DetailRow("Student Name", student.name)
                    DetailRow("Roll Number", student.rollNumber)
                    DetailRow("Course & Year", "${student.course} • ${student.year}")
                    DetailRow("Sheets Issued", "%02d".format(student.sheetCount))
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Actions
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (student.status != SheetStatus.RESOLVED) {
                        Button(
                            onClick = onMarkResolved,
                            colors = ButtonDefaults.buttonColors(containerColor = StatusGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_mark_resolved"),
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                            Text(
                                " Mark as Resolved (Returned)",
                                color = Color.Black,
                                fontFamily = Inter,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onAddExtraSheet,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_add_extra_sheet"),
                    ) {
                        Icon(Icons.Default.PlusOne, contentDescription = null, tint = Orange)
                        Text(
                            " Issue +1 Extra Sheet",
                            color = Orange,
                            fontFamily = Inter,
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    ) {
                        Text("Close", color = Color.Gray, fontFamily = Inter)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontFamily = Inter,
            fontSize = 12.sp,
            color = Color.Gray,
        )
        Text(
            text = value,
            fontFamily = Inter,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
        )
    }
}
